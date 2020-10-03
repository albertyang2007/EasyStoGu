package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.vo.table.ScheduleActionVO;
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
public class ScheduleActionTableHelper {
	private static Logger logger = LogHelper.getLogger(ScheduleActionTableHelper.class);
	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	protected String tableName = "SCHEDULE_ACTION";
	protected String INSERT_SQL = "INSERT INTO " + tableName
			+ " (stockId, runDate, createDate, actionDo, params) VALUES (:stockId, :runDate, :createDate, :actionDo, :params)";
	protected String QUERY_BY_ID_AND_RUNDATE_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockId = :stockId AND runDate = :runDate";
	protected String QUERY_ALL_BY_RUNDATE_SQL = "SELECT * FROM " + tableName + " WHERE runDate = :runDate";
	protected String QUERY_ALL_SHOULD_RUNDATE_SQL = "SELECT * FROM " + tableName + " WHERE runDate <= :runDate";
	protected String QUERY_ALL_BY_ID_AND_ACTION_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockId = :stockId AND actionDo = :actionDo";
	protected String DELETE_BY_STOCKID_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId";
	protected String DELETE_BY_STOCKID_AND_RUNDATE_SQL = "DELETE FROM " + tableName
			+ " WHERE stockId = :stockId AND runDate = :runDate";
	protected String DELETE_BY_STOCKID_AND_RUNDATE_AND_ACTION_SQL = "DELETE FROM " + tableName
			+ " WHERE stockId = :stockId AND runDate = :runDate AND actionDo = :actionDo";
	protected String DELETE_BY_STOCKID_AND_ACTION_SQL = "DELETE FROM " + tableName
			+ " WHERE stockId = :stockId AND actionDo = :actionDo";

	private static final class ScheduleActionVOMapper implements RowMapper<ScheduleActionVO> {
		public ScheduleActionVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ScheduleActionVO vo = new ScheduleActionVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setRunDate(rs.getString("runDate"));
			vo.setCreateDate(rs.getString("createDate"));
			vo.setActionDo(rs.getString("actionDo"));
			vo.setParams(rs.getString("params"));
			return vo;
		}
	}

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public void insert(ScheduleActionVO vo) {
		logger.debug("insert for {}", vo);

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.getStockId());
			namedParameters.addValue("runDate", vo.getRunDate());
			namedParameters.addValue("createDate", vo.getCreateDate());
			namedParameters.addValue("actionDo", vo.getActionDo());
			namedParameters.addValue("params", vo.getParams());

			namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			logger.error("exception meets for insert vo: " + vo, e);
			e.printStackTrace();
		}
	}

	public void deleteIfExistAndThenInsert(ScheduleActionVO vo) {
		this.delete(vo.stockId);
		this.insert(vo);
	}

	public void insert(List<ScheduleActionVO> list) throws Exception {
		for (ScheduleActionVO vo : list) {
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

	public void delete(String stockId, String runDate, String actionDo) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("runDate", runDate);
			namedParameters.addValue("actionDo", actionDo);
			namedParameterJdbcTemplate.execute(DELETE_BY_STOCKID_AND_RUNDATE_AND_ACTION_SQL, namedParameters,
					new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(String stockId, String actionDo) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("actionDo", actionDo);
			namedParameterJdbcTemplate.execute(DELETE_BY_STOCKID_AND_ACTION_SQL, namedParameters,
					new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ScheduleActionVO getScheduleActionVO(String stockId, String runDate) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("runDate", runDate);

			ScheduleActionVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_ID_AND_RUNDATE_SQL,
					namedParameters, new ScheduleActionVOMapper());

			return vo;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ScheduleActionVO> getByIDAndAction(String stockId, String actionDo) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("actionDo", actionDo);

			List<ScheduleActionVO> list = this.namedParameterJdbcTemplate.query(QUERY_ALL_BY_ID_AND_ACTION_SQL,
					namedParameters, new ScheduleActionVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ScheduleActionVO>();
		}
	}

	public List<ScheduleActionVO> getAllByRunDate(String runDate) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("runDate", runDate);

			List<ScheduleActionVO> list = this.namedParameterJdbcTemplate.query(QUERY_ALL_BY_RUNDATE_SQL,
					namedParameters, new ScheduleActionVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ScheduleActionVO>();
		}
	}

	public List<ScheduleActionVO> getAllShouldRunDate(String runDate) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("runDate", runDate);

			List<ScheduleActionVO> list = this.namedParameterJdbcTemplate.query(QUERY_ALL_SHOULD_RUNDATE_SQL,
					namedParameters, new ScheduleActionVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<ScheduleActionVO>();
		}
	}
}
