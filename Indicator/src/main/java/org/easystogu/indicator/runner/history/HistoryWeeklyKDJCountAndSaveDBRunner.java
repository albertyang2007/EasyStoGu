package org.easystogu.indicator.runner.history;

import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HistoryWeeklyKDJCountAndSaveDBRunner extends HistoryKDJCountAndSaveDBRunner {
	@Autowired
	private DBAccessFacdeFactory dBAccessFacdeFactory;

	public HistoryWeeklyKDJCountAndSaveDBRunner() {
		kdjTable = dBAccessFacdeFactory.getInstance(Constants.indWeekKDJ);
		qianFuQuanStockPriceTable = WeekStockPriceTableHelper.getInstance();
	}

	public void mainWork(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		this.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}
}
