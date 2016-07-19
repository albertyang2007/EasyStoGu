package org.easystogu.runner;

import java.util.regex.Pattern;
//test on line:
//https://regex101.com/r/bS7jM5/17#javascript
public class Test {
	public String pattern0 = "^((([0-9]|[a-z]|[A-Z]){1,},)|(tel:[0-9]{1,},)|(range:[0-9]{1,}-[0-9]{1,},)){0,}((([0-9]|[a-z]|[A-Z]){1,})|(tel:[0-9]{1,})|(range:[0-9]{1,}-[0-9]{1,}))$";
	public String pattern1 = "^(((?=[^\\s])[a-zA-Z0-9\\s]+(?<=\\S),)|(tel:[0-9]{1,},)|(range:[0-9]{1,}-[0-9]{1,},)){0,}((?=[^\\s])[a-zA-Z0-9\\s]+(?<=\\S)|(tel:[0-9]{1,})|(range:[0-9]{1,}-[0-9]{1,}))$";

	public void verify() {
		String name1 = "my sms,tel:123456,tel:654321,tel:888888,range:333333-555555,myms,yoursms";
		String name2 = "M";
		
		if (Pattern.matches(pattern0, name1)) {
			System.out.println("0 match");
		} else {
			System.out.println("0 not match");
		}
		
		if (Pattern.matches(pattern0, name2)) {
			System.out.println("0 match");
		} else {
			System.out.println("0 not match");
		}
		
		if (Pattern.matches(pattern1, name1)) {
			System.out.println("1 match");
		} else {
			System.out.println("1 not match");
		}

		if (Pattern.matches(pattern1, name2)) {
			System.out.println("2 match");
		} else {
			System.out.println("2 not match");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Test().verify();
	}

}
