package org.easystogu.cassandra.ks;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
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
	private FileConfigurationService config;
	private static Cluster cluster = null;

	public Cluster createCluster() {
		if (cluster != null && !cluster.isClosed())
			return cluster;

		logger.info("create Cassandra Cluster.");
		String contactPoints = config.getString(Constants.CassandraContactPoints);
		String contactPointsFromEnv = System.getenv(Constants.CassandraContactPoints);
		if(Strings.isNotEmpty(contactPointsFromEnv)) {
			contactPoints = contactPointsFromEnv;
			logger.info("Using env: {}, value: {}", Constants.CassandraContactPoints, contactPoints);
		}
		
		int port = config.getInt(Constants.CassandraPort);
		String portFromEnv = System.getenv(Constants.CassandraPort);
		if(Strings.isNotEmpty(portFromEnv)) {
			port = Integer.parseInt(portFromEnv);
			logger.info("Using env: {}, value: {}", Constants.CassandraPort, port);
		}
		
		String user = config.getString(Constants.CassandraUser);
		String userFromEnv = System.getenv(Constants.CassandraUser);
		if(Strings.isNotEmpty(userFromEnv)) {
			logger.info("Using env: {}, value: {}", Constants.CassandraUser, userFromEnv);
		}
		
		String pwd = config.getString(Constants.CassandraPassword);
		String pwdFromEnv = System.getenv(Constants.CassandraPassword);
		if(Strings.isNotEmpty(pwdFromEnv)) {
			logger.info("Using env: {}, value: {}", Constants.CassandraPassword, pwdFromEnv);
		}
		
		
		//cluster = Cluster.builder().addContactPoints(contactPoints).withPort(port).build();
	    //If docker using Authentication in cassandra, refer to https://hopding.com/cassandra-authentication-in-docker-container
        cluster = Cluster.builder().addContactPoints(contactPoints).withPort(port).withCredentials(user, pwd).build();

		return cluster;
	}
}
