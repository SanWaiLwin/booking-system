package com.swl.booking.system.request.packages;

import java.io.Serializable;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PurchasePackageRequest implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -5713800812270040507L;

	@NotNull(message = "Package id is required")
	@Min(value = 1, message = "Package id must be greater than or equal to 1")
	private Long packageId;
}
