package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.IndWeekKDJTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.WeekdayUtil;

public class DailyWeekKDJCountAndSaveDBRunner extends DailyKDJCountAndSaveDBRunner {   
    public DailyWeekKDJCountAndSaveDBRunner() {
        qianFuQuanStockPriceTable = WeekStockPriceTableHelper.getInstance();
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
    	CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
        DailyWeekKDJCountAndSaveDBRunner runner = new DailyWeekKDJCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("002327");
    }
}
