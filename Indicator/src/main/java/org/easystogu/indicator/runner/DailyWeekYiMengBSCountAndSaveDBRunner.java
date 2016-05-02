package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.IndWeekYiMengBSTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.WeekdayUtil;

public class DailyWeekYiMengBSCountAndSaveDBRunner extends DailyYiMengBSCountAndSaveDBRunner {

	public DailyWeekYiMengBSCountAndSaveDBRunner() {
		stockPriceTable = WeekStockPriceTableHelper.getInstance();
		yiMengBSTable = IndWeekYiMengBSTableHelper.getInstance();
	}

	@Override
	public void deleteYiMengBS(String stockId, String date) {
		List<String> dates = WeekdayUtil.getWeekWorkingDates(date);
		// first clean one tuple in week_stockprice table
		// loop all this week's date, in fact, only one tuple match and del
		for (String d : dates) {
			yiMengBSTable.delete(stockId, d);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		DailyWeekYiMengBSCountAndSaveDBRunner runner = new DailyWeekYiMengBSCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002194");
	}
}
