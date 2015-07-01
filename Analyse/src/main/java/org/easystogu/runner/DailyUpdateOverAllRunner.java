package org.easystogu.runner;

import java.util.Date;

import org.easystogu.multirunner.DayMultThreadRunner;
import org.easystogu.multirunner.WeekMultThreadRunner;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;

public class DailyUpdateOverAllRunner {

	public static void main(String[] args) {
		System.out.println("start at " + new Date());
		// day
		DailyStockPriceDownloadAndStoreDBRunner.main(args);
		ChuQuanChuXiCheckerRunner.main(args);
		// day ind
		 //DailyMacdCountAndSaveDBRunner.main(args);
		 //DailyKDJCountAndSaveDBRunner.main(args);
		 //DailyBollCountAndSaveDBRunner.main(args);
		 //DailyMai1Mai2CountAndSaveDBRunner.main(args);
		 //DailyShenXianCountAndSaveDBRunner.main(args);
		 //DailyXueShi2CountAndSaveDBRunner.main(args);
		new DayMultThreadRunner().run();

		// week
		DailyWeeklyStockPriceCountAndSaveDBRunner.main(args);
		// week ind
		 //DailyWeekMacdCountAndSaveDBRunner.main(args);
		 //DailyWeekKDJCountAndSaveDBRunner.main(args);
		 //DailyWeekBollCountAndSaveDBRunner.main(args);
		 //DailyWeekMai1Mai2CountAndSaveDBRunner.main(args);
		 //DailyWeekShenXianCountAndSaveDBRunner.main(args);
		// DailyWeekXueShi2CountAndSaveDBRunner.main(args);
		new WeekMultThreadRunner().run();
		// analyse
		DailySelectionRunner.main(args);

		System.out.println("stop at " + new Date());
	}
}
