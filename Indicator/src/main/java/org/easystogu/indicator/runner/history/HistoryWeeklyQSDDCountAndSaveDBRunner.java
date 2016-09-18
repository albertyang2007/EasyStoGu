package org.easystogu.indicator.runner.history;

import org.easystogu.db.access.table.IndWeekQSDDTableHelper;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;

public class HistoryWeeklyQSDDCountAndSaveDBRunner extends HistoryQSDDCountAndSaveDBRunner {
	public HistoryWeeklyQSDDCountAndSaveDBRunner() {
		qsddTable = IndWeekQSDDTableHelper.getInstance();
		qianFuQuanStockPriceTable = WeekStockPriceTableHelper.getInstance();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		HistoryWeeklyQSDDCountAndSaveDBRunner runner = new HistoryWeeklyQSDDCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}
}
