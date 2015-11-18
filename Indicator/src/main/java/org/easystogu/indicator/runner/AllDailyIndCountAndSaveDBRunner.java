package org.easystogu.indicator.runner;

import java.util.List;

public class AllDailyIndCountAndSaveDBRunner {
	public void runDailyIndForStockIds(List<String> stockIds) {
		// day ind
		new DailyMacdCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyKDJCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyBollCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyMai1Mai2CountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyShenXianCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyXueShi2CountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyZhuliJinChuCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyYiMengBSCountAndSaveDBRunner().countAndSaved(stockIds);
	}

	public void runDailyWeekIndForStockIds(List<String> stockIds) {
		// week ind
		new DailyWeekMacdCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyWeekKDJCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyWeekBollCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyWeekMai1Mai2CountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyWeekShenXianCountAndSaveDBRunner().countAndSaved(stockIds);
		new DailyWeekYiMengBSCountAndSaveDBRunner().countAndSaved(stockIds);
	}
}
