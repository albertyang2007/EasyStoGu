package org.easystogu.sina.runner.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockPriceHistoryOverAllRunner {
	@Autowired
	private HistoryStockPriceDownloadAndStoreDBRunner historyStockPriceDownloadAndStoreDBRunner;
	@Autowired
	private HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner historyQianFuQuanStockPriceDownloadAndStoreDBRunner;
	@Autowired
	private HistoryWeekStockPriceCountAndSaveDBRunner historyWeekStockPriceCountAndSaveDBRunner;

	public void run(String startDate, String endDate) {
		// had better clean all the data from DB
		// history stock price
		historyStockPriceDownloadAndStoreDBRunner.mainWork(new String[] { startDate, endDate });
		// hou fuquan history price
		// HistoryHouFuQuanStockPriceDownloadAndStoreDBRunner.main(args);
		// qian fuquan history price
		historyQianFuQuanStockPriceDownloadAndStoreDBRunner.mainWork(null);
		// count week price
		historyWeekStockPriceCountAndSaveDBRunner.mainWork(null);

	}
}
