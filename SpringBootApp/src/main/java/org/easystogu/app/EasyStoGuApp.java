package org.easystogu.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = { "org.easystogu", "com.tictactec.ta.lib" })
@EnableAutoConfiguration(exclude = { HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class,
		DataSourceAutoConfiguration.class, CassandraDataAutoConfiguration.class,
		PersistenceExceptionTranslationAutoConfiguration.class})
@EnableScheduling
public class EasyStoGuApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(EasyStoGuApp.class);

	public static void main(String[] args) {
		try {
			LOGGER.info("Starting EasyStoGuApp...");
			SpringApplication.run(EasyStoGuApp.class, args);
			LOGGER.info("EasyStoGuApp started.");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Fail to start EasyStoGuApp: {}", e.getMessage());

			System.exit(-1);
		}
	}
}
