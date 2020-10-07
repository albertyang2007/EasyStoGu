package org.easystogu.portal;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/portal/scheduler")
public class SchedulerEndPoint {
	private static Logger logger = LogHelper.getLogger(SchedulerEndPoint.class);
	@Autowired
	private ConfigurationServiceCache config;
	@Autowired
	private DailyOverAllRunner dailyOverAllRunner;
	@Autowired
	private DataBaseSanityCheck dataBaseSanityCheck;
	@Autowired
	private CompanyInfoFileHelper companyInfoHelper;
	@Autowired
	private HistoryStockPriceDownloadAndStoreDBRunner historyStockPriceDownloadAndStoreDBRunner;
	@Autowired
	private HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner historyQianFuQuanStockPriceDownloadAndStoreDBRunner;
	@Autowired
	private HistoryWeekStockPriceCountAndSaveDBRunner historyWeekStockPriceCountAndSaveDBRunner;

	@GetMapping("/DailyOverAllRunner")
	public String dailyOverAllRunner() {
		String zone = config.getString("zone", "");
		if (!Constants.ZONE_OFFICE.equals(zone)) {
			return zone + " not allow to run this method.";
		}
		
		new Thread(() -> dailyOverAllRunner.run()).start();
		return "DailyOverAllRunner already running, please check DB result.";

	}

	@GetMapping("/DailyOverAllRunnerAndSanity")
	public String dailyOverAllRunnerAndSanity() {
		String zone = config.getString("zone", "");
		if (!Constants.ZONE_OFFICE.equals(zone)) {
			return zone + " not allow to run this method.";
		}

		new Thread(() -> {
			dailyOverAllRunner.run();
			dataBaseSanityCheck.run();
		}).start();
		return "DailyOverAllRunnerAndSanity already running, please check DB result.";

	}

	@GetMapping("/DataBaseSanityCheck")
	public String dataBaseSanityCheck() {
		String zone = config.getString("zone", "");
		if (!Constants.ZONE_OFFICE.equals(zone)) {
			return zone + " not allow to run this method.";
		}

		new Thread(() -> dataBaseSanityCheck.run()).start();
		return "DataBaseSanityCheck already running, please check DB result.";

	}

	// sometime the stockprice has problem and will miss data or chuquan is not
	// 每周更新一下stockprice，每次选择一部分
	// please do not change the SAT-SUN, will impact the selected stockId
	// 请不要随意更改这个时间，跟选出的stockid算法有关。
	@GetMapping("/DailyUpdateStockPriceByBatch")
	public String dailyUpdateStockPriceByBatch() {
		String zone = config.getString("zone", "");

		if (!config.getBoolean(Constants.DailyUpdateStockPriceByBatch, false)) {
			return "dailyUpdateStockPriceByBatch is false, not allow to run.";
		}

		if (!Constants.ZONE_OFFICE.equalsIgnoreCase(zone)) {
			return zone + " not allow to run this method.";
		}

		String startDate = "2000-01-01";// 1990-01-01
		String endDate = WeekdayUtil.currentDate();

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

		return "DailyUpdateStockPriceByBatch already running, please check DB result.";

	}
}
