package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndWeekMai1Mai2TableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.multirunner.MultThreadRunner;
import org.easystogu.utils.WeekdayUtil;

public class DailyWeekMai1Mai2CountAndSaveDBRunner extends DailyMai1Mai2CountAndSaveDBRunner {
    public DailyWeekMai1Mai2CountAndSaveDBRunner(MultThreadRunner parentRunner) {
        super(parentRunner);
    }
    
	public DailyWeekMai1Mai2CountAndSaveDBRunner() {
		stockPriceTable = WeekStockPriceTableHelper.getInstance();
		mai1mai2Table = IndWeekMai1Mai2TableHelper.getInstance();
	}

	@Override
	public void deleteMai1Mai2(String stockId, String date) {
		List<String> dates = WeekdayUtil.getWeekWorkingDates(date);
		// first clean one tuple in week_stockprice table
		// loop all this week's date, in fact, only one tuple match and del
		for (String d : dates) {
			mai1mai2Table.delete(stockId, d);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		DailyWeekMai1Mai2CountAndSaveDBRunner runner = new DailyWeekMai1Mai2CountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600825");
	}

}
