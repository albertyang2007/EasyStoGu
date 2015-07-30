package org.easystogu.indicator.runner.history;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndWeekKDJTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;

public class WeeklyKDJCountAndSaveDBRunner extends HistoryKDJCountAndSaveDBRunner {
    public WeeklyKDJCountAndSaveDBRunner() {
        kdjTable = IndWeekKDJTableHelper.getInstance();
        stockPriceTable = WeekStockPriceTableHelper.getInstance();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
        WeeklyKDJCountAndSaveDBRunner runner = new WeeklyKDJCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("600750");
    }
}
