package org.easystogu.db.ds;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class PostgreSqlDataSourceFactory {
	private static Logger logger = LogHelper.getLogger(PostgreSqlDataSourceFactory.class);
	@Autowired
	private FileConfigurationService config;
	private static org.apache.tomcat.jdbc.pool.DataSource datasource = null;
	private static org.apache.tomcat.jdbc.pool.DataSource georedDatasource = null;

	public javax.sql.DataSource createDataSource() {

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
