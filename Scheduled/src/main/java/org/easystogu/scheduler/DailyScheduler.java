package org.easystogu.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.easymoney.runner.OverAllZiJinLiuAndDDXRunner;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.runner.DailyOverAllRunner;
import org.easystogu.runner.DailyUpdateStockPriceAndDDXRunner;
import org.easystogu.runner.DataBaseSanityCheck;
import org.easystogu.sina.runner.history.HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.history.HistoryStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.history.HistoryWeekStockPriceCountAndSaveDBRunner;
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
import org.easystogu.cache.ConfigurationServiceCache;

@Configuration
@EnableScheduling
public class DailyScheduler implements SchedulingConfigurer {
	private static Logger logger = LogHelper.getLogger(DailyScheduler.class);
	private ConfigurationServiceCache config = ConfigurationServiceCache.getInstance();
	private String zone = config.getString("zone", Constants.ZONE_OFFICE);
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

	// sometime the stockprice has problem and will miss data or chuquan is not
	// 每周更新一下stockprice，每次选择一部分
	// please do not change the SAT-SUN, will impact the selected stockId
	// 请不要随意更改这个时间，跟选出的stockid算法有关。
	// run at 02:00 every day
	@Scheduled(cron = "0 00 02 * * ?")
	private void DailyUpdateStockPriceByBatch() {
		logger.info("DailyUpdateStockPriceByBatch already running, please check DB result.");
		if (Constants.ZONE_ALIYUN.equalsIgnoreCase(zone)) {

			List<String> allStockIds = companyInfoHelper.getAllStockId();
			List<String> stockIds = new ArrayList<String>();
			// weekN is 1 ~ 53
			int weekN = WeekdayUtil.getWeekNumber();
			// offWeekN is 0 ~ 9
			int offWeekN = weekN % 10;
			for (String stockId : allStockIds) {
				int lastDig = Integer.parseInt(stockId.substring(5));
				if (lastDig == offWeekN) {
					stockIds.add(stockId);
					// System.out.println(stockId);
				}
			}

			logger.info("DailyUpdateStockPriceByBatch select stockId with last dig: " + offWeekN + ", size: "
					+ stockIds.size());
			new HistoryStockPriceDownloadAndStoreDBRunner().countAndSave(stockIds);
			new HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner().countAndSave(stockIds);
			new HistoryWeekStockPriceCountAndSaveDBRunner().countAndSave(stockIds);
			
			//after update price, do the sanity test
			new DataBaseSanityCheck().run();
		}
	}

	public static void main(String[] args) {
		new DailyScheduler().DailyUpdateStockPriceByBatch();
	}
}
