package com.swl.booking.system.service;

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
import com.swl.booking.system.response.booking.BookingClassData;
import com.swl.booking.system.response.booking.ViewAvaiableClassResponse;
import com.swl.booking.system.security.UserPrincipal;
import com.swl.booking.system.util.CommonUtil;

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
			PurchasedPackage purchasedPackage = getPurchasedPackage(req.getPurchasedPackageId());

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

	private void processBooking(User user, BookingClass bookingClass, PurchasedPackage purchasedPackage) {
		bookingClass.decrementAvailableSlots();
		purchasedPackage.decrementRemainingCredits(bookingClass.getRequiredCredits());

		saveEntities(bookingClass, purchasedPackage);
		addBooking(user, bookingClass);
	}

	private void saveEntities(BookingClass bookingClass, PurchasedPackage purchasedPackage) {
		bookingClassRepository.save(bookingClass);
		purchasedPackageRepository.save(purchasedPackage);
	}

	private void addBooking(User user, BookingClass bookingClass) {
		Booking booking = new Booking();
		booking.setUser(user);
		booking.setBookingClass(bookingClass);
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

	private BookingClass getBookingClass(Long bookingClassId) {
		return getEntityOrThrow(bookingClassRepository.findById(bookingClassId), "Booking class");
	}

	private PurchasedPackage getPurchasedPackage(Long purchasedPackageId) {
		return getEntityOrThrow(purchasedPackageRepository.findById(purchasedPackageId), "Purchased package");
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
}
