package org.easystogu.db.access.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.access.table.CompanyInfoTableHelper;
import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.view.StatisticsViewVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

//一元股,5元股统计
@Component
public class XXXYuanStockStatisticsViewHelper {
	private static Logger logger = LogHelper.getLogger(CompanyInfoTableHelper.class);
	@Autowired
	protected PostgreSqlDataSourceFactory postgreSqlDataSourceFactory;

	
	private NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(postgreSqlDataSourceFactory.createDataSource());
	}
	
	private static final class StatisticsInfoVOMapper implements RowMapper<StatisticsViewVO> {
		public StatisticsViewVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			StatisticsViewVO vo = new StatisticsViewVO();
			vo.setDate(rs.getString("date"));
			vo.setCount(rs.getInt("count"));
			return vo;
		}
	}

	private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	// currently only 3 views: howMuchYuan = OneYuan, FiveYuan or TenYuan
	// OneYuan_Stock_Statistics, FiveYuan_Stock_Statistics and
	// TenYuan_Stock_Statistics
	public List<StatisticsViewVO> getAll(String howMuchYuan) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();

			String viewName = "\"" + howMuchYuan + "_Stock_Statistics\"";
			String QUERY_ALL = "SELECT date, count FROM " + viewName + "order by date";

			List<StatisticsViewVO> list = this.getNamedParameterJdbcTemplate().query(QUERY_ALL, namedParameters,
					new StatisticsInfoVOMapper());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<StatisticsViewVO>();
	}
}
