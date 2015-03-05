package org.easystogu.db.access;

import java.util.List;

import org.easystogu.db.table.MacdVO;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class IndWeekMacdTableHelper extends IndMacdTableHelper {
    private static IndWeekMacdTableHelper instance = null;

    public static IndWeekMacdTableHelper getInstance() {
        if (instance == null) {
            instance = new IndWeekMacdTableHelper();
        }
        return instance;
    }

    protected IndWeekMacdTableHelper() {
        super();
        tableName = "IND_WEEK_MACD";
        // please modify this SQL in superClass
        INSERT_SQL = "INSERT INTO " + tableName
                + " (stockId, date, dif, dea, macd) VALUES (:stockId, :date, :dif, :dea, :macd)";
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
        IndWeekMacdTableHelper ins = IndWeekMacdTableHelper.getInstance();
        try {
            // System.out.println(ins.getMacd("000333", "2015-01-27"));
            List<MacdVO> list = ins.getNDateMacd("000333", 5);
            for (MacdVO vo : list) {
                System.out.println(vo);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
