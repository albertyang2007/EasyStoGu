package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.easystogu.config.Constants;
import org.easystogu.db.access.table.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.view.CommonViewHelper;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

public class AnalyseViewCache {
	private Logger logger = LogHelper.getLogger(AnalyseViewCache.class);
	private static AnalyseViewCache instance = null;
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
			.getInstance();
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private CommonViewHelper commonViewHelper = CommonViewHelper.getInstance();
	private LoadingCache<String, List<String>> cache;

	private AnalyseViewCache() {
		cache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(10, TimeUnit.MINUTES)
				.build(new CacheLoader<String, List<String>>() {
					@Override
					// key is like: type:parms, for example:
					// latestndate:10
					public List<String> load(String key) throws Exception {
						logger.info("load from database, key:" + key);
						String[] parms = key.split(":");
						if (Constants.cacheLatestNStockDate.equals(parms[0])) {
							return stockPriceTable.getLatestNStockDate(Integer.parseInt(parms[1]));
						} else if (Constants.cacheSZZSDayListByIdAndBetweenDates.equals(parms[0])) {
							return stockPriceTable.getSZZSDayListByIdAndBetweenDates(parms[1], parms[2]);
						} else if (Constants.cacheAllDealDate.equals(parms[0])) {
							return stockPriceTable.getAllDealDate(parms[1]);
						}
						logger.error("no such key, return empty list.");
						return new ArrayList<String>();
					}
				});
	}

	public static AnalyseViewCache getInstance() {
		if (instance == null) {
			instance = new AnalyseViewCache();
		}
		return instance;
	}

	public LoadingCache<String, List<String>> getLoadingCache() {
		return cache;
	}

	public void invalidateAll() {
		logger.info("invalidateAll");
		cache.invalidateAll();
	}

	public void refresh(String key) {
		logger.info("refresh for " + key);
		cache.refresh(key);
	}

	public void refreshAll() {
		logger.info("refreshAll");
		for (String key : cache.asMap().keySet()) {
			cache.refresh(key);
		}
	}

	public void put(String key, List<String> value) {
		logger.info("put for " + key);
		cache.put(key, value);
	}

	public List<String> get(String key) {
		logger.info("get from cache, key:" + key);
		try {
			return cache.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
}
