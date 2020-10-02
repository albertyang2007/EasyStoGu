package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.WeekdayUtil;
import org.springframework.stereotype.Component;

@Component
public class DailyWeekKDJCountAndSaveDBRunner extends DailyKDJCountAndSaveDBRunner {
	public DailyWeekKDJCountAndSaveDBRunner() {
		kdjTable = DBAccessFacdeFactory.getInstance(Constants.indWeekKDJ);
		qianFuQuanStockPriceTable = WeekStockPriceTableHelper.getInstance();
	}

	@Override
	public void deleteKDJ(String stockId, String date) {
		List<String> dates = WeekdayUtil.getWeekWorkingDates(date);
		// first clean one tuple in week_stockprice table
		// loop all this week's date, in fact, only one tuple match and del
		for (String d : dates) {
			kdjTable.delete(stockId, d);
		}
	}

	public void mainWork(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		this.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002327");
	}
}
