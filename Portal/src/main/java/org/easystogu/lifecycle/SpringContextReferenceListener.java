package org.easystogu.lifecycle;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SpringContextReferenceListener implements ServletContextListener {

	/**
	 * For multi-WAR in the same EAR, make sure the destroy method is called
	 * when the last WAR is deactived.
	 */
	private static volatile AtomicInteger packageReferenceNumber = new AtomicInteger(
			0);

	public void contextDestroyed(ServletContextEvent arg0) {
		int currVal = packageReferenceNumber.decrementAndGet();
		if (currVal == 0) {
			SpringContextReferenceRepository.getDefaultRepository()
					.releaseAll();
		}

	}

	public void contextInitialized(ServletContextEvent arg0) {
		int currVal = packageReferenceNumber.incrementAndGet();
	}

}
