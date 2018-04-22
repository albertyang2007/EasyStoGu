package org.easystogu.utils;

import java.util.Comparator;

public class StringComparator implements Comparator<Object> {

	public int compare(Object arg0, Object arg1) {
		String vo1 = (String) arg0;
		String vo2 = (String) arg1;

		return vo1.compareTo(vo2);
	}
}
