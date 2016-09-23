package org.easystogu.scheduler;

import org.easystogu.easymoney.runner.DailyZhuLiJingLiuRuRunner;
import org.easystogu.easymoney.runner.DailyZiJinLiuRunner;
import org.easystogu.log.LogHelper;
import org.easystogu.runner.DailyOverAllRunner;
import org.easystogu.runner.DailyUpdateAllStockRunner;
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
	private static Logger logger = LogHelper.getLogger(DailyScheduler.class);
	@Autowired
	@Qualifier("taskScheduler")
	private TaskScheduler taskScheduler;

	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskScheduler);
	}

	// refer to:
	// http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger

	// run at 10:32
	// @Scheduled(cron = "0 32 10 * * MON-FRI")
	public void _0_DailyOverAllRunner() {
		boolean isGetZiJinLiu = false;
		this.DailyOverAllRunner(isGetZiJinLiu);
	}

	// run at 11:32
	@Scheduled(cron = "0 32 11 * * MON-FRI")
	public void _1_DailyOverAllRunner() {
		boolean isGetZiJinLiu = true;
		this.DailyOverAllRunner(isGetZiJinLiu);
	}

	// run at 14:02
	// @Scheduled(cron = "0 02 14 * * MON-FRI")
	public void _2_DailyOverAllRunner() {
		boolean isGetZiJinLiu = false;
		this.DailyOverAllRunner(isGetZiJinLiu);
	}

	// run at 15:06
	@Scheduled(cron = "0 06 15 * * MON-FRI")
	public void _3_DailyOverAllRunner() {
		boolean isGetZiJinLiu = true;
		this.DailyOverAllRunner(isGetZiJinLiu);
	}

	// run at 21:30
	@Scheduled(cron = "0 30 21 * * MON-FRI")
	public void _0_DailyZiJinLiuAndDDX() {
		logger.info("DailyZiJinLiuRunner and DDX for all StockId already running.");
		DailyZiJinLiuRunner runner = new DailyZiJinLiuRunner();
		runner.resetToAllPage();
		Thread t = new Thread(runner);
		t.start();
	}

	// run at 23:00
	@Scheduled(cron = "0 00 23 * * MON-FRI")
	public void _0_DataBaseSanityCheck() {
		logger.info("DataBaseSanityCheck already running.");
		Thread t = new Thread(new DataBaseSanityCheck());
		t.start();
	}

	private void DailyOverAllRunner(boolean isGetZiJinLiu) {
		logger.info("DailyOverAllRunner already running, please check DB result.");
		DailyOverAllRunner runner = new DailyOverAllRunner(isGetZiJinLiu);
		Thread t = new Thread(runner);
		t.start();
	}
}
