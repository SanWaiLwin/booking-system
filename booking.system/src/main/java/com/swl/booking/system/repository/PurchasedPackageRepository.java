package com.swl.booking.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swl.booking.system.entity.PurchasedPackage; 

@Repository
public interface PurchasedPackageRepository extends JpaRepository<PurchasedPackage, Long> {

	List<PurchasedPackage> findByUserId(Long id);

	Optional<PurchasedPackage> findFirstByUserIdOrderByRemainingCreditsDesc(Long id); 

}
