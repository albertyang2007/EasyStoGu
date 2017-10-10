package org.easystogu.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cache.runner.AllCacheRunner;
import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.Constants;
import org.easystogu.easymoney.runner.OverAllZiJinLiuAndDDXRunner;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.report.HistoryAnalyseReport;
import org.easystogu.runner.DailyOverAllRunner;
import org.easystogu.runner.DailyUpdateStockPriceAndDDXRunner;
import org.easystogu.runner.DataBaseSanityCheck;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner2;
import org.easystogu.sina.runner.history.HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.history.HistoryStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.history.HistoryWeekStockPriceCountAndSaveDBRunner;
import org.easystogu.database.replicate.DailyReplicateRunner;
import org.easystogu.utils.WeekdayUtil;
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
    private ConfigurationServiceCache config = ConfigurationServiceCache.getInstance();
    private String zone = config.getString("zone", Constants.ZONE_OFFICE);
    private boolean dailyUpdateStockPriceByBatch = config.getBoolean(Constants.DailyUpdateStockPriceByBatch, false);
    private CompanyInfoFileHelper companyInfoHelper = CompanyInfoFileHelper.getInstance();

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
        if (Constants.ZONE_ALIYUN.equalsIgnoreCase(zone)) {
            boolean isGetZiJinLiu = true;
            this.DailyOverAllRunner(isGetZiJinLiu);
        }
    }

    // run at 15:06
    @Scheduled(cron = "0 06 15 * * MON-FRI")
    public void _3_DailyOverAllRunner() {
        if (Constants.ZONE_ALIYUN.equalsIgnoreCase(zone)) {
            boolean isGetZiJinLiu = true;
            this.DailyOverAllRunner(isGetZiJinLiu);
        }
    }

    // run at 16:30
    @Scheduled(cron = "0 30 16 * * MON-FRI")
    public void _3_DailyReplicateRunnerFromAliyun() {
        if (Constants.ZONE_OFFICE.equalsIgnoreCase(zone)) {
            logger.info("DailyReplicateRunnerFromAliyun already running.");
            new DailyReplicateRunner().run();

            // after update price, do the sanity test
            new DataBaseSanityCheck().run();
        }
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

    // run at 18:00
    @Scheduled(cron = "0 00 18 * * MON-FRI")
    public void _0_DataBaseSanityCheck() {
        // only run at office, since at aliyun, there is daily santy after price
        // update
        if (Constants.ZONE_OFFICE.equalsIgnoreCase(zone)) {
            logger.info("DataBaseSanityCheck already running.");
            Thread t = new Thread(new DataBaseSanityCheck());
            t.start();
        }
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

    // sometime the stockprice has problem and will miss data or chuquan is not
    // 每周更新一下stockprice，每次选择一部分
    // please do not change the SAT-SUN, will impact the selected stockId
    // 请不要随意更改这个时间，跟选出的stockid算法有关。
    // run at 20:00 every day
    @Scheduled(cron = "0 00 20 * * ?")
    private void DailyUpdateStockPriceByBatch() {
        if (!dailyUpdateStockPriceByBatch) {
            logger.info("dailyUpdateStockPriceByBatch is false, not run.");
            return;
        }

        if (Constants.ZONE_ALIYUN.equalsIgnoreCase(zone)) {
            logger.info("DailyUpdateStockPriceByBatch already running, please check DB result.");
            List<String> allStockIds = companyInfoHelper.getAllStockId();
            List<String> stockIds = new ArrayList<String>();
            // dayN is 1 ~ 31
            int dayN = Integer.parseInt(WeekdayUtil.currentDay());

            // each day will fetch about 1/30 of all stockIds
            int[] lastTwoDigs = { ((dayN - 1) * 3), ((dayN - 1) * 3 + 1), ((dayN - 1) * 3 + 2) };
            String[] strLastTwoDigs = { "" + lastTwoDigs[0], "" + lastTwoDigs[1], "" + lastTwoDigs[2] };

            // convert '1' to '01', '2' to '02' etc
            for (int i = 0; i < lastTwoDigs.length; i++) {
                if (lastTwoDigs[i] <= 9) {
                    strLastTwoDigs[i] = "0" + lastTwoDigs[i];
                }
            }

            for (String stockId : allStockIds) {
                String lastTwoDig = stockId.substring(4);
                if (lastTwoDig.equals(strLastTwoDigs[0]) || lastTwoDig.equals(strLastTwoDigs[0])
                        || lastTwoDig.equals(strLastTwoDigs[2])) {
                    stockIds.add(stockId);
                }

                // hardocde to add missing stockIds
                if (dayN == 1 && (lastTwoDig.equals("93") || lastTwoDig.equals("94") || lastTwoDig.equals("95")
                        || lastTwoDig.equals("96") || lastTwoDig.equals("97") || lastTwoDig.equals("98")
                        || lastTwoDig.equals("99"))) {
                    stockIds.add(stockId);
                }
            }

            logger.info("DailyUpdateStockPriceByBatch select stockId with last dig: " + strLastTwoDigs[0] + ", "
                    + strLastTwoDigs[1] + ", " + strLastTwoDigs[2] + ", size: " + stockIds.size());
            new HistoryStockPriceDownloadAndStoreDBRunner().countAndSave(stockIds);
            new HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner().countAndSave(stockIds);
            new HistoryWeekStockPriceCountAndSaveDBRunner().countAndSave(stockIds);

            // after update price, do the sanity test
            new DataBaseSanityCheck().run();
        }
    }

    // every 15 mins from 9 to 15, Monday to Friday
    @Scheduled(cron = "0 0/15 09,10,11,13,14 * * MON-FRI")
    public void updateStockPriceOnlyEveryMins() {
        if (Constants.ZONE_ALIYUN.equalsIgnoreCase(zone)) {
            String time = WeekdayUtil.currentTime();
            logger.info("updateStockPriceOnlyEvery15Mins start at " + time);
            if ((time.compareTo("09-25-00") >= 0 && time.compareTo("11-30-00") <= 0)
                    || (time.compareTo("13-00-00") >= 0 && time.compareTo("15-00-00") <= 0)) {
                // day (download all stockIds price)
                DailyStockPriceDownloadAndStoreDBRunner2 runner = new DailyStockPriceDownloadAndStoreDBRunner2();
                runner.run();
                // update cache
                AllCacheRunner cacheRunner = new AllCacheRunner();
                cacheRunner.refreshAll();
                logger.info("updateStockPriceOnlyEvery15Mins stop at " + WeekdayUtil.currentTime());
            }
        }

    }

    // just run onece
    @Scheduled(cron = "0 23 07 * * ?")
    public void JustRunOnce() {
        String time = WeekdayUtil.currentDate();
        if (time.equals("2017-10-10")) {
            List<String> stockIds = companyInfoHelper.getAllStockId();
            logger.info("JustRunOnce for " + stockIds);
            new HistoryStockPriceDownloadAndStoreDBRunner().countAndSave(stockIds);
            new HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner().countAndSave(stockIds);
            new HistoryWeekStockPriceCountAndSaveDBRunner().countAndSave(stockIds);

            // after update price, do the sanity test
            new DataBaseSanityCheck().run();
        }
    }
}
