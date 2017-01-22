package org.easystogu.db.access.table;

public class IndWeekShenXianTableHelper extends IndShenXianTableHelper {
	private static IndWeekShenXianTableHelper instance = null;
	private static IndWeekShenXianTableHelper configInstance = null;

	public static IndWeekShenXianTableHelper getInstance() {
		if (instance == null) {
			instance = new IndWeekShenXianTableHelper();
		}
		return instance;
	}
	
	public static IndWeekShenXianTableHelper getConfigInstance(javax.sql.DataSource datasource) {
		if (configInstance == null) {
			configInstance = new IndWeekShenXianTableHelper(datasource);
		}
		return configInstance;
	}
	
	protected IndWeekShenXianTableHelper(javax.sql.DataSource datasource) {
		super(datasource);
		refeshTableSQL();
	}

	protected IndWeekShenXianTableHelper() {
		super();
		refeshTableSQL();
	}
	
	private void refeshTableSQL() {
		tableName = "IND_WEEK_SHENXIAN";
		// please modify this SQL in superClass
		INSERT_SQL = "INSERT INTO " + tableName
				+ " (stockId, date, h1, h2, h3) VALUES (:stockId, :date, :h1, :h2, :h3)";
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
		IndWeekShenXianTableHelper ins = new IndWeekShenXianTableHelper();
		try {
			System.out.println(ins.getAllShenXian("002194").size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
