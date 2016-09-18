package org.easystogu.indicator.runner.history;

import org.easystogu.db.access.table.IndWeekShenXianTableHelper;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;

public class HistoryWeeklyShenXianCountAndSaveDBRunner extends HistoryShenXianCountAndSaveDBRunner {
	public HistoryWeeklyShenXianCountAndSaveDBRunner() {
        shenXianTable = IndWeekShenXianTableHelper.getInstance();
        qianFuQuanStockPriceTable = WeekStockPriceTableHelper.getInstance();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
    	CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
        HistoryWeeklyShenXianCountAndSaveDBRunner runner = new HistoryWeeklyShenXianCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("600750");
    }
}
