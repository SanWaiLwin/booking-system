package com.swl.booking.system.response.packages;

import java.io.Serializable; 

import lombok.Data;

@Data
public class PurchasePackageData implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -6737404994557389944L;

	private Long id;

	private String packageName;
	
	private String country;

	private int remainingCredits; 

	private String purchaseDate;
}
