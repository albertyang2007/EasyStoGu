package org.easystogu.db.access.view;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.access.table.CompanyInfoTableHelper;
import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.view.FavoritesStockCheckpointVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class FavoritesStockCheckpointViewHelper {
    private static Logger logger = LogHelper.getLogger(CompanyInfoTableHelper.class);
    private static FavoritesStockCheckpointViewHelper instance = null;
    private static FavoritesStockCheckpointViewHelper georedInstance = null;

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private FavoritesStockCheckpointViewHelper(javax.sql.DataSource datasource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(datasource);
    }

    public static FavoritesStockCheckpointViewHelper getInstance() {
        if (instance == null) {
            instance = new FavoritesStockCheckpointViewHelper(PostgreSqlDataSourceFactory.createDataSource());
        }
        return instance;
    }

    public static FavoritesStockCheckpointViewHelper getGeoredInstance() {
        if (georedInstance == null) {
            georedInstance = new FavoritesStockCheckpointViewHelper(
                    PostgreSqlDataSourceFactory.createGeoredDataSource());
        }
        return georedInstance;
    }

    private static final class FavoritesStockCheckpointVOMapper implements RowMapper<FavoritesStockCheckpointVO> {
        public FavoritesStockCheckpointVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            FavoritesStockCheckpointVO vo = new FavoritesStockCheckpointVO();
            vo.setStockId(rs.getString("stockId"));
            vo.setName(rs.getString("name"));
            vo.setDate(rs.getString("date"));
            vo.setCheckPoint(rs.getString("checkpoint"));
            vo.setUserId(rs.getString("userId"));
            return vo;
        }
    }

    private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
        public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
            return ps.executeUpdate();
        }
    }

    public List<FavoritesStockCheckpointVO> getByDateAndUserId(String date, String userId) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("date", date);
            namedParameters.addValue("userId", userId);

            String viewName = "\"favorites_stock_checkpoint_Details\"";
            String queryStr = "SELECT * FROM " + viewName + "WHERE date = :date AND userId = :userId";

            List<FavoritesStockCheckpointVO> list = this.namedParameterJdbcTemplate.query(queryStr, namedParameters,
                    new FavoritesStockCheckpointVOMapper());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<FavoritesStockCheckpointVO>();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        FavoritesStockCheckpointViewHelper ins = FavoritesStockCheckpointViewHelper.getInstance();
        List<FavoritesStockCheckpointVO> list = ins.getByDateAndUserId("2017-10-25", "admin");
        System.out.println(list.get(0));
    }
}
