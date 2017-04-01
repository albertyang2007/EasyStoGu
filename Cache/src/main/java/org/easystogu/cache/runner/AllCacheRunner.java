package org.easystogu.cache.runner;

import org.easystogu.cache.CheckPointStatisticsCache;
import org.easystogu.cache.StockIndicatorCache;
import org.easystogu.cache.StockPriceCache;

public class AllCacheRunner implements Runnable {
	private CheckPointStatisticsCache checkPointStatisticsCache = CheckPointStatisticsCache.getInstance();
	private StockIndicatorCache stockIndicatorCache = StockIndicatorCache.getInstance();
	private StockPriceCache stockPriceCache = StockPriceCache.getInstance();

	public void refreshAll() {
		this.checkPointStatisticsCache.refreshAll();
		this.stockIndicatorCache.refreshAll();
		this.stockPriceCache.refreshAll();
	}

	public void run() {
	}

	public static void main(String[] args) {
		new AllCacheRunner().run();
	}

}
