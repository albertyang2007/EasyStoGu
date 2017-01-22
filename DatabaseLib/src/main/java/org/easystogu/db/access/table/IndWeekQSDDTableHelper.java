package org.easystogu.db.access.table;

public class IndWeekQSDDTableHelper extends IndQSDDTableHelper {
	private static IndWeekQSDDTableHelper instance = null;
	private static IndWeekQSDDTableHelper configInstance = null;

	public static IndWeekQSDDTableHelper getInstance() {
		if (instance == null) {
			instance = new IndWeekQSDDTableHelper();
		}
		return instance;
	}
	
	public static IndWeekQSDDTableHelper getConfigInstance(javax.sql.DataSource datasource) {
		if (configInstance == null) {
			configInstance = new IndWeekQSDDTableHelper(datasource);
		}
		return configInstance;
	}
	
	protected IndWeekQSDDTableHelper(javax.sql.DataSource datasource) {
		super(datasource);
		refeshTableSQL();
	}

	protected IndWeekQSDDTableHelper() {
		super();
		refeshTableSQL();
	}
	
	private void refeshTableSQL() {
		tableName = "IND_WEEK_QSDD";
		// please modify this SQL in all subClass
		INSERT_SQL = "INSERT INTO " + tableName
				+ " (stockId, date, lonterm, midterm, shoterm) VALUES (:stockId, :date, :lonterm, :midterm, :shoterm)";
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

	}
}
