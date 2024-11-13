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

			validateClassDate(bookingClass.getExpiryDate());
			validateSufficientCredits(purchasedPackage, bookingClass);
			validateAvaiableSlot(user, bookingClass);

			processBooking(user, bookingClass, purchasedPackage);
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

	private void validateClassDate(Date expiryDate) {
		Instant currentDateTime = Instant.now();
		Instant eventExpDate = expiryDate.toInstant();

		if (!currentDateTime.isBefore(eventExpDate)) {
			throw new ResponseInfoException("Can not book this class. It is expired.");
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

	@Transactional
	@Override
	public void cancelClass(CancelClassRequest req) {
		Booking booking = getBooking(req.getBookingId());
		BookingClass bookingClass = booking.getBookingClass();
		PurchasedPackage purchasedPackage = getPurchasedPackage(booking.getPerchasedPackageId());

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

	public boolean isRefundForCancellation(Date startDate) {
		Instant currentDateTime = Instant.now();
		Instant eventStartDate = startDate.toInstant();

		long hoursDifference = Duration.between(currentDateTime, eventStartDate).toHours();

		return hoursDifference >= CommonConstant.DIFFERENT_HOURS;
	}
}
