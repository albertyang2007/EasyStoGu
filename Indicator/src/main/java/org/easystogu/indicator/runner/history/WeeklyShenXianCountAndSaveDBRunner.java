package org.easystogu.indicator.runner.history;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndWeekShenXianTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;

public class WeeklyShenXianCountAndSaveDBRunner extends HistoryShenXianCountAndSaveDBRunner {
	public WeeklyShenXianCountAndSaveDBRunner() {
        shenXianTable = IndWeekShenXianTableHelper.getInstance();
        stockPriceTable = WeekStockPriceTableHelper.getInstance();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
        WeeklyShenXianCountAndSaveDBRunner runner = new WeeklyShenXianCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("600750");
    }
}
