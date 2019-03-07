package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.easystogu.db.access.table.CheckPointDailyStatisticsTableHelper;
import org.easystogu.db.vo.table.CheckPointDailyStatisticsVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

public class CheckPointStatisticsCache {
	private Logger logger = LogHelper.getLogger(CheckPointStatisticsCache.class);
	private static CheckPointStatisticsCache instance = null;
	private CheckPointDailyStatisticsTableHelper checkPointStatisticsTable = CheckPointDailyStatisticsTableHelper
			.getInstance();
	private LoadingCache<String, List<CheckPointDailyStatisticsVO>> cache;

	private CheckPointStatisticsCache() {
		cache = CacheBuilder.newBuilder().maximumSize(100).refreshAfterWrite(30, TimeUnit.MINUTES)
				.build(new CacheLoader<String, List<CheckPointDailyStatisticsVO>>() {
					@Override
					public List<CheckPointDailyStatisticsVO> load(String key) throws Exception {
						logger.info("load from database, checkPointStatisticsTable key:" + key);
						return loadDataFromDB(key);
					}

					@Override
					public ListenableFuture<List<CheckPointDailyStatisticsVO>> reload(final String key,
							final List<CheckPointDailyStatisticsVO> oldValue) {
						logger.info("reload from database, checkPointStatisticsTable key:" + key);
						ListenableFutureTask<List<CheckPointDailyStatisticsVO>> task = ListenableFutureTask
								.create(new Callable<List<CheckPointDailyStatisticsVO>>() {
									public List<CheckPointDailyStatisticsVO> call() {
										List<CheckPointDailyStatisticsVO> newValue = oldValue;
										try {
											newValue = loadDataFromDB(key);
										} catch (Exception e) {
											logger.error("There was an exception when reloading the cache", e);
										}
										return newValue;
									}
								});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}

					private List<CheckPointDailyStatisticsVO> loadDataFromDB(String key) {
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
