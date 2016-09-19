package org.easystogu.db.access.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.view.CommonViewVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class LuzaoZijinLiuView {
	private static Logger logger = LogHelper.getLogger(LuzaoZijinLiuView.class);
	private static LuzaoZijinLiuView instance = null;
	protected DataSource dataSource = PostgreSqlDataSourceFactory.createDataSource();
	protected String viewName = "\"luzao_phaseII_zijinliu_top300_Details\"";
	// please modify this SQL in all subClass

	protected String QUERY_BY_DATE = "SELECT * FROM " + viewName + " WHERE date = :date";

	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public static LuzaoZijinLiuView getInstance() {
		if (instance == null) {
			instance = new LuzaoZijinLiuView();
		}
		return instance;
	}

	protected LuzaoZijinLiuView() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
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

	public List<CommonViewVO> queryByDate(String date) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("date", date);
			List<CommonViewVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_DATE, namedParameters,
					new CommonViewVOMapper());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CommonViewVO>();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LuzaoZijinLiuView ins = new LuzaoZijinLiuView();
		List<CommonViewVO> list = ins.queryByDate("2016-09-14");
		for (CommonViewVO vo : list) {
			System.out.println(vo.name);
		}
		try {
			System.out.println();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
