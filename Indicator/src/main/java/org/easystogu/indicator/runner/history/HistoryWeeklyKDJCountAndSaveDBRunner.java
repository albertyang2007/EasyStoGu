package org.easystogu.indicator.runner.history;

import org.easystogu.db.access.table.IndWeekKDJTableHelper;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;

public class HistoryWeeklyKDJCountAndSaveDBRunner extends HistoryKDJCountAndSaveDBRunner {
	public HistoryWeeklyKDJCountAndSaveDBRunner() {
		kdjTable = IndWeekKDJTableHelper.getInstance();
		qianFuQuanStockPriceTable = WeekStockPriceTableHelper.getInstance();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		HistoryWeeklyKDJCountAndSaveDBRunner runner = new HistoryWeeklyKDJCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}
}
