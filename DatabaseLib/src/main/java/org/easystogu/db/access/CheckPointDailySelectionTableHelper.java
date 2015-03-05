package org.easystogu.db.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.table.CheckPointDailySelectionVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CheckPointDailySelectionTableHelper {
    private static Logger logger = LogHelper.getLogger(CheckPointDailySelectionTableHelper.class);
    private DataSource dataSource = PostgreSqlDataSourceFactory.createDataSource();
    private static CheckPointDailySelectionTableHelper instance = null;
    private String tableName = "CHECKPOINT_DAILY_SELECTION";
    public String INSERT_SQL = "INSERT INTO " + tableName
            + " (date, checkpoint, stockidlist) VALUES (:date, :checkpoint, :stockidlist)";
    public String UPDATE_SQL = "UPDATE " + tableName
            + " SET stockidlist = :stockidlist WHERE date = :date AND checkpoint = :checkpoint";
    public String DELETE_SQL = "DELETE FROM " + tableName + " WHERE date = :date AND checkpoint = :checkpoint";
    public String DELETE_BY_DATE_SQL = "DELETE FROM " + tableName + " WHERE date = :date";
    public String QUERY_BY_DATE_AND_CHECKPOINT_SQL = "SELECT * FROM " + tableName
            + " WHERE date = :date AND checkpoint = :checkpoint";
    public String QUERY_BY_DATE_AND_CHECKPOINT_AND_ID_SQL = "SELECT * FROM " + tableName
            + " WHERE date = :date AND checkpoint = :checkpoint AND stockidlist LIKE :stockidlist";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static CheckPointDailySelectionTableHelper getInstance() {
        if (instance == null) {
            instance = new CheckPointDailySelectionTableHelper();
        }
        return instance;
    }

    private CheckPointDailySelectionTableHelper() {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private static final class IndEventVOMapper implements RowMapper<CheckPointDailySelectionVO> {
        public CheckPointDailySelectionVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            CheckPointDailySelectionVO vo = new CheckPointDailySelectionVO();
            vo.setDate(rs.getString("date"));
            vo.setCheckPoint(rs.getString("checkpoint"));
            vo.setStockIdList(rs.getString("stockidlist"));
            return vo;
        }
    }

    private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
        public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
            return ps.executeUpdate();
        }
    }

    public void insert(CheckPointDailySelectionVO vo) {
        logger.debug("insert for {}", vo);

        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("date", vo.getDate());
            namedParameters.addValue("checkpoint", vo.getCheckPoint());
            namedParameters.addValue("stockidlist", vo.getStockIdList());

            namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            logger.error("exception meets for insert vo: " + vo, e);
            e.printStackTrace();
        }
    }

    public void insert(List<CheckPointDailySelectionVO> list) throws Exception {
        for (CheckPointDailySelectionVO vo : list) {
            this.insert(vo);
        }
    }

    public CheckPointDailySelectionVO getEvent(String date, String checkpoint) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("date", date);
            namedParameters.addValue("checkpoint", checkpoint);

            CheckPointDailySelectionVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_DATE_AND_CHECKPOINT_SQL,
                    namedParameters, new IndEventVOMapper());

            return vo;
        } catch (EmptyResultDataAccessException ee) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(CheckPointDailySelectionVO vo) {
        logger.debug("update for {}", vo);
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("date", vo.getDate());
            namedParameters.addValue("checkpoint", vo.getCheckPoint());
            namedParameters.addValue("stockidlist", vo.getStockIdList());

            namedParameterJdbcTemplate.execute(UPDATE_SQL, namedParameters, new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String date, String checkpoint) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("date", date);
            namedParameters.addValue("checkpoint", checkpoint);

            namedParameterJdbcTemplate.execute(DELETE_SQL, namedParameters, new DefaultPreparedStatementCallback());
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

    public boolean isEventExist(String date, String checkpoint, String stockId) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("date", date);
            namedParameters.addValue("checkpoint", checkpoint);
            namedParameters.addValue("stockidlist", "%" + stockId + "%");

            CheckPointDailySelectionVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_DATE_AND_CHECKPOINT_AND_ID_SQL,
                    namedParameters, new IndEventVOMapper());

            if (vo != null) {
                return true;
            }
        } catch (EmptyResultDataAccessException ee) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        CheckPointDailySelectionTableHelper ins = new CheckPointDailySelectionTableHelper();
        try {
            boolean exist = ins.isEventExist("2015-02-17", "Volume_Big_1", "600050");
            System.out.println(exist);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
