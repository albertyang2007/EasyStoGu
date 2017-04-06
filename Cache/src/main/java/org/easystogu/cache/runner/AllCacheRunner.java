package org.easystogu.cache.runner;

import org.easystogu.cache.CheckPointDailySelectionTableCache;
import org.easystogu.cache.CheckPointStatisticsCache;
import org.easystogu.cache.CommonViewCache;
import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cache.StockIndicatorCache;
import org.easystogu.cache.StockPriceCache;

public class AllCacheRunner implements Runnable {
	private CheckPointStatisticsCache checkPointStatisticsCache = CheckPointStatisticsCache.getInstance();
	private StockIndicatorCache stockIndicatorCache = StockIndicatorCache.getInstance();
	private StockPriceCache stockPriceCache = StockPriceCache.getInstance();
	private CheckPointDailySelectionTableCache checkPointDailySelectionTableCache = CheckPointDailySelectionTableCache.getInstance();
	private CommonViewCache commonViewCache = CommonViewCache.getInstance();
	private ConfigurationServiceCache configurationServiceCache = ConfigurationServiceCache.getInstance();

	public void refreshAll() {
		this.checkPointStatisticsCache.refreshAll();
		this.stockIndicatorCache.refreshAll();
		this.stockPriceCache.refreshAll();
		this.checkPointDailySelectionTableCache.refreshAll();
		this.commonViewCache.refreshAll();
		this.configurationServiceCache.refreshAll();
	}

	public void run() {
	}

	public static void main(String[] args) {
		new AllCacheRunner().run();
	}

}
