package org.easystogu.indicator.runner.history;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndWeekKDJTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;

public class HistoryWeeklyKDJCountAndSaveDBRunner extends HistoryKDJCountAndSaveDBRunner {
    public HistoryWeeklyKDJCountAndSaveDBRunner() {
        kdjTable = IndWeekKDJTableHelper.getInstance();
        stockPriceTable = WeekStockPriceTableHelper.getInstance();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
        HistoryWeeklyKDJCountAndSaveDBRunner runner = new HistoryWeeklyKDJCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("600750");
    }
}
