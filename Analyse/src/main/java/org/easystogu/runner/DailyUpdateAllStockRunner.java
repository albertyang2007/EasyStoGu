package org.easystogu.runner;

import java.util.List;

import org.easystogu.easymoney.runner.DailyDDXRunner;
import org.easystogu.easymoney.runner.DailyZiJinLiuRunner;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.runner.AllDailyIndCountAndSaveDBRunner;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner2;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;

public class DailyUpdateAllStockRunner implements Runnable {
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private List<String> allStockIds = stockConfig.getAllStockId();
	private DailySelectionRunner dailySelectionRunner = null;
	public boolean isGetZiJinLiu = false;

	public DailyUpdateAllStockRunner(boolean isGetZiJinLiu) {
		this.isGetZiJinLiu = isGetZiJinLiu;
	}

	public void run() {
		String[] args = null;
		long st = System.currentTimeMillis();
		// day (download all stockIds price)
		DailyStockPriceDownloadAndStoreDBRunner2.main(args);
		// day ind
		new AllDailyIndCountAndSaveDBRunner().runDailyIndForStockIds(allStockIds);
		// week
		new DailyWeeklyStockPriceCountAndSaveDBRunner().countAndSave(allStockIds);
		// week ind
		new AllDailyIndCountAndSaveDBRunner().runDailyWeekIndForStockIds(allStockIds);

		// zijinliu
		if (isGetZiJinLiu) {
			new DailyZiJinLiuRunner().run();
			// DailyZhuLiJingLiuRuRunner.main(args);
		}

		// analyse
		dailySelectionRunner = new DailySelectionRunner();
		dailySelectionRunner.setFetchRealTimeZiJinLiu(false);
		dailySelectionRunner.runForStockIds(allStockIds);

		System.out.println("stop using " + (System.currentTimeMillis() - st) / 1000 + " seconds");
	}

	public static void main(String[] args) {
		// run today stockprice anaylse
		new DailyUpdateAllStockRunner(true).run();
	}
}
