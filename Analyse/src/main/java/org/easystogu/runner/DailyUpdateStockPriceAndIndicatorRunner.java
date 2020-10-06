package org.easystogu.runner;

import java.util.List;

import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.runner.AllDailyIndCountAndSaveDBRunner;
import org.easystogu.log.LogHelper;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner2;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DailyUpdateStockPriceAndIndicatorRunner {
	private static Logger logger = LogHelper.getLogger(DailyUpdateStockPriceAndIndicatorRunner.class);
	@Autowired
	private CompanyInfoFileHelper stockConfig;
	@Autowired
	private DailyStockPriceDownloadAndStoreDBRunner2 dailyStockPriceDownloadAndStoreDBRunner2;
	@Autowired
	private AllDailyIndCountAndSaveDBRunner allDailyIndCountAndSaveDBRunner;
	@Autowired
	private DailyWeeklyStockPriceCountAndSaveDBRunner dailyWeeklyStockPriceCountAndSaveDBRunner;

	public void run() {
		logger.info("start DailyUpdateStockPriceAndIndicatorRunner");
		String[] args = null;
		long st = System.currentTimeMillis();
		List<String> allStockIds = stockConfig.getAllStockId();
		// day (download all stockIds price)
		dailyStockPriceDownloadAndStoreDBRunner2.mainWork(args);
		// day ind
		allDailyIndCountAndSaveDBRunner.runDailyIndForStockIds(allStockIds);
		// week
		dailyWeeklyStockPriceCountAndSaveDBRunner.countAndSave(allStockIds);
		// week ind
		allDailyIndCountAndSaveDBRunner.runDailyWeekIndForStockIds(allStockIds);

		logger.info("stop DailyUpdateStockPriceAndIndicatorRunner using " + (System.currentTimeMillis() - st) / 1000 + " seconds");
	}
}
