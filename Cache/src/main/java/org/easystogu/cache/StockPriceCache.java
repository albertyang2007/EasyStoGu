package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.easystogu.config.Constants;
import org.easystogu.db.access.table.StockPriceTableHelper;
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

@Component
public class StockPriceCache {
	private Logger logger = LogHelper.getLogger(StockPriceCache.class);
	@Autowired
	@Qualifier("stockPriceTable")
	private StockPriceTableHelper stockPriceTable;
	
	private LoadingCache<String, List<String>> cache;

	@PostConstruct
	private void init() {
		cache = CacheBuilder.newBuilder().maximumSize(100).refreshAfterWrite(5, TimeUnit.MINUTES)
				.build(new CacheLoader<String, List<String>>() {
					// key is like: type:parms, for example:
					// latestndate:10
					@Override
					public List<String> load(String key) throws Exception {
						logger.info("load from database, stockPriceTable key:" + key);
						return loadDataFromDB(key);
					}
					
					@Override
					public ListenableFuture<List<String>> reload(final String key,
							final List<String> oldValue) {
						logger.info("reload from database, stockPriceTable key:" + key);
						ListenableFutureTask<List<String>> task = ListenableFutureTask
								.create(new Callable<List<String>>() {
									public List<String> call() {
										List<String> newValue = oldValue;
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

					private List<String> loadDataFromDB(String key) {
						String[] parms = key.split(":");
						if (Constants.cacheLatestNStockDate.equals(parms[0])) {
							return stockPriceTable.getLatestNStockDate(Integer.parseInt(parms[1]));
						} else if (Constants.cacheSZZSDayListByIdAndBetweenDates.equals(parms[0])) {
							return stockPriceTable.getSZZSDayListByIdAndBetweenDates(parms[1], parms[2]);
						} else if (Constants.cacheAllDealDate.equals(parms[0])) {
							return stockPriceTable.getAllDealDate(parms[1]);
						}
						logger.error("no such key, return empty list.");
						return new ArrayList<String>();
					}
				});
	}

	public LoadingCache<String, List<String>> getLoadingCache() {
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

	public void put(String key, List<String> value) {
		logger.info("put for " + key);
		cache.put(key, value);
	}

	public List<String> get(String key) {
		logger.info("get from cache, key:" + key);
		try {
			return cache.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
}
