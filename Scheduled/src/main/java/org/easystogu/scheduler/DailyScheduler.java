package org.easystogu.scheduler;

import org.easystogu.easymoney.runner.DailyZhuLiJingLiuRuRunner;
import org.easystogu.easymoney.runner.DailyZiJinLiuRunner;
import org.easystogu.log.LogHelper;
import org.easystogu.runner.DailyOverAllRunner;
import org.easystogu.runner.DailyUpdateAllStockRunner;
import org.easystogu.runner.DataBaseSanityCheck;
import org.easystogu.runner.PreEstimateStockPriceRunner;
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
	//@Scheduled(cron = "0 32 10 * * MON-FRI")
	public void _0_DailyOverAllRunner() {
		boolean isGetZiJinLiu = false;
		this.DailyOverAllRunner(isGetZiJinLiu);
	}

	// run at 11:32
	@Scheduled(cron = "0 32 11 * * MON-FRI")
	public void _1_DailyOverAllRunner() {
		boolean isGetZiJinLiu = false;
		this.DailyOverAllRunner(isGetZiJinLiu);
	}

	// run at 14:02
	//@Scheduled(cron = "0 02 14 * * MON-FRI")
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

	// run at 22:00
	@Scheduled(cron = "0 00 22 * * MON-FRI")
	public void _0_DataBaseSanityCheck() {
		logger.info("DataBaseSanityCheck already running.");
		Thread t = new Thread(new DataBaseSanityCheck());
		t.start();
	}

	private void DailyUpdateAllStockRunner(boolean isGetZiJinLiu) {
		logger.info("DailyUpdateAllStockRunner already running, please check folder result.");
		Thread t = new Thread(new DailyUpdateAllStockRunner(isGetZiJinLiu));
		t.start();
	}

	private void DailyZiJinLiuRunner() {
		logger.info("DailyZiJinLiuRunner already running, please check DB result.");
		DailyZiJinLiuRunner runner = new DailyZiJinLiuRunner();
		Thread t = new Thread(runner);
		t.start();

		logger.info("DailyZhuLiJingLiuRuRunner already running, please check DB result.");
		DailyZhuLiJingLiuRuRunner runner2 = new DailyZhuLiJingLiuRuRunner();
		Thread t2 = new Thread(runner2);
		t2.start();
	}

	private void PreEstimateStockPriceRunner() {
		logger.info("PreEstimateStockPriceRunner already running, please check DB result.");
		PreEstimateStockPriceRunner runner = new PreEstimateStockPriceRunner();
		Thread t = new Thread(runner);
		t.start();
	}

	private void DailyOverAllRunner(boolean isGetZiJinLiu) {
		logger.info("DailyOverAllRunner already running, please check DB result.");
		DailyOverAllRunner runner = new DailyOverAllRunner(isGetZiJinLiu);
		Thread t = new Thread(runner);
		t.start();
	}
}
