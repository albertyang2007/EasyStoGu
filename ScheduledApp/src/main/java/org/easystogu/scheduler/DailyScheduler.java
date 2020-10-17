package org.easystogu.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableScheduling
public class DailyScheduler implements SchedulingConfigurer {
	private static Logger logger = LoggerFactory.getLogger(DailyScheduler.class);
	private static WebClient webClient = WebClient.create("http://easystogu-portal:8080");

	@Autowired
	@Qualifier("taskSchedulerThreadPool")
	private TaskScheduler taskScheduler;

	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskScheduler);
	}

	// run at 11:31 DailyOverAllRunner
	@Scheduled(cron = "0 31 11 * * MON-FRI")
	public void dailyOverAllRunnerMorning() {
		logger.info("dailyOverAllRunnerMorning start");
		webClient.get()
		    .uri("/portal/scheduler/DailyOverAllRunner", 10L)
		    .accept(MediaType.TEXT_HTML)
		    .exchange()
            .flatMap( rtn -> {
              logger.info("status code is :{}", rtn.statusCode());
              return rtn.bodyToMono(String.class);
            }).subscribe( v -> logger.info("response is: {}", v));
		
		logger.info("dailyOverAllRunnerMorning stop, waiting async handling result.");
	}

	// run at 15:06 DailyOverAllRunner
	@Scheduled(cron = "0 06 15 * * MON-FRI")
	public void dailyOverAllRunnerAndSanityAfternoon() {
		logger.info("dailyOverAllRunnerAndSanityAfternoon start");
		webClient.get()
		    .uri("/portal/scheduler/DailyOverAllRunnerAndSanity", 10L)
		    .accept(MediaType.TEXT_HTML)
            .exchange()
            .flatMap( rtn -> {
              logger.info("status code is :{}", rtn.statusCode());
              return rtn.bodyToMono(String.class);
            }).subscribe( v -> logger.info("response is: {}", v));
		
		logger.info("dailyOverAllRunnerAndSanityAfternoon stop, waiting async handling result.");
	}

	// run at 19:00
	@Scheduled(cron = "0 00 19 * * MON-FRI")
	public void dataBaseSanityCheck() {
		logger.info("dataBaseSanityCheck start");
		webClient.get()
		    .uri("/portal/scheduler/DataBaseSanityCheck", 10L)
		    .accept(MediaType.TEXT_HTML)
            .exchange()
            .flatMap( rtn -> {
              logger.info("status code is :{}", rtn.statusCode());
              return rtn.bodyToMono(String.class);
            }).subscribe( v -> logger.info("response is: {}", v));
		
		logger.info("dataBaseSanityCheck stop, waiting async handling result.");
	}

	// sometime the stockprice has problem and will miss data or chuquan is not
	// 每周更新一下stockprice，每次选择一部分
	// please do not change the SAT-SUN, will impact the selected stockId
	// 请不要随意更改这个时间，跟选出的stockid算法有关。
	// run at 21:00 every day
	@Scheduled(cron = "0 00 21 * * ?")
	public void dailyUpdateStockPriceByBatch() {
		logger.info("dailyUpdateStockPriceByBatch start");
		webClient.get()
		    .uri("/portal/scheduler/DailyUpdateStockPriceByBatch", 10L)
		    .accept(MediaType.TEXT_HTML)
            .exchange()
            .flatMap( rtn -> {
              logger.info("status code is :{}", rtn.statusCode());
              return rtn.bodyToMono(String.class);
            }).subscribe( v -> logger.info("response is: {}", v));
		
		logger.info("dailyUpdateStockPriceByBatch stop, waiting async handling result.");
	}
}
