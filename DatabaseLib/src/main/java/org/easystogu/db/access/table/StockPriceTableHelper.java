package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.access.table.cache.CacheAbleStock;
import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.ChuQuanChuXiVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class StockPriceTableHelper implements CacheAbleStock {
	private static Logger logger = LogHelper.getLogger(StockPriceTableHelper.class);
	private static StockPriceTableHelper instance = null;
	private static StockPriceTableHelper georedInstance = null;
	protected String tableName = "STOCKPRICE";
	// please modify this SQL in all subClass
	protected String INSERT_SQL = "INSERT INTO " + tableName
			+ " (stockId, date, open, high, low, close, volume, lastclose) VALUES (:stockId, :date, :open, :high, :low, :close, :volume, :lastclose)";
	protected String SELECT_CLOSE_PRICE_SQL = "SELECT close AS rtn FROM " + tableName
			+ " WHERE stockId = :stockId ORDER BY DATE";
	protected String SELECT_LOW_PRICE_SQL = "SELECT low AS rtn FROM " + tableName
			+ " WHERE stockId = :stockId ORDER BY DATE";
	protected String SELECT_HIGH_PRICE_SQL = "SELECT high AS rtn FROM " + tableName
			+ " WHERE stockId = :stockId ORDER BY DATE";
	protected String QUERY_BY_STOCKID_AND_BETWEEN_DATE = "SELECT * FROM " + tableName
			+ " WHERE stockId = :stockId AND date >= :date1 AND date <= :date2 ORDER BY DATE";
	// macd used this sql
	protected String QUERY_BY_STOCKID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId ORDER BY DATE";
	// avg price, for example MA5, MA10, MA20, MA30
	protected String AVG_CLOSE_PRICE_SQL = "SELECT avg(close) AS rtn from (SELECT close FROM " + tableName
			+ " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit) AS myma";
	// avg volume, for example MAVOL5, MAVOL10
	protected String AVG_VOLUME_SQL = "SELECT avg(volume) AS rtn from (SELECT volume FROM " + tableName
			+ " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit) AS mymavol";
	// kdj used this: Low(n)
	protected String SELECT_LOW_N_PRICE_SQL = "SELECT min(low) AS rtn from (SELECT low FROM " + tableName
			+ " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit) AS mylown";
	// kdj used this: High(n)
	protected String SELECT_HIGH_N_PRICE_SQL = "SELECT max(high) AS rtn from (SELECT high FROM " + tableName
			+ " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit) AS myhighn";
	// query price by Id and date
	protected String QUERY_BY_STOCKID_DATE_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockId = :stockId AND date = :date";
	// query the last date
	protected String GET_LATEST_STOCK_DATE = "SELECT date as rtn FROM " + tableName
			+ " WHERE stockId = :stockId ORDER BY DATE DESC limit 1";
	// query the last N date
	protected String GET_LATEST_N_STOCK_DATE = "SELECT date as rtn FROM " + tableName
			+ " WHERE stockId = :stockId ORDER BY DATE DESC limit :limit";
	// query the latest N date price
	protected String QUERY_LATEST_PRICE_N_DATE_STOCKID_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit";
	// query the low price between date1(not include) and date2(include)
	protected String QUERY_LOW_PRICE_BETWEEN_DATE_SQL = "SELECT min(low) AS rtn from (SELECT low from " + tableName
			+ " WHERE stockId = :stockId AND date > :startDate AND date <= :endDate) AS mylowQuery";
	// query the high price between date1(not include) and date2(include)
	protected String QUERY_HIGH_PRICE_BETWEEN_DATE_SQL = "SELECT max(high) AS rtn from (SELECT high from " + tableName
			+ " WHERE stockId = :stockId AND date > :startDate AND date <= :endDate) AS myHighQuery";
	// query the high price date between date1(not include) and date2(include)
	protected String QUERY_HIGH_PRICE_DATE_BETWEEN_DATE_SQL = "SELECT date AS rtn from " + tableName
			+ " WHERE stockId = :stockId AND high = :high AND date > :startDate AND date <= :endDate";
	protected String DELETE_BY_STOCKID_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId";
	protected String DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName
			+ " WHERE stockId = :stockId AND date = :date";
	protected String DELETE_BY_STOCKID_AND_AFTER_DATE_SQL = "DELETE FROM " + tableName
			+ " WHERE stockId = :stockId AND date >= :date";
	protected String DELETE_BY_STOCKID_AND_BETWEEN_DATE_SQL = "DELETE FROM " + tableName
			+ " WHERE stockId = :stockId AND date >= :date1 AND date <= :date2";
	protected String DELETE_BY_DATE_SQL = "DELETE FROM " + tableName + " WHERE date = :date";
	protected String COUNT_DAYS_BETWEEN_DATE1_DATE2 = "SELECT COUNT(*) AS rtn FROM " + tableName
			+ " WHERE stockId = :stockId AND DATE >= :date1 AND DATE <= :date2";
	protected String QUERY_DAYS_BETWEEN_DATE1_DATE2 = "SELECT date AS rtn FROM " + tableName
			+ " WHERE stockId = :stockId AND DATE >= :date1 AND DATE <= :date2";
	protected String QUERY_PREVIOUS_DATE_SQL = "SELECT date as rtn FROM " + tableName
			+ " WHERE stockId = :stockId  AND date < :date ORDER BY DATE DESC limit 1";
	// only use for weekPrice, query the weekPrice based on date
	// protected String QUERY_BY_STOCKID_AND_BETWEEN_DATE = "SELECT * FROM " +
	// tableName
	// +
	// " WHERE stockId = :stockId AND DATE >= :date1 AND DATE <= :date2 ORDER BY
	// DATE";
	protected String QUERY_BY_STOCKID_AND_BEFORE_DATE = "SELECT * FROM " + tableName
			+ " WHERE stockId = :stockId AND DATE <= :date ORDER BY DATE";
	// update batch price based on gaoSongZhuan and date
	protected String UPDATE_BATCH_PRICE_BASED_ON_CHUQUAN_AND_DATE = "UPDATE " + tableName
			+ " SET open = open*:rate, high = high*:rate, low = low*:rate, close = close*:rate "
			+ " WHERE stockId = :stockId AND DATE < :date";
	// update price based on gaoSongZhuan and date
	protected String UPDATE_PRICE_BASED_ON_CHUQUAN_AND_DATE = "UPDATE " + tableName
			+ " SET open = :open, high = :high, low = :low, close = :close "
			+ " WHERE stockId = :stockId AND DATE = :date";
	// query the latest N date
	protected String QUERY_LATEST_N_DATE_STOCKID_SQL = "SELECT date AS rtn FROM " + tableName
			+ " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit";
	protected String QUERY_ALL_BY_DATE = "SELECT * FROM " + tableName + " WHERE DATE = :date ORDER BY stockId";

	// to check if a date is in deal time, to exclude the holiday
	protected String IS_DATE_IN_DEAL = "SELECT count(*) AS rtn FROM " + tableName
			+ " WHERE stockid='999999' AND DATE = :date";
	protected String COUNT_BY_ID_SQL = "SELECT COUNT(*) AS rtn FROM " + tableName + " WHERE stockId = :stockId";
	protected String COUNT_ALL_SQL = "SELECT count(*) AS rtn from " + tableName;
	// select all distinct stockIDs
	protected String QUERY_DISTINCT_ID = "SELECT distinct(stockid) AS rtn FROM " + tableName + " order by stockid";
	protected String QUERY_DEAL_DATE_BY_ID = "SELECT date AS rtn FROM " + tableName
			+ " WHERE stockId = :stockId ORDER BY date DESC";

	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	protected StockPriceTableHelper(javax.sql.DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(datasource);
	}

	public static StockPriceTableHelper getInstance() {
		if (instance == null) {
			instance = new StockPriceTableHelper(PostgreSqlDataSourceFactory.createDataSource());
		}
		return instance;
	}

	public static StockPriceTableHelper getGeoredInstance() {
		if (georedInstance == null) {
			georedInstance = new StockPriceTableHelper(PostgreSqlDataSourceFactory.createGeoredDataSource());
		}
		return georedInstance;
	}

	private static final class StockPriceVOMapper implements RowMapper<StockPriceVO> {
		public StockPriceVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			StockPriceVO vo = new StockPriceVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setDate(rs.getString("date"));
			vo.setClose(rs.getDouble("close"));
			vo.setHigh(rs.getDouble("high"));
			vo.setLow(rs.getDouble("low"));
			vo.setOpen(rs.getDouble("open"));
			vo.setVolume(rs.getLong("volume"));
			vo.setLastClose(rs.getDouble("lastclose"));
			return vo;
		}
	}

	private static final class DoubleVOMapper implements RowMapper<Double> {
		public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getDouble("rtn");
		}
	}

	private static final class StringVOMapper implements RowMapper<String> {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("rtn");
		}
	}

	private static final class IntVOMapper implements RowMapper<Integer> {
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getInt("rtn");
		}
	}

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public void insert(StockPriceVO vo) {
		logger.debug("insert for {}", vo);

		if (!vo.isValidated()) {
			logger.debug(vo.getStockId() + " is not validated, skip. vo= {}", vo);
			return;
		}

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.getStockId());
			namedParameters.addValue("date", vo.getDate());
			namedParameters.addValue("open", vo.getOpen());
			namedParameters.addValue("high", vo.getHigh());
			namedParameters.addValue("low", vo.getLow());
			namedParameters.addValue("close", vo.getClose());
			namedParameters.addValue("volume", vo.getVolume());
			namedParameters.addValue("lastclose", vo.getLastClose());

			namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
		} catch (Exception e) {
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

	public void deleteAfterDate(String stockId, String date) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date", date);
			namedParameterJdbcTemplate.execute(DELETE_BY_STOCKID_AND_AFTER_DATE_SQL, namedParameters,
					new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteBetweenDate(String stockId, String startDate, String endDate) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date1", startDate);
			namedParameters.addValue("date2", endDate);
			namedParameterJdbcTemplate.execute(DELETE_BY_STOCKID_AND_BETWEEN_DATE_SQL, namedParameters,
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

	public void insert(List<StockPriceVO> list) throws Exception {
		for (StockPriceVO vo : list) {
			this.insert(vo);
		}
	}

	public Double getAvgClosePrice(String stockId, int day) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", day);

			Double avg = this.namedParameterJdbcTemplate.queryForObject(AVG_CLOSE_PRICE_SQL, namedParameters,
					new DoubleVOMapper());

			return avg;
		} catch (EmptyResultDataAccessException ee) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	public Double getLowPrice(String stockId, int day) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", day);

			Double min = this.namedParameterJdbcTemplate.queryForObject(SELECT_LOW_N_PRICE_SQL, namedParameters,
					new DoubleVOMapper());

			return min;
		} catch (EmptyResultDataAccessException ee) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	public Double getHighPrice(String stockId, int day) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", day);

			Double max = this.namedParameterJdbcTemplate.queryForObject(SELECT_HIGH_N_PRICE_SQL, namedParameters,
					new DoubleVOMapper());

			return max;
		} catch (Exception e) {
			logger.error("exception meets for getMinClosePrice stockId=" + stockId, e);
			return 0.0;
		}
	}

	public Long getAvgVolume(String stockId, int day) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", day);

			Double avg = this.namedParameterJdbcTemplate.queryForObject(AVG_VOLUME_SQL, namedParameters,
					new DoubleVOMapper());

			return avg.longValue();
		} catch (EmptyResultDataAccessException ee) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0l;
	}

	public List<Double> getAllClosePrice(String stockId) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<Double> closes = this.namedParameterJdbcTemplate.query(SELECT_CLOSE_PRICE_SQL, namedParameters,
					new DoubleVOMapper());

			return closes;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Double>();
		}
	}

	public List<Double> getAllLowPrice(String stockId) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<Double> lows = this.namedParameterJdbcTemplate.query(SELECT_LOW_PRICE_SQL, namedParameters,
					new DoubleVOMapper());

			return lows;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Double>();
		}
	}

	public List<Double> getAllHighPrice(String stockId) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<Double> highs = this.namedParameterJdbcTemplate.query(SELECT_HIGH_PRICE_SQL, namedParameters,
					new DoubleVOMapper());

			return highs;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Double>();
		}
	}

	public List<StockPriceVO> getStockPriceById(String stockId) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<StockPriceVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_STOCKID_SQL, namedParameters,
					new StockPriceVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<StockPriceVO>();
		}
	}

	public List<StockPriceVO> getNdateStockPriceById(String stockId, int day) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", day);

			List<StockPriceVO> list = this.namedParameterJdbcTemplate.query(QUERY_LATEST_PRICE_N_DATE_STOCKID_SQL,
					namedParameters, new StockPriceVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<StockPriceVO>();
	}

	public StockPriceVO getStockPriceByIdAndDate(String stockId, String date) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date", date);

			StockPriceVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_STOCKID_DATE_SQL, namedParameters,
					new StockPriceVOMapper());
			return vo;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<StockPriceVO> getStockPriceByIdAndBetweenDate(String stockId, String StartDate, String endDate) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date1", StartDate);
			namedParameters.addValue("date2", endDate);

			List<StockPriceVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_STOCKID_AND_BETWEEN_DATE,
					namedParameters, new StockPriceVOMapper());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<StockPriceVO>();
	}

	// include the endDay
	public List<StockPriceVO> getStockPriceByIdBeforeDate(String stockId, String endDay) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date", endDay);

			List<StockPriceVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_STOCKID_AND_BEFORE_DATE,
					namedParameters, new StockPriceVOMapper());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<StockPriceVO>();
	}

	public int getNumberDayByIdAndBetweenDates(String stockId, String date1, String date2) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date1", date1);
			namedParameters.addValue("date2", date2);

			List<String> dayList = this.namedParameterJdbcTemplate.query(QUERY_DAYS_BETWEEN_DATE1_DATE2,
					namedParameters, new StringVOMapper());
			return dayList.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<String> getDayListByIdAndBetweenDates(String stockId, String date1, String date2) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date1", date1);
			namedParameters.addValue("date2", date2);

			List<String> dayList = this.namedParameterJdbcTemplate.query(QUERY_DAYS_BETWEEN_DATE1_DATE2,
					namedParameters, new StringVOMapper());
			return dayList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public List<String> getSZZSDayListByIdAndBetweenDates(String date1, String date2) {
		return this.getDayListByIdAndBetweenDates("999999", date1, date2);
	}

	public String getLatestStockDate() {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", "999999");

			String date = this.namedParameterJdbcTemplate.queryForObject(GET_LATEST_STOCK_DATE, namedParameters,
					new StringVOMapper());

			return date;
		} catch (EmptyResultDataAccessException ee) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public List<String> getLatestNStockDate(int limit) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", "999999");
			namedParameters.addValue("limit", limit);

			List<String> date = this.namedParameterJdbcTemplate.query(GET_LATEST_N_STOCK_DATE, namedParameters,
					new StringVOMapper());

			return date;
		} catch (EmptyResultDataAccessException ee) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public String getPreviousStockDate(String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", "999999");
			namedParameters.addValue("date", date);

			String rtc = this.namedParameterJdbcTemplate.queryForObject(QUERY_PREVIOUS_DATE_SQL, namedParameters,
					new StringVOMapper());

			return rtc;
		} catch (EmptyResultDataAccessException ee) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public Double getLowPriceBetweenDate(String stockId, String startDate, String endDate) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("startDate", startDate);
			namedParameters.addValue("endDate", endDate);

			Double low = this.namedParameterJdbcTemplate.queryForObject(QUERY_LOW_PRICE_BETWEEN_DATE_SQL,
					namedParameters, new DoubleVOMapper());

			return low;
		} catch (EmptyResultDataAccessException ee) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	public Double getHighPriceBetweenDate(String stockId, String startDate, String endDate) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("startDate", startDate);
			namedParameters.addValue("endDate", endDate);

			Double high = this.namedParameterJdbcTemplate.queryForObject(QUERY_HIGH_PRICE_BETWEEN_DATE_SQL,
					namedParameters, new DoubleVOMapper());

			return high;
		} catch (EmptyResultDataAccessException ee) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	public String getHighPriceDateBetweenDate(String stockId, double high, String startDate, String endDate) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("high", high);
			namedParameters.addValue("startDate", startDate);
			namedParameters.addValue("endDate", endDate);

			List<String> dates = this.namedParameterJdbcTemplate.query(QUERY_HIGH_PRICE_DATE_BETWEEN_DATE_SQL,
					namedParameters, new StringVOMapper());

			if (dates != null && dates.size() > 0)
				return dates.get(0);
		} catch (EmptyResultDataAccessException ee) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "9999-99-99";
	}

	public String getLastNDate(String stockId, int limitDays) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", limitDays);

			List<String> dates = this.namedParameterJdbcTemplate.query(QUERY_LATEST_N_DATE_STOCKID_SQL, namedParameters,
					new StringVOMapper());

			if (dates != null && dates.size() > 0)
				return dates.get(dates.size() - 1);
		} catch (EmptyResultDataAccessException ee) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "9999-99-99";
	}

	public List<String> getAllLastNDate(String stockId, int limitDays) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", limitDays);

			List<String> dates = this.namedParameterJdbcTemplate.query(QUERY_LATEST_N_DATE_STOCKID_SQL, namedParameters,
					new StringVOMapper());

			return dates;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public List<String> getAllDealDate(String stockId) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<String> dates = this.namedParameterJdbcTemplate.query(QUERY_DEAL_DATE_BY_ID, namedParameters,
					new StringVOMapper());

			return dates;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public List<String> getAllSZZSDealDate() {
		return getAllDealDate("999999");
	}

	public List<String> getDistinctStockIDs() {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();

			List<String> ids = this.namedParameterJdbcTemplate.query(QUERY_DISTINCT_ID, namedParameters,
					new StringVOMapper());

			return ids;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	public void updateChuQuanBatchPrice(ChuQuanChuXiVO vo) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.stockId);
			namedParameters.addValue("rate", vo.rate);
			namedParameters.addValue("date", vo.date);

			namedParameterJdbcTemplate.execute(UPDATE_BATCH_PRICE_BASED_ON_CHUQUAN_AND_DATE, namedParameters,
					new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateChuQuanPrice(StockPriceVO vo) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.stockId);
			namedParameters.addValue("date", vo.date);
			namedParameters.addValue("open", vo.open);
			namedParameters.addValue("high", vo.high);
			namedParameters.addValue("low", vo.low);
			namedParameters.addValue("close", vo.close);

			namedParameterJdbcTemplate.execute(UPDATE_PRICE_BASED_ON_CHUQUAN_AND_DATE, namedParameters,
					new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<StockPriceVO> getAllStockPriceByDate(String date) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);

			List<StockPriceVO> list = this.namedParameterJdbcTemplate.query(QUERY_ALL_BY_DATE, namedParameters,
					new StockPriceVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<StockPriceVO>();
		}
	}

	public boolean isDateInDealDate(String date) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);

			int rtn = this.namedParameterJdbcTemplate.queryForObject(IS_DATE_IN_DEAL, namedParameters,
					new IntVOMapper());

			return rtn == 1 ? true : false;
		} catch (EmptyResultDataAccessException ee) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public int countByStockId(String stockId) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			int rtn = this.namedParameterJdbcTemplate.queryForObject(COUNT_BY_ID_SQL, namedParameters,
					new IntVOMapper());

			return rtn;
		} catch (EmptyResultDataAccessException ee) {
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int countAll() {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();

			int rtn = this.namedParameterJdbcTemplate.queryForObject(COUNT_ALL_SQL, namedParameters, new IntVOMapper());
			return rtn;
		} catch (EmptyResultDataAccessException ee) {
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int countTuplesByIDAndBetweenDate(String stockId, String start, String end) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date1", start);
			namedParameters.addValue("date2", end);

			int rtn = this.namedParameterJdbcTemplate.queryForObject(COUNT_DAYS_BETWEEN_DATE1_DATE2, namedParameters,
					new IntVOMapper());

			return rtn;
		} catch (EmptyResultDataAccessException ee) {
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<StockPriceVO> queryByStockId(String stockId) {
		return this.getStockPriceById(stockId);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockPriceTableHelper ins = StockPriceTableHelper.getInstance();
		try {
			List<String> dates = ins.getLatestNStockDate(3);
			for (String date : dates)
				System.out.println(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
