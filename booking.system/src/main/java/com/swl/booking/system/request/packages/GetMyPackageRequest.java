package com.swl.booking.system.request.packages;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetMyPackageRequest implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 6928296553397715964L;

	@NotNull(message = "User id is required")
	@Min(value = 1, message = "User id  must be greater than or equal to 1")
	private Long userId;
}
