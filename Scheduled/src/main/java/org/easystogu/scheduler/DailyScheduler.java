package org.easystogu.scheduler;

import org.easystogu.easymoney.runner.DailyZiJinLiuXiangRunner;
import org.easystogu.log.LogHelper;
import org.easystogu.runner.DailyOverAllRunner;
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
    // run at 10:45
    @Scheduled(cron = "0 45 10 * * MON-FRI")
    public void morningUpdateOverAllRunner() {
        this.DailyOverAllRunner();
    }

    // run at 11:32
    @Scheduled(cron = "0 32 11 * * MON-FRI")
    public void noonUpdateOverAllRunner() {
        this.DailyOverAllRunner();
    }

    // run at 14:15
    @Scheduled(cron = "0 15 14 * * MON-FRI")
    public void afternoonUpdateOverAllRunner() {
        this.DailyOverAllRunner();
    }

    // run at 15:02
    @Scheduled(cron = "0 02 15 * * MON-FRI")
    public void FinallyUpdateOverAllRunner() {
        this.DailyOverAllRunner();
    }

    // run at 21:00
    @Scheduled(cron = "0 00 21 * * MON-FRI")
    public void morningUpdateAllZiJinLiuXiangRunner() {
        this.DailyZiJinLiuXiangRunner();
    }

    private void DailyOverAllRunner() {
        logger.info("DailyUpdateOverAllRunner already running, please check folder result.");
        Thread t = new Thread(new DailyOverAllRunner());
        t.start();
    }

    private void DailyZiJinLiuXiangRunner() {
        logger.info("DailyZiJinLiuXiangRunner already running, please check DB result.");
        DailyZiJinLiuXiangRunner runner = new DailyZiJinLiuXiangRunner();
        runner.resetToAllPage();
        Thread t = new Thread(runner);
        t.start();
    }

    // run at 22:00
    @Scheduled(cron = "0 00 22 * * MON-FRI")
    public void dataBaseSanityCheck() {
        logger.info("DataBaseSanityCheck already running.");
        Thread t = new Thread(new DataBaseSanityCheck());
        t.start();
    }
}
