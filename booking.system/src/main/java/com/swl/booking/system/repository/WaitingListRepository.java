package com.swl.booking.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swl.booking.system.entity.BookingClass;
import com.swl.booking.system.entity.WaitingList;

@Repository
public interface WaitingListRepository extends JpaRepository<WaitingList, Long> {

	List<WaitingList> findByUserId(Long id);

//	Optional<WaitingList> findFirstByBookingClassIdOrderByWaitingListDateAsc(Long bookingClassId);

	Optional<WaitingList> findFirstByWaitingListClassOrderByWaitingListDateAsc(BookingClass bookingClassId);

}
