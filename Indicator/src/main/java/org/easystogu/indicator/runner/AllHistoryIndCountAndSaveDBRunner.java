package org.easystogu.indicator.runner;

import org.easystogu.indicator.runner.history.HistoryBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryQSDDCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryShenXianCountAndSaveDBRunner;

public class AllHistoryIndCountAndSaveDBRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HistoryMacdCountAndSaveDBRunner.main(args);
		HistoryKDJCountAndSaveDBRunner.main(args);
		HistoryBollCountAndSaveDBRunner.main(args);
		// HistoryMai1Mai2CountAndSaveDBRunner.main(args);
		HistoryShenXianCountAndSaveDBRunner.main(args);
		// HistoryXueShi2CountAndSaveDBRunner.main(args);
		HistoryQSDDCountAndSaveDBRunner.main(args);
	}
}
