package org.easystogu.runner;

import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.db.access.table.WSFConfigTableHelper;
import org.easystogu.easymoney.runner.OverAllZiJinLiuAndDDXRunner;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.runner.AllDailyIndCountAndSaveDBRunner;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner2;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;

public class DailyUpdateAllStockRunner implements Runnable {
	private WSFConfigTableHelper wsfConfig = WSFConfigTableHelper.getInstance();
	private String zone = wsfConfig.getValue("zone", Constants.ZONE_OFFICE);
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
			new OverAllZiJinLiuAndDDXRunner().run();
		}

		// analyse by java code
		dailySelectionRunner = new DailySelectionRunner();
		dailySelectionRunner.setFetchRealTimeZiJinLiu(false);
		dailySelectionRunner.runForStockIds(allStockIds);

		// alaylse by view names
		new DailyViewAnalyseRunner().run();

		System.out.println("stop using " + (System.currentTimeMillis() - st) / 1000 + " seconds");
	}

	public static void main(String[] args) {
		// run today stockprice anaylse
		new DailyUpdateAllStockRunner(true).run();
	}
}
