package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
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
	private static CheckPointDailySelectionTableHelper instance = null;
	private static CheckPointDailySelectionTableHelper georedInstance = null;
	private String tableName = "CHECKPOINT_DAILY_SELECTION";
	protected String INSERT_SQL = "INSERT INTO " + tableName
			+ " (stockid, date, checkpoint) VALUES (:stockid, :date, :checkpoint)";
	protected String DELETE_SQL = "DELETE FROM " + tableName
			+ " WHERE stockid = :stockid AND date = :date AND checkpoint = :checkpoint";
	protected String QUERY_BY_STOCKID_AND_DATE_AND_CHECKPOINT_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockid = :stockid AND date = :date AND checkpoint = :checkpoint";
	protected String QUERY_BY_DATE_AND_CHECKPOINT_SQL = "SELECT * FROM " + tableName
			+ " WHERE date = :date AND checkpoint = :checkpoint";
	protected String COUNT_BY_DATE_AND_CHECKPOINT_SQL = "SELECT count(*) AS rtn FROM " + tableName
			+ " WHERE date = :date AND checkpoint = :checkpoint";
	protected String QUERY_BY_STOCKID_AND_DATE_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockid = :stockid AND date = :date";
	protected String QUERY_LATEST_BY_STOCKID_AND_NOT_CHECKPOINT_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockid = :stockid AND checkpoint != :checkpoint ORDER BY DATE DESC LIMIT 1";
	protected String DELETE_BY_DATE_SQL = "DELETE FROM " + tableName + " WHERE date = :date";
	protected String QUERY_BY_DATE_SQL = "SELECT * FROM " + tableName + " WHERE date = :date";
	protected String QUERY_BY_RECENT_DAYS_SQL = "SELECT * FROM " + tableName
			+ " WHERE date >= :date ORDER BY DATE DESC";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public static CheckPointDailySelectionTableHelper getInstance() {
		if (instance == null) {
			instance = new CheckPointDailySelectionTableHelper(PostgreSqlDataSourceFactory.createDataSource());
		}
		return instance;
	}

	private CheckPointDailySelectionTableHelper(javax.sql.DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(datasource);
	}

	public static CheckPointDailySelectionTableHelper getGeoredInstance() {
		if (georedInstance == null) {
			georedInstance = new CheckPointDailySelectionTableHelper(
					PostgreSqlDataSourceFactory.createGeoredDataSource());
		}
		return georedInstance;
	}

	private static final class IntVOMapper implements RowMapper<Integer> {
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getInt("rtn");
		}
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

	public List<CheckPointDailySelectionVO> getCheckPointSelection(String stockId, String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockid", stockId);
			namedParameters.addValue("date", date);

			List<CheckPointDailySelectionVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_STOCKID_AND_DATE_SQL,
					namedParameters, new IndEventVOMapper());

			return list;
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

	public List<CheckPointDailySelectionVO> getCheckPointByDate(String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);

			List<CheckPointDailySelectionVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_DATE_SQL,
					namedParameters, new IndEventVOMapper());

			return list;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<CheckPointDailySelectionVO> getRecentDaysCheckPoint(String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);

			List<CheckPointDailySelectionVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_RECENT_DAYS_SQL,
					namedParameters, new IndEventVOMapper());

			return list;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<CheckPointDailySelectionVO> queryByDateAndCheckPoint(String date, String checkPoint) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);
			namedParameters.addValue("checkpoint", checkPoint);

			List<CheckPointDailySelectionVO> list = this.namedParameterJdbcTemplate
					.query(QUERY_BY_DATE_AND_CHECKPOINT_SQL, namedParameters, new IndEventVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CheckPointDailySelectionVO>();
	}

	public int countByDateAndCheckPoint(String date, String checkPoint) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);
			namedParameters.addValue("checkpoint", checkPoint);

			int rtn = this.namedParameterJdbcTemplate.queryForObject(COUNT_BY_DATE_AND_CHECKPOINT_SQL, namedParameters,
					new IntVOMapper());

			return rtn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CheckPointDailySelectionTableHelper ins = CheckPointDailySelectionTableHelper.getInstance();
		try {
			List<CheckPointDailySelectionVO> list = ins.queryByDateAndCheckPoint("2016-09-23",
					"luzao_phaseII_zijinliu_top300");
			System.out.println(list.size());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
