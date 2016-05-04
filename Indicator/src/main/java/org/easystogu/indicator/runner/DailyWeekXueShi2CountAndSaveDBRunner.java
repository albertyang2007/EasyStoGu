package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.IndWeekXueShi2TableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.WeekdayUtil;

public class DailyWeekXueShi2CountAndSaveDBRunner extends DailyXueShi2CountAndSaveDBRunner {
	public DailyWeekXueShi2CountAndSaveDBRunner() {
		xueShi2Table = IndWeekXueShi2TableHelper.getInstance();
		stockPriceTable = WeekStockPriceTableHelper.getInstance();
		needChuQuan = false;
	}

	@Override
	public void deleteXueShi2(String stockId, String date) {
		List<String> dates = WeekdayUtil.getWeekWorkingDates(date);
		// first clean one tuple in week_stockprice table
		// loop all this week's date, in fact, only one tuple match and del
		for (String d : dates) {
			xueShi2Table.delete(stockId, d);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		DailyWeekXueShi2CountAndSaveDBRunner runner = new DailyWeekXueShi2CountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002214");
	}

}
