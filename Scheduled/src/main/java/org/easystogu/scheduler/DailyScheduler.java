package org.easystogu.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.config.Constants;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.runner.DailyOverAllRunner;
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

@Configuration
@EnableScheduling
public class DailyScheduler implements SchedulingConfigurer {
	private static Logger logger = LogHelper.getLogger(DailyScheduler.class);
	@Autowired
	protected CompanyInfoFileHelper companyInfoHelper;
	@Autowired
	private DailyOverAllRunner dailyOverAllRunner;
	@Autowired
	private DataBaseSanityCheck dataBaseSanityCheck;
	@Autowired
	private HistoryStockPriceDownloadAndStoreDBRunner historyStockPriceDownloadAndStoreDBRunner;
	@Autowired
	private HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner historyQianFuQuanStockPriceDownloadAndStoreDBRunner;
	@Autowired
	private HistoryWeekStockPriceCountAndSaveDBRunner historyWeekStockPriceCountAndSaveDBRunner;
	@Autowired
	private ConfigurationServiceCache config;

	@Autowired
	@Qualifier("taskSchedulerThreadPool")
	private TaskScheduler taskScheduler;

	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskScheduler);
	}

	// refer to:
	// http://www.quartz-scheduler.org/documentation/quartz-1.x/tutorials/crontrigger

	// run at 11:31 DailyOverAllRunner
	@Scheduled(cron = "0 31 11 * * MON-FRI")
	public void _1_DailyOverAllRunner() {
		if (Constants.ZONE_OFFICE.equalsIgnoreCase(config.getString("zone", Constants.ZONE_OFFICE))) {
			logger.info("DailyOverAllRunner already running, please check DB result.");
			new Thread(() -> dailyOverAllRunner.run()).start();
		}
	}

	// run at 15:06 DailyOverAllRunner
	@Scheduled(cron = "0 06 15 * * MON-FRI")
	public void _3_DailyOverAllRunner() {
		if (Constants.ZONE_OFFICE.equalsIgnoreCase(config.getString("zone", Constants.ZONE_OFFICE))) {
			Runnable runnable = () -> {
				dailyOverAllRunner.run();
				dataBaseSanityCheck.run();
			};
			Thread t = new Thread(runnable);
			t.start();
		}
	}

	// run at 18:00
	// @Scheduled(cron = "0 00 18 * * MON-FRI")
	public void _0_DataBaseSanityCheck() {
		// only run at office, since at aliyun, there is daily santy after price
		// update
		if (Constants.ZONE_OFFICE.equalsIgnoreCase(config.getString("zone", Constants.ZONE_OFFICE))) {
			logger.info("DataBaseSanityCheck already running.");
			new Thread(() -> dataBaseSanityCheck.run()).start();
		}
	}

	// sometime the stockprice has problem and will miss data or chuquan is not
	// 每周更新一下stockprice，每次选择一部分
	// please do not change the SAT-SUN, will impact the selected stockId
	// 请不要随意更改这个时间，跟选出的stockid算法有关。
	// run at 20:00 every day
	// @Scheduled(cron = "0 00 20 * * ?")
	public void DailyUpdateStockPriceByBatch() {
		if (!config.getBoolean(Constants.DailyUpdateStockPriceByBatch, false)) {
			logger.info("dailyUpdateStockPriceByBatch is false, not run.");
			return;
		}

		String startDate = "2000-01-01";// 1990-01-01
		String endDate = WeekdayUtil.currentDate();

		if (Constants.ZONE_ALIYUN.equalsIgnoreCase(config.getString("zone", Constants.ZONE_OFFICE))) {
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

			Runnable runnable = () -> {
				historyStockPriceDownloadAndStoreDBRunner.countAndSave(stockIds, startDate, endDate);
				historyQianFuQuanStockPriceDownloadAndStoreDBRunner.countAndSave(stockIds);
				historyWeekStockPriceCountAndSaveDBRunner.countAndSave(stockIds);

				// after update price, do the sanity test
				dataBaseSanityCheck.run();
			};
			Thread t = new Thread(runnable);
			t.start();
		}
	}
}
