package org.easystogu.runner;

import org.easystogu.config.DBConfigurationService;
import org.easystogu.easymoney.runner.OverAllZiJinLiuAndDDXRunner;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//only download stockprice and zijinliu, ddx, 
//no other ind counted,
//this will be run on aliyun
@Component
public class DailyUpdateStockPriceAndDDXRunner implements Runnable {
	@Autowired
	private DBConfigurationService config;
	private boolean fetchAllZiJinLiu = false;

	public boolean isFetchAllZiJinLiu() {
		return fetchAllZiJinLiu;
	}

	public void setFetchAllZiJinLiu(boolean fetchAllZiJinLiu) {
		this.fetchAllZiJinLiu = fetchAllZiJinLiu;
	}

	public void run() {
		long st = System.currentTimeMillis();
		// daily price (download all stockIds price)
		DailyStockPriceDownloadAndStoreDBRunner2.main(null);

		// daily zijinliu and ddx for all
		if (config.getBoolean("count_zijin_and_ddx", false)) {
			OverAllZiJinLiuAndDDXRunner zijinliuRunner = new OverAllZiJinLiuAndDDXRunner();
			zijinliuRunner.run();
		}
		System.out.println("stop using " + (System.currentTimeMillis() - st) / 1000 + " seconds");
	}
}
