package org.easystogu.indicator.runner.history;

import org.easystogu.db.access.table.IndWeekMai1Mai2TableHelper;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;

public class HistoryWeeklyMai1Mai2CountAndSaveDBRunner extends HistoryMai1Mai2CountAndSaveDBRunner {
	public HistoryWeeklyMai1Mai2CountAndSaveDBRunner() {
		mai1mai2Table = IndWeekMai1Mai2TableHelper.getInstance();
		qianFuQuanStockPriceTable = WeekStockPriceTableHelper.getInstance();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		HistoryWeeklyMai1Mai2CountAndSaveDBRunner runner = new HistoryWeeklyMai1Mai2CountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("600750");
	}

}
