package com.swl.booking.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import com.swl.booking.system.entity.WaitingList;

@Repository
public interface WaitingListRepository extends JpaRepository<WaitingList, Long> { 

}
