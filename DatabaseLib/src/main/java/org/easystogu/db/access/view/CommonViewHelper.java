package org.easystogu.db.access.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.view.CommonViewVO;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class CommonViewHelper {
	private static Logger logger = LogHelper.getLogger(CommonViewHelper.class);
	private static CommonViewHelper instance = null;
	protected DataSource dataSource = PostgreSqlDataSourceFactory.createDataSource();
	protected String QUERY_BY_VIEW_NAMES = "SELECT COUNT(*) AS rtn FROM pg_views WHERE VIEWNAME = :viewName";

	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public static CommonViewHelper getInstance() {
		if (instance == null) {
			instance = new CommonViewHelper();
		}
		return instance;
	}

	protected CommonViewHelper() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	private static final class IntVOMapper implements RowMapper<Integer> {
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getInt("rtn");
		}
	}

	private static final class CommonViewVOMapper implements RowMapper<CommonViewVO> {
		public CommonViewVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			CommonViewVO vo = new CommonViewVO();
			vo.setStockId(rs.getString("stockId"));
			vo.setName(rs.getString("name"));
			vo.setDate(rs.getString("date"));
			return vo;
		}
	}

	private boolean isViewExist(String viewName) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("viewName", viewName);

		int rtn = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_VIEW_NAMES, namedParameters,
				new IntVOMapper());
		return rtn > 0 ? true : false;
	}

	public List<CommonViewVO> queryByDate(String viewName, String date) {
		try {
			if (isViewExist(viewName)) {
				MapSqlParameterSource namedParameters = new MapSqlParameterSource();
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT * FROM \"" + viewName + "\"");
				if (Strings.isNotEmpty(date)) {
					sql.append(" WHERE date = :date");
					namedParameters.addValue("date", date);
				}

				List<CommonViewVO> list = this.namedParameterJdbcTemplate.query(sql.toString(), namedParameters,
						new CommonViewVOMapper());
				return list;
			}
			logger.error("ViewName not exist:" + viewName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CommonViewVO>();
	}

	public List<CommonViewVO> queryAll(String viewName) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM \"" + viewName + "\" ORDER BY date DESC");

			List<CommonViewVO> list = this.namedParameterJdbcTemplate.query(sql.toString(), namedParameters,
					new CommonViewVOMapper());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CommonViewVO>();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommonViewHelper ins = new CommonViewHelper();
		List<CommonViewVO> list = ins.queryByDate("luzao_phaseII_zijinliu_top300_Details", "");
		for (CommonViewVO vo : list) {
			System.out.println(vo);
		}
		try {
			System.out.println();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
