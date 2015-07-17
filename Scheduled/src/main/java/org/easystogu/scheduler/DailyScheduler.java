package org.easystogu.scheduler;

import org.easystogu.log.LogHelper;
import org.easystogu.runner.DailyUpdateOverAllRunner;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableAsync
@EnableScheduling
public class DailyScheduler implements SchedulingConfigurer {
	private static Logger logger = LogHelper.getLogger(DailyScheduler.class);
	@Autowired
	@Qualifier("dailyScheduler")
	private TaskScheduler dailyScheduler;

	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(dailyScheduler);
	}

	// refer to:
	// http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger
	@Scheduled(cron = "0 0/31 9-16 * * MON-FRI")
	public void every30MinutesUpdateOverAllRunner() {
		Thread t = new Thread(new DailyUpdateOverAllRunner());
		t.start();
		logger.info("every30MinutesUpdateOverAllRunner already running, please check folder result.");
		System.out
				.println("every30MinutesUpdateOverAllRunner already running, please check folder result.");
	}
}
