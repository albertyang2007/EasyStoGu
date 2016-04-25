package org.easystogu.db.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.table.CheckPointDailyStatisticsVO;
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
	private DataSource dataSource = PostgreSqlDataSourceFactory.createDataSource();
	private static CheckPointDailyStatisticsTableHelper instance = null;
	private String tableName = "CHECKPOINT_DAILY_STATISTICS";
	protected String INSERT_SQL = "INSERT INTO " + tableName
			+ " (date, checkpoint, count) VALUES (:date, :checkpoint, :count)";
	protected String DELETE_SQL = "DELETE FROM " + tableName + " WHERE date = :date AND checkpoint = :checkpoint";
	protected String COUNT_BY_DATE_AND_CHECKPOINT_SQL = "SELECT count AS rtn FROM " + tableName
			+ " WHERE date = :date AND checkpoint = :checkpoint";
	protected String DELETE_BY_CHECKPOINT = "DELETE FROM " + tableName + " WHERE checkPoint = :checkPoint";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public static CheckPointDailyStatisticsTableHelper getInstance() {
		if (instance == null) {
			instance = new CheckPointDailyStatisticsTableHelper();
		}
		return instance;
	}

	private CheckPointDailyStatisticsTableHelper() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
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
		CheckPointDailyStatisticsTableHelper ins = new CheckPointDailyStatisticsTableHelper();
		try {
			CheckPointDailyStatisticsVO vo = new CheckPointDailyStatisticsVO();
			vo.date = "2016-04-21";
			vo.checkPoint = "HengPan_3_Weeks_MA5_MA10_MA20_MA30_RongHe_Break_Platform";
			vo.count = 10;
			// ins.insert(vo);
			ins.delete(vo.date, vo.checkPoint);
			System.out.println();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}