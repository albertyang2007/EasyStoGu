package org.easystogu.db.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
			+ " (stockid, date, checkpoint) VALUES (:stockid, :date, :checkpoint)";
	public String DELETE_SQL = "DELETE FROM " + tableName
			+ " WHERE stockid = :stockid AND date = :date AND checkpoint = :checkpoint";
	public String QUERY_BY_STOCKID_AND_DATE_AND_CHECKPOINT_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockid = :stockid AND date = :date AND checkpoint = :checkpoint";
	public String QUERY_LATEST_BY_STOCKID_AND_NOT_CHECKPOINT_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockid = :stockid AND checkpoint != :checkpoint ORDER BY DATE DESC LIMIT 1";

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
			vo.setStockId(rs.getString("stockid"));
			vo.setDate(rs.getString("date"));
			vo.setCheckPoint(rs.getString("checkpoint"));
			return vo;
		}
	}

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public void insert(CheckPointDailySelectionVO vo) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockid", vo.getStockId());
			namedParameters.addValue("date", vo.getDate());
			namedParameters.addValue("checkpoint", vo.getCheckPoint());

			namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertIfNotExist(CheckPointDailySelectionVO vo) {

		if (isEventExist(vo.stockId, vo.date, vo.checkPoint)) {
			return;
		}

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockid", vo.getStockId());
			namedParameters.addValue("date", vo.getDate());
			namedParameters.addValue("checkpoint", vo.getCheckPoint());

			namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insert(List<CheckPointDailySelectionVO> list) throws Exception {
		for (CheckPointDailySelectionVO vo : list) {
			this.insert(vo);
		}
	}

	public CheckPointDailySelectionVO getCheckPointSelection(String stockId, String date, String checkpoint) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockid", stockId);
			namedParameters.addValue("date", date);
			namedParameters.addValue("checkpoint", checkpoint);

			CheckPointDailySelectionVO vo = this.namedParameterJdbcTemplate.queryForObject(
					QUERY_BY_STOCKID_AND_DATE_AND_CHECKPOINT_SQL, namedParameters, new IndEventVOMapper());

			return vo;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public CheckPointDailySelectionVO getDifferentLatestCheckPointSelection(String stockId, String checkPoint) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockid", stockId);
			namedParameters.addValue("checkpoint", checkPoint);

			CheckPointDailySelectionVO vo = this.namedParameterJdbcTemplate.queryForObject(
					QUERY_LATEST_BY_STOCKID_AND_NOT_CHECKPOINT_SQL, namedParameters, new IndEventVOMapper());

			return vo;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void delete(String stockId, String date, String checkpoint) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockid", stockId);
			namedParameters.addValue("date", date);
			namedParameters.addValue("checkpoint", checkpoint);

			namedParameterJdbcTemplate.execute(DELETE_SQL, namedParameters, new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isEventExist(String stockId, String date, String checkpoint) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockid", stockId);
			namedParameters.addValue("date", date);
			namedParameters.addValue("checkpoint", checkpoint);

			CheckPointDailySelectionVO vo = this.namedParameterJdbcTemplate.queryForObject(
					QUERY_BY_STOCKID_AND_DATE_AND_CHECKPOINT_SQL, namedParameters, new IndEventVOMapper());

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
			CheckPointDailySelectionVO vo = new CheckPointDailySelectionVO();
			vo.stockId = "601311";
			vo.date = "2015-05-18";
			vo.checkPoint = "HengPan_3_Weeks_MA5_MA10_MA20_MA30_RongHe_Break_Platform";
			ins.insertIfNotExist(vo);
			boolean exist = ins.isEventExist(vo.stockId, vo.date, vo.checkPoint);
			System.out.println(exist);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
