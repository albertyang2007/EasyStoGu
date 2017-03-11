package org.easystogu.db.access.table;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;

public class IndWeekShenXianTableHelper extends IndShenXianTableHelper {
	private static IndWeekShenXianTableHelper instance = null;
	private static IndWeekShenXianTableHelper georedInstance = null;

	public static IndWeekShenXianTableHelper getInstance() {
		if (instance == null) {
			instance = new IndWeekShenXianTableHelper(PostgreSqlDataSourceFactory.createDataSource());
		}
		return instance;
	}

	public static IndWeekShenXianTableHelper getGeoredInstance() {
		if (georedInstance == null) {
			georedInstance = new IndWeekShenXianTableHelper(PostgreSqlDataSourceFactory.createGeoredDataSource());
		}
		return georedInstance;
	}

	protected IndWeekShenXianTableHelper(javax.sql.DataSource datasource) {
		super(datasource);
		refeshTableSQL();
	}

	private void refeshTableSQL() {
		tableName = "IND_WEEK_SHENXIAN";
		// please modify this SQL in superClass
		INSERT_SQL = "INSERT INTO " + tableName
				+ " (stockId, date, h1, h2, h3) VALUES (:stockId, :date, :h1, :h2, :h3)";
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IndWeekShenXianTableHelper ins = IndWeekShenXianTableHelper.getInstance();
		try {
			System.out.println(ins.getAllShenXian("002194").size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
