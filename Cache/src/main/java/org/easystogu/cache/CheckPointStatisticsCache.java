package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.easystogu.config.Constants;
import org.easystogu.db.access.table.CheckPointDailyStatisticsTableHelper;
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
import org.easystogu.db.vo.table.CheckPointDailyStatisticsVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CheckPointStatisticsCache {
	private Logger logger = LogHelper.getLogger(CheckPointStatisticsCache.class);
	private static CheckPointStatisticsCache instance = null;
	private CheckPointDailyStatisticsTableHelper checkPointStatisticsTable = CheckPointDailyStatisticsTableHelper
			.getInstance();
	private LoadingCache<String, List<CheckPointDailyStatisticsVO>> cache;

	private CheckPointStatisticsCache() {
		cache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(60, TimeUnit.MINUTES)
				.build(new CacheLoader<String, List<CheckPointDailyStatisticsVO>>() {
					@Override
					// key is date
					public List<CheckPointDailyStatisticsVO> load(String key) throws Exception {
						logger.info("load from database, key:" + key);
						String[] parms = key.split(":");
						return checkPointStatisticsTable.getAllCheckPointBetweenDate(parms[0], parms[1]);
					}
				});
	}

	public static CheckPointStatisticsCache getInstance() {
		if (instance == null) {
			instance = new CheckPointStatisticsCache();
		}
		return instance;
	}

	public LoadingCache<String, List<CheckPointDailyStatisticsVO>> getLoadingCache() {
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

	public void put(String key, List<CheckPointDailyStatisticsVO> value) {
		logger.info("put for " + key);
		cache.put(key, value);
	}

	public List<CheckPointDailyStatisticsVO> get(String key) {
		logger.info("get from cache, key:" + key);
		try {
			return cache.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CheckPointDailyStatisticsVO>();
	}
}
