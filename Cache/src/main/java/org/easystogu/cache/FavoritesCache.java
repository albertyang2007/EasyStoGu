package org.easystogu.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.easystogu.db.access.table.FavoritesStockHelper;
import org.easystogu.db.vo.table.CompanyInfoVO;
import org.easystogu.db.vo.view.FavoritesStockVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class FavoritesCache {
	private Logger logger = LogHelper.getLogger(FavoritesCache.class);
	private static FavoritesCache instance = null;
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private FavoritesStockHelper favoritesStockHelper = FavoritesStockHelper.getInstance();
	private LoadingCache<String, List<FavoritesStockVO>> cache;

	private FavoritesCache() {
		cache = CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(12, TimeUnit.HOURS)
				.build(new CacheLoader<String, List<FavoritesStockVO>>() {
					@Override
					public List<FavoritesStockVO> load(String key) throws Exception {
						logger.info("load from database, favoritesStockHelper key:" + key);
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

	public static FavoritesCache getInstance() {
		if (instance == null) {
			instance = new FavoritesCache();
		}
		return instance;
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
