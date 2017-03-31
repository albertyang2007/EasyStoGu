package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.easystogu.config.Constants;
import org.easystogu.db.access.table.IndBollTableHelper;
import org.easystogu.db.access.table.IndDDXTableHelper;
import org.easystogu.db.access.table.IndKDJTableHelper;
import org.easystogu.db.access.table.IndMATableHelper;
import org.easystogu.db.access.table.IndMacdTableHelper;
import org.easystogu.db.access.table.IndQSDDTableHelper;
import org.easystogu.db.access.table.IndShenXianTableHelper;
import org.easystogu.db.access.table.IndWRTableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.table.cache.CacheAbleStock;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class StockPriceCache {
	private Logger logger = LogHelper.getLogger(StockPriceCache.class);
	private static StockPriceCache instance = null;
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private LoadingCache<String, List<String>> cache;

	private StockPriceCache() {
		cache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(60, TimeUnit.MINUTES)
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

	public static StockPriceCache getInstance() {
		if (instance == null) {
			instance = new StockPriceCache();
		}
		return instance;
	}

	public LoadingCache<String, List<String>> getLoadingCache() {
		return cache;
	}

	public void invalidateAll() {
		cache.invalidateAll();
	}

	public void refresh(String key) {
		logger.info("refresh for " + key);
		cache.refresh(key);
	}

	public void refreshAll() {
		logger.info("refresh all");
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
