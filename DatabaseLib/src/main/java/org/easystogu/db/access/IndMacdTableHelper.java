package org.easystogu.db.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.table.IndMacdVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class IndMacdTableHelper {
	private static Logger logger = LogHelper
			.getLogger(IndMacdTableHelper.class);
	private DataSource dataSource = PostgreSqlDataSourceFactory
			.createDataSource();
	public static final String INSERT_SQL = "INSERT INTO IND_MACD (stockId, date, dif, dea, macd) VALUES (:stockId, :date, :dif, :dea, :macd)";
	public static final String QUERY_SQL = "SELECT * FROM IND_MACD WHERE stockId = :stockId AND date = :date";

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public IndMacdTableHelper() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				dataSource);
	}

	private static final class IndMacdVOMapper implements RowMapper<IndMacdVO> {
		public IndMacdVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			IndMacdVO vo = new IndMacdVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setDate(rs.getString("date"));
			vo.setDif(rs.getDouble("dif"));
			vo.setDea(rs.getDouble("dea"));
			vo.setMacd(rs.getDouble("macd"));
			return vo;
		}
	}

	private static final class DefaultPreparedStatementCallback implements
			PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps)
				throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public void insert(IndMacdVO vo) throws Exception {
		logger.debug("insert for {}", vo);

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.getStockId());
			namedParameters.addValue("date", vo.getDate());
			namedParameters.addValue("dif", vo.getDif());
			namedParameters.addValue("dea", vo.getDea());
			namedParameters.addValue("macd", vo.getMacd());

			namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters,
					new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			logger.error("exception meets for insert vo: " + vo, e);
			throw e;
		}
	}

	public void insert(List<IndMacdVO> list) throws Exception {
		for (IndMacdVO vo : list) {
			this.insert(vo);
		}
	}

	public IndMacdVO getMacd(String stockId, String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date", date);

			IndMacdVO vo = this.namedParameterJdbcTemplate.queryForObject(
					QUERY_SQL, namedParameters, new IndMacdVOMapper());

			return vo;
		} catch (Exception e) {
			logger.error("exception meets for getAvgClosePrice stockId="
					+ stockId, e);
			return null;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IndMacdTableHelper ins = new IndMacdTableHelper();
		try {
			System.out.println(ins.getMacd("000333", "2015-01-27"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
