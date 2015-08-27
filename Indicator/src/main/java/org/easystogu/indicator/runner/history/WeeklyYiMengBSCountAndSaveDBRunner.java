package org.easystogu.indicator.runner.history;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndWeekYiMengBSTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;

public class WeeklyYiMengBSCountAndSaveDBRunner extends HistoryYiMengBSCountAndSaveDBRunner {
	public WeeklyYiMengBSCountAndSaveDBRunner() {
		yiMengBSTable = IndWeekYiMengBSTableHelper.getInstance();
		stockPriceTable = WeekStockPriceTableHelper.getInstance();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		WeeklyYiMengBSCountAndSaveDBRunner runner = new WeeklyYiMengBSCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002194");
	}

}
