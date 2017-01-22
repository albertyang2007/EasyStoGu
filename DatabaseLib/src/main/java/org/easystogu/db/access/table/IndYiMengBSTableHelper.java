package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.YiMengBSVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class IndYiMengBSTableHelper {
	private static Logger logger = LogHelper.getLogger(IndYiMengBSTableHelper.class);
	private static IndYiMengBSTableHelper instance = null;
	private static IndYiMengBSTableHelper configInstance = null;
	protected String tableName = "IND_YIMENGBS";
	// please modify this SQL in all subClass
	protected String INSERT_SQL = "INSERT INTO " + tableName
			+ " (stockId, date, x2, x3) VALUES (:stockId, :date, :x2, :x3)";
	protected String QUERY_BY_ID_AND_DATE_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockId = :stockId AND date = :date";
	protected String QUERY_ALL_BY_ID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId ORDER BY date";
	protected String QUERY_LATEST_N_BY_ID_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit";
	protected String DELETE_BY_STOCKID_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId";
	protected String DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName
			+ " WHERE stockId = :stockId AND date = :date";
	protected String DELETE_BY_DATE_SQL = "DELETE FROM " + tableName + " WHERE date = :date";

	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public static IndYiMengBSTableHelper getInstance() {
		if (instance == null) {
			instance = new IndYiMengBSTableHelper();
		}
		return instance;
	}

	protected IndYiMengBSTableHelper() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				PostgreSqlDataSourceFactory.createDataSource());
	}
	
	public static IndYiMengBSTableHelper getConfigInstance(javax.sql.DataSource datasource) {
		if (configInstance == null) {
			configInstance = new IndYiMengBSTableHelper(datasource);
		}
		return configInstance;
	}

	protected IndYiMengBSTableHelper(javax.sql.DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(datasource);
	}

	private static final class YiMengBSVOMapper implements RowMapper<YiMengBSVO> {
		public YiMengBSVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			YiMengBSVO vo = new YiMengBSVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setDate(rs.getString("date"));
			vo.setX2(rs.getDouble("x2"));
			vo.setX3(rs.getDouble("x3"));
			return vo;
		}
	}

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public void insert(YiMengBSVO vo) {
		logger.debug("insert for {}", vo);

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.getStockId());
			namedParameters.addValue("date", vo.getDate());
			namedParameters.addValue("x2", vo.getX2());
			namedParameters.addValue("x3", vo.getX3());

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

	public void insert(List<YiMengBSVO> list) throws Exception {
		for (YiMengBSVO vo : list) {
			this.insert(vo);
		}
	}

	public YiMengBSVO getYiMengBS(String stockId, String date) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date", date);

			YiMengBSVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_ID_AND_DATE_SQL, namedParameters,
					new YiMengBSVOMapper());

			return vo;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<YiMengBSVO> getAllYiMengBS(String stockId) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<YiMengBSVO> list = this.namedParameterJdbcTemplate.query(QUERY_ALL_BY_ID_SQL, namedParameters,
					new YiMengBSVOMapper());

			return list;
		} catch (Exception e) {
			logger.error("exception meets for getAllKDJ stockId=" + stockId, e);
			e.printStackTrace();
			return new ArrayList<YiMengBSVO>();
		}
	}

	// 最近几天的，必须使用时间倒序的SQL
	public List<YiMengBSVO> getNDateYiMengBS(String stockId, int day) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", day);

			List<YiMengBSVO> list = this.namedParameterJdbcTemplate.query(QUERY_LATEST_N_BY_ID_SQL, namedParameters,
					new YiMengBSVOMapper());

			return list;
		} catch (Exception e) {
			logger.error("exception meets for getAvgClosePrice stockId=" + stockId, e);
			e.printStackTrace();
			return new ArrayList<YiMengBSVO>();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IndYiMengBSTableHelper ins = new IndYiMengBSTableHelper();
		try {
			System.out.println(ins.getAllYiMengBS("002194").size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
