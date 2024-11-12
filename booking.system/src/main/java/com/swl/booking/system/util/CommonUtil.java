package com.swl.booking.system.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

	public static Date calculateExpiryDate(int hours) {
		return new Date(System.currentTimeMillis() + hours * 60 * 60 * 1000);
	}

	public static String dateToString(String format, Date date) {
		if (date == null) {
			return "";
		}
		if (!validString(format)) {
			format = CommonConstant.STD_DATE_TIME_FORMAT;
		}
		return new SimpleDateFormat(format).format(date);
	}

	public static boolean validString(String value) {
		return value != null && !value.trim().isEmpty();
	}
}
