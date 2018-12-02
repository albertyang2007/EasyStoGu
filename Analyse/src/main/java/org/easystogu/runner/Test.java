package org.easystogu.runner;

public class Test {
	public static void main(String[] args) {
		Boolean isDr = null;
		if (isDr == null)
			System.out.println("null");
		else System.out.println(isDr);
		
		isDr = true;
		
		if(isDr == false)
			System.out.println("false");
		
		if (isDr == null)
			System.out.println("null");
		
		boolean rtn = Boolean.TRUE.equals(isDr) ? true : false;
		System.out.println("rtn:"+rtn);
	}

}
