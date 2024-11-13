package com.swl.booking.system.response.booking;

import java.io.Serializable;

import com.swl.booking.system.entity.BookingClass; 

import lombok.Data;

@Data
public class BookingClassData implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -2518363505552636230L;

	private Long id;

	private String name;

	private String countryName;

	private int requiredCredits;

	private int availableSlots;

	public BookingClassData(BookingClass data) {
		if (data != null) {
			this.id = data.getId();
			this.name = data.getName() != null ? data.getName() : "Unknown";
			this.countryName = data.getCountry().getName();
			this.requiredCredits = data.getRequiredCredits();
			this.availableSlots = data.getAvailableSlots();
		}
	}
}
