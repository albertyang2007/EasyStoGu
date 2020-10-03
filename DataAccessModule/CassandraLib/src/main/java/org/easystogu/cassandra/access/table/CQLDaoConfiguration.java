package org.easystogu.cassandra.access.table;

import org.easystogu.cassandra.ks.CassandraKepSpaceFactory;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.driver.core.Session;

//another is SQLDaoConfiguration
@Configuration
public class CQLDaoConfiguration {
	private Logger logger = LogHelper.getLogger(CQLDaoConfiguration.class);
	@Autowired
	private CassandraKepSpaceFactory cassandraKepSpaceFactory;

	@Bean
	public Session cassandraSession() {
		logger.info("cassandraSession");
		return cassandraKepSpaceFactory.createCluster().connect();
	}
}
