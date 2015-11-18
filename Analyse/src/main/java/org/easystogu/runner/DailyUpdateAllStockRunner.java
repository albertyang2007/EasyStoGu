package org.easystogu.runner;

import java.util.List;

import org.easystogu.easymoney.runner.DailyZiJinLiuXiangRunner;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.runner.AllDailyIndCountAndSaveDBRunner;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;

public class DailyUpdateAllStockRunner implements Runnable {
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private List<String> allStockIds = stockConfig.getAllStockId();

	public void run() {
		String[] args = null;
		long st = System.currentTimeMillis();
		// day
		new DailyStockPriceDownloadAndStoreDBRunner().runForStockIds(allStockIds);
		// chuquan
		ChuQuanChuXiCheckerRunner.main(args);
		// day ind
		new AllDailyIndCountAndSaveDBRunner().runDailyIndForStockIds(allStockIds);
		// week
		DailyWeeklyStockPriceCountAndSaveDBRunner.main(args);
		// week ind
		new AllDailyIndCountAndSaveDBRunner().runDailyWeekIndForStockIds(allStockIds);

		// zijinliu
		DailyZiJinLiuXiangRunner.main(args);
		// analyse
		new DailySelectionRunner().runForStockIds(allStockIds);

		System.out.println("stop using " + (System.currentTimeMillis() - st) / 1000 + " seconds");
	}

	public static void main(String[] args) {
		// run today stockprice anaylse
		new DailyUpdateAllStockRunner().run();
	}
}
