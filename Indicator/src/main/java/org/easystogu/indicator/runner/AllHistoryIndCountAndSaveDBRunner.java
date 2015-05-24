package org.easystogu.indicator.runner;

import org.easystogu.indicator.runner.history.HistoryBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryXueShi2CountAndSaveDBRunner;

public class AllHistoryIndCountAndSaveDBRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HistoryMacdCountAndSaveDBRunner.main(args);
		HistoryKDJCountAndSaveDBRunner.main(args);
		HistoryBollCountAndSaveDBRunner.main(args);
		HistoryXueShi2CountAndSaveDBRunner.main(args);
	}
}
