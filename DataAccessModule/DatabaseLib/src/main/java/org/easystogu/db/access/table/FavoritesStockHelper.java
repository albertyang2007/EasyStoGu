package org.easystogu.db.access.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.view.FavoritesStockVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class FavoritesStockHelper {
	private static Logger logger = LogHelper.getLogger(FavoritesStockHelper.class);
	@Autowired
	protected PostgreSqlDataSourceFactory postgreSqlDataSourceFactory;
	protected String tableName = "FAVORITES_STOCK";
	protected String INSERT_SQL = "INSERT INTO " + tableName + " (stockId, userId) VALUES (:stockId, :userId)";
	protected String QUERY_BY_USERID_SQL = "SELECT * FROM " + tableName + " WHERE userId = :userId";
	protected String DELETE_BY_STOCKID_AND_USERID_SQL = "DELETE FROM " + tableName + " WHERE stockId =:stockId AND userId = :userId";

	
	private NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(postgreSqlDataSourceFactory.createDataSource());
	}
	
	private static final class FavoritesStockVOMapper implements RowMapper<FavoritesStockVO> {
		public FavoritesStockVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			FavoritesStockVO vo = new FavoritesStockVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setUserId(rs.getString("userId"));
			return vo;
		}
	}

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	public void insert(FavoritesStockVO vo) {
		logger.debug("insert for {}", vo);

		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", vo.getStockId());
			namedParameters.addValue("userId", vo.getUserId());

			getNamedParameterJdbcTemplate().execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			logger.error("exception meets for insert vo: " + vo, e);
			e.printStackTrace();
		}
	}

	public void insert(List<FavoritesStockVO> list) throws Exception {
		for (FavoritesStockVO vo : list) {
			this.insert(vo);
		}
	}

	public void delete(String stockId, String userId) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("userId", userId);
			getNamedParameterJdbcTemplate().execute(DELETE_BY_STOCKID_AND_USERID_SQL, namedParameters,
					new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<FavoritesStockVO> getByUserId(String userId) {
		try {

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("userId", userId);

			List<FavoritesStockVO> list = this.getNamedParameterJdbcTemplate().query(QUERY_BY_USERID_SQL, namedParameters,
					new FavoritesStockVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<FavoritesStockVO>();
	}
}
