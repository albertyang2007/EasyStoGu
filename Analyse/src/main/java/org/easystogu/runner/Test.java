package org.easystogu.runner;

public class Test {
	public static void main(String[] args) {
		String stockId = "600020";
		String stockIds = "sh000001,sz399001,sz399006";
		if (!stockId.equals("999999") && !stockId.equals("399001") && !stockId.equals("399006")) {
			if (stockId.startsWith("6")) {
				stockIds += ",sh" + stockId;
			} else if (stockId.startsWith("0") || stockId.startsWith("3")) {
				stockIds += ",sz" + stockId;
			}
		}
		System.out.println(stockIds);
	}

}
