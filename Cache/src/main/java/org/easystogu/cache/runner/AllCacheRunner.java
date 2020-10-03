package org.easystogu.cache.runner;

import org.easystogu.cache.CheckPointDailySelectionTableCache;
import org.easystogu.cache.CheckPointStatisticsCache;
import org.easystogu.cache.CommonViewCache;
import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cache.FavoritesCache;
import org.easystogu.cache.StockIndicatorCache;
import org.easystogu.cache.StockPriceCache;
import org.easystogu.cache.XXXYuanStockStatisticsCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AllCacheRunner {
	@Autowired
	private CheckPointStatisticsCache checkPointStatisticsCache;
	@Autowired
	private StockIndicatorCache stockIndicatorCache;
	@Autowired
	private StockPriceCache stockPriceCache;
	@Autowired
	private CheckPointDailySelectionTableCache checkPointDailySelectionTableCache;
	@Autowired
	private CommonViewCache commonViewCache;
	@Autowired
	private ConfigurationServiceCache configurationServiceCache;
	@Autowired
	private XXXYuanStockStatisticsCache stockStatisticsCache;
	@Autowired
	private FavoritesCache favoritesCache;

	public void refreshAll() {
		this.configurationServiceCache.refreshAll();
		this.checkPointStatisticsCache.refreshAll();
		this.stockPriceCache.refreshAll();
		this.stockIndicatorCache.refreshAll();
		this.checkPointDailySelectionTableCache.refreshAll();
		this.commonViewCache.refreshAll();
		this.stockStatisticsCache.refreshAll();
		this.favoritesCache.refreshAll();
	}
}
