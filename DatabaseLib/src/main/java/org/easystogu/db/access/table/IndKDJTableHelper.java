package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class IndKDJTableHelper {

    private static Logger logger = LogHelper.getLogger(IndKDJTableHelper.class);
    private static IndKDJTableHelper instance = null;
    protected DataSource dataSource = PostgreSqlDataSourceFactory.createDataSource();
    protected String tableName = "IND_KDJ";
    // please modify this SQL in all subClass
    protected String INSERT_SQL = "INSERT INTO " + tableName
            + " (stockId, date, k, d, j, rsv) VALUES (:stockId, :date, :k, :d, :j, :rsv)";
    protected String QUERY_BY_ID_AND_DATE_SQL = "SELECT * FROM " + tableName
            + " WHERE stockId = :stockId AND date = :date";
    protected String QUERY_ALL_BY_ID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId ORDER BY date";
    protected String QUERY_LATEST_N_BY_ID_SQL = "SELECT * FROM " + tableName
            + " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit";
    protected String DELETE_BY_STOCKID_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId";
    protected String DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName
            + " WHERE stockId = :stockId AND date = :date";
    protected String DELETE_BY_DATE_SQL = "DELETE FROM " + tableName + " WHERE date = :date";
    protected String QUERY_BY_STOCKID_AND_BETWEEN_DATE = "SELECT * FROM " + tableName
            + " WHERE stockId = :stockId AND DATE >= :date1 AND DATE <= :date2 ORDER BY DATE";

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static IndKDJTableHelper getInstance() {
        if (instance == null) {
            instance = new IndKDJTableHelper();
        }
        return instance;
    }

    protected IndKDJTableHelper() {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private static final class KDJVOMapper implements RowMapper<KDJVO> {
        public KDJVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            KDJVO vo = new KDJVO();
            vo.setStockId(rs.getString("stockId"));
            vo.setDate(rs.getString("date"));
            vo.setK(rs.getDouble("k"));
            vo.setD(rs.getDouble("d"));
            vo.setJ(rs.getDouble("j"));
            vo.setRsv(rs.getDouble("rsv"));
            return vo;
        }
    }

    private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
        public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
            return ps.executeUpdate();
        }
    }

    public void insert(KDJVO vo) {
        logger.debug("insert for {}", vo);

        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", vo.getStockId());
            namedParameters.addValue("date", vo.getDate());
            namedParameters.addValue("k", vo.getK());
            namedParameters.addValue("d", vo.getD());
            namedParameters.addValue("j", vo.getJ());
            namedParameters.addValue("rsv", vo.getRsv());

            namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            logger.error("exception meets for insert vo: " + vo, e);
            e.printStackTrace();
        }
    }

    public void delete(String stockId) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameterJdbcTemplate.execute(DELETE_BY_STOCKID_SQL, namedParameters,
                    new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            e.printStackTrace();
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

    public void deleteByDate(String date) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("date", date);
            namedParameterJdbcTemplate.execute(DELETE_BY_DATE_SQL, namedParameters,
                    new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(List<KDJVO> list) throws Exception {
        for (KDJVO vo : list) {
            this.insert(vo);
        }
    }

    public KDJVO getKDJ(String stockId, String date) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("date", date);

            KDJVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_ID_AND_DATE_SQL, namedParameters,
                    new KDJVOMapper());

            return vo;
        } catch (EmptyResultDataAccessException ee) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<KDJVO> getAllKDJ(String stockId) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);

            List<KDJVO> list = this.namedParameterJdbcTemplate.query(QUERY_ALL_BY_ID_SQL, namedParameters,
                    new KDJVOMapper());

            return list;
        } catch (Exception e) {
            logger.error("exception meets for getAllKDJ stockId=" + stockId, e);
            e.printStackTrace();
            return new ArrayList<KDJVO>();
        }
    }
    
    public List<KDJVO> getByIdAndBetweenDate(String stockId, String StartDate, String endDate) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("date1", StartDate);
            namedParameters.addValue("date2", endDate);

            List<KDJVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_STOCKID_AND_BETWEEN_DATE,
                    namedParameters, new KDJVOMapper());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<KDJVO>();
    }

    // 最近几天的，必须使用时间倒序的SQL
    public List<KDJVO> getNDateKDJ(String stockId, int day) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("limit", day);

            List<KDJVO> list = this.namedParameterJdbcTemplate.query(QUERY_LATEST_N_BY_ID_SQL, namedParameters,
                    new KDJVOMapper());

            return list;
        } catch (Exception e) {
            logger.error("exception meets for getAvgClosePrice stockId=" + stockId, e);
            e.printStackTrace();
            return new ArrayList<KDJVO>();
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        IndKDJTableHelper ins = new IndKDJTableHelper();
        try {

            System.out.println(ins.getNDateKDJ("600589", 40).size());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
