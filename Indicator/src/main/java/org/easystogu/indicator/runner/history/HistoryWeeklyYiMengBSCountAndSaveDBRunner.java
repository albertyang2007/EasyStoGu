package org.easystogu.indicator.runner.history;

import org.easystogu.db.access.table.IndWeekYiMengBSTableHelper;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.springframework.stereotype.Component;

@Component
public class HistoryWeeklyYiMengBSCountAndSaveDBRunner extends HistoryYiMengBSCountAndSaveDBRunner {
	public HistoryWeeklyYiMengBSCountAndSaveDBRunner() {
		yiMengBSTable = IndWeekYiMengBSTableHelper.getInstance();
		qianFuQuanStockPriceTable = WeekStockPriceTableHelper.getInstance();
	}

	public void mainWork(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		this.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002194");
	}

}
