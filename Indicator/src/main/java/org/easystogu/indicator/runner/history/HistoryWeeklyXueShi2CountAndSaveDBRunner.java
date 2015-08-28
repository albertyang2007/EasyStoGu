package org.easystogu.indicator.runner.history;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndWeekXueShi2TableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;

public class HistoryWeeklyXueShi2CountAndSaveDBRunner extends HistoryXueShi2CountAndSaveDBRunner {
	public HistoryWeeklyXueShi2CountAndSaveDBRunner() {
		xueShi2Table = IndWeekXueShi2TableHelper.getInstance();
		stockPriceTable = WeekStockPriceTableHelper.getInstance();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		HistoryWeeklyXueShi2CountAndSaveDBRunner runner = new HistoryWeeklyXueShi2CountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002194");
	}

}
