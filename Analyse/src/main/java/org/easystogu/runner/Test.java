package org.easystogu.runner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		String digits = Double.toString(56.304).replace(".", "");
		List<Integer> digs = new ArrayList<Integer>();
		for (int index = 0; index < digits.length(); index++) {
			char ch = digits.charAt(index);
			digs.add(new Integer(ch - '0'));
		}
		Collections.sort(digs);
		System.out.println(digs);
	}

}
