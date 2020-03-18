package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.easystogu.db.access.view.CommonViewHelper;
import org.easystogu.db.vo.view.CommonViewVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

public class CommonViewCache {
	private Logger logger = LogHelper.getLogger(CommonViewCache.class);
	private static CommonViewCache instance = null;
	private CommonViewHelper commonViewHelper = CommonViewHelper.getInstance();
	private LoadingCache<String, List<CommonViewVO>> cache;

	private CommonViewCache() {
		cache = CacheBuilder.newBuilder().maximumSize(100).refreshAfterWrite(5, TimeUnit.MINUTES)
				.build(new CacheLoader<String, List<CommonViewVO>>() {
					@Override
					public List<CommonViewVO> load(String key) throws Exception {
						logger.info("load from database, commonViewHelper key:" + key);
						return loadDataFromDB(key);
					}

					@Override
					public ListenableFuture<List<CommonViewVO>> reload(final String key,
							final List<CommonViewVO> oldValue) {
						logger.info("reload from database, commonViewHelper key:" + key);
						ListenableFutureTask<List<CommonViewVO>> task = ListenableFutureTask
								.create(new Callable<List<CommonViewVO>>() {
									public List<CommonViewVO> call() {
										List<CommonViewVO> newValue = oldValue;
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

					private List<CommonViewVO> loadDataFromDB(String key) {
						String[] parms = key.split(":");
						return commonViewHelper.queryByDateForViewDirectlySearch(parms[0], parms[1]);
					}
				});
	}

	public static CommonViewCache getInstance() {
		if (instance == null) {
			instance = new CommonViewCache();
		}
		return instance;
	}

	public LoadingCache<String, List<CommonViewVO>> getLoadingCache() {
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

	public void put(String key, List<CommonViewVO> value) {
		logger.info("put for " + key);
		cache.put(key, value);
	}

	public List<CommonViewVO> get(String key) {
		logger.info("get from cache, key:" + key);
		try {
			return cache.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CommonViewVO>();
	}

	public List<CommonViewVO> queryByDateForViewDirectlySearch(String date, String searchViewName) {
		return get(date + ":" + searchViewName);
	}
}
