package org.easystogu.indicator.runner;

public class AllDailyIndCountAndSaveDBRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// day
		DailyMacdCountAndSaveDBRunner.main(args);
		DailyKDJCountAndSaveDBRunner.main(args);
		DailyBollCountAndSaveDBRunner.main(args);
		DailyMai1Mai2CountAndSaveDBRunner.main(args);
		DailyShenXianCountAndSaveDBRunner.main(args);
		DailyXueShi2CountAndSaveDBRunner.main(args);
		// week
		DailyWeekMacdCountAndSaveDBRunner.main(args);
		DailyWeekKDJCountAndSaveDBRunner.main(args);
		DailyWeekBollCountAndSaveDBRunner.main(args);
		DailyWeekMai1Mai2CountAndSaveDBRunner.main(args);
		DailyWeekShenXianCountAndSaveDBRunner.main(args);
		// DailyWeekXueShi2CountAndSaveDBRunner.main(args);
	}

}
