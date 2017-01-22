package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.MAVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class IndMATableHelper {

	private static Logger logger = LogHelper.getLogger(IndMATableHelper.class);
	private static IndMATableHelper instance = null;
	private static IndMATableHelper configInstance = null;
	protected String tableName = "IND_MA";
	// please modify this SQL in all subClass
	protected String INSERT_SQL = "INSERT INTO " + tableName
			+ " (stockId, date, ma5, ma10, ma19, ma20, ma30, ma43, ma60, ma86, ma120, ma250, close) VALUES (:stockId, :date, :ma5, :ma10, :ma19, :ma20, :ma30, :ma43, :ma60, :ma86, :ma120, :ma250, :close)";
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

	public static IndMATableHelper getInstance() {
		if (instance == null) {
			instance = new IndMATableHelper();
		}
		return instance;
	}

	protected IndMATableHelper() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				PostgreSqlDataSourceFactory.createDataSource());
	}
	
	public static IndMATableHelper getConfigInstance(javax.sql.DataSource datasource) {
		if (configInstance == null) {
			configInstance = new IndMATableHelper(datasource);
		}
		return configInstance;
	}

	protected IndMATableHelper(javax.sql.DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(datasource);
	}

	private static final class MAVOMapper implements RowMapper<MAVO> {
		public MAVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			MAVO vo = new MAVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setDate(rs.getString("date"));
			vo.setMa5(rs.getDouble("ma5"));
			vo.setMa10(rs.getDouble("ma10"));
			vo.setMa19(rs.getDouble("ma19"));
			vo.setMa20(rs.getDouble("ma20"));
			vo.setMa30(rs.getDouble("ma30"));
			vo.setMa43(rs.getDouble("ma43"));
			vo.setMa60(rs.getDouble("ma60"));
			vo.setMa86(rs.getDouble("ma86"));
			vo.setMa120(rs.getDouble("ma120"));
			vo.setMa250(rs.getDouble("ma250"));
			vo.setClose(rs.getDouble("close"));
			return vo;
		}
	}

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public void insert(MAVO vo) {
		logger.debug("insert for {}", vo);

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.getStockId());
			namedParameters.addValue("date", vo.getDate());
			namedParameters.addValue("ma5", vo.getMa5());
			namedParameters.addValue("ma10", vo.getMa10());
			namedParameters.addValue("ma19", vo.getMa19());
			namedParameters.addValue("ma20", vo.getMa20());
			namedParameters.addValue("ma30", vo.getMa30());
			namedParameters.addValue("ma43", vo.getMa43());
			namedParameters.addValue("ma60", vo.getMa60());
			namedParameters.addValue("ma86", vo.getMa86());
			namedParameters.addValue("ma120", vo.getMa120());
			namedParameters.addValue("ma250", vo.getMa250());
			namedParameters.addValue("close", vo.getClose());

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

	public void insert(List<MAVO> list) throws Exception {
		for (MAVO vo : list) {
			this.insert(vo);
		}
	}

	public MAVO getMA(String stockId, String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date", date);

			MAVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_ID_AND_DATE_SQL, namedParameters,
					new MAVOMapper());

			return vo;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<MAVO> getAllMA(String stockId) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<MAVO> list = this.namedParameterJdbcTemplate.query(QUERY_ALL_BY_ID_SQL, namedParameters,
					new MAVOMapper());

			return list;
		} catch (Exception e) {
			logger.error("exception meets for getAllQSDD stockId=" + stockId, e);
			e.printStackTrace();
			return new ArrayList<MAVO>();
		}
	}

	// 最近几天的，必须使用时间倒序的SQL
	public List<MAVO> getNDateMA(String stockId, int day) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", day);

			List<MAVO> list = this.namedParameterJdbcTemplate.query(QUERY_LATEST_N_BY_ID_SQL, namedParameters,
					new MAVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<MAVO>();
		}
	}

	public List<MAVO> getByIdAndBetweenDate(String stockId, String StartDate, String endDate) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date1", StartDate);
			namedParameters.addValue("date2", endDate);

			List<MAVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_STOCKID_AND_BETWEEN_DATE, namedParameters,
					new MAVOMapper());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<MAVO>();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IndMATableHelper ins = new IndMATableHelper();
		try {
			System.out.println(ins.getMA("000673", "2016-07-19"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
