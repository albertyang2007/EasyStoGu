package org.easystogu.db.access;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class IndWeekBollTableHelper extends IndBollTableHelper {
    private static IndWeekBollTableHelper instance = null;

    public static IndWeekBollTableHelper getInstance() {
        if (instance == null) {
            instance = new IndWeekBollTableHelper();
        }
        return instance;
    }

    protected IndWeekBollTableHelper() {
        super();
        tableName = "IND_WEEK_BOLL";
        INSERT_SQL = "INSERT INTO " + tableName
                + " (stockId, date, mb, up, dn) VALUES (:stockId, :date, :mb, :up, :dn)";
        QUERY_BY_ID_AND_DATE_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId AND date = :date";
        QUERY_ALL_BY_ID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId ORDER BY date";
        QUERY_LATEST_N_BY_ID_SQL = "SELECT * FROM " + tableName
                + " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit";
        DELETE_BY_STOCKID_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId";
        DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId AND date = :date";
        DELETE_BY_DATE_SQL = "DELETE FROM " + tableName + " WHERE date = :date";

        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
}
