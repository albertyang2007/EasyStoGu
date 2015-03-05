package org.easystogu.db.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.table.CheckPointHistoryAnalyseVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CheckPointHistoryAnalyseTableHelper {
    private static Logger logger = LogHelper.getLogger(CheckPointHistoryAnalyseTableHelper.class);
    private static CheckPointHistoryAnalyseTableHelper instance = null;
    protected DataSource dataSource = PostgreSqlDataSourceFactory.createDataSource();
    protected String tableName = "CHECKPOINT_HISTORY_ANALYSE";
    protected String INSERT_SQL = "INSERT INTO "
            + tableName
            + " (checkpoint, total_satisfy, close_earn_percent, high_earn_percent, low_earn_percent, avg_hold_days, total_high_earn) VALUES (:checkpoint, :total_satisfy, :close_earn_percent, :high_earn_percent, :low_earn_percent, :avg_hold_days, :total_high_earn)";
    protected String DELETE_BY_CHECKPOINT = "DELETE FROM " + tableName + " WHERE checkPoint = :checkPoint";
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static CheckPointHistoryAnalyseTableHelper getInstance() {
        if (instance == null) {
            instance = new CheckPointHistoryAnalyseTableHelper();
        }
        return instance;
    }

    protected CheckPointHistoryAnalyseTableHelper() {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private static final class HistoryAnalyseMapper implements RowMapper<CheckPointHistoryAnalyseVO> {
        public CheckPointHistoryAnalyseVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            CheckPointHistoryAnalyseVO vo = new CheckPointHistoryAnalyseVO();
            vo.setCheckPoint(rs.getString("checkpoint"));
            vo.setTotalSatisfy(rs.getInt("total_satisfy"));
            vo.setCloseEarnPercent(rs.getDouble("close_earn_percent"));
            vo.setHighEarnPercent(rs.getDouble("high_earn_percent"));
            vo.setLowEarnPercent(rs.getDouble("low_earn_percent"));
            vo.setAvgHoldDays(rs.getInt("avg_hold_days"));
            vo.setTotalHighEarn(rs.getInt("total_high_earn"));

            return vo;
        }
    }

    private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
        public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
            return ps.executeUpdate();
        }
    }

    public void insert(CheckPointHistoryAnalyseVO vo) {
        logger.debug("insert for {}", vo);

        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("checkpoint", vo.getCheckPoint());
            namedParameters.addValue("total_satisfy", vo.getTotalSatisfy());
            namedParameters.addValue("close_earn_percent", vo.getCloseEarnPercent());
            namedParameters.addValue("high_earn_percent", vo.getHighEarnPercent());
            namedParameters.addValue("low_earn_percent", vo.getLowEarnPercent());
            namedParameters.addValue("avg_hold_days", vo.getAvgHoldDays());
            namedParameters.addValue("total_high_earn", vo.getTotalHighEarn());

            namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            logger.error("exception meets for insert vo: " + vo, e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CheckPointHistoryAnalyseTableHelper ins = CheckPointHistoryAnalyseTableHelper.getInstance();
        CheckPointHistoryAnalyseVO vo = new CheckPointHistoryAnalyseVO();
        vo.setCheckPoint("Test");
        vo.setTotalSatisfy(1);
        vo.setCloseEarnPercent(2);
        vo.setHighEarnPercent(3);
        vo.setLowEarnPercent(4);
        vo.setAvgHoldDays(5);
        vo.setTotalHighEarn(6);
        ins.insert(vo);
    }
}
