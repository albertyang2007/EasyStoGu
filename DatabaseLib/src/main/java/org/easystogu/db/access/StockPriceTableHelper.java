package org.easystogu.db.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

	private static final class QosPreparedStatementCallback implements
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
					new QosPreparedStatementCallback());
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
}
