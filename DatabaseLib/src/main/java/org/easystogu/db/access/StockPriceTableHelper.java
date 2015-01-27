package org.easystogu.db.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class StockPriceTableHelper {
	private static Logger logger = LogHelper
			.getLogger(StockPriceTableHelper.class);
	private DataSource dataSource = PostgreSqlDataSourceFactory
			.createDataSource();
	public static final String INSERT_SQL = "INSERT INTO STOCKPRICE (stockId, date, open, high, low, close, volume) VALUES (:stockId, :date, :open, :high, :low, :close, :volume)";
	public static final String SELECT_CLOSE_PRICE_SQL = "SELECT close FROM STOCKPRICE WHERE stockId = :stockId ORDER BY DATE";
	public static final String QUERY_BY_STOCKID_SQL = "SELECT * FROM STOCKPRICE WHERE stockId = :stockId ORDER BY DATE";

	// avg price, for example MA5, MA10, MA20, MA30
	public static final String AVG_CLOSE_PRICE_SQL = "SELECT avg(ma) from (SELECT close AS ma FROM STOCKPRICE WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit) AS myma";

	// avg volume, for example MAVOL5, MAVOL10
	public static final String AVG_VOLUME_SQL = "SELECT avg(mavol) from (SELECT volume AS mavol FROM STOCKPRICE WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit) AS mymavol";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public StockPriceTableHelper() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				dataSource);
	}

	private static final class StockPriceVOMapper implements
			RowMapper<StockPriceVO> {
		public StockPriceVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			StockPriceVO vo = new StockPriceVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setDate(rs.getString("date"));
			vo.setClose(rs.getDouble("close"));
			vo.setHigh(rs.getDouble("high"));
			vo.setLow(rs.getDouble("low"));
			vo.setOpen(rs.getDouble("open"));
			vo.setVolume(rs.getLong("volume"));
			return vo;
		}
	}

	private static final class AVGPriceVOMapper implements RowMapper<Double> {
		public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getDouble("avg");
		}
	}

	private static final class AVGVolumeVOMapper implements RowMapper<Double> {
		public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getDouble("avg");
		}
	}

	private static final class ClosePriceVOMapper implements RowMapper<Double> {
		public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getDouble("close");
		}
	}

	private static final class DefaultPreparedStatementCallback implements
			PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps)
				throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public void insert(StockPriceVO vo) throws Exception {
		logger.debug("insert for {}", vo);

		if (!vo.isValidated()) {
			logger.debug(vo.getStockId() + " is not validated, skip. vo= {}",
					vo);
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

			namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters,
					new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			logger.error("exception meets for insert vo: " + vo, e);
			throw e;
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

			Double avg = this.namedParameterJdbcTemplate.queryForObject(
					AVG_CLOSE_PRICE_SQL, namedParameters,
					new AVGPriceVOMapper());

			return avg;
		} catch (Exception e) {
			logger.error("exception meets for getAvgClosePrice stockId="
					+ stockId, e);
			return 0.0;
		}
	}

	public Long getAvgVolume(String stockId, int day) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", day);

			Double avg = this.namedParameterJdbcTemplate.queryForObject(
					AVG_VOLUME_SQL, namedParameters, new AVGVolumeVOMapper());

			return avg.longValue();
		} catch (Exception e) {
			logger.error("exception meets for getAvgVolume stockId=" + stockId,
					e);
			return 0l;
		}
	}

	public List<Double> getAllClosePrice(String stockId) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<Double> closes = this.namedParameterJdbcTemplate.query(
					SELECT_CLOSE_PRICE_SQL, namedParameters,
					new ClosePriceVOMapper());

			return closes;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Double>();
		}
	}

	public List<StockPriceVO> getStockPriceById(String stockId) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<StockPriceVO> list = this.namedParameterJdbcTemplate.query(
					QUERY_BY_STOCKID_SQL, namedParameters,
					new StockPriceVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<StockPriceVO>();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockPriceTableHelper ins = new StockPriceTableHelper();
		try {
			System.out.println(ins.getAvgClosePrice("000333", 5));
			System.out.println(ins.getAvgVolume("000333", 10));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
