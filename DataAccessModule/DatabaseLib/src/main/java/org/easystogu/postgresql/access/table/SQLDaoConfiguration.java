package org.easystogu.postgresql.access.table;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

//aonther is CQLDaoConfiguration
@Configuration
public class SQLDaoConfiguration {
	@Autowired
	PostgreSqlDataSourceFactory postgreSqlDataSourceFactory;

	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(postgreSqlDataSourceFactory.createDataSource());
	}
}
