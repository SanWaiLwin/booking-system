package com.swl.booking.system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import com.swl.booking.system.entity.WaitingList;

@Repository
public interface WaitingListRepository extends JpaRepository<WaitingList, Long> {

	List<WaitingList> findByUserId(Long id); 

}
