package org.easystogu.db.access.table;

public class IndWeekXueShi2TableHelper extends IndXueShi2TableHelper {
	private static IndWeekXueShi2TableHelper instance = null;
	private static IndWeekXueShi2TableHelper configInstance = null;

	public static IndWeekXueShi2TableHelper getInstance() {
		if (instance == null) {
			instance = new IndWeekXueShi2TableHelper();
		}
		return instance;
	}

	public static IndWeekXueShi2TableHelper getConfigInstance(javax.sql.DataSource datasource) {
		if (configInstance == null) {
			configInstance = new IndWeekXueShi2TableHelper(datasource);
		}
		return configInstance;
	}

	protected IndWeekXueShi2TableHelper(javax.sql.DataSource datasource) {
		super(datasource);
		refeshTableSQL();
	}

	protected IndWeekXueShi2TableHelper() {
		super();
		refeshTableSQL();
	}

	private void refeshTableSQL() {
		tableName = "IND_WEEK_XUESHI2";
		INSERT_SQL = "INSERT INTO " + tableName + " (stockId, date, up, dn) VALUES (:stockId, :date, :up, :dn)";
		QUERY_BY_ID_AND_DATE_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId AND date = :date";
		QUERY_ALL_BY_ID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId ORDER BY date";
		QUERY_LATEST_N_BY_ID_SQL = "SELECT * FROM " + tableName
				+ " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit";
		DELETE_BY_STOCKID_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId";
		DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId AND date = :date";
		DELETE_BY_DATE_SQL = "DELETE FROM " + tableName + " WHERE date = :date";
	}
}
