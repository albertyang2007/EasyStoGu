package org.easystogu.runner;

import org.easystogu.cache.runner.AllCacheRunner;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DailyOverAllRunner {
	private static Logger logger = LogHelper.getLogger(DailyOverAllRunner.class);
	@Autowired
	private DailyScheduleActionRunner dailyScheduleActionRunner;
	@Autowired
	private DailyUpdateAllStockRunner dailyUpdateAllStockRunner;
	@Autowired
	private AllCacheRunner allCacheRunner;

	public void run() {
		logger.info("start overall runner");
		dailyScheduleActionRunner.run();
		dailyUpdateAllStockRunner.run();
		allCacheRunner.refreshAll();
		logger.info("stop overall runner");
	}
}
