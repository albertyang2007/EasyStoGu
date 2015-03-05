package org.easystogu.db.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class StockPriceTableHelper {
    private static Logger logger = LogHelper.getLogger(StockPriceTableHelper.class);
    private static StockPriceTableHelper instance = null;
    protected DataSource dataSource = PostgreSqlDataSourceFactory.createDataSource();
    protected String tableName = "STOCKPRICE";
    // please modify this SQL in all subClass
    protected String INSERT_SQL = "INSERT INTO "
            + tableName
            + " (stockId, date, open, high, low, close, volume) VALUES (:stockId, :date, :open, :high, :low, :close, :volume)";
    protected String SELECT_CLOSE_PRICE_SQL = "SELECT close AS rtn FROM " + tableName
            + " WHERE stockId = :stockId ORDER BY DATE";
    protected String SELECT_LOW_PRICE_SQL = "SELECT low AS rtn FROM " + tableName
            + " WHERE stockId = :stockId ORDER BY DATE";
    protected String SELECT_HIGH_PRICE_SQL = "SELECT high AS rtn FROM " + tableName
            + " WHERE stockId = :stockId ORDER BY DATE";
    protected String SELECT_BY_STOCKID_AND_BETWEEN_DATE_SQL = "SELECT * FROM " + tableName
            + " WHERE stockId = :stockId AND date >= :date1 AND date <= :date2 ORDER BY DATE";
    // macd used this sql
    protected String QUERY_BY_STOCKID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId ORDER BY DATE";
    // avg price, for example MA5, MA10, MA20, MA30
    protected String AVG_CLOSE_PRICE_SQL = "SELECT avg(close) AS rtn from (SELECT close FROM " + tableName
            + " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit) AS myma";
    // avg volume, for example MAVOL5, MAVOL10
    protected String AVG_VOLUME_SQL = "SELECT avg(volume) AS rtn from (SELECT volume FROM " + tableName
            + " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit) AS mymavol";
    // kdj used this: Low(n)
    protected String SELECT_LOW_N_PRICE_SQL = "SELECT min(low) AS rtn from (SELECT low FROM " + tableName
            + " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit) AS mylown";
    // kdj used this: High(n)
    protected String SELECT_HIGH_N_PRICE_SQL = "SELECT max(high) AS rtn from (SELECT high FROM " + tableName
            + " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit) AS myhighn";
    // query price by Id and date
    protected String QUERY_BY_STOCKID_DATE_SQL = "SELECT * FROM " + tableName
            + " WHERE stockId = :stockId AND date = :date";
    // query the last date
    protected String GET_LATEST_STOCK_DATE = "SELECT date as rtn FROM " + tableName + " ORDER BY DATE DESC limit 1";
    // query the latest N date
    protected String QUERY_LATEST_N_DATE_STOCKID_SQL = "SELECT * FROM " + tableName
            + " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit";
    // query the low price between date1(not include) and date2(include)
    protected String QUERY_LOW_PRICE_BETWEEN_DATE_SQL = "SELECT min(low) AS rtn from (SELECT low from " + tableName
            + " WHERE stockId = :stockId AND date > :startDate AND date <= :endDate) AS mylowQuery";
    // query the high price between date1(not include) and date2(include)
    protected String QUERY_HIGH_PRICE_BETWEEN_DATE_SQL = "SELECT max(high) AS rtn from (SELECT high from " + tableName
            + " WHERE stockId = :stockId AND date > :startDate AND date <= :endDate) AS myHighQuery";
    protected String DELETE_BY_STOCKID_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId";
    protected String DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName
            + " WHERE stockId = :stockId AND date = :date";
    protected String DELETE_BY_DATE_SQL = "DELETE FROM " + tableName + " WHERE date = :date";
    protected String COUNT_DAYS_BETWEEN_DATE1_DATE2 = "SELECT COUNT(*) FROM " + tableName
            + " WHERE stockId = :stockId AND DATE >= :date1 AND DATE <= :date2";

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static StockPriceTableHelper getInstance() {
        if (instance == null) {
            instance = new StockPriceTableHelper();
        }
        return instance;
    }

    protected StockPriceTableHelper() {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private static final class StockPriceVOMapper implements RowMapper<StockPriceVO> {
        public StockPriceVO mapRow(ResultSet rs, int rowNum) throws SQLException {
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

    private static final class DoubleVOMapper implements RowMapper<Double> {
        public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getDouble("rtn");
        }
    }

    private static final class StringVOMapper implements RowMapper<String> {
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString("rtn");
        }
    }

    private static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
        public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
            return ps.executeUpdate();
        }
    }

    public void insert(StockPriceVO vo) {
        logger.debug("insert for {}", vo);

        if (!vo.isValidated()) {
            logger.debug(vo.getStockId() + " is not validated, skip. vo= {}", vo);
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

            namedParameterJdbcTemplate.execute(INSERT_SQL, namedParameters, new DefaultPreparedStatementCallback());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String stockId) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameterJdbcTemplate.execute(DELETE_BY_STOCKID_SQL, namedParameters,
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

    public void insert(List<StockPriceVO> list) throws Exception {
        for (StockPriceVO vo : list) {
            this.insert(vo);
        }
    }

    public Double getAvgClosePrice(String stockId, int day) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("limit", day);

            Double avg = this.namedParameterJdbcTemplate.queryForObject(AVG_CLOSE_PRICE_SQL, namedParameters,
                    new DoubleVOMapper());

            return avg;
        } catch (EmptyResultDataAccessException ee) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public Double getLowPrice(String stockId, int day) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("limit", day);

            Double min = this.namedParameterJdbcTemplate.queryForObject(SELECT_LOW_N_PRICE_SQL, namedParameters,
                    new DoubleVOMapper());

            return min;
        } catch (EmptyResultDataAccessException ee) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public Double getHighPrice(String stockId, int day) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("limit", day);

            Double max = this.namedParameterJdbcTemplate.queryForObject(SELECT_HIGH_N_PRICE_SQL, namedParameters,
                    new DoubleVOMapper());

            return max;
        } catch (Exception e) {
            logger.error("exception meets for getMinClosePrice stockId=" + stockId, e);
            return 0.0;
        }
    }

    public Long getAvgVolume(String stockId, int day) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("limit", day);

            Double avg = this.namedParameterJdbcTemplate.queryForObject(AVG_VOLUME_SQL, namedParameters,
                    new DoubleVOMapper());

            return avg.longValue();
        } catch (EmptyResultDataAccessException ee) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    public List<Double> getAllClosePrice(String stockId) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);

            List<Double> closes = this.namedParameterJdbcTemplate.query(SELECT_CLOSE_PRICE_SQL, namedParameters,
                    new DoubleVOMapper());

            return closes;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Double>();
        }
    }

    public List<Double> getAllLowPrice(String stockId) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);

            List<Double> lows = this.namedParameterJdbcTemplate.query(SELECT_LOW_PRICE_SQL, namedParameters,
                    new DoubleVOMapper());

            return lows;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Double>();
        }
    }

    public List<Double> getAllHighPrice(String stockId) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);

            List<Double> highs = this.namedParameterJdbcTemplate.query(SELECT_HIGH_PRICE_SQL, namedParameters,
                    new DoubleVOMapper());

            return highs;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Double>();
        }
    }

    public List<StockPriceVO> getStockPriceById(String stockId) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);

            List<StockPriceVO> list = this.namedParameterJdbcTemplate.query(QUERY_BY_STOCKID_SQL, namedParameters,
                    new StockPriceVOMapper());

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<StockPriceVO>();
        }
    }

    public List<StockPriceVO> getNdateStockPriceById(String stockId, int day) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("limit", day);

            List<StockPriceVO> list = this.namedParameterJdbcTemplate.query(QUERY_LATEST_N_DATE_STOCKID_SQL,
                    namedParameters, new StockPriceVOMapper());

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<StockPriceVO>();
    }

    public StockPriceVO getStockPriceByIdAndDate(String stockId, String date) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("date", date);

            StockPriceVO vo = this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_STOCKID_DATE_SQL,
                    namedParameters, new StockPriceVOMapper());
            return vo;
        } catch (EmptyResultDataAccessException ee) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getDaysByIdAndBetweenDates(String stockId, String date1, String date2) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("date1", date1);
            namedParameters.addValue("date2", date2);

            int days = this.namedParameterJdbcTemplate.queryForInt(COUNT_DAYS_BETWEEN_DATE1_DATE2, namedParameters);
            return days;
        } catch (EmptyResultDataAccessException ee) {
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<StockPriceVO> getStockPriceByIdAndBetweenDate(String stockId, String date1, String date2) {
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("date1", date1);
            namedParameters.addValue("date2", date2);

            List<StockPriceVO> list = this.namedParameterJdbcTemplate.query(SELECT_BY_STOCKID_AND_BETWEEN_DATE_SQL,
                    namedParameters, new StockPriceVOMapper());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<StockPriceVO>();
    }

    public String getLatestStockDate() {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();

            String date = this.namedParameterJdbcTemplate.queryForObject(GET_LATEST_STOCK_DATE, namedParameters,
                    new StringVOMapper());

            return date;
        } catch (EmptyResultDataAccessException ee) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public Double getLowPriceBetweenDate(String stockId, String startDate, String endDate) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("startDate", startDate);
            namedParameters.addValue("endDate", endDate);

            Double low = this.namedParameterJdbcTemplate.queryForObject(QUERY_LOW_PRICE_BETWEEN_DATE_SQL,
                    namedParameters, new DoubleVOMapper());

            return low;
        } catch (EmptyResultDataAccessException ee) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public Double getHighPriceBetweenDate(String stockId, String startDate, String endDate) {
        try {

            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("stockId", stockId);
            namedParameters.addValue("startDate", startDate);
            namedParameters.addValue("endDate", endDate);

            Double high = this.namedParameterJdbcTemplate.queryForObject(QUERY_HIGH_PRICE_BETWEEN_DATE_SQL,
                    namedParameters, new DoubleVOMapper());

            return high;
        } catch (EmptyResultDataAccessException ee) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockPriceTableHelper ins = new StockPriceTableHelper();
        try {
            System.out.println(ins.getAvgClosePrice("000333", 5));
            System.out.println(ins.getAvgVolume("000333", 10));
            System.out.println(ins.getLowPrice("000333", 9));
            System.out.println(ins.getHighPrice("000333", 9));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
