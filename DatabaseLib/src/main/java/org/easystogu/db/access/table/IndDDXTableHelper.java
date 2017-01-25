package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.DDXVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class IndDDXTableHelper {
	private static Logger logger = LogHelper.getLogger(IndDDXTableHelper.class);
	private static IndDDXTableHelper instance = null;
	private static IndDDXTableHelper georedInstance = null;
	protected String tableName = "IND_DDX";
	// please modify this SQL in all subClass
	protected String INSERT_SQL = "INSERT INTO " + tableName
			+ " (stockId, date, ddx, ddy, ddz) VALUES (:stockId, :date, :ddx, :ddy, :ddz)";
	protected String QUERY_BY_ID_AND_DATE_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockId = :stockId AND date = :date";
	protected String QUERY_BY_DATE_SQL = "SELECT * FROM " + tableName + " WHERE date = :date";
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

	private IndDDXTableHelper(javax.sql.DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(datasource);
	}

	public static IndDDXTableHelper getInstance() {
		if (instance == null) {
			instance = new IndDDXTableHelper(PostgreSqlDataSourceFactory.createDataSource());
		}
		return instance;
	}

	public static IndDDXTableHelper getGeoredInstance() {
		if (georedInstance == null) {
			georedInstance = new IndDDXTableHelper(PostgreSqlDataSourceFactory.createGeoredDataSource());
		}
		return georedInstance;
	}

	private static final class DDXVOMapper implements RowMapper<DDXVO> {
		public DDXVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			DDXVO vo = new DDXVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setDate(rs.getString("date"));
			vo.setDdx(rs.getDouble("ddx"));
			vo.setDdy(rs.getDouble("ddy"));
			vo.setDdz(rs.getDouble("ddz"));
			return vo;
		}
	}

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public void insert(DDXVO vo) {
		logger.debug("insert for {}", vo);

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.getStockId());
			namedParameters.addValue("date", vo.getDate());
			namedParameters.addValue("ddx", vo.getDdx());
			namedParameters.addValue("ddy", vo.getDdy());
			namedParameters.addValue("ddz", vo.getDdz());

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

	public void insert(List<DDXVO> list) throws Exception {
		for (DDXVO vo : list) {
			this.insert(vo);
		}
	}

	public DDXVO getDDX(String stockId, String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date", date);

			DDXVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_ID_AND_DATE_SQL, namedParameters,
					new DDXVOMapper());

			return vo;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<DDXVO> getAllDDX(String stockId) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<DDXVO> list = this.namedParameterJdbcTemplate.query(QUERY_ALL_BY_ID_SQL, namedParameters,
					new DDXVOMapper());

			return list;
		} catch (Exception e) {
			logger.error("exception meets for getAllDDX stockId=" + stockId, e);
			e.printStackTrace();
			return new ArrayList<DDXVO>();
		}
	}
	
	public List<DDXVO> getByDate(String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);

			List<DDXVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_DATE_SQL, namedParameters,
					new DDXVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<DDXVO>();
		}
	}

	// 最近几天的，必须使用时间倒序的SQL
	public List<DDXVO> getNDateDDX(String stockId, int day) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", day);

			List<DDXVO> list = this.namedParameterJdbcTemplate.query(QUERY_LATEST_N_BY_ID_SQL, namedParameters,
					new DDXVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<DDXVO>();
		}
	}

	public List<DDXVO> getByIdAndBetweenDate(String stockId, String StartDate, String endDate) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date1", StartDate);
			namedParameters.addValue("date2", endDate);

			List<DDXVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_STOCKID_AND_BETWEEN_DATE, namedParameters,
					new DDXVOMapper());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<DDXVO>();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IndDDXTableHelper ins = IndDDXTableHelper.getInstance();
		try {

			System.out.println(ins.getNDateDDX("600589", 40).size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
