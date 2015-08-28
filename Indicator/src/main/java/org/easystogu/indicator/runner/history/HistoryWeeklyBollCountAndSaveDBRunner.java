package org.easystogu.indicator.runner.history;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndWeekBollTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;

public class HistoryWeeklyBollCountAndSaveDBRunner extends HistoryBollCountAndSaveDBRunner {
    public HistoryWeeklyBollCountAndSaveDBRunner() {
        bollTable = IndWeekBollTableHelper.getInstance();
        stockPriceTable = WeekStockPriceTableHelper.getInstance();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
        HistoryWeeklyBollCountAndSaveDBRunner runner = new HistoryWeeklyBollCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("600750");
    }
}
