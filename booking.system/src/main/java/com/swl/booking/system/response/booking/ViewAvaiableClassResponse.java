package com.swl.booking.system.response.booking;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class ViewAvaiableClassResponse implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -7118324710551542198L;

	private List<BookingClassData> classList;

}
