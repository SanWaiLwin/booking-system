package com.swl.booking.system.service;

import com.swl.booking.system.entity.BookingClass;
import com.swl.booking.system.entity.User; 
import com.swl.booking.system.response.booking.WaitingListResponse;

public interface WaitingListService {

	WaitingListResponse getWaitingList();

	void addToWaitingListInNewTransaction(User user, BookingClass bookingClass); 

}
