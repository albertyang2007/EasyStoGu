package org.easystogu.runner;

import org.easystogu.easymoney.runner.DailyZiJinLiuXiangRunner;
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
import org.easystogu.indicator.runner.DailyWeekYiMengBSCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyXueShi2CountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyYiMengBSCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyZhuliJinChuCountAndSaveDBRunner;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;

public class DailyUpdateOverAllRunner implements Runnable {
	public void run() {
		String[] args = null;
		// zijinliu
		DailyZiJinLiuXiangRunner.main(args);
		//
		long st = System.currentTimeMillis();
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
		DailyYiMengBSCountAndSaveDBRunner.main(args);
		// week
		DailyWeeklyStockPriceCountAndSaveDBRunner.main(args);
		// week ind
		DailyWeekMacdCountAndSaveDBRunner.main(args);
		DailyWeekKDJCountAndSaveDBRunner.main(args);
		DailyWeekBollCountAndSaveDBRunner.main(args);
		DailyWeekMai1Mai2CountAndSaveDBRunner.main(args);
		DailyWeekShenXianCountAndSaveDBRunner.main(args);
		DailyWeekYiMengBSCountAndSaveDBRunner.main(args);

		// analyse
		DailySelectionRunner.main(args);

		System.out.println("stop using " + (System.currentTimeMillis() - st) / 1000 + " seconds");
	}

	public static void main(String[] args) {
		// run today stockprice anaylse
		new DailyUpdateOverAllRunner().run();
	}
}
