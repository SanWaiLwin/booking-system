package com.swl.booking.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swl.booking.system.entity.BookingClass;

@Repository
public interface BookingClassRepository extends JpaRepository<BookingClass, Long> {

	List<BookingClass> findByCountryId(Long countryId);

}
