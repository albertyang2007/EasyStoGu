package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.CheckPointDailyStatisticsVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CheckPointDailyStatisticsTableHelper {
	private static Logger logger = LogHelper.getLogger(CheckPointDailyStatisticsTableHelper.class);
	private static CheckPointDailyStatisticsTableHelper instance = null;
	private static CheckPointDailyStatisticsTableHelper georedInstance = null;
	private String tableName = "CHECKPOINT_DAILY_STATISTICS";
	protected String INSERT_SQL = "INSERT INTO " + tableName
			+ " (date, checkpoint, count) VALUES (:date, :checkpoint, :count)";
	protected String DELETE_SQL = "DELETE FROM " + tableName + " WHERE date = :date AND checkpoint = :checkpoint";
	protected String COUNT_BY_DATE_AND_CHECKPOINT_SQL = "SELECT count AS rtn FROM " + tableName
			+ " WHERE date = :date AND checkpoint = :checkpoint";
	protected String DELETE_BY_CHECKPOINT = "DELETE FROM " + tableName + " WHERE checkPoint = :checkPoint";
	protected String DELETE_BY_DATE = "DELETE FROM " + tableName + " WHERE date = :date";
	protected String QUERY_BY_CHECKPOINT_AND_DATE = "SELECT * FROM " + tableName
			+ " WHERE checkPoint = :checkpoint AND date = :date";
	protected String QUERY_BY_DATE = "SELECT * FROM " + tableName + " WHERE date = :date";
	protected String QUERY_BY_CHECK_POINT = "SELECT * FROM " + tableName + " WHERE checkpoint = :checkpoint";
	protected String QUERY_BY_CHECK_POINT_ORDER_BY_DATE = "SELECT * FROM " + tableName
			+ " WHERE checkpoint = :checkpoint ORDER BY DATE";
	protected String QUERY_BY_BETWEEN_DATE = "SELECT * FROM " + tableName
			+ " WHERE date >= :startDate AND date <= :endDate";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private CheckPointDailyStatisticsTableHelper(javax.sql.DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(datasource);
	}

	public static CheckPointDailyStatisticsTableHelper getInstance() {
		if (instance == null) {
			instance = new CheckPointDailyStatisticsTableHelper(PostgreSqlDataSourceFactory.createDataSource());
		}
		return instance;
	}

	public static CheckPointDailyStatisticsTableHelper getGeoredInstance() {
		if (georedInstance == null) {
			georedInstance = new CheckPointDailyStatisticsTableHelper(
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

	private static final class CheckPointDailyStatisticsVOMapper implements RowMapper<CheckPointDailyStatisticsVO> {
		public CheckPointDailyStatisticsVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			CheckPointDailyStatisticsVO vo = new CheckPointDailyStatisticsVO();
			vo.setCheckPoint(rs.getString("checkpoint"));
			vo.setDate(rs.getString("date"));
			vo.setCount(rs.getInt("count"));
			return vo;
		}
	}

	public CheckPointDailyStatisticsVO getByCheckPointAndDate(String date, String checkpoint) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);
			namedParameters.addValue("checkpoint", checkpoint);

			CheckPointDailyStatisticsVO vo = this.namedParameterJdbcTemplate.queryForObject(
					QUERY_BY_CHECKPOINT_AND_DATE, namedParameters, new CheckPointDailyStatisticsVOMapper());

			return vo;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<CheckPointDailyStatisticsVO> getByDate(String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);

			List<CheckPointDailyStatisticsVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_DATE,
					namedParameters, new CheckPointDailyStatisticsVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CheckPointDailyStatisticsVO>();
	}

	public List<CheckPointDailyStatisticsVO> getAllCheckPointBetweenDate(String startDate, String endDate) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("startDate", startDate);
			namedParameters.addValue("endDate", endDate);

			List<CheckPointDailyStatisticsVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_BETWEEN_DATE,
					namedParameters, new CheckPointDailyStatisticsVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CheckPointDailyStatisticsVO>();
	}

	public List<CheckPointDailyStatisticsVO> getByCheckPoint(String checkpoint) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("checkpoint", checkpoint);

			List<CheckPointDailyStatisticsVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_CHECK_POINT,
					namedParameters, new CheckPointDailyStatisticsVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CheckPointDailyStatisticsVO>();
	}

	public List<CheckPointDailyStatisticsVO> getByCheckPointOrderByDate(String checkpoint) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("checkpoint", checkpoint);

			List<CheckPointDailyStatisticsVO> list = this.namedParameterJdbcTemplate.query(
					QUERY_BY_CHECK_POINT_ORDER_BY_DATE, namedParameters, new CheckPointDailyStatisticsVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CheckPointDailyStatisticsVO>();
	}

	public void insert(CheckPointDailyStatisticsVO vo) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", vo.getDate());
			namedParameters.addValue("checkpoint", vo.getCheckPoint());
			namedParameters.addValue("count", vo.getCount());

			namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insert(List<CheckPointDailyStatisticsVO> list) throws Exception {
		for (CheckPointDailyStatisticsVO vo : list) {
			this.insert(vo);
		}
	}

	public int countByDateAndCheckPoint(String date, String checkPoint) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);
			namedParameters.addValue("checkpoint", checkPoint);

			int rtn = this.namedParameterJdbcTemplate.queryForObject(COUNT_BY_DATE_AND_CHECKPOINT_SQL, namedParameters,
					new IntVOMapper());

			return rtn;
		} catch (EmptyResultDataAccessException ee) {
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
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
			namedParameterJdbcTemplate.execute(DELETE_BY_DATE, namedParameters, new DefaultPreparedStatementCallback());
		} catch (Exception e) {
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
		CheckPointDailyStatisticsTableHelper ins = CheckPointDailyStatisticsTableHelper.getInstance();
		try {
			CheckPointDailyStatisticsVO vo = ins.getByCheckPointAndDate("2016-05-10", "MACD_Dead");
			System.out.println(vo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
