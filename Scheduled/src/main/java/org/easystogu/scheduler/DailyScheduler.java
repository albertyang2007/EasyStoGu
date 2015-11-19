package org.easystogu.scheduler;

import org.easystogu.easymoney.runner.DailyZiJinLiuRunner;
import org.easystogu.log.LogHelper;
import org.easystogu.runner.DailyUpdateAllStockRunner;
import org.easystogu.runner.DailyUpdateEstimateStockRunner;
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
	@Scheduled(cron = "0 32 10 * * MON-FRI")
	public void _0_DailyUpdateEstimateStockRunner() {
		this.DailyUpdateEstimateStockRunner();
	}

	// run at 11:32
	@Scheduled(cron = "0 32 11 * * MON-FRI")
	public void _0_DailyUpdateAllStockRunner() {
		this.DailyUpdateAllStockRunner();
	}

	// run at 13:42
	@Scheduled(cron = "0 42 13 * * MON-FRI")
	public void _1_DailyUpdateEstimateStockRunner() {
		this.DailyUpdateEstimateStockRunner();
	}

	// run at 14:35
	@Scheduled(cron = "0 35 14 * * MON-FRI")
	public void _2_DailyUpdateEstimateStockRunner() {
		this.DailyUpdateEstimateStockRunner();
	}

	// run at 15:02
	@Scheduled(cron = "0 02 15 * * MON-FRI")
	public void _0_PreEstimateStockPriceRunner() {
		this.PreEstimateStockPriceRunner();
	}

	// run at 21:00
	@Scheduled(cron = "0 00 21 * * MON-FRI")
	public void _0_DailyZiJinLiuRunner() {
		this.DailyZiJinLiuRunner();
	}

	// run at 22:00
	@Scheduled(cron = "0 00 22 * * MON-FRI")
	public void _0_DataBaseSanityCheck() {
		logger.info("DataBaseSanityCheck already running.");
		Thread t = new Thread(new DataBaseSanityCheck());
		t.start();
	}
	
	private void DailyUpdateEstimateStockRunner() {
		logger.info("DailyUpdateEstimateStockRunner already running, please check folder result.");
		Thread t = new Thread(new DailyUpdateEstimateStockRunner());
		t.start();
	}
	
	private void DailyUpdateAllStockRunner(){
	    logger.info("DailyUpdateAllStockRunner already running, please check folder result.");
        Thread t = new Thread(new DailyUpdateAllStockRunner());
        t.start();
	}

	private void DailyZiJinLiuRunner() {
		logger.info("DailyZiJinLiuRunner already running, please check DB result.");
		DailyZiJinLiuRunner runner = new DailyZiJinLiuRunner();
		runner.resetToAllPage();
		Thread t = new Thread(runner);
		t.start();
	}
	
	private void PreEstimateStockPriceRunner() {
        logger.info("PreEstimateStockPriceRunner already running, please check DB result.");
        PreEstimateStockPriceRunner runner = new PreEstimateStockPriceRunner();
        Thread t = new Thread(runner);
        t.start();
    }
}
