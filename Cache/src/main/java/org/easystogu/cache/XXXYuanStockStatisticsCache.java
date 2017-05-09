package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.easystogu.db.access.view.XXXYuanStockStatisticsViewHelper;
import org.easystogu.db.vo.view.StatisticsViewVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class XXXYuanStockStatisticsCache {
	private Logger logger = LogHelper.getLogger(XXXYuanStockStatisticsCache.class);
	private static XXXYuanStockStatisticsCache instance = null;
	private XXXYuanStockStatisticsViewHelper xtockStatisticsViewHelper = XXXYuanStockStatisticsViewHelper.getInstance();
	private LoadingCache<String, List<StatisticsViewVO>> cache;

	private XXXYuanStockStatisticsCache() {
		cache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(12, TimeUnit.HOURS)
				.build(new CacheLoader<String, List<StatisticsViewVO>>() {
					@Override
					// key is
					public List<StatisticsViewVO> load(String key) throws Exception {
						logger.info("load from database, key:" + key);
						// key is One, Five or Ten
						return xtockStatisticsViewHelper.getAll(key);
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
