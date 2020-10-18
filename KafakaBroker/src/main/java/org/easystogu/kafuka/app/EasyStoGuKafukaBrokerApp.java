package org.easystogu.kafuka.app;

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
@ComponentScan(basePackages = { "org.easystogu" })
@EnableAutoConfiguration(exclude = { HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class,
		DataSourceAutoConfiguration.class, CassandraDataAutoConfiguration.class,
		PersistenceExceptionTranslationAutoConfiguration.class})
@EnableScheduling
public class EasyStoGuKafukaBrokerApp {
	private static final Logger LOGGER = LoggerFactory.getLogger(EasyStoGuKafukaBrokerApp.class);

	public static void main(String[] args) {
		try {
			LOGGER.info("Starting EasyStoGu KafukaBroker...");
			SpringApplication.run(EasyStoGuKafukaBrokerApp.class, args);
			LOGGER.info("EasyStoGu KafukaBroker started.");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Fail to start EasyStoGu KafukaBroker: {}", e.getMessage());

			System.exit(-1);
		}
	}
}
