package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.easystogu.db.access.table.CheckPointDailySelectionTableHelper;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class CheckPointDailySelectionTableCache {
	private Logger logger = LogHelper.getLogger(CheckPointDailySelectionTableCache.class);
	private static CheckPointDailySelectionTableCache instance = null;
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
			.getInstance();
	private LoadingCache<String, List<CheckPointDailySelectionVO>> cache;

	private CheckPointDailySelectionTableCache() {
		cache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(24, TimeUnit.HOURS)
				.build(new CacheLoader<String, List<CheckPointDailySelectionVO>>() {
					@Override
					public List<CheckPointDailySelectionVO> load(String key) throws Exception {
						logger.info("load from database, key:" + key);
						String[] parms = key.split(":");
						// key is like: date + ":" + checkpoint
						if (parms.length == 2) {
							return checkPointDailySelectionTable.queryByDateAndCheckPoint(parms[0], parms[1]);
						} else if (parms.length == 1) {
							if (key.contains("-")) {
								// by date (2017-01-01)
								return checkPointDailySelectionTable.getCheckPointByDate(key);
							} else {
								// by stockId
								return checkPointDailySelectionTable.getCheckPointByStockID(key);
							}
						}
						return null;
					}
				});
	}

	public static CheckPointDailySelectionTableCache getInstance() {
		if (instance == null) {
			instance = new CheckPointDailySelectionTableCache();
		}
		return instance;
	}

	public LoadingCache<String, List<CheckPointDailySelectionVO>> getLoadingCache() {
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

	public void put(String key, List<CheckPointDailySelectionVO> value) {
		logger.info("put for " + key);
		cache.put(key, value);
	}

	public List<CheckPointDailySelectionVO> get(String key) {
		logger.info("get from cache, key:" + key);
		try {
			return cache.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CheckPointDailySelectionVO>();
	}

	public List<CheckPointDailySelectionVO> queryByDateAndCheckPoint(String date, String checkpoint) {
		return get(date + ":" + checkpoint);
	}

	public List<CheckPointDailySelectionVO> getCheckPointByDate(String key) {
		return get(key);
	}

	public List<CheckPointDailySelectionVO> getCheckPointByStockId(String stockId) {
		return get(stockId);
	}
}
