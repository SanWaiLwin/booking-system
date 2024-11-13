package com.swl.booking.system.response.booking;

import java.io.Serializable;

import lombok.Data;

@Data
public class WaitingListData implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -1683626035749095635L;

	private Long id;
	
	private String className;
	
	private String countryName; 
	
	private String expiryDate;
	
	private String waitingStartDate;
}
