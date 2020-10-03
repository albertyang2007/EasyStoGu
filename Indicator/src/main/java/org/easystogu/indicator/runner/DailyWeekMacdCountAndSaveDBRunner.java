package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.WeekdayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DailyWeekMacdCountAndSaveDBRunner extends DailyMacdCountAndSaveDBRunner {
	@Autowired
	private DBAccessFacdeFactory dBAccessFacdeFactory;

	public DailyWeekMacdCountAndSaveDBRunner() {
		macdTable = dBAccessFacdeFactory.getInstance(Constants.indWeekMacd);
		qianFuQuanStockPriceTable = WeekStockPriceTableHelper.getInstance();
	}

	@Override
	public void deleteMacd(String stockId, String date) {
		List<String> dates = WeekdayUtil.getWeekWorkingDates(date);
		// first clean one tuple in week_stockprice table
		// loop all this week's date, in fact, only one tuple match and del
		for (String d : dates) {
			macdTable.delete(stockId, d);
		}
	}

	public void mainWork(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		this.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002327");
	}

}
