package org.easystogu.indicator.runner.history;

import org.easystogu.db.access.IndWeekBollTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;

public class HistoryWeeklyBollCountAndSaveDBRunner extends HistoryBollCountAndSaveDBRunner {
    public HistoryWeeklyBollCountAndSaveDBRunner() {
        bollTable = IndWeekBollTableHelper.getInstance();
        stockPriceTable = WeekStockPriceTableHelper.getInstance();
        needChuQuan = false;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
        HistoryWeeklyBollCountAndSaveDBRunner runner = new HistoryWeeklyBollCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("600750");
    }
}
