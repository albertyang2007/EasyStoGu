package org.easystogu.indicator.runner.history;

import org.easystogu.db.access.table.IndWeekBollTableHelper;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;

public class HistoryWeeklyBollCountAndSaveDBRunner extends HistoryBollCountAndSaveDBRunner {
    public HistoryWeeklyBollCountAndSaveDBRunner() {
        bollTable = IndWeekBollTableHelper.getInstance();
        qianFuQuanStockPriceTable = WeekStockPriceTableHelper.getInstance();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
        HistoryWeeklyBollCountAndSaveDBRunner runner = new HistoryWeeklyBollCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("600750");
    }
}
