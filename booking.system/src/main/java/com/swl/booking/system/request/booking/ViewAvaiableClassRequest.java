package com.swl.booking.system.request.booking;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ViewAvaiableClassRequest implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -947118287298298797L;

	@NotNull(message = "Country id is required")
	@Min(value = 1, message = "Country id  must be greater than or equal to 1")
	private Long countryId;
}
