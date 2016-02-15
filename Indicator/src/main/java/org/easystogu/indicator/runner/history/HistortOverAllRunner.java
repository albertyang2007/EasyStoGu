package org.easystogu.indicator.runner.history;

public class HistortOverAllRunner {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// pls limit the startDay to avoid too much time
		// day
		HistoryMacdCountAndSaveDBRunner.main(args);
		HistoryKDJCountAndSaveDBRunner.main(args);
		HistoryBollCountAndSaveDBRunner.main(args);
		HistoryShenXianCountAndSaveDBRunner.main(args);
		HistoryQSDDCountAndSaveDBRunner.main(args);
		// HistoryMai1Mai2CountAndSaveDBRunner.main(args);
		// HistoryXueShi2CountAndSaveDBRunner.main(args);
		// HistoryZhuliJinChuCountAndSaveDBRunner.main(args);
		// week
		HistoryWeeklyMacdCountAndSaveDBRunner.main(args);
		HistoryWeeklyKDJCountAndSaveDBRunner.main(args);
		HistoryWeeklyBollCountAndSaveDBRunner.main(args);
		HistoryWeeklyShenXianCountAndSaveDBRunner.main(args);
		// HistoryWeeklyMai1Mai2CountAndSaveDBRunner.main(args);
		HistoryWeeklyQSDDCountAndSaveDBRunner.main(args);
	}
}
