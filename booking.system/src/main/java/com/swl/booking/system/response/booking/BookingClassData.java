package com.swl.booking.system.response.booking;

import java.io.Serializable;

import com.swl.booking.system.entity.BookingClass;
import com.swl.booking.system.util.CommonConstant;
import com.swl.booking.system.util.CommonUtil;

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

	private String startDate;

	private String expiryDate;

	public BookingClassData() {
		super();
	}

	public BookingClassData(BookingClass data) {
		if (data != null) {
			this.id = data.getId();
			this.name = data.getName() != null ? data.getName() : "Unknown";
			this.countryName = data.getCountry().getName();
			this.requiredCredits = data.getRequiredCredits();
			this.availableSlots = data.getAvailableSlots();
			this.startDate = CommonUtil.dateToString(CommonConstant.STD_DATE_FORMAT, data.getStartDate());
			this.expiryDate = CommonUtil.dateToString(CommonConstant.STD_DATE_FORMAT, data.getExpiryDate());

		}
	}
}
