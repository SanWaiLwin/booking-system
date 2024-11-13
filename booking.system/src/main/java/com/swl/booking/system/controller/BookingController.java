package com.swl.booking.system.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swl.booking.system.request.ApiRequest;
import com.swl.booking.system.request.booking.BookClassRequest;
import com.swl.booking.system.request.booking.ViewAvaiableClassRequest;
import com.swl.booking.system.response.ApiResponse;
import com.swl.booking.system.response.booking.ViewAvaiableClassResponse;
import com.swl.booking.system.service.BookingClassService;
import com.swl.booking.system.service.BookingService;
import com.swl.booking.system.util.CommonConstant;

import jakarta.validation.Valid;

@RestController
@Validated
@RequestMapping("/api/auth/booking")
public class BookingController {

	private final BookingService bookingService;

	private final BookingClassService bookingClassService;

	public BookingController(BookingService bookingService, BookingClassService bookingClassService) {
		this.bookingService = bookingService;
		this.bookingClassService = bookingClassService;
	}

	@PostMapping("/get-class")
	public ApiResponse<ViewAvaiableClassResponse> viewAvaiableClass(
			@Valid @RequestBody ApiRequest<ViewAvaiableClassRequest> apiRequest) {
		ViewAvaiableClassRequest req = apiRequest.getData();
		ViewAvaiableClassResponse resp = bookingClassService.getByFilter(req);
		return new ApiResponse<>(CommonConstant.MSG_PREFIX_SUCCESS, "Avaiable Class", resp);
	}

	@PostMapping("/book-class")
	public ApiResponse<String> bookClass(@Valid @RequestBody ApiRequest<BookClassRequest> apiRequest) {
		BookClassRequest req = apiRequest.getData();
		bookingService.bookClass(req);
		return new ApiResponse<>(CommonConstant.MSG_PREFIX_SUCCESS, "Booking class is successful.");
	} 
	
	@PostMapping("/get-waiting-list")
	public ApiResponse<String> bookClass() { 
//		bookingService.bookClass(req);
		return new ApiResponse<>(CommonConstant.MSG_PREFIX_SUCCESS, "Booking class is successful.");
	} 
}
