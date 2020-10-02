package org.easystogu.indicator.runner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AllDailyIndCountAndSaveDBRunner {
	@Autowired
	DailyMacdCountAndSaveDBRunner dailyMacdCountAndSaveDBRunner;
	@Autowired
	DailyKDJCountAndSaveDBRunner dailyKDJCountAndSaveDBRunner;
	@Autowired
	DailyBollCountAndSaveDBRunner dailyBollCountAndSaveDBRunner;
	@Autowired
	DailyShenXianCountAndSaveDBRunner dailyShenXianCountAndSaveDBRunner;
	@Autowired
	DailyQSDDCountAndSaveDBRunner dailyQSDDCountAndSaveDBRunner;
	@Autowired
	DailyWRCountAndSaveDBRunner dailyWRCountAndSaveDBRunner;
	@Autowired
	DailyMACountAndSaveDBRunner dailyMACountAndSaveDBRunner;
	@Autowired
	DailyWeekMacdCountAndSaveDBRunner dailyWeekMacdCountAndSaveDBRunner;
	@Autowired
	DailyWeekKDJCountAndSaveDBRunner dailyWeekKDJCountAndSaveDBRunner;
	
	public void runDailyIndForStockIds(List<String> stockIds) {
		// day ind
		dailyMacdCountAndSaveDBRunner.countAndSaved(stockIds);
		dailyKDJCountAndSaveDBRunner.countAndSaved(stockIds);
		dailyBollCountAndSaveDBRunner.countAndSaved(stockIds);
		dailyShenXianCountAndSaveDBRunner.countAndSaved(stockIds);
		dailyQSDDCountAndSaveDBRunner.countAndSaved(stockIds);
		dailyWRCountAndSaveDBRunner.countAndSaved(stockIds);
		dailyMACountAndSaveDBRunner.countAndSaved(stockIds);
	}

	public void runDailyWeekIndForStockIds(List<String> stockIds) {
		// week ind
		dailyWeekMacdCountAndSaveDBRunner.countAndSaved(stockIds);
		dailyWeekKDJCountAndSaveDBRunner.countAndSaved(stockIds);
	}
}
