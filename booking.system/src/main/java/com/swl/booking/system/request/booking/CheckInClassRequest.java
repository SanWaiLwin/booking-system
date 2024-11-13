package com.swl.booking.system.request.booking;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckInClassRequest implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -6315884728259608901L;

	@NotNull(message = "Booking id is required")
	@Min(value = 1, message = "Booking id  must be greater than or equal to 1")
	private Long bookingId;
}
