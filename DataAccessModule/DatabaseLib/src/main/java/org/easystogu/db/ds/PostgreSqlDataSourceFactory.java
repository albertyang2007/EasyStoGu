package org.easystogu.db.ds;

import org.easystogu.config.ConfigurationService;
import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.table.WSFConfigTableHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
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
		// for k8s deployment, the parm is getting from configMap,
		// they are set to system environment
		String urlFromEnv = System.getenv(Constants.JdbcUrl);
		if (Strings.isNotEmpty(urlFromEnv)) {
			logger.info("Using env: {}, value: {}", Constants.JdbcUrl, urlFromEnv);
			url = urlFromEnv;
		}

		String user = config.getString(Constants.JdbcUser);
		String userFromEnv = System.getenv(Constants.JdbcUser);
		if (Strings.isNotEmpty(userFromEnv)) {
			logger.info("Using env: {}, value: {}", Constants.JdbcUser, userFromEnv);
			user = userFromEnv;
		}

		String password = config.getString(Constants.JdbcPassword);
		String pwdFromEnv = System.getenv(Constants.JdbcPassword);
		if (Strings.isNotEmpty(pwdFromEnv)) {
			logger.info("Using env: {}, value: {}", Constants.JdbcPassword, pwdFromEnv);
			password = pwdFromEnv;
		}

		int active = config.getInt(Constants.JdbcMaxActive, 200);
		String activeFromEnv = System.getenv(Constants.JdbcMaxActive);
		if (Strings.isNotEmpty(activeFromEnv)) {
			logger.info("Using env: {}, value: {}", Constants.JdbcMaxActive, activeFromEnv);
			active = Integer.parseInt(activeFromEnv);
		}

		int idle = config.getInt(Constants.JdbcMaxIdle, 100);
		String idleFromEnv = System.getenv(Constants.JdbcMaxIdle);
		if (Strings.isNotEmpty(idleFromEnv)) {
			logger.info("Using env: {}, value: {}", Constants.JdbcMaxIdle, idleFromEnv);
			idle = Integer.parseInt(idleFromEnv);
		}

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

		WSFConfigTableHelper wsfconfig = WSFConfigTableHelper.getInstance();

		logger.info("build postgrel Geored datasource.");
		String driver = config.getString(Constants.GeoredJdbcDriver);

		String url = wsfconfig.getValue(Constants.GeoredJdbcUrl);
		String urlFromEnv = System.getenv(Constants.GeoredJdbcUrl);
		if (Strings.isNotEmpty(urlFromEnv)) {
			logger.info("Using env: {}, value: {}", Constants.GeoredJdbcUrl, urlFromEnv);
			url = urlFromEnv;
		}

		String user = config.getString(Constants.GeoredJdbcUser);
		String userFromEnv = System.getenv(Constants.GeoredJdbcUser);
		if (Strings.isNotEmpty(userFromEnv)) {
			logger.info("Using env: {}, value: {}", Constants.GeoredJdbcUser, userFromEnv);
			user = userFromEnv;
		}

		String password = config.getString(Constants.GeoredJdbcPassword);
		String pwdFromEnv = System.getenv(Constants.GeoredJdbcPassword);
		if (Strings.isNotEmpty(pwdFromEnv)) {
			logger.info("Using env: {}, value: {}", Constants.GeoredJdbcPassword, pwdFromEnv);
			password = pwdFromEnv;
		}

		int active = config.getInt(Constants.GeoredJdbcMaxActive, 200);
		String activeFromEnv = System.getenv(Constants.GeoredJdbcMaxActive);
		if (Strings.isNotEmpty(activeFromEnv)) {
			logger.info("Using env: {}, value: {}", Constants.GeoredJdbcMaxActive, activeFromEnv);
			active = Integer.parseInt(activeFromEnv);
		}

		int idle = config.getInt(Constants.GeoredJdbcMaxIdle, 100);
		String idleFromEnv = System.getenv(Constants.GeoredJdbcMaxIdle);
		if (Strings.isNotEmpty(idleFromEnv)) {
			logger.info("Using env: {}, value: {}", Constants.GeoredJdbcMaxIdle, idleFromEnv);
			idle = Integer.parseInt(idleFromEnv);
		}

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

	public static void main(String[] args) {
		PostgreSqlDataSourceFactory.createGeoredDataSource();
	}
}
