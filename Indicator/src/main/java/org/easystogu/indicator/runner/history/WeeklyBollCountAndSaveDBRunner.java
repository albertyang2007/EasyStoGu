package org.easystogu.indicator.runner.history;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndWeekBollTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;

public class WeeklyBollCountAndSaveDBRunner extends HistoryBollCountAndSaveDBRunner {
    public WeeklyBollCountAndSaveDBRunner() {
        bollTable = IndWeekBollTableHelper.getInstance();
        stockPriceTable = WeekStockPriceTableHelper.getInstance();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
        WeeklyBollCountAndSaveDBRunner runner = new WeeklyBollCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("600750");
    }
}
