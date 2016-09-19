package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class IndMacdTableHelper {
    private static Logger logger = LogHelper.getLogger(IndMacdTableHelper.class);
    private static IndMacdTableHelper instance = null;
    protected DataSource dataSource = PostgreSqlDataSourceFactory.createDataSource();
    // please modify this SQL in all subClass
    protected String tableName = "IND_MACD";
    protected String INSERT_SQL = "INSERT INTO " + tableName
            + " (stockId, date, dif, dea, macd) VALUES (:stockId, :date, :dif, :dea, :macd)";
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

    public static IndMacdTableHelper getInstance() {
        if (instance == null) {
            instance = new IndMacdTableHelper();
        }
        return instance;
    }

    protected IndMacdTableHelper() {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private static final class IndMacdVOMapper implements RowMapper<MacdVO> {
        public MacdVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            MacdVO vo = new MacdVO();
            vo.setStockId(rs.getString("stockId"));
            vo.setDate(rs.getString("date"));
            vo.setDif(rs.getDouble("dif"));
            vo.setDea(rs.getDouble("dea"));
            vo.setMacd(rs.getDouble("macd"));
            return vo;
        }
    }

    private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
        public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
            return ps.executeUpdate();
        }
    }

    public void insert(MacdVO vo) {
        logger.debug("insert for {}", vo);

        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", vo.getStockId());
            namedParameters.addValue("date", vo.getDate());
            namedParameters.addValue("dif", vo.getDif());
            namedParameters.addValue("dea", vo.getDea());
            namedParameters.addValue("macd", vo.getMacd());

            namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            logger.error("exception meets for insert vo: " + vo, e);
            e.printStackTrace();
        }
    }

    public void insert(List<MacdVO> list) throws Exception {
        for (MacdVO vo : list) {
            this.insert(vo);
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

    public MacdVO getMacd(String stockId, String date) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("date", date);

            MacdVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_ID_AND_DATE_SQL, namedParameters,
                    new IndMacdVOMapper());

            return vo;
        } catch (EmptyResultDataAccessException ee) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MacdVO> getAllMacd(String stockId) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);

            List<MacdVO> list = this.namedParameterJdbcTemplate.query(QUERY_ALL_BY_ID_SQL, namedParameters,
                    new IndMacdVOMapper());

            return list;
        } catch (Exception e) {
            logger.error("exception meets for getAllMacd stockId=" + stockId, e);
            e.printStackTrace();
            return new ArrayList<MacdVO>();
        }
    }

    public List<MacdVO> getByIdAndBetweenDate(String stockId, String StartDate, String endDate) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("date1", StartDate);
            namedParameters.addValue("date2", endDate);

            List<MacdVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_STOCKID_AND_BETWEEN_DATE,
                    namedParameters, new IndMacdVOMapper());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<MacdVO>();
    }

    // 最近几天的，必须使用时间倒序的SQL
    public List<MacdVO> getNDateMacd(String stockId, int day) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("limit", day);

            List<MacdVO> list = this.namedParameterJdbcTemplate.query(QUERY_LATEST_N_BY_ID_SQL, namedParameters,
                    new IndMacdVOMapper());

            return list;
        } catch (Exception e) {
            logger.error("exception meets for getAvgClosePrice stockId=" + stockId, e);
            e.printStackTrace();
            return new ArrayList<MacdVO>();
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        IndMacdTableHelper ins = IndMacdTableHelper.getInstance();
        try {
            System.out.println(ins.getMacd("000333", "2015-01-27"));
            List<MacdVO> list = ins.getNDateMacd("000333", 20);
            for (MacdVO vo : list) {
                System.out.println(vo);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
