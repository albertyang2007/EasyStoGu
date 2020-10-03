package org.easystogu.runner;

import java.util.List;

import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.runner.AllDailyIndCountAndSaveDBRunner;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner2;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;
import org.springframework.stereotype.Component;

@Component
public class DailyUpdateStockPriceAndIndicatorRunner implements Runnable {
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private List<String> allStockIds = stockConfig.getAllStockId();

	public DailyUpdateStockPriceAndIndicatorRunner() {
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

		System.out.println("stop using " + (System.currentTimeMillis() - st) / 1000 + " seconds");
	}
}
