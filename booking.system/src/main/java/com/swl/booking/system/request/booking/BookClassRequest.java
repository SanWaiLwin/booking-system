package com.swl.booking.system.request.booking;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookClassRequest implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -535331053687737746L;

	@NotNull(message = "Booking class id is required")
	@Min(value = 1, message = "Booking class id  must be greater than or equal to 1")
	private Long bookingClassId;
}
