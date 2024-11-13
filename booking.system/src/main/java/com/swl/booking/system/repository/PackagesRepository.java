package com.swl.booking.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swl.booking.system.entity.Packages;

@Repository
public interface PackagesRepository extends JpaRepository<Packages, Long> {

	List<Packages> findByCountryId(Long countryId);

}
