package org.easystogu.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class BeanConfiguration {
	@Bean("taskSchedulerThreadPool")
	public ThreadPoolTaskScheduler taskSchedulerThreadPool() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(5);
		threadPoolTaskScheduler.setThreadNamePrefix("DailyScheduler-Thread-Pool");
		return threadPoolTaskScheduler;
	}
}
