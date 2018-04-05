package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.StockBehaviorStatisticsVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class StockBehaviorStatisticsTableHelper {
    private static Logger logger = LogHelper.getLogger(StockBehaviorStatisticsTableHelper.class);
    private static StockBehaviorStatisticsTableHelper instance = null;
    private static StockBehaviorStatisticsTableHelper georedInstance = null;
    private String tableName = "STOCK_BEHAVIOR_STATISTICS";
    protected String INSERT_SQL = "INSERT INTO " + tableName
            + " (stockid, checkpoint, statistics) VALUES (:stockid, :checkpoint, :statistics)";

    protected String DELETE_SQL = "DELETE FROM " + tableName + " WHERE stockid = :stockid AND checkpoint = :checkpoint";
    protected String DELETE_BY_CHECK_POINT = "DELETE FROM " + tableName + " WHERE checkpoint = :checkpoint";

    protected String QUERY_BY_STOCKID = "SELECT * FROM " + tableName + " WHERE stockid = :stockid";
    protected String QUERY_BY_STOCKID_AND_CHECK_POINT = "SELECT * FROM " + tableName
            + " WHERE stockid = :stockid AND checkpoint = :checkpoint";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private StockBehaviorStatisticsTableHelper(javax.sql.DataSource datasource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(datasource);
    }

    public static StockBehaviorStatisticsTableHelper getInstance() {
        if (instance == null) {
            instance = new StockBehaviorStatisticsTableHelper(PostgreSqlDataSourceFactory.createDataSource());
        }
        return instance;
    }

    public static StockBehaviorStatisticsTableHelper getGeoredInstance() {
        if (georedInstance == null) {
            georedInstance = new StockBehaviorStatisticsTableHelper(
                    PostgreSqlDataSourceFactory.createGeoredDataSource());
        }
        return georedInstance;
    }

    private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
        public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
            return ps.executeUpdate();
        }
    }

    private static final class IntVOMapper implements RowMapper<Integer> {
        public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getInt("rtn");
        }
    }

    private static final class StockBehaviorStatisticsVOMapper implements RowMapper<StockBehaviorStatisticsVO> {
        public StockBehaviorStatisticsVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            StockBehaviorStatisticsVO vo = new StockBehaviorStatisticsVO();
            vo.setStockId(rs.getString("stockid"));
            vo.setCheckPoint(rs.getString("checkpoint"));
            vo.setStatistics(rs.getDouble("statistics"));
            return vo;
        }
    }

    public StockBehaviorStatisticsVO getByStockIdAndCheckPoint(String stockId, String checkpoint) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockid", stockId);
            namedParameters.addValue("checkpoint", checkpoint);

            StockBehaviorStatisticsVO vo = this.namedParameterJdbcTemplate.queryForObject(
                    QUERY_BY_STOCKID_AND_CHECK_POINT, namedParameters, new StockBehaviorStatisticsVOMapper());

            return vo;
        } catch (EmptyResultDataAccessException ee) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<StockBehaviorStatisticsVO> getByStockId(String stockId) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockid", stockId);

            List<StockBehaviorStatisticsVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_STOCKID,
                    namedParameters, new StockBehaviorStatisticsVOMapper());

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<StockBehaviorStatisticsVO>();
    }

    public void insert(StockBehaviorStatisticsVO vo) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockid", vo.getStockId());
            namedParameters.addValue("checkpoint", vo.getCheckPoint());
            namedParameters.addValue("statistics", vo.getStatistics());

            namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(List<StockBehaviorStatisticsVO> list) throws Exception {
        for (StockBehaviorStatisticsVO vo : list) {
            this.insert(vo);
        }
    }

    public void delete(String stockid, String checkpoint) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockid", stockid);
            namedParameters.addValue("checkpoint", checkpoint);

            namedParameterJdbcTemplate.execute(DELETE_SQL, namedParameters, new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteByCheckPoint(String checkpoint) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("checkpoint", checkpoint);

            namedParameterJdbcTemplate.execute(DELETE_BY_CHECK_POINT, namedParameters, new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockBehaviorStatisticsTableHelper ins = StockBehaviorStatisticsTableHelper.getInstance();
        try {
            StockBehaviorStatisticsVO vo = ins.getByStockIdAndCheckPoint("601318", "");
            System.out.println(vo);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
