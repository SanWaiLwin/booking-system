package com.swl.booking.system.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swl.booking.system.entity.Booking;
import com.swl.booking.system.entity.BookingClass;
import com.swl.booking.system.entity.PurchasedPackage;
import com.swl.booking.system.entity.User;
import com.swl.booking.system.entity.WaitingList;
import com.swl.booking.system.exception.ResponseInfoException;
import com.swl.booking.system.repository.BookingClassRepository;
import com.swl.booking.system.repository.BookingRepository;
import com.swl.booking.system.repository.PurchasedPackageRepository;
import com.swl.booking.system.repository.UserRepository;
import com.swl.booking.system.repository.WaitingListRepository;
import com.swl.booking.system.request.booking.BookClassRequest;
import com.swl.booking.system.request.booking.CancelClassRequest;
import com.swl.booking.system.request.booking.CheckInClassRequest;
import com.swl.booking.system.response.booking.BookingClassData;
import com.swl.booking.system.response.booking.ViewAvaiableClassResponse;
import com.swl.booking.system.security.UserPrincipal;
import com.swl.booking.system.util.CommonConstant;
import com.swl.booking.system.util.CommonUtil;
import com.swl.booking.system.util.enums.BookingStatus;

@Service
public class BookingServiceImpl implements BookingService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final BookingRepository bookingRepository;
	private final UserRepository userRepository;
	private final BookingClassRepository bookingClassRepository;
	private final PurchasedPackageRepository purchasedPackageRepository;
	private final WaitingListRepository waitingListRepository;

	public BookingServiceImpl(RedisTemplate<String, Object> redisTemplate, BookingRepository bookingRepository,
			UserRepository userRepository, BookingClassRepository bookingClassRepository,
			PurchasedPackageRepository purchasedPackageRepository, WaitingListRepository waitingListRepository) {
		this.redisTemplate = redisTemplate;
		this.bookingRepository = bookingRepository;
		this.userRepository = userRepository;
		this.bookingClassRepository = bookingClassRepository;
		this.purchasedPackageRepository = purchasedPackageRepository;
		this.waitingListRepository = waitingListRepository;
	}

	@Override
	public ViewAvaiableClassResponse getMyClassList() {
		UserPrincipal userData = CommonUtil.getUserPrincipalFromAuthentication();
		List<Booking> dataList = bookingRepository.findByUserId(userData.getId());

		ViewAvaiableClassResponse resp = new ViewAvaiableClassResponse();
		List<BookingClassData> bookingClassData = dataList.stream().map(d -> prepareBookingClassData(d))
				.collect(Collectors.toList());
		resp.setClassList(bookingClassData);
		return resp;
	}

	@Transactional
	@Override
	public void bookClass(BookClassRequest req) {
		String lockKey = "lock:class:" + req.getBookingClassId();
		boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 30, TimeUnit.SECONDS);
		if (!locked)
			throw new IllegalStateException("Another booking is in process for this booking class.");

		try {
			User user = getUser();
			BookingClass bookingClass = getBookingClass(req.getBookingClassId());
			PurchasedPackage purchasedPackage = getAvaiablePurchasedPackage(user.getId());

			validateClassExpiryDate(bookingClass.getExpiryDate());
			validateClassIsAlreadyBooked(user, bookingClass);
			validateSufficientCredits(purchasedPackage, bookingClass);
			validateAvaiableSlot(user, bookingClass);

			processBooking(user, bookingClass, purchasedPackage);
		} finally {
			redisTemplate.delete(lockKey);
		}
	}

	@Transactional
	@Override
	public void cancelClass(CancelClassRequest req) {
		Booking booking = getBooking(req.getBookingId());
		BookingClass bookingClass = booking.getBookingClass();
		PurchasedPackage purchasedPackage = getPurchasedPackage(booking.getPerchasedPackageId());

		validateBookingIsActive(booking);
		if (isRefundForCancellation(bookingClass.getStartDate())) {
			refundBooking(bookingClass, purchasedPackage, booking);
		} else {
			throw new ResponseInfoException("You can not cancel this order.. according my rule and regulation.");
		}

		Optional<WaitingList> waitingListOpt = getWaitingList(bookingClass);

		if (waitingListOpt.isPresent()) {
			BookClassRequest reqForBooking = new BookClassRequest();
			reqForBooking.setBookingClassId(bookingClass.getId());
			bookClass(reqForBooking);
		}
	}

	@Transactional
	@Override
	public void checkInClass(CheckInClassRequest req) {
		User user = getUser();
		String lockKey = "lock:checkin:class:" + req.getBookingId() + ":user:" + user.getId();

		Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 10, TimeUnit.SECONDS);
		if (!locked) {
			throw new IllegalStateException("Another check-in process is currently in progress for this class.");
		}

		try {
			Booking booking = getBooking(req.getBookingId());
			BookingClass bookingClass = booking.getBookingClass();

//			validateClassIsAlreadyBooked(user, bookingClass);

			validateClassCheckinTime(booking.getBookingClass().getStartDate());

			if (booking.getStatus() == BookingStatus.CHECK_IN.getCode()) {
				throw new IllegalStateException("User has already checked in for this class.");
			}

			booking.setStatus(BookingStatus.CHECK_IN.getCode());
			bookingRepository.save(booking);
		} finally {
			redisTemplate.delete(lockKey);
		}

	} 

	private <T> T getEntityOrThrow(Optional<T> entity, String entityName) {
		return entity.orElseThrow(() -> new ResponseInfoException(entityName + " not found."));
	}

	private void validateAvaiableSlot(User user, BookingClass bookingClass) {
		if (bookingClass.getAvailableSlots() <= 0) {
			addWaitingList(user, bookingClass);
			throw new ResponseInfoException("Booking Class is full. You have added to waiting list.");
		}
	}

	private void validateSufficientCredits(PurchasedPackage purchasedPackage, BookingClass bookingClass) {
		if (purchasedPackage.getRemainingCredits() < bookingClass.getRequiredCredits())
			throw new ResponseInfoException("Insufficient credits.");
	}

	private void validateBookingIsActive(Booking booking) {
		if (booking.getStatus() != BookingStatus.ACTIVE.getCode()) {
			throw new ResponseInfoException("You can not canceled this book. It is not avaiable right now.");
		}
	}

	private void validateClassIsAlreadyBooked(User user, BookingClass bookingClass) {
		Optional<Booking> bookingOpt = bookingRepository.findByUserAndBookingClassAndStatus(user, bookingClass,
				BookingStatus.ACTIVE.getCode());
		if (bookingOpt.isPresent()) {
			throw new ResponseInfoException("This class is already booked.");
		}
	} 

	private void processBooking(User user, BookingClass bookingClass, PurchasedPackage purchasedPackage) {
		bookingClass.decrementAvailableSlots();
		purchasedPackage.decrementRemainingCredits(bookingClass.getRequiredCredits());

		saveEntities(bookingClass, purchasedPackage);
		addBooking(user, bookingClass, purchasedPackage);
	}

	private void saveEntities(BookingClass bookingClass, PurchasedPackage purchasedPackage) {
		bookingClassRepository.save(bookingClass);
		purchasedPackageRepository.save(purchasedPackage);
	}

	private void addBooking(User user, BookingClass bookingClass, PurchasedPackage purchasedPackage) {
		Booking booking = new Booking();
		booking.setUser(user);
		booking.setBookingClass(bookingClass);
		booking.setPerchasedPackageId(purchasedPackage.getId());
		booking.setStatus(BookingStatus.ACTIVE.getCode());
		booking.setExpiryDate(bookingClass.getExpiryDate());
		bookingRepository.save(booking);
	}

	private void updateBooking(Booking booking) {
		booking.setStatus(BookingStatus.CANCEL.getCode());
		bookingRepository.save(booking);
	}

	private void addWaitingList(User user, BookingClass bookingClass) {
		WaitingList waitingList = new WaitingList();
		waitingList.setUser(user);
		waitingList.setWaitingListClass(bookingClass);
		waitingListRepository.save(waitingList);
	}

	private User getUser() {
		UserPrincipal userData = CommonUtil.getUserPrincipalFromAuthentication();
		return getEntityOrThrow(userRepository.findById(userData.getId()), "User");
	}

	private PurchasedPackage getAvaiablePurchasedPackage(Long id) {
		return getEntityOrThrow(purchasedPackageRepository.findFirstByUserIdOrderByRemainingCreditsDesc(id),
				"Purchased packages");
	}

	private BookingClass getBookingClass(Long bookingClassId) {
		return getEntityOrThrow(bookingClassRepository.findById(bookingClassId), "Booking class");
	}

	private PurchasedPackage getPurchasedPackage(Long purchasedPackageId) {
		return getEntityOrThrow(purchasedPackageRepository.findById(purchasedPackageId), "Purchased package");
	}

	private Booking getBooking(Long bookingId) {
		return getEntityOrThrow(bookingRepository.findById(bookingId), "Booking");
	}

	private Optional<WaitingList> getWaitingList(BookingClass bookingClassId) {
		return waitingListRepository.findFirstByWaitingListClassOrderByWaitingListDateAsc(bookingClassId);
	}

	private BookingClassData prepareBookingClassData(Booking d) {
		BookingClassData data = new BookingClassData();
		data.setId(d.getId());
		data.setName(d.getBookingClass().getName());
		data.setCountryName(d.getBookingClass().getCountry().getName());
		data.setRequiredCredits(d.getBookingClass().getRequiredCredits());
		data.setAvailableSlots(d.getBookingClass().getAvailableSlots());
		return data;
	}

	private void refundBooking(BookingClass bookingClass, PurchasedPackage purchasedPackage, Booking booking) {
		bookingClass.incrementAvailableSlots();
		purchasedPackage.incrementRemainingCredits(bookingClass.getRequiredCredits());

		saveEntities(bookingClass, purchasedPackage);
		updateBooking(booking);
	}

	public boolean isRefundForCancellation(Date startDate) {
		Instant currentDateTime = Instant.now();
		Instant eventStartDate = startDate.toInstant();

		long hoursDifference = Duration.between(currentDateTime, eventStartDate).toHours();

		return hoursDifference >= CommonConstant.DIFFERENT_HOURS;
	}

	private void validateClassExpiryDate(Date expiryDate) {
		Instant currentDateTime = Instant.now();
		Instant eventExpDate = expiryDate.toInstant();

		if (!currentDateTime.isBefore(eventExpDate)) {
			throw new ResponseInfoException("This class is expired.");
		}
	}

	private void validateClassCheckinTime(Date startDate) {
		Instant currentDateTime = Instant.now();
		Instant startDateTime = startDate.toInstant();

		long minutesDifference = Duration.between(currentDateTime, startDateTime).toMinutes();
		Boolean isValid = minutesDifference <= CommonConstant.CHECK_IN_WINDOW_MIN && minutesDifference >= 0;
		if (!isValid) {
			throw new ResponseInfoException("Check-in is only allowed within the valid check-in timeframe.");
		}
	}

}
