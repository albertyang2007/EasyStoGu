package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.easystogu.db.access.table.CheckPointDailySelectionTableHelper;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

public class CheckPointDailySelectionTableCache {
	private Logger logger = LogHelper.getLogger(CheckPointDailySelectionTableCache.class);
	private static CheckPointDailySelectionTableCache instance = null;
	@Autowired
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable;
	private LoadingCache<String, List<CheckPointDailySelectionVO>> cache;

	private CheckPointDailySelectionTableCache() {
		cache = CacheBuilder.newBuilder().maximumSize(100).refreshAfterWrite(5, TimeUnit.MINUTES)
				.build(new CacheLoader<String, List<CheckPointDailySelectionVO>>() {
					@Override
					public List<CheckPointDailySelectionVO> load(String key) throws Exception {
						logger.info("load from database, checkPointDailySelectionTable key:" + key);
						return loadDataFromDB(key);
					}

					@Override
					public ListenableFuture<List<CheckPointDailySelectionVO>> reload(final String key,
							final List<CheckPointDailySelectionVO> oldValue) {
						logger.info("reload from database, checkPointDailySelectionTable key:" + key);
						ListenableFutureTask<List<CheckPointDailySelectionVO>> task = ListenableFutureTask
								.create(new Callable<List<CheckPointDailySelectionVO>>() {
									public List<CheckPointDailySelectionVO> call() {
										List<CheckPointDailySelectionVO> newValue = oldValue;
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

					private List<CheckPointDailySelectionVO> loadDataFromDB(String key) {
						// key is like: checkpoint + "@" + date
						if (key.contains("@")) {
							String[] parms = key.split("@");
							return checkPointDailySelectionTable.queryByDateAndCheckPoint(parms[1], parms[0]);
						}

						if (key.contains("stockId=")) {
							// key likes stockId=999999
							return checkPointDailySelectionTable.getCheckPointByStockID(key.split("stockId=")[1]);
						}

						if (key.contains("CheckPoint>=")) {
							// check point in recent days
							return checkPointDailySelectionTable.getRecentDaysCheckPoint(key.split("CheckPoint>=")[1]);
						}

						if (key.contains("CheckPoint=") && key.contains("-")) {
							// by date (2017-01-01)
							return checkPointDailySelectionTable.getCheckPointByDate(key);
						}

						logger.error("Error no cache key for " + key);
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
		return get(checkpoint + "@" + date);
	}

	public List<CheckPointDailySelectionVO> getRecentDaysCheckPoint(String date) {
		return get("CheckPoint>=" + date);
	}

	public List<CheckPointDailySelectionVO> getCheckPointByDate(String date) {
		return get("CheckPoint=" + date);
	}

	public List<CheckPointDailySelectionVO> getCheckPointByStockId(String stockId) {
		return get("stockId=" + stockId);
	}
}
