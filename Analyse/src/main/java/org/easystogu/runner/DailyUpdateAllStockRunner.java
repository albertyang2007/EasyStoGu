package org.easystogu.runner;

import java.util.List;

import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.runner.AllDailyIndCountAndSaveDBRunner;
import org.easystogu.log.LogHelper;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner2;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DailyUpdateAllStockRunner {
	private static Logger logger = LogHelper.getLogger(DailyUpdateAllStockRunner.class);
	@Autowired
	private CompanyInfoFileHelper stockConfig;
	@Autowired
	private DailySelectionRunner dailySelectionRunner;
	@Autowired
	private DailyStockPriceDownloadAndStoreDBRunner2 dailyStockPriceDownloadAndStoreDBRunner2;
	@Autowired
	private AllDailyIndCountAndSaveDBRunner allDailyIndCountAndSaveDBRunner;
	@Autowired
	private DailyWeeklyStockPriceCountAndSaveDBRunner dailyWeeklyStockPriceCountAndSaveDBRunner;
	@Autowired
	private DailyViewAnalyseRunner dailyViewAnalyseRunner;
	@Autowired
	@Qualifier("stockPriceTable")
	protected StockPriceTableHelper stockPriceTable;

	public void run() {
		logger.info("start DailyUpdateAllStockRunner");
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

		// analyse by java code
		String latestDate = stockPriceTable.getLatestStockDate();
		boolean addToScheduleActionTable = true;
		boolean checkDayPriceEqualWeekPrice = true;
		dailySelectionRunner.runForStockIds(allStockIds, latestDate, addToScheduleActionTable, checkDayPriceEqualWeekPrice);

		// alaylse by view names
		dailyViewAnalyseRunner.run();

		logger.info("stop DailyUpdateAllStockRunner using " + (System.currentTimeMillis() - st) / 1000 + " seconds");
	}
}
