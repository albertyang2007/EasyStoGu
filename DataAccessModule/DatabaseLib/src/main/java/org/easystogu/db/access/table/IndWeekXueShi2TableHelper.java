package org.easystogu.db.access.table;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class IndWeekXueShi2TableHelper extends IndXueShi2TableHelper {
	@PostConstruct
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
