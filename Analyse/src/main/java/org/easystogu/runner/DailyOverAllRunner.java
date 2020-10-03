package org.easystogu.runner;

import org.easystogu.cache.runner.AllCacheRunner;
import org.springframework.stereotype.Component;

@Component
public class DailyOverAllRunner implements Runnable {

	public boolean isGetZiJinLiu = true;

	public DailyOverAllRunner(boolean isGetZiJinLiu) {
		this.isGetZiJinLiu = isGetZiJinLiu;
	}

	public void run() {
		new DailyScheduleActionRunner().run();
		new DailyUpdateAllStockRunner(this.isGetZiJinLiu).run();
		//new RecentlySelectionRunner().run();
		new AllCacheRunner().refreshAll();
	}
}
