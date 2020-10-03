package org.easystogu.cassandra.ks;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Cluster;

@Lazy
@Component
public class CassandraKepSpaceFactory {
	private static Logger logger = LogHelper.getLogger(CassandraKepSpaceFactory.class);
	@Autowired
	private static FileConfigurationService config;
	private static Cluster cluster = null;

	public Cluster createCluster() {
		if (cluster != null && !cluster.isClosed())
			return cluster;

		logger.info("create Cassandra Cluster.");
		String contactPoints = config.getString(Constants.CassandraContactPoints);
		int port = config.getInt(Constants.CassandraPort);

		cluster = Cluster.builder().addContactPoints(contactPoints).withPort(port).build();

		return cluster;
	}
}
