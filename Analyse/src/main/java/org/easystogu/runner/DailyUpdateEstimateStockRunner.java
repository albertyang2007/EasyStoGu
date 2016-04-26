package org.easystogu.runner;

import java.util.List;

import org.easystogu.db.access.EstimateStockTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.indicator.runner.AllDailyIndCountAndSaveDBRunner;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;

public class DailyUpdateEstimateStockRunner implements Runnable {

	private EstimateStockTableHelper estimateStockTable = EstimateStockTableHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private String currentDate = null;
	// private String nextDate = WeekdayUtil.nextWorkingDate(currentDate);
	private List<String> estimateStockIds = null;
	private DailySelectionRunner dailySelectionRunner = new DailySelectionRunner();

	public void run() {
		String[] args = null;
		long st = System.currentTimeMillis();
		// day (must download all stockIds price!)
		DailyStockPriceDownloadAndStoreDBRunner.main(args);
		// chuquan
		new ChuQuanChuXiCheckerRunner().dailyCheckChuQuanEvent(estimateStockIds);

		currentDate = stockPriceTable.getLatestStockDate();
		estimateStockIds = estimateStockTable.getAllEstimateStockIdsByDate(currentDate);
		System.out.println("start running EstimateStock for " + currentDate);

		// day ind
		new AllDailyIndCountAndSaveDBRunner().runDailyIndForStockIds(estimateStockIds);
		// week
		new DailyWeeklyStockPriceCountAndSaveDBRunner().countAndSave(estimateStockIds);
		// week ind
		new AllDailyIndCountAndSaveDBRunner().runDailyWeekIndForStockIds(estimateStockIds);

		// Do not need to fetch all zijinliu
		// only get realTime zijinliu for checkpoint that satisfy
		// DailyZiJinLiuRunner.main(args);
		// ddx
		// new DailyDDXRunner().countAndSaved(estimateStockIds);

		// analyse
		dailySelectionRunner.setFetchRealTimeZiJinLiu(false);
		dailySelectionRunner.runForStockIds(estimateStockIds);

		System.out.println("stop using " + (System.currentTimeMillis() - st) / 1000 + " seconds");
	}

	public static void main(String[] args) {
		// run today stockprice anaylse
		new DailyUpdateEstimateStockRunner().run();
	}
}
