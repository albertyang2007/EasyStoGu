package org.easystogu.scheduler;

import org.easystogu.config.Constants;
import org.easystogu.db.access.table.WSFConfigTableHelper;
import org.easystogu.easymoney.runner.OverAllZiJinLiuAndDDXRunner;
import org.easystogu.log.LogHelper;
import org.easystogu.runner.DailyOverAllRunner;
import org.easystogu.runner.DailyUpdateStockPriceAndDDXRunner;
import org.easystogu.runner.DataBaseSanityCheck;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class DailyScheduler implements SchedulingConfigurer {
	private WSFConfigTableHelper wsfConfig = WSFConfigTableHelper.getInstance();
	private String zone = wsfConfig.getValue("zone", Constants.ZONE_OFFICE);
	private static Logger logger = LogHelper.getLogger(DailyScheduler.class);
	@Autowired
	@Qualifier("taskScheduler")
	private TaskScheduler taskScheduler;

	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskScheduler);
	}

	// refer to:
	// http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger

	// run at 11:31
	@Scheduled(cron = "0 31 11 * * MON-FRI")
	public void _1_DailyOverAllRunner() {
		boolean isGetZiJinLiu = true;
		this.DailyOverAllRunner(isGetZiJinLiu);
	}

	// run at 15:06
	@Scheduled(cron = "0 06 15 * * MON-FRI")
	public void _3_DailyOverAllRunner() {
		boolean isGetZiJinLiu = true;
		this.DailyOverAllRunner(isGetZiJinLiu);
	}

	// run at 21:45
	// @Scheduled(cron = "0 45 21 * * MON-FRI")
	public void _0_DailyZiJinLiuAndDDX() {
		logger.info("OverAllZiJinLiuAndDDXRunner and DDX for all StockId already running.");
		if (Constants.ZONE_OFFICE.equalsIgnoreCase(zone)) {
			OverAllZiJinLiuAndDDXRunner runner = new OverAllZiJinLiuAndDDXRunner();
			runner.resetToAllPage();
			Thread t = new Thread(runner);
			t.start();
		}
	}

	// run at 23:00
	@Scheduled(cron = "0 00 23 * * MON-FRI")
	public void _0_DataBaseSanityCheck() {
		logger.info("DataBaseSanityCheck already running.");
		Thread t = new Thread(new DataBaseSanityCheck());
		t.start();
	}

	// run at 15:06
	// @Scheduled(cron = "0 06 15 * * MON-FRI")
	public void _0_DailyUpdateStockPriceAndDDXRunner() {
		logger.info("DailyUpdateStockPriceAndDDXRunner already running, please check DB result.");
		if (Constants.ZONE_ALIYUN.equalsIgnoreCase(zone)) {
			// daily (download all stockIds price and all zijinliu for ddx)
			DailyUpdateStockPriceAndDDXRunner runner = new DailyUpdateStockPriceAndDDXRunner();
			runner.setFetchAllZiJinLiu(true);
			Thread t = new Thread(runner);
			t.start();
		}
	}

	private void DailyOverAllRunner(boolean isGetZiJinLiu) {
		logger.info("DailyOverAllRunner already running, please check DB result.");
		DailyOverAllRunner runner = new DailyOverAllRunner(isGetZiJinLiu);
		Thread t = new Thread(runner);
		t.start();
	}
}
