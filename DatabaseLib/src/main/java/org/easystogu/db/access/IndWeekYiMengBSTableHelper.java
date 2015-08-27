package org.easystogu.db.access;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class IndWeekYiMengBSTableHelper extends IndYiMengBSTableHelper {
	private static IndWeekYiMengBSTableHelper instance = null;

	public static IndWeekYiMengBSTableHelper getInstance() {
		if (instance == null) {
			instance = new IndWeekYiMengBSTableHelper();
		}
		return instance;
	}

	protected IndWeekYiMengBSTableHelper() {
		super();
		tableName = "IND_WEEK_YIMENGBS";
		// please modify this SQL in superClass
		INSERT_SQL = "INSERT INTO " + tableName + " (stockId, date, x2, x3) VALUES (:stockId, :date, :x2, :x3)";
		QUERY_BY_ID_AND_DATE_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId AND date = :date";
		QUERY_ALL_BY_ID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId ORDER BY date";
		QUERY_LATEST_N_BY_ID_SQL = "SELECT * FROM " + tableName
				+ " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit";
		DELETE_BY_STOCKID_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId";
		DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId AND date = :date";
		DELETE_BY_DATE_SQL = "DELETE FROM " + tableName + " WHERE date = :date";

		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IndWeekYiMengBSTableHelper ins = new IndWeekYiMengBSTableHelper();
		try {
			System.out.println(ins.getAllYiMengBS("002194").size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
