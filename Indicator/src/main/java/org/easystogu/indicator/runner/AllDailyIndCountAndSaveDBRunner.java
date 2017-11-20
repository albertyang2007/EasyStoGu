package org.easystogu.indicator.runner;

import java.util.List;

public class AllDailyIndCountAndSaveDBRunner {
	public void runDailyIndForStockIds(List<String> stockIds) {
		// day ind
		new DailyMacdCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyKDJCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyBollCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyShenXianCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyQSDDCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyWRCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyMACountAndSaveDBRunner().countAndSaved(stockIds);
	}

	public void runDailyWeekIndForStockIds(List<String> stockIds) {
		// week ind
		new DailyWeekMacdCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyWeekKDJCountAndSaveDBRunner().countAndSaved(stockIds);
	}
}
