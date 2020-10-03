package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.easystogu.db.access.table.FavoritesStockHelper;
import org.easystogu.db.vo.table.CompanyInfoVO;
import org.easystogu.db.vo.view.FavoritesStockVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

@Component
public class FavoritesCache {
	private Logger logger = LogHelper.getLogger(FavoritesCache.class);
	@Autowired
	private CompanyInfoFileHelper stockConfig;
	@Autowired
	private FavoritesStockHelper favoritesStockHelper;
	private LoadingCache<String, List<FavoritesStockVO>> cache;

	@PostConstruct
	private void init() {
		cache = CacheBuilder.newBuilder().maximumSize(100).refreshAfterWrite(5, TimeUnit.MINUTES)
				.build(new CacheLoader<String, List<FavoritesStockVO>>() {
					@Override
					public List<FavoritesStockVO> load(String key) throws Exception {
						logger.info("load from database, favoritesStockHelper key:" + key);
						return loadDataFromDB(key);
					}

					@Override
					public ListenableFuture<List<FavoritesStockVO>> reload(final String key,
							final List<FavoritesStockVO> oldValue) {
						logger.info("reload from database, favoritesStockHelper key:" + key);
						ListenableFutureTask<List<FavoritesStockVO>> task = ListenableFutureTask
								.create(new Callable<List<FavoritesStockVO>>() {
									public List<FavoritesStockVO> call() {
										List<FavoritesStockVO> newValue = oldValue;
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

					private List<FavoritesStockVO> loadDataFromDB(String key) {
						// key is userId
						List<FavoritesStockVO> rtn = favoritesStockHelper.getByUserId(key);
						for (FavoritesStockVO vo : rtn) {
							CompanyInfoVO cvo = stockConfig.getByStockId(vo.stockId);
							if (cvo != null) {
								vo.setName(cvo.name);
							}
						}
						return rtn;
					}
				});
	}

	public LoadingCache<String, List<FavoritesStockVO>> getLoadingCache() {
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

	public void put(String key, List<FavoritesStockVO> value) {
		logger.info("put for " + key);
		cache.put(key, value);
	}

	public List<FavoritesStockVO> get(String key) {
		logger.info("get from cache, key:" + key);
		try {
			return cache.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<FavoritesStockVO>();
	}
}
