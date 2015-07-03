package org.easystogu.indicator.runner.history;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndWeekMacdTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;

public class WeeklyMacdCountAndSaveDBRunner extends HistoryMacdCountAndSaveDBRunner {

    public WeeklyMacdCountAndSaveDBRunner() {
        macdTable = IndWeekMacdTableHelper.getInstance();
        stockPriceTable = WeekStockPriceTableHelper.getInstance();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
        WeeklyMacdCountAndSaveDBRunner runner = new WeeklyMacdCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("600750");
    }
}
