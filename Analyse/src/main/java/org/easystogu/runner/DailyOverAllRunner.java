package org.easystogu.runner;

public class DailyOverAllRunner implements Runnable {

	public boolean isGetZiJinLiu = true;

	public DailyOverAllRunner(boolean isGetZiJinLiu) {
		this.isGetZiJinLiu = isGetZiJinLiu;
	}

	public void run() {
		new DailyScheduleActionRunner().run();
		new DailyUpdateAllStockRunner(this.isGetZiJinLiu).run();
		new RecentlySelectionRunner().run();
	}

	public static void main(String[] args) {
		new DailyOverAllRunner(false).run();
	}
}
