package org.easystogu.db.access.table;

public class IndWeekMai1Mai2TableHelper extends IndMai1Mai2TableHelper {
	private static IndWeekMai1Mai2TableHelper instance = null;
	private static IndWeekMai1Mai2TableHelper configInstance = null;

	public static IndWeekMai1Mai2TableHelper getInstance() {
		if (instance == null) {
			instance = new IndWeekMai1Mai2TableHelper();
		}
		return instance;
	}

	public static IndWeekMai1Mai2TableHelper getConfigInstance(javax.sql.DataSource datasource) {
		if (configInstance == null) {
			configInstance = new IndWeekMai1Mai2TableHelper(datasource);
		}
		return configInstance;
	}
	
	protected IndWeekMai1Mai2TableHelper(javax.sql.DataSource datasource) {
		super(datasource);
		refeshTableSQL();
	}
	
	protected IndWeekMai1Mai2TableHelper() {
		super();
		refeshTableSQL();
	}
	
	private void refeshTableSQL() {
		tableName = "IND_WEEK_MAI1MAI2";
		// please modify this SQL in superClass
		INSERT_SQL = "INSERT INTO " + tableName + " (stockId, date, sd, sk) VALUES (:stockId, :date, :sd, :sk)";
		QUERY_BY_ID_AND_DATE_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId AND date = :date";
		QUERY_ALL_BY_ID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId ORDER BY date";
		QUERY_LATEST_N_BY_ID_SQL = "SELECT * FROM " + tableName
				+ " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit";
		DELETE_BY_STOCKID_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId";
		DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId AND date = :date";
		DELETE_BY_DATE_SQL = "DELETE FROM " + tableName + " WHERE date = :date";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IndWeekMai1Mai2TableHelper ins = new IndWeekMai1Mai2TableHelper();
		try {
			System.out.println(ins.getAllMai1Mai2("600359").size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
