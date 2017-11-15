package org.easystogu.analyse.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DigitInOrderHelper {
	// 11.11
	public static boolean allDigitsSame(double d) {
		List<Integer> list = doubleToOrderDigis(d);
		int first = list.get(0);
		for (int i = 1; i < list.size(); i++) {
			if (first != list.get(i)) {
				return false;
			}
		}
		return true;
	}

	// 12.34, or 76.45, or 46.82
	public static boolean digitsInOrder(double d, int inc) {
		List<Integer> list = doubleToOrderDigis(d);
		for (int i = 0; i < list.size() - 1; i++) {
			if ((list.get(i) + inc) != list.get(i + 1)) {
				return false;
			}
		}
		return true;
	}

	// 56.56, or 76.67
	public static boolean sameAtTwoSides(double d) {
		String digits = Double.toString(d).replace(".", "");
		if (digits.length() != 4) {
			return false;
		}
		char[] twoParts = digits.toCharArray();
		if (twoParts[0] == twoParts[2] && twoParts[1] == twoParts[3]) {
			return true;
		}
		if (twoParts[0] == twoParts[3] && twoParts[1] == twoParts[2]) {
			return true;
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
		// System.out.println(digs);
		return digs;
	}

	public static boolean checkAll(double d) {
		if (allDigitsSame(d))
			return true;
		if (digitsInOrder(d, 1))
			return true;
		if (digitsInOrder(d, 2))
			return true;
		if (digitsInOrder(d, 3))
			return true;
		if (sameAtTwoSides(d))
			return true;
		return false;
	}

	public static void main(String[] args) {
		System.out.println(allDigitsSame(11.11));
		System.out.println(digitsInOrder(21.43, 1));
		System.out.println(digitsInOrder(31.75, 2));
		System.out.println(digitsInOrder(42.86, 2));
		System.out.println(digitsInOrder(240.68, 2));
		System.out.println(sameAtTwoSides(56.56));
		System.out.println(sameAtTwoSides(56.65));
		System.out.println(sameAtTwoSides(56.75));
	}
}
