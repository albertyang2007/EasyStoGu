package org.easystogu.runner;

public class DailyOverAllRunner implements Runnable {

	public void run() {
		// TODO Auto-generated method stub
		new DailyUpdateOverAllRunner().run();
		new PreEstimateStockPriceRunner().run();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new DailyOverAllRunner().run();
	}

}
