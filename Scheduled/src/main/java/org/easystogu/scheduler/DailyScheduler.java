package org.easystogu.scheduler;

import org.easystogu.log.LogHelper;
import org.easystogu.runner.DailyUpdateOverAllRunner;
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
	// run at 9:59,10:59
	@Scheduled(cron = "0 59 9-10 * * MON-FRI")
	public void morningUpdateOverAllRunner() {
		this.DailyUpdateOverAllRunner();
	}

	// run at 13:30, 14:30
	@Scheduled(cron = "0 30 13-14 * * MON-FRI")
	public void afternoonUpdateOverAllRunner() {
		this.DailyUpdateOverAllRunner();
	}

	// run at 11:35
	@Scheduled(cron = "0 35 11 * * MON-FRI")
	public void noonUpdateOverAllRunner() {
		this.DailyUpdateOverAllRunner();
	}

	// run at 15:05
	@Scheduled(cron = "0 05 15 * * MON-FRI")
	public void FinallyUpdateOverAllRunner() {
		this.DailyUpdateOverAllRunner();
	}

	private void DailyUpdateOverAllRunner() {
		logger.info("DailyUpdateOverAllRunner already running, please check folder result.");
		System.out
				.println("DailyUpdateOverAllRunner already running, please check folder result.");

		Thread t = new Thread(new DailyUpdateOverAllRunner());
		t.start();
	}

	// @Scheduled(cron = "0 0/5 9-20 * * MON-FRI")
	// public void test1() {
	// logger.info("test 1: every 5 mins during 9-20 between MON-FRI");
	// System.out.println("test 1: every 5 mins during 9-20 between MON-FRI");
	// }

	// @Scheduled(fixedDelay = 60 * 1000)
	// public void test2() {
	// logger.info("test 2: every 1 mins");
	// System.out.println("test 2: every 1 mins");
	// }
}
