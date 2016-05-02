package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.IndWeekMacdTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.WeekdayUtil;

public class DailyWeekMacdCountAndSaveDBRunner extends DailyMacdCountAndSaveDBRunner {

    public DailyWeekMacdCountAndSaveDBRunner() {
        macdTable = IndWeekMacdTableHelper.getInstance();
        stockPriceTable = WeekStockPriceTableHelper.getInstance();
    }

    @Override
    public void deleteMacd(String stockId, String date) {
        List<String> dates = WeekdayUtil.getWeekWorkingDates(date);
        // first clean one tuple in week_stockprice table
        // loop all this week's date, in fact, only one tuple match and del
        for (String d : dates) {
            macdTable.delete(stockId, d);
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
        DailyWeekMacdCountAndSaveDBRunner runner = new DailyWeekMacdCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("002327");
    }

}
