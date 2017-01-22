package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.ZhuliJinChuVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class IndZhuliJinChuTableHelper {
	private static Logger logger = LogHelper.getLogger(IndZhuliJinChuTableHelper.class);
	private static IndZhuliJinChuTableHelper instance = null;
	protected String tableName = "IND_ZHULIJINCHU";
	// please modify this SQL in all subClass
	protected String INSERT_SQL = "INSERT INTO " + tableName
			+ " (stockId, date, duofang, kongfang) VALUES (:stockId, :date, :duofang, :kongfang)";
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

	public static IndZhuliJinChuTableHelper getInstance() {
		if (instance == null) {
			instance = new IndZhuliJinChuTableHelper();
		}
		return instance;
	}

	protected IndZhuliJinChuTableHelper() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				PostgreSqlDataSourceFactory.createDataSource());
	}

	private static final class ZhuliJinChuVOMapper implements RowMapper<ZhuliJinChuVO> {
		public ZhuliJinChuVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ZhuliJinChuVO vo = new ZhuliJinChuVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setDate(rs.getString("date"));
			vo.setDuofang(rs.getDouble("duofang"));
			vo.setKongfang(rs.getDouble("kongfang"));
			return vo;
		}
	}

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public void insert(ZhuliJinChuVO vo) {
		logger.debug("insert for {}", vo);

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.getStockId());
			namedParameters.addValue("date", vo.getDate());
			namedParameters.addValue("duofang", vo.getDuofang());
			namedParameters.addValue("kongfang", vo.getKongfang());

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

	public void insert(List<ZhuliJinChuVO> list) throws Exception {
		for (ZhuliJinChuVO vo : list) {
			this.insert(vo);
		}
	}

	public ZhuliJinChuVO getZhuliJinChu(String stockId, String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date", date);

			ZhuliJinChuVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_ID_AND_DATE_SQL, namedParameters,
					new ZhuliJinChuVOMapper());

			return vo;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ZhuliJinChuVO> getAllZhuliJinChu(String stockId) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<ZhuliJinChuVO> list = this.namedParameterJdbcTemplate.query(QUERY_ALL_BY_ID_SQL, namedParameters,
					new ZhuliJinChuVOMapper());

			return list;
		} catch (Exception e) {
			logger.error("exception meets for getAllKDJ stockId=" + stockId, e);
			e.printStackTrace();
			return new ArrayList<ZhuliJinChuVO>();
		}
	}

	// 最近几天的，必须使用时间倒序的SQL
	public List<ZhuliJinChuVO> getNDateZhuliJinChu(String stockId, int day) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", day);

			List<ZhuliJinChuVO> list = this.namedParameterJdbcTemplate.query(QUERY_LATEST_N_BY_ID_SQL, namedParameters,
					new ZhuliJinChuVOMapper());

			return list;
		} catch (Exception e) {
			logger.error("exception meets for getAvgClosePrice stockId=" + stockId, e);
			e.printStackTrace();
			return new ArrayList<ZhuliJinChuVO>();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IndMai1Mai2TableHelper ins = new IndMai1Mai2TableHelper();
		try {
			System.out.println(ins.getAllMai1Mai2("600000").size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
