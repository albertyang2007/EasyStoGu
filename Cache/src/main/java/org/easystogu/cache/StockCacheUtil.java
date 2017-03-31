package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.easystogu.config.Constants;
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
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

//refer to https://github.com/google/guava/wiki/CachesExplained
public class StockCacheUtil {
	private static Logger logger = LogHelper.getLogger(StockCacheUtil.class);
	private static StockCacheUtil instance = null;
	private static LoadingCache<String, List<Object>> stockPriceCache;
	private static ConcurrentHashMap<String, CacheAbleStock> stockTablesMap = new ConcurrentHashMap<String, CacheAbleStock>();

	private StockCacheUtil() {

		stockTablesMap.put(Constants.stockPrice, StockPriceTableHelper.getInstance());
		stockTablesMap.put(Constants.qianFuQuanStockPrice, QianFuQuanStockPriceTableHelper.getInstance());
		stockTablesMap.put(Constants.indKDJ, IndKDJTableHelper.getInstance());
		stockTablesMap.put(Constants.indMacd, IndMacdTableHelper.getInstance());
		stockTablesMap.put(Constants.indBoll, IndBollTableHelper.getInstance());
		stockTablesMap.put(Constants.indMA, IndMATableHelper.getInstance());
		stockTablesMap.put(Constants.indShenXian, IndShenXianTableHelper.getInstance());
		stockTablesMap.put(Constants.indQSDD, IndQSDDTableHelper.getInstance());
		stockTablesMap.put(Constants.indWR, IndWRTableHelper.getInstance());
		stockTablesMap.put(Constants.indDDX, IndDDXTableHelper.getInstance());

		stockPriceCache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(60, TimeUnit.MINUTES)
				.build(new CacheLoader<String, List<Object>>() {
					@Override
					// key is like: type:stockId, for example:
					// stockPrice:999999, indDKJ:999999
					public List<Object> load(String key) throws Exception {
						System.out.println("load from database, key:" + key);
						CacheAbleStock cacheTable = stockTablesMap.get(key.split(":")[0]);
						return cacheTable.queryByStockId(key.split(":")[1]);
					}
				});
	}

	public static StockCacheUtil getInstance() {
		if (instance == null) {
			instance = new StockCacheUtil();
		}
		return instance;
	}

	public static LoadingCache<String, List<Object>> getLoadingCache() {
		return stockPriceCache;
	}

	public static List<Object> queryByStockId(String key) {
		try {
			System.out.println("Cache Size:" + stockPriceCache.size());
			System.out.println("getStockPriceById from cache, key:" + key);
			return stockPriceCache.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Object>();
	}

	public static void invalidateAll() {
		stockPriceCache.invalidateAll();
	}

	public static void refresh(String key) {
		System.out.println("refresh for " + key);
		stockPriceCache.refresh(key);
	}

	public static void refreshAll() {
		System.out.println("refresh all");
		for (String key : stockPriceCache.asMap().keySet()) {
			stockPriceCache.refresh(key);
		}
	}
}
