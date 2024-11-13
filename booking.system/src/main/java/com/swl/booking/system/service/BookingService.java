package com.swl.booking.system.service;

import com.swl.booking.system.request.booking.BookClassRequest;
import com.swl.booking.system.request.booking.CancelClassRequest;
import com.swl.booking.system.request.booking.CheckInClassRequest;
import com.swl.booking.system.response.booking.ViewAvaiableClassResponse;

public interface BookingService {

	void bookClass(BookClassRequest req);

	ViewAvaiableClassResponse getMyClassList();

	void cancelClass(CancelClassRequest req);

	void checkInClass(CheckInClassRequest req); 

}
