package org.easystogu.indicator.runner.history;

import java.util.List;

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
        List<String> stockIds = stockConfig.getAllStockId();
        runner.countAndSaved(stockIds);
        //runner.countAndSaved("002194");
    }
}
