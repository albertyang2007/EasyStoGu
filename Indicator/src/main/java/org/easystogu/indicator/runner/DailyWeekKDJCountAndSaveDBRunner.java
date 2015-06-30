package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndWeekKDJTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.multirunner.DayMultThreadRunner;
import org.easystogu.utils.WeekdayUtil;

public class DailyWeekKDJCountAndSaveDBRunner extends DailyKDJCountAndSaveDBRunner {
    public DailyWeekKDJCountAndSaveDBRunner(DayMultThreadRunner parentRunner) {
        super(parentRunner);
    }
    
    public DailyWeekKDJCountAndSaveDBRunner() {
        stockPriceTable = WeekStockPriceTableHelper.getInstance();
        kdjTable = IndWeekKDJTableHelper.getInstance();
    }

    @Override
    public void deleteKDJ(String stockId, String date) {
        List<String> dates = WeekdayUtil.getWeekWorkingDates(date);
        // first clean one tuple in week_stockprice table
        // loop all this week's date, in fact, only one tuple match and del
        for (String d : dates) {
            kdjTable.delete(stockId, d);
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
        DailyWeekKDJCountAndSaveDBRunner runner = new DailyWeekKDJCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("002327");
    }
}
