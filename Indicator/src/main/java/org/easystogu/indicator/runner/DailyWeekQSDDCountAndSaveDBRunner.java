package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.IndWeekQSDDTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.WeekdayUtil;

public class DailyWeekQSDDCountAndSaveDBRunner extends DailyQSDDCountAndSaveDBRunner {

	public DailyWeekQSDDCountAndSaveDBRunner() {
		stockPriceTable = WeekStockPriceTableHelper.getInstance();
		qsddTable = IndWeekQSDDTableHelper.getInstance();
		needChuQuan = false;
	}

	@Override
	public void deleteQSDD(String stockId, String date) {
		List<String> dates = WeekdayUtil.getWeekWorkingDates(date);
		// first clean one tuple in week_stockprice table
		// loop all this week's date, in fact, only one tuple match and del
		for (String d : dates) {
			qsddTable.delete(stockId, d);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		DailyWeekQSDDCountAndSaveDBRunner runner = new DailyWeekQSDDCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002327");
	}
}
