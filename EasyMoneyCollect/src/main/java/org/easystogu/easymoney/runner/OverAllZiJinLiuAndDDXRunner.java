package org.easystogu.easymoney.runner;

public class OverAllZiJinLiuAndDDXRunner implements Runnable {
	private boolean fetchAllZiJinLiu = false;

	public void resetToAllPage() {
		fetchAllZiJinLiu = true;
	}

	public void run() {
		System.out.println("Fetch ZiJinLiu all.");
		DailyZiJinLiuRunner runner1 = new DailyZiJinLiuRunner();
		if (this.fetchAllZiJinLiu) {
			runner1.resetToAllPage();
		}
		runner1.countAndSaved();

		System.out.println("Count DDX for all.");
		new DailyDDXRunner().countAndSaved();
	}

	public static void main(String[] args) {
		OverAllZiJinLiuAndDDXRunner runner = new OverAllZiJinLiuAndDDXRunner();
		runner.run();
	}
}
