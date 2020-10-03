package org.easystogu.cassandra.access.table;

import org.easystogu.cassandra.ks.CassandraKepSpaceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.driver.core.Session;

//another is SQLDaoConfiguration
@Configuration
public class CQLDaoConfiguration {
	@Autowired
	private CassandraKepSpaceFactory cassandraKepSpaceFactory;

	@Bean
	public Session cassandraSession() {
		return cassandraKepSpaceFactory.createCluster().connect();
	}
}
