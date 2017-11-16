package org.easystogu.config;

public class Constants {
	public static final String httpProxyServer = "http.proxy.server";
	public static final String httpProxyPort = "http.proxy.port";

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
	//public static String cacheIndKDJ = "indKDJ";
	//public static String cacheIndMacd = "indMacd";
	//public static String cacheIndBoll = "indBoll";
	//public static String cacheIndMA = "indMA";
	//public static String cacheIndShenXian = "indShenXian";
	//public static String cacheIndQSDD = "indQSDD";
	//public static String cacheIndWR = "indWR";
	//public static String cacheIndDDX = "indDDX";

	public static String cacheLatestNStockDate = "latestndate";
	public static String cacheSZZSDayListByIdAndBetweenDates = "SZZSDayListByIdAndBetweenDates";
	public static String cacheAllDealDate = "AllDealDate";

	public static String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	public static String fromToRegex = dateRegex + "_" + dateRegex;
	public static String HHmmss = "00:00:00";
	
	public static String CassandraKeySpace = "EasyStoGu";

	public static void main(String[] args) {
		for (int i = 1; i <= 31; i++) {
			System.out.println("{" + (i - 1) * 3 + ", " + ((i - 1) * 3 + 1) + ", " + ((i - 1) * 3 + 2) + "},");
		}
	}
}
