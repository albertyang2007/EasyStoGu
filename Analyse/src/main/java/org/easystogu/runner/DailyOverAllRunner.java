package org.easystogu.runner;

import org.easystogu.cache.runner.AllCacheRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DailyOverAllRunner {
	@Autowired
	private DailyScheduleActionRunner dailyScheduleActionRunner;
	@Autowired
	private DailyUpdateAllStockRunner dailyUpdateAllStockRunner;
	@Autowired
	private AllCacheRunner allCacheRunner;

	public void run() {
		dailyScheduleActionRunner.run();
		dailyUpdateAllStockRunner.run();
		allCacheRunner.refreshAll();
	}
}
