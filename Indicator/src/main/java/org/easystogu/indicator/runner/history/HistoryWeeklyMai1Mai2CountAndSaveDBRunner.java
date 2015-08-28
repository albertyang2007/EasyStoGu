package org.easystogu.indicator.runner.history;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndWeekMai1Mai2TableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;

public class HistoryWeeklyMai1Mai2CountAndSaveDBRunner extends HistoryMai1Mai2CountAndSaveDBRunner {
	public HistoryWeeklyMai1Mai2CountAndSaveDBRunner() {
		mai1mai2Table = IndWeekMai1Mai2TableHelper.getInstance();
		stockPriceTable = WeekStockPriceTableHelper.getInstance();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		HistoryWeeklyMai1Mai2CountAndSaveDBRunner runner = new HistoryWeeklyMai1Mai2CountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("600750");
	}

}
