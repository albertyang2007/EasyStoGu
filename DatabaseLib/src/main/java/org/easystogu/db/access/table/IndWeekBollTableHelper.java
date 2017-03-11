package org.easystogu.db.access.table;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;

public class IndWeekBollTableHelper extends IndBollTableHelper {
	private static IndWeekBollTableHelper instance = null;
	private static IndWeekBollTableHelper georedInstance = null;

	public static IndWeekBollTableHelper getInstance() {
		if (instance == null) {
			instance = new IndWeekBollTableHelper(PostgreSqlDataSourceFactory.createDataSource());
		}
		return instance;
	}

	public static IndWeekBollTableHelper getGeoredInstance() {
		if (georedInstance == null) {
			georedInstance = new IndWeekBollTableHelper(PostgreSqlDataSourceFactory.createGeoredDataSource());
		}
		return georedInstance;
	}

	protected IndWeekBollTableHelper(javax.sql.DataSource datasource) {
		super(datasource);
		refeshTableSQL();
	}

	private void refeshTableSQL() {
		tableName = "IND_WEEK_BOLL";
		INSERT_SQL = "INSERT INTO " + tableName
				+ " (stockId, date, mb, up, dn) VALUES (:stockId, :date, :mb, :up, :dn)";
		QUERY_BY_DATE_SQL = "SELECT * FROM " + tableName + " WHERE date = :date";
		QUERY_BY_ID_AND_DATE_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId AND date = :date";
		QUERY_ALL_BY_ID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId ORDER BY date";
		QUERY_LATEST_N_BY_ID_SQL = "SELECT * FROM " + tableName
				+ " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit";
		DELETE_BY_STOCKID_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId";
		DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId AND date = :date";
		DELETE_BY_DATE_SQL = "DELETE FROM " + tableName + " WHERE date = :date";
		QUERY_BY_STOCKID_AND_BETWEEN_DATE = "SELECT * FROM " + tableName
				+ " WHERE stockId = :stockId AND DATE >= :date1 AND DATE <= :date2 ORDER BY DATE";
	}
}
