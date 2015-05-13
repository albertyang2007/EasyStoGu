package org.easystogu.db.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.table.GaoSongZhuanVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EventGaoSongZhuanTableHelper {
    private static Logger logger = LogHelper.getLogger(EventGaoSongZhuanTableHelper.class);
    private static EventGaoSongZhuanTableHelper instance = null;
    protected DataSource dataSource = PostgreSqlDataSourceFactory.createDataSource();
    protected String tableName = "EVENT_GAOSONGZHUAN";
    protected String INSERT_SQL = "INSERT INTO " + tableName
            + " (stockId, date, rate, alreadyupdateprice) VALUES (:stockId, :date, :rate, :alreadyupdateprice)";
    protected String QUERY_BY_ID_AND_DATE_SQL = "SELECT * FROM " + tableName
            + " WHERE stockId = :stockId AND date = :date";
    protected String QUERY_ALL_BY_ID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId ORDER BY date";
    protected String QUERY_LATEST_N_BY_ID_SQL = "SELECT * FROM " + tableName
            + " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit";
    protected String DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName
            + " WHERE stockId = :stockId AND date = :date";
    protected String UPDATE_FLAG_BY_STOCKID_AND_DATE = "UPDATE " + tableName
            + " SET alreadyupdateprice = :alreadyupdateprice" + " WHERE stockId = :stockId AND date = :date";

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static EventGaoSongZhuanTableHelper getInstance() {
        if (instance == null) {
            instance = new EventGaoSongZhuanTableHelper();
        }
        return instance;
    }

    protected EventGaoSongZhuanTableHelper() {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private static final class GaoSongZhuanVOMapper implements RowMapper<GaoSongZhuanVO> {
        public GaoSongZhuanVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            GaoSongZhuanVO vo = new GaoSongZhuanVO();
            vo.setStockId(rs.getString("stockId"));
            vo.setDate(rs.getString("date"));
            vo.setRate(rs.getDouble("rate"));
            vo.setAlreadyUpdatePrice(rs.getBoolean("alreadyupdateprice"));
            return vo;
        }
    }

    private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
        public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
            return ps.executeUpdate();
        }
    }

    public void insert(GaoSongZhuanVO vo) {
        logger.debug("insert for {}", vo);

        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", vo.getStockId());
            namedParameters.addValue("date", vo.getDate());
            namedParameters.addValue("rate", vo.getRate());
            namedParameters.addValue("alreadyupdateprice", vo.isAlreadyUpdatePrice());

            namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            logger.error("exception meets for insert vo: " + vo, e);
            e.printStackTrace();
        }
    }

    public void insert(List<GaoSongZhuanVO> list) throws Exception {
        for (GaoSongZhuanVO vo : list) {
            this.insert(vo);
        }
    }

    public GaoSongZhuanVO getGaoSongZhuanVO(String stockId, String date) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("date", date);

            GaoSongZhuanVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_ID_AND_DATE_SQL,
                    namedParameters, new GaoSongZhuanVOMapper());

            return vo;
        } catch (EmptyResultDataAccessException ee) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<GaoSongZhuanVO> getAllGaoSongZhuanVO(String stockId) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);

            List<GaoSongZhuanVO> list = this.namedParameterJdbcTemplate.query(QUERY_ALL_BY_ID_SQL, namedParameters,
                    new GaoSongZhuanVOMapper());

            return list;
        } catch (Exception e) {
            logger.error("exception meets for getAllKDJ stockId=" + stockId, e);
            e.printStackTrace();
            return new ArrayList<GaoSongZhuanVO>();
        }
    }

    public List<GaoSongZhuanVO> getNDateGaoSongZhuanVO(String stockId, int day) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("limit", day);

            List<GaoSongZhuanVO> list = this.namedParameterJdbcTemplate.query(QUERY_LATEST_N_BY_ID_SQL,
                    namedParameters, new GaoSongZhuanVOMapper());

            return list;
        } catch (Exception e) {
            logger.error("exception meets for getAvgClosePrice stockId=" + stockId, e);
            e.printStackTrace();
            return new ArrayList<GaoSongZhuanVO>();
        }
    }

    public void delete(String stockId, String date) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("date", date);
            namedParameterJdbcTemplate.execute(DELETE_BY_STOCKID_AND_DATE_SQL, namedParameters,
                    new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateGaoSongZhuanVO(GaoSongZhuanVO vo) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", vo.stockId);
            namedParameters.addValue("date", vo.date);
            namedParameters.addValue("alreadyupdateprice", vo.isAlreadyUpdatePrice());

            namedParameterJdbcTemplate.execute(UPDATE_FLAG_BY_STOCKID_AND_DATE, namedParameters,
                    new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
