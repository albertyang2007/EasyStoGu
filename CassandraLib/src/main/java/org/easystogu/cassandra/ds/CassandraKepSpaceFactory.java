package org.easystogu.cassandra.ds;

import org.easystogu.config.ConfigurationService;
import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

import com.datastax.driver.core.Cluster;

public class CassandraKepSpaceFactory {
	private static Logger logger = LogHelper.getLogger(CassandraKepSpaceFactory.class);
	private static ConfigurationService config = FileConfigurationService.getInstance();
	private static Cluster cluster = null;

	public static Cluster createCluster() {
		if (cluster != null)
			return cluster;

		logger.info("create Cassandra Cluster.");
		String contactPoints = config.getString(Constants.CassandraContactPoints);
		int port = config.getInt(Constants.CassandraPort);

		cluster = Cluster.builder().addContactPoints(contactPoints).withPort(port).build();

		return cluster;
	}
}
