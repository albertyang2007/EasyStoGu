package org.easystogu.runner;

import java.util.List;

import org.easystogu.db.access.EstimateStockTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.easymoney.runner.DailyZiJinLiuXiangRunner;
import org.easystogu.indicator.runner.AllDailyIndCountAndSaveDBRunner;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;
import org.easystogu.utils.WeekdayUtil;

public class DailyUpdateEstimateStockRunner implements Runnable {

	private EstimateStockTableHelper estimateStockTable = EstimateStockTableHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private String currentDate = stockPriceTable.getLatestStockDate();
	private String nextDate = WeekdayUtil.nextWorkingDate(currentDate);
	private List<String> estimateStockIds = estimateStockTable.getAllEstimateStockIdsByDate(nextDate);

	public void run() {
		String[] args = null;
		long st = System.currentTimeMillis();
		// day
		new DailyStockPriceDownloadAndStoreDBRunner().runForStockIds(estimateStockIds);
		// chuquan
		ChuQuanChuXiCheckerRunner.main(args);
		// day ind
		new AllDailyIndCountAndSaveDBRunner().runDailyIndForStockIds(estimateStockIds);
		// week
		DailyWeeklyStockPriceCountAndSaveDBRunner.main(args);
		// week ind
		new AllDailyIndCountAndSaveDBRunner().runDailyWeekIndForStockIds(estimateStockIds);

		// zijinliu
		DailyZiJinLiuXiangRunner.main(args);
		// analyse
		new DailySelectionRunner().runForStockIds(estimateStockIds);

		System.out.println("stop using " + (System.currentTimeMillis() - st) / 1000 + " seconds");
	}

	public static void main(String[] args) {
		// run today stockprice anaylse
		new DailyUpdateEstimateStockRunner().run();
	}
}
