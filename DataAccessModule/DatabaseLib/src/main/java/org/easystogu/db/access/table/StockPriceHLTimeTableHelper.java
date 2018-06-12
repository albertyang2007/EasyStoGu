package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.StockPriceHLTimeVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class StockPriceHLTimeTableHelper {
	private static Logger logger = LogHelper.getLogger(StockPriceHLTimeTableHelper.class);
	private static StockPriceHLTimeTableHelper instance = null;
	protected String tableName = "STOCKPRICE_HL_TIME";
	// please modify this SQL in all subClass
	protected String INSERT_SQL = "INSERT INTO " + tableName
			+ " (stockId, date, hight_time, low_time) VALUES (:stockId, :date, :hight_time, :low_time)";
	protected String SELECT_BY_ID = "SELECT * FROM " + tableName + " WHERE stockId = :stockId";
	protected String SELECT_BY_ID_AND_DATE_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockId = :stockId AND date = :date";
	protected String SELECT_BY_DATE = "SELECT * FROM " + tableName + " WHERE date = :date";
	protected String UPDATE_HIGH_PRICE_TIME = "UPDATE " + tableName
			+ " SET hight_time = :hight_time WHERE stockId = :stockId AND DATE = :date";
	protected String UPDATE_LOW_PRICE_TIME = "UPDATE " + tableName
			+ " SET low_time = :low_time WHERE stockId = :stockId AND DATE = :date";

	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	protected StockPriceHLTimeTableHelper(javax.sql.DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(datasource);
	}

	public static StockPriceHLTimeTableHelper getInstance() {
		if (instance == null) {
			instance = new StockPriceHLTimeTableHelper(PostgreSqlDataSourceFactory.createDataSource());
		}
		return instance;
	}

	private static final class StockPriceHLTimeVOMapper implements RowMapper<StockPriceHLTimeVO> {
		public StockPriceHLTimeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			StockPriceHLTimeVO vo = new StockPriceHLTimeVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setDate(rs.getString("date"));
			vo.setHightTime(rs.getString("hight_time"));
			vo.setLowTime(rs.getString("low_time"));
			return vo;
		}
	}

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public void insert(StockPriceHLTimeVO vo) {
		logger.debug("insert for {}", vo);

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.getStockId());
			namedParameters.addValue("date", vo.getDate());
			namedParameters.addValue("hight_time", vo.getHightTime());
			namedParameters.addValue("low_time", vo.getLowTime());

			namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public StockPriceHLTimeVO getByStockIdAndDate(String stockId, String date) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date", date);

			StockPriceHLTimeVO vo = this.namedParameterJdbcTemplate.queryForObject(SELECT_BY_ID_AND_DATE_SQL,
					namedParameters, new StockPriceHLTimeVOMapper());

			return vo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<StockPriceHLTimeVO> getByStockId(String stockId) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<StockPriceHLTimeVO> vos = this.namedParameterJdbcTemplate.query(SELECT_BY_ID, namedParameters,
					new StockPriceHLTimeVOMapper());

			return vos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<StockPriceHLTimeVO>();
	}

	public List<StockPriceHLTimeVO> getByDate(String date) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);

			List<StockPriceHLTimeVO> vos = this.namedParameterJdbcTemplate.query(SELECT_BY_DATE, namedParameters,
					new StockPriceHLTimeVOMapper());

			return vos;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<StockPriceHLTimeVO>();
	}

	public void updateHighPriceTime(StockPriceHLTimeVO vo) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.stockId);
			namedParameters.addValue("date", vo.date);
			namedParameters.addValue("hight_time", vo.getHightTime());

			namedParameterJdbcTemplate.execute(UPDATE_HIGH_PRICE_TIME, namedParameters,
					new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateLowPriceTime(StockPriceHLTimeVO vo) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.stockId);
			namedParameters.addValue("date", vo.date);
			namedParameters.addValue("low_time", vo.getLowTime());

			namedParameterJdbcTemplate.execute(UPDATE_LOW_PRICE_TIME, namedParameters,
					new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
