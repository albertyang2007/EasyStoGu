package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndWeekMai1Mai2TableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;

public class WeeklyMai1Mai2CountAndSaveDBRunner extends HistoryMai1Mai2CountAndSaveDBRunner {
	public WeeklyMai1Mai2CountAndSaveDBRunner() {
		mai1mai2Table = IndWeekMai1Mai2TableHelper.getInstance();
		stockPriceTable = WeekStockPriceTableHelper.getInstance();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		WeeklyMai1Mai2CountAndSaveDBRunner runner = new WeeklyMai1Mai2CountAndSaveDBRunner();
		List<String> stockIds = stockConfig.getAllStockId();
		runner.countAndSaved(stockIds);
		//runner.countAndSaved("600359");
	}

}