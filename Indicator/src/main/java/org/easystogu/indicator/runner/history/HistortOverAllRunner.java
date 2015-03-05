package org.easystogu.indicator.runner.history;

public class HistortOverAllRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//day
		HistoryMacdCountAndSaveDBRunner.main(args);
		HistoryKDJCountAndSaveDBRunner.main(args);
		HistoryBollCountAndSaveDBRunner.main(args);
		//week
		WeeklyMacdCountAndSaveDBRunner.main(args);
		WeeklyKDJCountAndSaveDBRunner.main(args);
		WeeklyBollCountAndSaveDBRunner.main(args);
	}

}
