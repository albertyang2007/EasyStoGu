package org.easystogu.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

public class MyDiaTimeOutCache {
	private static Logger LOGGER = LogHelper.getLogger(MyDiaTimeOutCache.class);
	private static LoadingCache<String, Integer> CACHE;
	private static Integer defaultValue = 10 * 1000;// Milliseconds

	static {
		init();
	}

	private static void init() {
		CACHE = CacheBuilder.newBuilder().maximumSize(20)
		        .refreshAfterWrite(5, TimeUnit.SECONDS)//refresh value each 30 seconds if have access
				//.expireAfterWrite(60, TimeUnit.SECONDS)//reload value if no access in 60 seconds
				.removalListener(new RemovalListener<Object, Object>() {
					public void onRemoval(RemovalNotification<Object, Object> notification) {
						System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
					}
				}).build(new CacheLoader<String, Integer>() {
					@Override
					public Integer load(String key) throws Exception {
						System.out.println("load by " + Thread.currentThread().getName());
						return createValue(key);
					}

					//@Override
					//the caller thread will wait until the reload complete
					//the reload is in a same thread.
					//If other caller thread also access the key in cache, it will return 
					//the old value if the first caller is still waiting the reload complete
					public ListenableFuture<Integer> reloadSync(String key, Integer oldValue) throws Exception {
						System.out.println("reload by " + Thread.currentThread().getName());
						return Futures.immediateFuture(createValue(key));
					}
					
					/*
					 * async reload the data.
					 * if the first caller access the cache and the reload will be in
					 * another new thread. the first caller will use the old value.					
					 */
					@Override
					public ListenableFuture<Integer> reload(final String key, final Integer oldValue) {
						System.out.println("reload by " + Thread.currentThread().getName());
						ListenableFutureTask<Integer> task = ListenableFutureTask.create(new Callable<Integer>() {
							public Integer call() {
								Integer newValue = oldValue;
								try {
									newValue = createValue(key);
								} catch (Exception e) {
									e.printStackTrace();
								}
								return newValue;
							}
						});
						Executors.newSingleThreadExecutor().execute(task);
						return task;
					}

				});
	}
	
	private static Integer createValue(String key){
		try {
			System.out.println(Thread.currentThread().getName()
					+ ": sleeping 5 secs and load value for key " + key);
			Thread.sleep(5000);
			return 5;
		} catch (Exception e) {
			e.printStackTrace();
			return defaultValue;
		}
	}
	
	public static Integer get(String diaApp) {
		try {
			return CACHE.get(diaApp);
		} catch (ExecutionException e) {
			e.printStackTrace();
			return defaultValue;
		}
	}

	public static void main(String[] args) {
		try {
			System.out.println("1 get:" + MyDiaTimeOutCache.get("T4"));
			System.out.println("2 get:" + MyDiaTimeOutCache.get("T4"));
			System.out.println("sleeping 6 secs");
			Thread.sleep(6000);
			System.out.println("3 get:" + MyDiaTimeOutCache.get("T4"));
			
			System.out.println("sleeping 30 secs");
			Thread.sleep(30000);
			System.out.println("4 get:" + MyDiaTimeOutCache.get("T4"));
			
			System.out.println("sleeping 60 secs");
			Thread.sleep(60000);
			System.out.println("5 get:" + MyDiaTimeOutCache.get("T4"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
