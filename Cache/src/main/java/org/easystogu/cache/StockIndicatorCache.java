package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.easystogu.config.Constants;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.table.cache.CacheAbleStock;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

//refer to https://github.com/google/guava/wiki/CachesExplained
//refer to https://bl.ocks.org/kashyapp/5309855
public class StockIndicatorCache {
	private static Logger logger = LogHelper.getLogger(StockIndicatorCache.class);
	private static StockIndicatorCache instance = null;
	private LoadingCache<String, List<StockPriceVO>> cache;
	private ConcurrentHashMap<String, CacheAbleStock> stockTablesMap = new ConcurrentHashMap<String, CacheAbleStock>();

	private StockIndicatorCache() {
		stockTablesMap.put(Constants.cacheStockPrice, StockPriceTableHelper.getInstance());
		stockTablesMap.put(Constants.cacheQianFuQuanStockPrice, QianFuQuanStockPriceTableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndKDJ, IndKDJTableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndMacd, IndMacdTableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndBoll, IndBollTableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndMA, IndMATableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndShenXian, IndShenXianTableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndQSDD, IndQSDDTableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndWR, IndWRTableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndDDX, IndDDXTableHelper.getInstance());

		cache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(30, TimeUnit.MINUTES)
				.build(new CacheLoader<String, List<StockPriceVO>>() {
					@Override
					// key is like: type:stockId, for example:
					// stockPrice:999999, indDKJ:999999
					public List<StockPriceVO> load(String key) throws Exception {
						logger.info("load from database, stockTablesMap key:" + key);
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

	public LoadingCache<String, List<StockPriceVO>> getLoadingCache() {
		return cache;
	}

	public List<StockPriceVO> queryByStockId(String key) {
		try {
			logger.info("Cache Size:" + cache.size());
			logger.info("getStockPriceById from cache, key:" + key);
			return cache.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<StockPriceVO>();
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
}
