package com.swl.booking.system.service;
 
import com.swl.booking.system.request.booking.ViewAvaiableClassRequest;
import com.swl.booking.system.response.booking.ViewAvaiableClassResponse;

public interface BookingClassService {

	ViewAvaiableClassResponse getByFilter(ViewAvaiableClassRequest req); 

}
