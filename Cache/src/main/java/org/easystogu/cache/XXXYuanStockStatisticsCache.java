package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.easystogu.db.access.view.XXXYuanStockStatisticsViewHelper;
import org.easystogu.db.vo.view.StatisticsViewVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

public class XXXYuanStockStatisticsCache {
	private Logger logger = LogHelper.getLogger(XXXYuanStockStatisticsCache.class);
	private static XXXYuanStockStatisticsCache instance = null;
	private XXXYuanStockStatisticsViewHelper xtockStatisticsViewHelper = XXXYuanStockStatisticsViewHelper.getInstance();
	private LoadingCache<String, List<StatisticsViewVO>> cache;

	private XXXYuanStockStatisticsCache() {
		cache = CacheBuilder.newBuilder().maximumSize(100).refreshAfterWrite(12, TimeUnit.HOURS)
				.build(new CacheLoader<String, List<StatisticsViewVO>>() {
					// key is One, Five or Ten
					@Override
					public List<StatisticsViewVO> load(String key) throws Exception {
						logger.info("load from database, xtockStatisticsViewHelper key:" + key);
						return xtockStatisticsViewHelper.getAll(key);
					}
					
					@Override
					public ListenableFuture<List<StatisticsViewVO>> reload(final String key,
							final List<StatisticsViewVO> oldValue) {
						logger.info("reload from database, xtockStatisticsViewHelper key:" + key);
						ListenableFutureTask<List<StatisticsViewVO>> task = ListenableFutureTask
								.create(new Callable<List<StatisticsViewVO>>() {
									public List<StatisticsViewVO> call() {
										List<StatisticsViewVO> newValue = oldValue;
										try {
											newValue = xtockStatisticsViewHelper.getAll(key);
										} catch (Exception e) {
											logger.error("There was an exception when reloading the cache", e);
										}
										return newValue;
									}
								});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}
				});
	}

	public static XXXYuanStockStatisticsCache getInstance() {
		if (instance == null) {
			instance = new XXXYuanStockStatisticsCache();
		}
		return instance;
	}

	public LoadingCache<String, List<StatisticsViewVO>> getLoadingCache() {
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

	public void put(String key, List<StatisticsViewVO> value) {
		logger.info("put for " + key);
		cache.put(key, value);
	}

	public List<StatisticsViewVO> get(String key) {
		logger.info("get from cache, key:" + key);
		try {
			return cache.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<StatisticsViewVO>();
	}
}
