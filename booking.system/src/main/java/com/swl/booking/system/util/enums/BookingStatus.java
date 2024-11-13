package com.swl.booking.system.util.enums;

import java.util.ArrayList;
import java.util.List;

public enum BookingStatus {

	ACTIVE(1, "Active"), CANCEL(2, "Cancel"), EXPIRY(3, "Expiry"), CHECK_IN(4, "Check in");

	private int code;
	private String desc;

	BookingStatus(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDescByCode(int code) {
		for (BookingStatus dt : values()) {
			if (dt.getCode() == code) {
				return dt.getDesc();
			}
		}
		return "";
	}

	public static List<BookingStatus> getAll() {
		List<BookingStatus> dataList = new ArrayList<>();
		for (BookingStatus t : BookingStatus.values()) {
			dataList.add(t);
		}
		return dataList;
	}
}
