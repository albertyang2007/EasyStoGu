package org.easystogu.lifecycle;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * SpringContextReferenceRepository hold all the BeanFactoryReference and
 * ApplicationContext, when Application want to get the BeanFactory, please use
 * blow: SpringContextReferenceRepository.getDefaultRepository().getBeanFactory(
 * BEANREF_XML, BEANFACTORY_ID).
 * 
 * All the bean can be released all when Application is stop. This class is
 * thread-safe.
 * 
 */
public class SpringContextReferenceRepository {

	private static final Logger logger = LoggerFactory
			.getLogger("facility.lcm");

	// This Map hold the Spring BeanFactoryReference, key is the resourcePath
	// and beanFactoryId.
	private static final SequencedRepo<String, BeanFactoryReference> beanFactoryReferences = new SequencedRepo<String, BeanFactoryReference>();

	// This Map hold the Spring ApplicationContext, can be added to this map by
	// external code.
	private static final SequencedRepo<String, ApplicationContext> applicationContexts = new SequencedRepo<String, ApplicationContext>();

	private static final SpringContextReferenceRepository defaultRepository = new SpringContextReferenceRepository();

	public static SpringContextReferenceRepository getDefaultRepository() {
		return defaultRepository;
	}

	/**
	 * Get the BeanFactory based on the resourcePath and beanFactoryId, first
	 * will try to get from the saved internal map. Will call
	 * ContextSingletonBeanFactoryLocator to get the BeanFactoryReference if can
	 * not find.
	 */
	public BeanFactory getBeanFactory(final String resourcePath,
			final String beanFactoryId) {
		logger.debug("Get beanFactoryReference. resourcePath=" + resourcePath
				+ ", beanFactoryId=" + beanFactoryId);
		String brfKey = resourcePath + "," + beanFactoryId;
		BeanFactoryReference brfRecord = beanFactoryReferences
				.putIfAbsent(
						brfKey,
						new org.easystogu.lifecycle.SpringContextReferenceRepository.SequencedRepo.SequencedMapConstructer<String, BeanFactoryReference>() {
							// @Override
							public BeanFactoryReference build(String key) {
								logger.debug("Create new beanFactoryReference@"
										+ key);
								return ContextSingletonBeanFactoryLocator
										.getInstance(resourcePath)
										.useBeanFactory(beanFactoryId);
							}
						});

		return brfRecord.getFactory();
	}

	/**
	 * Release the beanFactory, which will destroy the spring application
	 * context and remove from the internal map.
	 */
	public void releaseBeanFactory(final String resourcePath,
			final String beanFactoryId) {
		String brfKey = resourcePath + "," + beanFactoryId;
		beanFactoryReferences
				.destroy(
						brfKey,
						new org.easystogu.lifecycle.SpringContextReferenceRepository.SequencedRepo.SequencedMapDestructer<String, BeanFactoryReference>() {
							// @Override
							public void destroy(String key,
									BeanFactoryReference value) {
								logger.debug("Destroy beanFactoryReference@"
										+ key);
								value.release();
							}
						});
	}

	/**
	 * Get the ClassPathXmlApplicationContext if there is a matched application
	 * context based on the beanDefFileName, otherwise will create a new one.
	 */
	public ApplicationContext getApplicationContext(String beanDefFileName) {
		logger.debug("Get ClassPathXmlApplicationContext. beanDefFileName="
				+ beanDefFileName);
		return applicationContexts
				.putIfAbsent(
						beanDefFileName,
						new org.easystogu.lifecycle.SpringContextReferenceRepository.SequencedRepo.SequencedMapConstructer<String, ApplicationContext>() {
							// @Override
							public ApplicationContext build(String key) {
								logger.debug("Create new ClassPathXmlApplicationContext@"
										+ key);
								return new ClassPathXmlApplicationContext(key);
							}
						});
	}

	/**
	 * Put the ApplicationContext into the internal map.
	 */
	public void registerApplicationContext(String beanDefFileName,
			ApplicationContext ac) {
		applicationContexts.put(beanDefFileName, ac);
	}

	/**
	 * Close the application context and remove from the internal map.
	 */
	public void releaseApplicationContext(String beanDefFileName) {
		applicationContexts
				.destroy(
						beanDefFileName,
						new org.easystogu.lifecycle.SpringContextReferenceRepository.SequencedRepo.SequencedMapDestructer<String, ApplicationContext>() {
							// @Override
							public void destroy(String key,
									ApplicationContext value) {
								logger.debug("Destroy ClassPathXmlApplicationContext@"
										+ key);
								((ConfigurableApplicationContext) value)
										.close();
							}
						});
	}

	/**
	 * Release all the registered application context, normally this is called
	 * when the application is stop.
	 */
	public void releaseAll() {
		releaseBeanFactoryReference();

		releaseApplicationContext();
	}

	// Release from the tail to head, in case there are dependency between
	// application contexts.
	private void releaseBeanFactoryReference() {
		beanFactoryReferences
				.clearReversely(new org.easystogu.lifecycle.SpringContextReferenceRepository.SequencedRepo.SequencedMapDestructer<String, BeanFactoryReference>() {
					// @Override
					public void destroy(String key, BeanFactoryReference value) {
						logger.debug("Destroy beanFactoryReference@" + key);
						value.release();
					}
				});
	}

	private void releaseApplicationContext() {
		applicationContexts
				.clearReversely(new org.easystogu.lifecycle.SpringContextReferenceRepository.SequencedRepo.SequencedMapDestructer<String, ApplicationContext>() {
					// @Override
					public void destroy(String key, ApplicationContext value) {
						logger.debug("Destroy ClassPathXmlApplicationContext@"
								+ key);
						((ConfigurableApplicationContext) value).close();
					}
				});
	}

	static class SequencedRepo<K, V> {
		private Map<K, V> innerMap = new LinkedHashMap<K, V>();

		public synchronized V putIfAbsent(K key,
				SequencedMapConstructer<K, V> constructer) {
			V value = innerMap.get(key);
			if (value == null) {
				value = constructer.build(key);
				innerMap.put(key, value);
			}
			return value;
		}

		public synchronized void put(K key, V value) {
			innerMap.put(key, value);
		}

		@SuppressWarnings("unused")
		public synchronized V get(K key) {
			return innerMap.get(key);
		}

		public synchronized void destroy(K key,
				SequencedMapDestructer<K, V> destructer) {
			V value = innerMap.get(key);
			if (value != null) {
				destructer.destroy(key, value);
				innerMap.remove(key);
			} else {
				logger.warn("Can not find object by key=" + key);
			}
		}

		@SuppressWarnings("unused")
		public synchronized void clear(SequencedMapDestructer<K, V> destructer) {
			for (Entry<K, V> entry : innerMap.entrySet()) {
				destructer.destroy(entry.getKey(), entry.getValue());
			}
			innerMap.clear();
		}

		public synchronized void clearReversely(
				SequencedMapDestructer<K, V> destructer) {
			List<Entry<K, V>> sequencedList = new LinkedList<Entry<K, V>>(
					innerMap.entrySet());
			ListIterator<Entry<K, V>> iter = sequencedList
					.listIterator(innerMap.size());
			while (iter.hasPrevious()) {
				Entry<K, V> entry = iter.previous();
				destructer.destroy(entry.getKey(), entry.getValue());
			}
			innerMap.clear();
			sequencedList.clear();
		}

		interface SequencedMapConstructer<K, V> {
			public V build(K key);
		}

		interface SequencedMapDestructer<K, V> {
			public void destroy(K key, V value);
		}
	}
}
