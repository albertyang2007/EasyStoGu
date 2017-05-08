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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

//一元股,5元股统计
public class XXXYuanStockStatisticsViewHelper {
	private static Logger logger = LogHelper.getLogger(CompanyInfoTableHelper.class);
	private static XXXYuanStockStatisticsViewHelper instance = null;
	private static XXXYuanStockStatisticsViewHelper georedInstance = null;

	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private XXXYuanStockStatisticsViewHelper(javax.sql.DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(datasource);
	}

	public static XXXYuanStockStatisticsViewHelper getInstance() {
		if (instance == null) {
			instance = new XXXYuanStockStatisticsViewHelper(PostgreSqlDataSourceFactory.createDataSource());
		}
		return instance;
	}

	public static XXXYuanStockStatisticsViewHelper getGeoredInstance() {
		if (georedInstance == null) {
			georedInstance = new XXXYuanStockStatisticsViewHelper(PostgreSqlDataSourceFactory.createGeoredDataSource());
		}
		return georedInstance;
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

	//currently only 3 views: howMuchYuan = OneYuan, FiveYuan or TenYuan
	//OneYuan_Stock_Statistics, FiveYuan_Stock_Statistics and TenYuan_Stock_Statistics
	public List<StatisticsViewVO> getAll(String howMuchYuan) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();

			String viewName = "\"" + howMuchYuan + "_Stock_Statistics\"";
			String QUERY_ALL = "SELECT date, count FROM " + viewName + "order by date desc";

			List<StatisticsViewVO> list = this.namedParameterJdbcTemplate.query(QUERY_ALL, namedParameters,
					new StatisticsInfoVOMapper());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<StatisticsViewVO>();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XXXYuanStockStatisticsViewHelper ins = XXXYuanStockStatisticsViewHelper.getInstance();
		List<StatisticsViewVO> list = ins.getAll("OneYuan");
		System.out.println(list.get(0));
	}

}
