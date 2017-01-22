package org.easystogu.db.ds;

import org.easystogu.config.ConfigurationService;
import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

public class PostgreSqlDataSourceFactory {
	private static Logger logger = LogHelper.getLogger(PostgreSqlDataSourceFactory.class);
	private static ConfigurationService config = FileConfigurationService.getInstance();
	private static org.apache.tomcat.jdbc.pool.DataSource datasource = null;
	private static org.apache.tomcat.jdbc.pool.DataSource georedDatasource = null;

	public static javax.sql.DataSource createDataSource() {

		if (datasource != null)
			return datasource;

		logger.info("build postgrel datasource.");
		String driver = config.getString(Constants.JdbcDriver);
		String url = config.getString(Constants.JdbcUrl);
		String user = config.getString(Constants.JdbcUser);
		String password = config.getString(Constants.JdbcPassword);
		int active = config.getInt(Constants.JdbcMaxActive, 200);
		int idle = config.getInt(Constants.JdbcMaxIdle, 100);

		datasource = new org.apache.tomcat.jdbc.pool.DataSource();
		datasource.setDriverClassName(driver);
		datasource.setUrl(url);
		datasource.setUsername(user);
		datasource.setPassword(password);
		datasource.setMaxActive(active);
		datasource.setMaxIdle(idle);
		datasource.setMaxWait(10000);

		return datasource;
	}

	public static javax.sql.DataSource createGeoredDataSource() {

		if (georedDatasource != null)
			return georedDatasource;

		logger.info("build postgrel Geored datasource.");
		String driver = config.getString(Constants.GeoredJdbcDriver);
		String url = config.getString(Constants.GeoredJdbcUrl);
		String user = config.getString(Constants.GeoredJdbcUser);
		String password = config.getString(Constants.GeoredJdbcPassword);
		int active = config.getInt(Constants.GeoredJdbcMaxActive, 200);
		int idle = config.getInt(Constants.GeoredJdbcMaxIdle, 100);

		georedDatasource = new org.apache.tomcat.jdbc.pool.DataSource();
		georedDatasource.setDriverClassName(driver);
		georedDatasource.setUrl(url);
		georedDatasource.setUsername(user);
		georedDatasource.setPassword(password);
		georedDatasource.setMaxActive(active);
		georedDatasource.setMaxIdle(idle);
		georedDatasource.setMaxWait(10000);

		return georedDatasource;
	}

	public static void shutdown() {
		logger.info("close postgrel datasource.");
		if (datasource != null) {
			datasource.close();
		}

		if (georedDatasource != null) {
			georedDatasource.close();
		}
	}
}
