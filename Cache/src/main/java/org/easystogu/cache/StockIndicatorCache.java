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

//refer to https://github.com/google/guava/wiki/CachesExplained
public class StockIndicatorCache {
	private static Logger logger = LogHelper.getLogger(StockIndicatorCache.class);
	private static StockIndicatorCache instance = null;
	private LoadingCache<String, List<Object>> cache;
	private ConcurrentHashMap<String, CacheAbleStock> stockTablesMap = new ConcurrentHashMap<String, CacheAbleStock>();

	private StockIndicatorCache() {
		stockTablesMap.put(Constants.cacheStockPrice, StockPriceTableHelper.getInstance());
		stockTablesMap.put(Constants.cacheQianFuQuanStockPrice, QianFuQuanStockPriceTableHelper.getInstance());
		stockTablesMap.put(Constants.cacheIndKDJ, IndKDJTableHelper.getInstance());
		stockTablesMap.put(Constants.cacheIndMacd, IndMacdTableHelper.getInstance());
		stockTablesMap.put(Constants.cacheIndBoll, IndBollTableHelper.getInstance());
		stockTablesMap.put(Constants.cacheIndMA, IndMATableHelper.getInstance());
		stockTablesMap.put(Constants.cacheIndShenXian, IndShenXianTableHelper.getInstance());
		stockTablesMap.put(Constants.cacheIndQSDD, IndQSDDTableHelper.getInstance());
		stockTablesMap.put(Constants.cacheIndWR, IndWRTableHelper.getInstance());
		stockTablesMap.put(Constants.cacheIndDDX, IndDDXTableHelper.getInstance());

		cache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(60, TimeUnit.MINUTES)
				.build(new CacheLoader<String, List<Object>>() {
					@Override
					// key is like: type:stockId, for example:
					// stockPrice:999999, indDKJ:999999
					public List<Object> load(String key) throws Exception {
						logger.info("load from database, key:" + key);
						CacheAbleStock cacheTable = stockTablesMap.get(key.split(":")[0]);
						return cacheTable.queryByStockId(key.split(":")[1]);
					}
				});
	}

	public static StockIndicatorCache getInstance() {
		if (instance == null) {
			instance = new StockIndicatorCache();
		}
		return instance;
	}

	public LoadingCache<String, List<Object>> getLoadingCache() {
		return cache;
	}

	public List<Object> queryByStockId(String key) {
		try {
			logger.info("Cache Size:" + cache.size());
			logger.info("getStockPriceById from cache, key:" + key);
			return cache.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Object>();
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
}
