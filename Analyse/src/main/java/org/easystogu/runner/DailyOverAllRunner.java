package org.easystogu.runner;

public class DailyOverAllRunner implements Runnable {
	public void run() {
		// TODO Auto-generated method stub
		String[] args = null;
		DailyUpdateAllStockRunner.main(args);
		PreEstimateStockPriceRunner.main(args);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new DailyOverAllRunner().run();
	}
}
