package com.swl.booking.system.request.user;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserProfileRequest implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -2901161642229321197L;

	@NotBlank(message = "Phone no is required")
	private String phno;
}
