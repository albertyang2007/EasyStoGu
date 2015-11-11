package org.easystogu.indicator.runner.history;

import org.easystogu.db.access.IndWeekYiMengBSTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;

public class HistoryWeeklyYiMengBSCountAndSaveDBRunner extends HistoryYiMengBSCountAndSaveDBRunner {
	public HistoryWeeklyYiMengBSCountAndSaveDBRunner() {
		yiMengBSTable = IndWeekYiMengBSTableHelper.getInstance();
		stockPriceTable = WeekStockPriceTableHelper.getInstance();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		HistoryWeeklyYiMengBSCountAndSaveDBRunner runner = new HistoryWeeklyYiMengBSCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002194");
	}

}
