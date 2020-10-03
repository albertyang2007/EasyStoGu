package org.easystogu.db.access.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.vo.view.CommonViewVO;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommonViewHelper {
    private static Logger logger = LogHelper.getLogger(CommonViewHelper.class);
    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected String QUERY_BY_VIEW_NAMES = "SELECT COUNT(*) AS rtn FROM pg_views WHERE VIEWNAME = :viewName";

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

    private static final class CommonViewVOWithOutNameMapper implements RowMapper<CommonViewVO> {
        public CommonViewVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            CommonViewVO vo = new CommonViewVO();
            vo.setStockId(rs.getString("stockId"));
            // vo.setName(rs.getString("name"));
            vo.setDate(rs.getString("date"));
            return vo;
        }
    }

    private static final class StringVOMapper implements RowMapper<String> {
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString("stockId");
        }
    }

    public boolean isViewExist(String viewName) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("viewName", viewName);

        int rtn = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_VIEW_NAMES, namedParameters,
                new IntVOMapper());
        return rtn > 0 ? true : false;
    }

    public List<CommonViewVO> queryByDateForViewDirectlySearch(String date, String viewName) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM \"" + viewName + "\"");
            //specific the date
            if ("Latest_20".equals(date)) {
                //not specific the date, search least 20
                sql.append(" ORDER BY date DESC LIMIT 20");
            } else if (Strings.isNotEmpty(date)) {
                sql.append(" WHERE date = :date");
                namedParameters.addValue("date", date);
            }

            logger.info("queryByDateForViewDirectlySearch sql =" + sql.toString());

            List<CommonViewVO> list = this.namedParameterJdbcTemplate.query(sql.toString(), namedParameters,
                    new CommonViewVOMapper());
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<CommonViewVO>();
    }

    public List<CommonViewVO> queryByDateForCheckPoint(String viewName, String date) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM \"" + viewName + "\"");
            if (Strings.isNotEmpty(date)) {
                sql.append(" WHERE date = :date");
                namedParameters.addValue("date", date);
            }

            List<CommonViewVO> list = this.namedParameterJdbcTemplate.query(sql.toString(), namedParameters,
                    new CommonViewVOWithOutNameMapper());
            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<CommonViewVO>();
    }

    public List<String> queryAllStockIds(String viewName) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT stockId FROM \"" + viewName + "\"");

            List<String> list = this.namedParameterJdbcTemplate.query(sql.toString(), namedParameters,
                    new StringVOMapper());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }
}
