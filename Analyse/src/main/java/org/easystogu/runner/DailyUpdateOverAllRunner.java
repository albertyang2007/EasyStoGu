package org.easystogu.runner;

import java.util.Date;

import org.easystogu.indicator.runner.DailyBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyMai1Mai2CountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyShenXianCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekMai1Mai2CountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekShenXianCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyXueShi2CountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyZhuliJinChuCountAndSaveDBRunner;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;

public class DailyUpdateOverAllRunner {
	public static void main(String[] args) {
		System.out.println("start at " + new Date());
		// day
		DailyStockPriceDownloadAndStoreDBRunner.main(args);
		// chuquan
		ChuQuanChuXiCheckerRunner.main(args);
		// day ind
		DailyMacdCountAndSaveDBRunner.main(args);
		DailyKDJCountAndSaveDBRunner.main(args);
		DailyBollCountAndSaveDBRunner.main(args);
		DailyMai1Mai2CountAndSaveDBRunner.main(args);
		DailyShenXianCountAndSaveDBRunner.main(args);
		DailyXueShi2CountAndSaveDBRunner.main(args);
		DailyZhuliJinChuCountAndSaveDBRunner.main(args);
		// week
		DailyWeeklyStockPriceCountAndSaveDBRunner.main(args);
		// week ind
		DailyWeekMacdCountAndSaveDBRunner.main(args);
		DailyWeekKDJCountAndSaveDBRunner.main(args);
		DailyWeekBollCountAndSaveDBRunner.main(args);
		DailyWeekMai1Mai2CountAndSaveDBRunner.main(args);
		DailyWeekShenXianCountAndSaveDBRunner.main(args);
		// DailyWeekXueShi2CountAndSaveDBRunner.main(args);

		// analyse
		DailySelectionRunner.main(args);

		System.out.println("stop at " + new Date());
	}
}
