package org.easystogu.indicator.runner;

public class AllDailyIndCountAndSaveDBRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// day
		DailyMacdCountAndSaveDBRunner.main(args);
		DailyKDJCountAndSaveDBRunner.main(args);
		DailyBollCountAndSaveDBRunner.main(args);
		DailyShenXianCountAndSaveDBRunner.main(args);
		// week
		DailyWeekMacdCountAndSaveDBRunner.main(args);
		DailyWeekKDJCountAndSaveDBRunner.main(args);
		DailyWeekBollCountAndSaveDBRunner.main(args);
		DailyWeekShenXianCountAndSaveDBRunner.main(args);
	}

}
