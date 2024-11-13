package com.swl.booking.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swl.booking.system.entity.Booking;
import com.swl.booking.system.entity.BookingClass;
import com.swl.booking.system.entity.User; 

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByUserId(Long id); 
	
	Optional<Booking> findByUserAndBookingClassAndStatus(User user, BookingClass bookingClass, Integer code);

}
