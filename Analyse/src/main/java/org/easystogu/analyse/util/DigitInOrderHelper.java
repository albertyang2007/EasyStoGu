package org.easystogu.analyse.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DigitInOrderHelper {
	// 11.11
	public static boolean allDigitsSame(List<Integer> list) {
		if (list.size() < 4)
			return false;

		int first = list.get(0);
		for (int i = 1; i < list.size(); i++) {
			if (first != list.get(i)) {
				return false;
			}
		}
		return true;
	}

	// 12.34, or 76.45, or 46.82, or 123.45
	public static boolean digitsInOrder(List<Integer> list, int inc) {

		if (list.size() < 4)
			return false;

		for (int i = 0; i < list.size() - 1; i++) {
			if ((list.get(i) + inc) != list.get(i + 1)) {
				return false;
			}
		}
		return true;
	}

	// 56.56, or 76.67, or 121.21, 351.53, 351.35
	public static boolean sameAtTwoSides(double d) {
		String digits = Double.toString(d).replace(".", "");
		if (digits.length() == 4) {
			char[] twoParts = digits.toCharArray();
			if (twoParts[0] == twoParts[2] && twoParts[1] == twoParts[3]) {
				return true;
			}
			if (twoParts[0] == twoParts[3] && twoParts[1] == twoParts[2]) {
				return true;
			}
		} else if (digits.length() == 5) {
			char[] twoParts = digits.toCharArray();
			if (twoParts[0] == twoParts[4] && twoParts[1] == twoParts[3]) {
				return true;
			}
			if (twoParts[0] == twoParts[3] && twoParts[1] == twoParts[4]) {
				return true;
			}
		}

		return false;
	}

	public static List<Integer> doubleToOrderDigis(double d) {
		String digits = Double.toString(d).replace(".", "");
		List<Integer> digs = new ArrayList<Integer>();
		for (int index = 0; index < digits.length(); index++) {
			char ch = digits.charAt(index);
			digs.add(new Integer(ch - '0'));
		}
		Collections.sort(digs);
		return digs;
	}

	public static boolean checkAll(double d) {
		List<Integer> list = doubleToOrderDigis(d);
		if (allDigitsSame(list))
			return true;
		if (digitsInOrder(list, 1))
			return true;
		if (digitsInOrder(list, 2))
			return true;
		if (digitsInOrder(list, 3))
			return true;
		if (sameAtTwoSides(d))
			return true;
		return false;
	}

	public static void main(String[] args) {
		System.out.println(checkAll(11.11));
		System.out.println(checkAll(21.43));
		System.out.println(checkAll(31.75));
		System.out.println(checkAll(42.86));
		System.out.println(checkAll(56.56));
		System.out.println(checkAll(56.65));
		System.out.println(checkAll(240.68));
		System.out.println(checkAll(121.21));
		System.out.println(checkAll(351.53));
		System.out.println(checkAll(351.35));
	}
}
