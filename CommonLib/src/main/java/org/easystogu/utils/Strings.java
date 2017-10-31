package org.easystogu.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strings {
	public static Pattern p = Pattern.compile("\\s+");
	public static String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";

	public static String replaceAllSpaceToSingleSpace(String str) {
		Matcher m = p.matcher(str);
		return m.replaceAll(" ");
	}

	public static boolean isDateValidate(String date) {
		if (date != null && Pattern.matches(dateRegex, date)) {
			return true;
		}

		return false;
	}

	public static boolean isEmpty(String string) {
		return (string == null) || (string.isEmpty());
	}

	public static boolean isNotEmpty(String string) {
		return (string != null) && (!string.isEmpty());
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static double convert2ScaleDecimal(double num) {
		if (Double.isNaN(num) || Double.isInfinite(num)) {
			return 0;
		}
		BigDecimal bd = new BigDecimal(num);
		num = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return num;
	}

	public static double convert2ScaleDecimal(double num, int scale) {
		if (Double.isNaN(num) || Double.isInfinite(num)) {
			return 0;
		}
		BigDecimal bd = new BigDecimal(num);
		num = bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		return num;
	}

	public static String convert2ScaleDecimalAlignWithLength(double num, int scale) {
		double d = convert2ScaleDecimal(num, scale);
		String s = Double.toString(d);
		String[] arr = s.split("\\.");
		if (arr.length == 2) {
		    if (arr[0].length() == 1) {
                return " " + s;
            }
			if (arr[1].length() == 1) {
				return s + "0";
			}
		}
		return s;
	}

	// convert from string to date and then to time
	// dateStr is like:yyyy-MM-dd HH:mm:ss
	public static long stringToTime(String dateStr) {
		Date date = new Date();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(dateStr);
			return date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0l;
	}

	// date is like: 2015-11-09
	public static boolean isDateSelected(String date1, String date2, String aDate) {
		if ((stringToTime(aDate) >= stringToTime(date1)) && (stringToTime(aDate) <= stringToTime(date2))) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		System.out.println(convert2ScaleDecimalAlignWithLength(9.0, 2));
	}
}
