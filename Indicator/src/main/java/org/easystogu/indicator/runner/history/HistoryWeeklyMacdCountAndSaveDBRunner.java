package org.easystogu.indicator.runner.history;

import org.easystogu.db.access.IndWeekMacdTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;

public class HistoryWeeklyMacdCountAndSaveDBRunner extends HistoryMacdCountAndSaveDBRunner {

	public HistoryWeeklyMacdCountAndSaveDBRunner() {
		macdTable = IndWeekMacdTableHelper.getInstance();
		stockPriceTable = WeekStockPriceTableHelper.getInstance();
		needChuQuan = false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		HistoryWeeklyMacdCountAndSaveDBRunner runner = new HistoryWeeklyMacdCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}
}
