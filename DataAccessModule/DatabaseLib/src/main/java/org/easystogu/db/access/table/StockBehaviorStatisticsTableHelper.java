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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class StockBehaviorStatisticsTableHelper {
    private static Logger logger = LogHelper.getLogger(StockBehaviorStatisticsTableHelper.class);
	@Autowired
	protected PostgreSqlDataSourceFactory postgreSqlDataSourceFactory;
    private String tableName = "STOCK_BEHAVIOR_STATISTICS";
    protected String INSERT_SQL = "INSERT INTO " + tableName
            + " (stockid, checkpoint, statistics) VALUES (:stockid, :checkpoint, :statistics)";

    protected String DELETE_SQL = "DELETE FROM " + tableName + " WHERE stockid = :stockid AND checkpoint = :checkpoint";
    protected String DELETE_BY_CHECK_POINT = "DELETE FROM " + tableName + " WHERE checkpoint = :checkpoint";

    protected String QUERY_BY_STOCKID = "SELECT * FROM " + tableName + " WHERE stockid = :stockid";
    protected String QUERY_BY_STOCKID_AND_CHECK_POINT = "SELECT * FROM " + tableName
            + " WHERE stockid = :stockid AND checkpoint = :checkpoint";

	
	private NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(postgreSqlDataSourceFactory.createDataSource());
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
            vo.setStatistics(rs.getString("statistics"));
            return vo;
        }
    }

    public StockBehaviorStatisticsVO getByStockIdAndCheckPoint(String stockId, String checkpoint) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockid", stockId);
            namedParameters.addValue("checkpoint", checkpoint);

            StockBehaviorStatisticsVO vo = this.getNamedParameterJdbcTemplate().queryForObject(
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

            List<StockBehaviorStatisticsVO> list = this.getNamedParameterJdbcTemplate().query(QUERY_BY_STOCKID,
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

            getNamedParameterJdbcTemplate().execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
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

            getNamedParameterJdbcTemplate().execute(DELETE_SQL, namedParameters, new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteByCheckPoint(String checkpoint) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("checkpoint", checkpoint);

            getNamedParameterJdbcTemplate().execute(DELETE_BY_CHECK_POINT, namedParameters, new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
