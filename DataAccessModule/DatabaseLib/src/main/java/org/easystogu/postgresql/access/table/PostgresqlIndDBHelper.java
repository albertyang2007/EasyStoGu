package org.easystogu.postgresql.access.table;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public abstract class PostgresqlIndDBHelper implements IndicatorDBHelperIF {
	private static Logger logger = LogHelper.getLogger(PostgresqlIndDBHelper.class);
	protected Class<? extends IndicatorVO> indicatorVOClass;
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	protected String tableName;// To be set later
	protected String INSERT_SQL;
	protected String QUERY_ALL_BY_ID_SQL;
	protected String QUERY_BY_ID_AND_DATE_SQL;
	protected String QUERY_BY_STOCKID_AND_BETWEEN_DATE;
	protected String QUERY_LATEST_N_BY_ID_SQL;
	protected String DELETE_BY_STOCKID_SQL;
	protected String DELETE_BY_STOCKID_AND_DATE_SQL;

	protected PostgresqlIndDBHelper(String tableNameParm, Class<? extends IndicatorVO> indicatorVOClass) {
		this.indicatorVOClass = indicatorVOClass;
		this.tableName = tableNameParm;

		String[] paris = generateFieldsNamePairs();

		// INSERT INTO ind.macd (stockId, date, dif, dea, macd) VALUES (:stockId, :date,
		// :dif, :dea, :macd);
		INSERT_SQL = "INSERT INTO " + tableName + " (" + paris[0] + ") VALUES (" + paris[1] + ")";
		QUERY_ALL_BY_ID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId ORDER BY date";
		QUERY_BY_ID_AND_DATE_SQL = "SELECT * FROM " + tableName + " WHERE stockId = :stockId AND date = :date";
		QUERY_BY_STOCKID_AND_BETWEEN_DATE = "SELECT * FROM " + tableName
				+ " WHERE stockId = :stockId AND DATE >= :date1 AND DATE <= :date2 ORDER BY DATE";
		QUERY_LATEST_N_BY_ID_SQL = "SELECT * FROM " + tableName
				+ " WHERE stockId = :stockId ORDER BY date DESC LIMIT :limit";
		DELETE_BY_STOCKID_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId";
		DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName + " WHERE stockId = :stockId AND date = :date";

		// System.out.println(INSERT_SQL);

		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				PostgreSqlDataSourceFactory.createDataSource());
	}

	private String[] generateFieldsNamePairs() {
		StringBuffer sbNames = new StringBuffer();
		StringBuffer sbQuota = new StringBuffer();
		Field[] fields = indicatorVOClass.getDeclaredFields();
		for (Field f : fields) {
			sbNames.append(f.getName() + ",");
			sbQuota.append(":" + f.getName() + ",");
		}
		return new String[] { sbNames.toString().substring(0, sbNames.length() - 1),
				sbQuota.toString().substring(0, sbQuota.length() - 1) };
	}

	protected final class IndVOMapper implements RowMapper<IndicatorVO> {
		public IndicatorVO mapRow(ResultSet r, int rowNum) throws SQLException {
			try {
				IndicatorVO vo = indicatorVOClass.newInstance();
				Field[] fields = indicatorVOClass.getDeclaredFields();

				for (Field f : fields) {
					PropertyDescriptor pd = new PropertyDescriptor(f.getName(), indicatorVOClass);
					Method wM = pd.getWriteMethod();
					if (String.class.equals(f.getType())) {
						wM.invoke(vo, r.getString(f.getName()));
					} else if (int.class.equals(f.getType())) {
						wM.invoke(vo, r.getInt(f.getName()));
					} else if (double.class.equals(f.getType())) {
						wM.invoke(vo, r.getDouble(f.getName()));
					} else if (float.class.equals(f.getType())) {
						wM.invoke(vo, r.getFloat(f.getName()));
					}
				}
				System.out.println(vo);
				return vo;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	protected static final class DefaultPreparedStatementCallback implements PreparedStatementCallback<Integer> {
		public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			return ps.executeUpdate();
		}
	}

	protected MapSqlParameterSource generateNameParms(IndicatorVO vo) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		try {
			Field[] fields = indicatorVOClass.getDeclaredFields();
			for (Field f : fields) {
				namedParameters.addValue(f.getName(), f.get(vo));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return namedParameters;
	}

	public <T extends IndicatorVO> void insert(T vo) {
		try {
			namedParameterJdbcTemplate.execute(INSERT_SQL, generateNameParms(vo),
					new DefaultPreparedStatementCallback());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public <T extends IndicatorVO> void insert(List<T> list) {
		for (IndicatorVO vo : list) {
			insert(vo);
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends IndicatorVO> T getSingle(String stockId, String date) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date", date);

			T vo = (T) this.namedParameterJdbcTemplate.queryForObject(QUERY_BY_ID_AND_DATE_SQL, namedParameters,
					new IndVOMapper());

			return vo;
		} catch (EmptyResultDataAccessException ee) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T extends IndicatorVO> List<T> getAll(String stockId) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);

			List<T> list = (List<T>) this.namedParameterJdbcTemplate.query(QUERY_ALL_BY_ID_SQL, namedParameters,
					new IndVOMapper());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<T>();
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

	@SuppressWarnings("unchecked")
	public <T extends IndicatorVO> List<T> getByIdAndBetweenDate(String stockId, String startDate, String endDate) {
		logger.info("getByIdAndBetweenDate: stockId=" + stockId + ",startDate=" + startDate + ",endDate=" + endDate
				+ ",SQL=" + QUERY_BY_STOCKID_AND_BETWEEN_DATE);
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("date1", startDate);
			namedParameters.addValue("date2", endDate);

			List<T> list = (List<T>) this.namedParameterJdbcTemplate.query(QUERY_BY_STOCKID_AND_BETWEEN_DATE,
					namedParameters, new IndVOMapper());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<T>();
	}

	@SuppressWarnings("unchecked")
	public <T extends IndicatorVO> List<T> getByIdAndLatestNDate(String stockId, int day) {
		try {
			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("stockId", stockId);
			namedParameters.addValue("limit", day);

			List<T> list = (List<T>) this.namedParameterJdbcTemplate.query(QUERY_LATEST_N_BY_ID_SQL, namedParameters,
					new IndVOMapper());

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<T>();
		}
	}
}
