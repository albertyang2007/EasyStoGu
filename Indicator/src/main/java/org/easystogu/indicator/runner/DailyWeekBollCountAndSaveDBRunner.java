package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndWeekBollTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.utils.WeekdayUtil;

public class DailyWeekBollCountAndSaveDBRunner extends DailyBollCountAndSaveDBRunner {
    public DailyWeekBollCountAndSaveDBRunner() {
        bollTable = IndWeekBollTableHelper.getInstance();
        stockPriceTable = WeekStockPriceTableHelper.getInstance();
    }

    @Override
    public void deleteBoll(String stockId, String date) {
        List<String> dates = WeekdayUtil.getWeekWorkingDates(date);
        // first clean one tuple in week_stockprice table
        // loop all this week's date, in fact, only one tuple match and del
        for (String d : dates) {
            bollTable.delete(stockId, d);
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
        DailyWeekBollCountAndSaveDBRunner runner = new DailyWeekBollCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("002214");
    }
}
