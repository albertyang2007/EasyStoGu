package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.WRVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class IndWRTableHelper {

	private static Logger logger = LogHelper.getLogger(IndQSDDTableHelper.class);
	private static IndWRTableHelper instance = null;
	protected String tableName = "IND_WR";
	// please modify this SQL in all subClass
	protected String INSERT_SQL = "INSERT INTO " + tableName
			+ " (stockId, date, lonterm, midterm, shoterm) VALUES (:stockId, :date, :lonterm, :midterm, :shoterm)";
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

	public static IndWRTableHelper getInstance() {
		if (instance == null) {
			instance = new IndWRTableHelper();
		}
		return instance;
	}

	protected IndWRTableHelper() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				PostgreSqlDataSourceFactory.createDataSource());
	}

	private static final class WRVOMapper implements RowMapper<WRVO> {
		public WRVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			WRVO vo = new WRVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setDate(rs.getString("date"));
			vo.setLonTerm(rs.getDouble("lonterm"));
			vo.setMidTerm(rs.getDouble("midterm"));
			vo.setShoTerm(rs.getDouble("shoterm"));
			return vo;
		}
	}

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public void insert(WRVO vo) {
		logger.debug("insert for {}", vo);

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.getStockId());
			namedParameters.addValue("date", vo.getDate());
			namedParameters.addValue("lonterm", vo.getLonTerm());
			namedParameters.addValue("midterm", vo.getMidTerm());
			namedParameters.addValue("shoterm", vo.getShoTerm());

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

	public void insert(List<WRVO> list) throws Exception {
		for (WRVO vo : list) {
			this.insert(vo);
		}
	}

	public WRVO getWR(String stockId, String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date", date);

			WRVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_ID_AND_DATE_SQL, namedParameters,
					new WRVOMapper());

			return vo;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<WRVO> getAllWR(String stockId) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<WRVO> list = this.namedParameterJdbcTemplate.query(QUERY_ALL_BY_ID_SQL, namedParameters,
					new WRVOMapper());

			return list;
		} catch (Exception e) {
			logger.error("exception meets for getAllQSDD stockId=" + stockId, e);
			e.printStackTrace();
			return new ArrayList<WRVO>();
		}
	}

	// 最近几天的，必须使用时间倒序的SQL
	public List<WRVO> getNDateWR(String stockId, int day) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", day);

			List<WRVO> list = this.namedParameterJdbcTemplate.query(QUERY_LATEST_N_BY_ID_SQL, namedParameters,
					new WRVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<WRVO>();
		}
	}

	public List<WRVO> getByIdAndBetweenDate(String stockId, String StartDate, String endDate) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date1", StartDate);
			namedParameters.addValue("date2", endDate);

			List<WRVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_STOCKID_AND_BETWEEN_DATE, namedParameters,
					new WRVOMapper());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<WRVO>();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IndWRTableHelper ins = new IndWRTableHelper();
		try {
			System.out.println(ins.getNDateWR("000673", 40).size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
