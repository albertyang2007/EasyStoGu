package org.easystogu.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.easystogu.config.DBConfigurationService;
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
public class ConfigurationServiceCache {
	private Logger logger = LogHelper.getLogger(ConfigurationServiceCache.class);
	private static ConfigurationServiceCache instance = null;
	@Autowired
    private DBConfigurationService configServiceTable;
	private LoadingCache<String, Object> cache;

	private ConfigurationServiceCache() {
		cache = CacheBuilder.newBuilder().maximumSize(100).refreshAfterWrite(5, TimeUnit.MINUTES)
				.build(new CacheLoader<String, Object>() {
					@Override
					public Object load(String key) throws Exception {
						logger.info("load from database, configServiceTable key:" + key);
						return loadDataFromDB(key);
					}

					@Override
					public ListenableFuture<Object> reload(final String key, final Object oldValue) {
						logger.info("reload from database, configServiceTable key:" + key);
						ListenableFutureTask<Object> task = ListenableFutureTask.create(new Callable<Object>() {
							public Object call() {
								Object newValue = oldValue;
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

					private Object loadDataFromDB(String key) {
						return configServiceTable.getObject(key);
					}
				});
	}

	public static ConfigurationServiceCache getInstance() {
		if (instance == null) {
			instance = new ConfigurationServiceCache();
		}
		return instance;
	}

	public LoadingCache<String, Object> getLoadingCache() {
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

	public void put(String key, Object value) {
		logger.info("put for " + key);
		cache.put(key, value);
	}

	public Object get(String key) {
		logger.info("get from cache, key:" + key);
		try {
			return cache.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getString(String key) {
		return (String) get(key);
	}

	public String getString(String key, String defaultValue) {
		Object obj = get(key);
		if (obj != null) {
			return (String) obj;
		}
		return defaultValue;
	}

	public int getInt(String key) {
		return (Integer) get(key);
	}

	public int getInt(String key, int defaultValue) {
		Object obj = get(key);
		if (obj != null) {
			return (Integer) obj;
		}
		return defaultValue;
	}

	public boolean getBoolean(String key) {
		return (Boolean) get(key);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		Object obj = get(key);
		if (obj != null) {
			if (obj instanceof Boolean) {
				return (Boolean) obj;
			} else if (obj instanceof String) {
				return Boolean.parseBoolean((String) obj);
			}
		}
		return defaultValue;
	}

	public double getDouble(String key) {
		return (Double) get(key);
	}

	public double getDouble(String key, double defaultValue) {
		Object obj = get(key);
		if (obj != null) {
			return (Double) obj;
		}
		return defaultValue;
	}

	public static void main(String[] args) {
		System.out.println(Boolean.parseBoolean("True"));
	}
}
