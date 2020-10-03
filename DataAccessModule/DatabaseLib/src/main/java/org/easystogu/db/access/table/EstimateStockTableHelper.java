package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.easystogu.db.vo.table.EstimateStockVO;
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
public class EstimateStockTableHelper {
	private static Logger logger = LogHelper.getLogger(EstimateStockTableHelper.class);
	@Autowired
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	protected String tableName = "ESTIMATE_STOCK";
	// please modify this SQL in all subClass
	protected String INSERT_SQL = "INSERT INTO " + tableName + " (stockId, date) VALUES (:stockId, :date)";

	protected String DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName
			+ " WHERE stockId = :stockId AND date = :date";

	protected String DELETE_BY_DATE_SQL = "DELETE FROM " + tableName + " WHERE date = :date";

	protected String QUERY_BY_ID_AND_DATE_SQL = "SELECT * FROM " + tableName
			+ " WHERE stockId = :stockId AND date = :date";

	protected String QUERY_BY_DATE_SQL = "SELECT * FROM " + tableName + " WHERE date = :date";

	protected String QUERY_STOCKIDS_BY_DATE_SQL = "SELECT stockId FROM " + tableName + " WHERE date = :date";

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	private static final class EstimateStockVOMapper implements RowMapper<EstimateStockVO> {
		public EstimateStockVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EstimateStockVO vo = new EstimateStockVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setDate(rs.getString("date"));
			return vo;
		}
	}

	private static final class EstimateStockIdMapper implements RowMapper<String> {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("stockId");
		}
	}

	public void insert(EstimateStockVO vo) {
		logger.debug("insert for {}", vo);

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.getStockId());
			namedParameters.addValue("date", vo.getDate());

			namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertIfNotExist(EstimateStockVO vo) {

		if (this.getEstimateStock(vo.stockId, vo.date) != null) {
			return;
		}

		this.insert(vo);
	}

	public void delete(String date) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);
			namedParameterJdbcTemplate.execute(DELETE_BY_DATE_SQL, namedParameters,
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

	public EstimateStockVO getEstimateStock(String stockId, String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date", date);

			EstimateStockVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_ID_AND_DATE_SQL,
					namedParameters, new EstimateStockVOMapper());

			return vo;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<EstimateStockVO> getAllEstimateStockVOByDate(String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);

			List<EstimateStockVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_DATE_SQL, namedParameters,
					new EstimateStockVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> getAllEstimateStockIdsByDate(String date) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);

			List<String> list = this.namedParameterJdbcTemplate.query(QUERY_STOCKIDS_BY_DATE_SQL, namedParameters,
					new EstimateStockIdMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
