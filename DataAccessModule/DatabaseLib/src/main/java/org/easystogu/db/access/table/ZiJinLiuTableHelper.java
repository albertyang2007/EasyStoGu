package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.ZiJinLiuVO;
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
public class ZiJinLiuTableHelper {
	private static Logger logger = LogHelper.getLogger(ZiJinLiuTableHelper.class);
	@Autowired
	protected PostgreSqlDataSourceFactory postgreSqlDataSourceFactory;
	// please modify this SQL in all subClass
	protected String tableName = "ZIJINLIU";
	protected String INSERT_SQL = "INSERT INTO " + tableName
			+ " (stockId, date, rate, incPer, majorNetIn, majorNetPer, biggestNetIn, biggestNetPer, bigNetIn, bigNetPer, midNetIn, midNetPer, smallNetIn, smallNetPer) VALUES (:stockId, :date, :rate, :incPer, :majorNetIn, :majorNetPer, :biggestNetIn, :biggestNetPer, :bigNetIn, :bigNetPer, :midNetIn, :midNetPer, :smallNetIn, :smallNetPer)";
	protected String QUERY_BY_ID_AND_DATE_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockId = :stockId AND date = :date";
	protected String QUERY_ALL_BY_ID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId ORDER BY date";
	protected String QUERY_LATEST_N_BY_ID_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit";
	protected String DELETE_BY_STOCKID_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId";
	protected String DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName
			+ " WHERE stockId = :stockId AND date = :date";
	protected String DELETE_BY_DATE_SQL = "DELETE FROM " + tableName + " WHERE date = :date";
	protected String QUERY_BY_DATE_SQL = "SELECT * FROM " + tableName + " WHERE date = :date";

	
	private NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(postgreSqlDataSourceFactory.createDataSource());
	}
	
	private static final class ZiJinLiuVOMapper implements RowMapper<ZiJinLiuVO> {
		public ZiJinLiuVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ZiJinLiuVO vo = new ZiJinLiuVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setDate(rs.getString("date"));
			vo.setRate(rs.getInt("rate"));
			vo.setIncPer(rs.getString("incPer"));
			vo.setMajorNetIn(rs.getDouble("majornetin"));
			vo.setMajorNetPer(rs.getDouble("majornetper"));
			vo.setBiggestNetIn(rs.getDouble("biggestnetin"));
			vo.setBiggestNetPer(rs.getDouble("biggestnetper"));
			vo.setBigNetIn(rs.getDouble("bignetin"));
			vo.setBigNetPer(rs.getDouble("bignetper"));
			vo.setMidNetIn(rs.getDouble("midnetin"));
			vo.setMidNetPer(rs.getDouble("midnetper"));
			vo.setSmallNetIn(rs.getDouble("smallnetin"));
			vo.setSmallNetPer(rs.getDouble("smallnetper"));
			return vo;
		}
	}

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public void insert(ZiJinLiuVO vo) {
		logger.debug("insert for {}", vo);

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.getStockId());
			namedParameters.addValue("date", vo.getDate());
			namedParameters.addValue("rate", vo.getRate());
			namedParameters.addValue("incPer", vo.getIncPer());
			namedParameters.addValue("majorNetIn", vo.getMajorNetIn());
			namedParameters.addValue("majorNetPer", vo.getMajorNetPer());
			namedParameters.addValue("biggestNetIn", vo.getBiggestNetIn());
			namedParameters.addValue("biggestNetPer", vo.getBiggestNetPer());
			namedParameters.addValue("bigNetIn", vo.getBigNetIn());
			namedParameters.addValue("bigNetPer", vo.getBigNetPer());
			namedParameters.addValue("midNetIn", vo.getMidNetIn());
			namedParameters.addValue("midNetPer", vo.getMidNetPer());
			namedParameters.addValue("smallNetIn", vo.getSmallNetIn());
			namedParameters.addValue("smallNetPer", vo.getSmallNetPer());

			getNamedParameterJdbcTemplate().execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			logger.error("exception meets for insert vo: " + vo, e);
			e.printStackTrace();
		}
	}

	public void insert(List<ZiJinLiuVO> list) throws Exception {
		for (ZiJinLiuVO vo : list) {
			this.insert(vo);
		}
	}

	public void insertIfNotExist(ZiJinLiuVO vo) {
		if (getZiJinLiu(vo.stockId, vo.date) == null) {
			insert(vo);
		}

	}

	public void delete(String stockId) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			getNamedParameterJdbcTemplate().execute(DELETE_BY_STOCKID_SQL, namedParameters,
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
			getNamedParameterJdbcTemplate().execute(DELETE_BY_STOCKID_AND_DATE_SQL, namedParameters,
					new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteByDate(String date) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);
			getNamedParameterJdbcTemplate().execute(DELETE_BY_DATE_SQL, namedParameters,
					new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ZiJinLiuVO getZiJinLiu(String stockId, String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date", date);

			ZiJinLiuVO vo = this.getNamedParameterJdbcTemplate().queryForObject(QUERY_BY_ID_AND_DATE_SQL, namedParameters,
					new ZiJinLiuVOMapper());

			return vo;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ZiJinLiuVO> getZiJinLiu(String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);

			List<ZiJinLiuVO> vo = this.getNamedParameterJdbcTemplate().query(QUERY_BY_DATE_SQL, namedParameters,
					new ZiJinLiuVOMapper());

			return vo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ZiJinLiuVO>();
	}

	public List<ZiJinLiuVO> getAllZiJinLiu(String stockId) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<ZiJinLiuVO> list = this.getNamedParameterJdbcTemplate().query(QUERY_ALL_BY_ID_SQL, namedParameters,
					new ZiJinLiuVOMapper());

			return list;
		} catch (Exception e) {
			logger.error("exception meets for getAllKDJ stockId=" + stockId, e);
			e.printStackTrace();
			return new ArrayList<ZiJinLiuVO>();
		}
	}

	// 最近几天的，必须使用时间倒序的SQL
	public List<ZiJinLiuVO> getNDateZiJinLiu(String stockId, int day) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", day);

			List<ZiJinLiuVO> list = this.getNamedParameterJdbcTemplate().query(QUERY_LATEST_N_BY_ID_SQL, namedParameters,
					new ZiJinLiuVOMapper());

			return list;
		} catch (Exception e) {
			logger.error("exception meets for getAvgClosePrice stockId=" + stockId, e);
			e.printStackTrace();
			return new ArrayList<ZiJinLiuVO>();
		}
	}
}
