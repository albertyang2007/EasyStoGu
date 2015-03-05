package org.easystogu.db.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.table.CheckPointHistorySelectionVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CheckPointHistorySelectionTableHelper {
    private static Logger logger = LogHelper.getLogger(CheckPointHistorySelectionTableHelper.class);
    private static CheckPointHistorySelectionTableHelper instance = null;
    protected DataSource dataSource = PostgreSqlDataSourceFactory.createDataSource();
    protected String tableName = "CHECKPOINT_HISTORY_SELECTION";
    protected String INSERT_SQL = "INSERT INTO " + tableName
            + " (stockId, checkPoint, buyDate, sellDate) VALUES (:stockId, :checkPoint, :buyDate, :sellDate)";
    protected String DELETE_BY_CHECKPOINT = "DELETE FROM " + tableName + " WHERE checkPoint = :checkPoint";
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static CheckPointHistorySelectionTableHelper getInstance() {
        if (instance == null) {
            instance = new CheckPointHistorySelectionTableHelper();
        }
        return instance;
    }

    protected CheckPointHistorySelectionTableHelper() {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private static final class HistoryReportMapper implements RowMapper<CheckPointHistorySelectionVO> {
        public CheckPointHistorySelectionVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            CheckPointHistorySelectionVO vo = new CheckPointHistorySelectionVO();
            vo.setStockId(rs.getString("stockId"));
            vo.setCheckPoint(rs.getString("checkPoint"));
            vo.setBuyDate(rs.getString("buyDate"));
            vo.setSellDate(rs.getString("sellDate"));

            return vo;
        }
    }

    private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
        public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
            return ps.executeUpdate();
        }
    }

    public void insert(CheckPointHistorySelectionVO vo) {
        logger.debug("insert for {}", vo);

        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", vo.getStockId());
            namedParameters.addValue("checkPoint", vo.getCheckPoint());
            namedParameters.addValue("buyDate", vo.getBuyDate());
            namedParameters.addValue("sellDate", vo.getSellDate());

            namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            logger.error("exception meets for insert vo: " + vo, e);
            e.printStackTrace();
        }
    }

    public void deleteByCheckPoint(String checkPoint) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("checkPoint", checkPoint);
            namedParameterJdbcTemplate.execute(DELETE_BY_CHECKPOINT, namedParameters,
                    new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        CheckPointHistorySelectionTableHelper ins = CheckPointHistorySelectionTableHelper.getInstance();
        CheckPointHistorySelectionVO vo = new CheckPointHistorySelectionVO();
        vo.stockId = "000333";
        vo.checkPoint = "checkPoint";
        vo.buyDate = "123";
        vo.sellDate = "234";
        ins.insert(vo);
    }

}
