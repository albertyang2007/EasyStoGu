package org.easystogu.utils;

import java.math.BigDecimal;
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
		num = bd.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		return num;
	}
}
