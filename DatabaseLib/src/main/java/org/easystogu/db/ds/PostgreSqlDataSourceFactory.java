package org.easystogu.db.ds;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

public class PostgreSqlDataSourceFactory {
	private static Logger logger = LogHelper.getLogger(PostgreSqlDataSourceFactory.class);
	private static FileConfigurationService config = FileConfigurationService.getInstance();
	private static org.apache.tomcat.jdbc.pool.DataSource ds = null;

	public static javax.sql.DataSource createDataSource() {

		if (ds != null)
			return ds;
		
		logger.info("build postgrel datasource.");
		String driver = config.getString(Constants.JdbcDriver);
		String url = config.getString(Constants.JdbcUrl);
		String user = config.getString(Constants.JdbcUser);
		String password = config.getString(Constants.JdbcPassword);
		int active = config.getInt(Constants.JdbcMaxActive, 200);
		int idle = config.getInt(Constants.JdbcMaxIdle, 100);

		ds = new org.apache.tomcat.jdbc.pool.DataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url);
		ds.setUsername(user);
		ds.setPassword(password);
		ds.setMaxActive(active);
		ds.setMaxIdle(idle);
		ds.setMaxWait(10000);

		return ds;
	}

	public static void shutdown() {
		logger.info("close postgrel datasource.");
		if (ds != null) {
			ds.close();
		}
	}
}
