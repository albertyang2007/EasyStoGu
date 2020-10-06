package org.easystogu.config;

public class Constants {
	public static final String httpProxyServer = "http.proxy.server";
	public static final String httpProxyPort = "http.proxy.port";

	//jdbc config will be set from system environment for k8s deployment
	public static final String JdbcDriver = "jdbc.driver";
	public static final String JdbcUrl = "jdbc.url";
	public static final String JdbcUser = "jdbc.user";
	public static final String JdbcPassword = "jdbc.password";
	public static final String JdbcMaxActive = "jdbc.maxActive";
	public static final String JdbcMaxIdle = "jdbc.maxIdle";

	public static final String GeoredJdbcDriver = "geored.jdbc.driver";
	public static final String GeoredJdbcUrl = "geored.jdbc.url";
	public static final String GeoredJdbcUser = "geored.jdbc.user";
	public static final String GeoredJdbcPassword = "geored.jdbc.password";
	public static final String GeoredJdbcMaxActive = "geored.jdbc.maxActive";
	public static final String GeoredJdbcMaxIdle = "geored.jdbc.maxIdle";

	public static final String ZONE_OFFICE = "office";
	public static final String ZONE_HOME = "home";
	public static final String ZONE_ALIYUN = "aliyun";
	public static final String DailyUpdateStockPriceByBatch = "DailyUpdateStockPriceByBatch";

	public static final String CassandraContactPoints = "cassandra.contactpoints";
	public static final String CassandraPort = "cassandra.port";

	public static String cacheStockPrice = "stockPrice";
	public static String cacheQianFuQuanStockPrice = "qianFuQuanStockPrice";

	public static String indKDJ = "indKDJ";
	public static String indMacd = "indMacd";
	public static String indBoll = "indBoll";
	public static String indShenXian = "indShenXian";
	public static String indQSDD = "indQSDD";
	public static String indWR = "indWR";

	public static String indWeekKDJ = "indWeekKDJ";
	public static String indWeekMacd = "indWeekMacd";

	public static String cacheLatestNStockDate = "latestndate";
	public static String cacheSZZSDayListByIdAndBetweenDates = "SZZSDayListByIdAndBetweenDates";
	public static String cacheAllDealDate = "AllDealDate";

	public static String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	public static String fromToRegex = dateRegex + "_" + dateRegex;
	public static String HHmmss = "00:00:00";

	public static final String CassandraKeySpace = "EasyStoGu";
	
	//indicatorDBType indicator store in Cassandra or Postgresql
	//It will be set from system environment, default is SQL
	//CQL for cassandra and sql for Postgresql
	public static String indicatorDBType = "indicatorDBType";

	public static void main(String[] args) {
		for (int i = 1; i <= 31; i++) {
			System.out.println("{" + (i - 1) * 3 + ", " + ((i - 1) * 3 + 1) + ", " + ((i - 1) * 3 + 2) + "},");
		}
	}
}
