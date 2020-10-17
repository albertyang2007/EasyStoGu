package org.easystogu.sina.runner.history;

import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockPriceHistoryOverAllRunner {
    private static Logger logger = LogHelper.getLogger(StockPriceHistoryOverAllRunner.class);
	@Autowired
	private HistoryStockPriceDownloadAndStoreDBRunner historyStockPriceDownloadAndStoreDBRunner;
	@Autowired
	private HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner historyQianFuQuanStockPriceDownloadAndStoreDBRunner;
	@Autowired
	private HistoryWeekStockPriceCountAndSaveDBRunner historyWeekStockPriceCountAndSaveDBRunner;

	public void run(String startDate, String endDate) {
	    logger.info("StockPriceHistoryOverAllRunner startDate=" + startDate + " and endDate=" + endDate);
	    long ts = System.currentTimeMillis();
		// had better clean all the data from DB
		// history stock price
		historyStockPriceDownloadAndStoreDBRunner.mainWork(new String[] { startDate, endDate });
		// hou fuquan history price
		// HistoryHouFuQuanStockPriceDownloadAndStoreDBRunner.main(args);
		// qian fuquan history price
		historyQianFuQuanStockPriceDownloadAndStoreDBRunner.mainWork(null);
		// count week price
		historyWeekStockPriceCountAndSaveDBRunner.mainWork(null);
		
		logger.info("StockPriceHistoryOverAllRunner end, spent mins: {}", (System.currentTimeMillis() - ts)/(1000 * 60));
	}
}
