package org.easystogu.postgresql.access.table;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

//aonther is CQLDaoConfiguration
@Configuration
public class SQLDaoConfiguration {
	private Logger logger = LogHelper.getLogger(SQLDaoConfiguration.class);
	@Autowired
	PostgreSqlDataSourceFactory postgreSqlDataSourceFactory;

	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
		logger.info("namedParameterJdbcTemplate");
		return new NamedParameterJdbcTemplate(postgreSqlDataSourceFactory.createDataSource());
	}
}
