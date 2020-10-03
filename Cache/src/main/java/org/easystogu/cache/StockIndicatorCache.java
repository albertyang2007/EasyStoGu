package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.easystogu.config.Constants;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.table.cache.CacheAbleStock;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

//refer to https://github.com/google/guava/wiki/CachesExplained
//refer to https://bl.ocks.org/kashyapp/5309855
@Component
public class StockIndicatorCache {
	private static Logger logger = LogHelper.getLogger(StockIndicatorCache.class);
	@Autowired
	@Qualifier("stockPriceTable")
	private StockPriceTableHelper stockPriceTable;
	
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	private StockPriceTableHelper qianFuQuanstockPriceTable;
	
	private LoadingCache<String, List<StockPriceVO>> cache;
	private ConcurrentHashMap<String, CacheAbleStock> stockTablesMap = new ConcurrentHashMap<String, CacheAbleStock>();

	@PostConstruct
	private void init() {
		stockTablesMap.put(Constants.cacheStockPrice, stockPriceTable);
		stockTablesMap.put(Constants.cacheQianFuQuanStockPrice, qianFuQuanstockPriceTable);
		//stockTablesMap.put(Constants.cacheIndKDJ, IndKDJTableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndMacd, IndMacdTableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndBoll, IndBollTableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndMA, IndMATableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndShenXian, IndShenXianTableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndQSDD, IndQSDDTableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndWR, IndWRTableHelper.getInstance());
		//stockTablesMap.put(Constants.cacheIndDDX, IndDDXTableHelper.getInstance());

		cache = CacheBuilder.newBuilder().maximumSize(100).refreshAfterWrite(5, TimeUnit.MINUTES)
				.build(new CacheLoader<String, List<StockPriceVO>>() {
					// key is like: type:stockId, for example:
					// stockPrice:999999, indDKJ:999999
					@Override
					public List<StockPriceVO> load(String key) throws Exception {
						logger.info("load from database, stockTablesMap key:" + key);
						return loadDataFromDB(key);
					}
					
					@Override
					public ListenableFuture<List<StockPriceVO>> reload(final String key,
							final List<StockPriceVO> oldValue) {
						logger.info("reload from database, stockTablesMap key:" + key);
						ListenableFutureTask<List<StockPriceVO>> task = ListenableFutureTask
								.create(new Callable<List<StockPriceVO>>() {
									public List<StockPriceVO> call() {
										List<StockPriceVO> newValue = oldValue;
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

					private List<StockPriceVO> loadDataFromDB(String key) {
						CacheAbleStock cacheTable = stockTablesMap.get(key.split(":")[0]);
						return cacheTable.queryByStockId(key.split(":")[1]);
					}
				});
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
