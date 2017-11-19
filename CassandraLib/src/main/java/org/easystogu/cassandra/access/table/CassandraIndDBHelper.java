package org.easystogu.cassandra.access.table;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.cassandra.ks.CassandraKepSpaceFactory;
import org.easystogu.config.Constants;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.IndicatorVO;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public abstract class CassandraIndDBHelper implements IndicatorDBHelperIF {
	protected Class<? extends IndicatorVO> indicatorVOClass;
	protected Session session;
	protected String tableName;// To be set later
	protected String INSERT_SQL;
	protected String QUERY_ALL_BY_ID_SQL;
	protected String QUERY_BY_ID_AND_DATE_SQL;
	protected String QUERY_BY_STOCKID_AND_BETWEEN_DATE;
	protected String QUERY_LATEST_N_BY_ID_SQL;
	protected String DELETE_BY_STOCKID_SQL;
	protected String DELETE_BY_STOCKID_AND_DATE_SQL;

	protected CassandraIndDBHelper(String tableNameParm, Class<? extends IndicatorVO> indicatorVOClass) {
		this.indicatorVOClass = indicatorVOClass;
		this.tableName = Constants.CassandraKeySpace + "." + tableNameParm;

		String[] paris = generateFieldsNamePairs();

		// INSERT INTO ind.macd (stockId, date, dif, dea, macd) VALUES (?, ?, ?,
		// ?, ?);
		INSERT_SQL = "INSERT INTO " + tableName + " (" + paris[0] + ") VALUES (" + paris[1] + ")";
		QUERY_ALL_BY_ID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = ? ORDER BY date";
		QUERY_BY_ID_AND_DATE_SQL = "SELECT * FROM " + tableName + " WHERE stockId = ? AND date = ?";
		QUERY_BY_STOCKID_AND_BETWEEN_DATE = "SELECT * FROM " + tableName
				+ " WHERE stockId = ? AND DATE >= ? AND DATE <= ? ORDER BY DATE";
		QUERY_LATEST_N_BY_ID_SQL = "SELECT * FROM " + tableName + " WHERE stockId = ? ORDER BY date DESC LIMIT ?";
		DELETE_BY_STOCKID_SQL = "DELETE FROM " + tableName + " WHERE stockId = ?";
		DELETE_BY_STOCKID_AND_DATE_SQL = "DELETE FROM " + tableName + " WHERE stockId = ? AND date = ?";

		// System.out.println(INSERT_SQL);

		this.session = CassandraKepSpaceFactory.createCluster().connect();
	}

	private String[] generateFieldsNamePairs() {
		StringBuffer sbNames = new StringBuffer();
		StringBuffer sbQuota = new StringBuffer();
		Field[] fields = indicatorVOClass.getDeclaredFields();
		for (Field f : fields) {
			sbNames.append(f.getName() + ",");
			sbQuota.append("?,");
		}
		return new String[] { sbNames.toString().substring(0, sbNames.length() - 1),
				sbQuota.toString().substring(0, sbQuota.length() - 1) };
	}

	@SuppressWarnings("unchecked")
	protected <T extends IndicatorVO> T mapRowToVO(Row r) {
		try {

			if (r == null)
				return null;

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
			return (T) vo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object[] generateBindParms(IndicatorVO vo) {
		List<Object> list = new ArrayList<Object>();
		try {
			Field[] fields = indicatorVOClass.getDeclaredFields();
			for (Field f : fields) {
				list.add(f.get(vo));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list.toArray();
	}

	@SuppressWarnings("unchecked")
	protected <T extends IndicatorVO> List<T> mapResultSetToList(ResultSet results) {
		List<IndicatorVO> list = new ArrayList<IndicatorVO>();
		for (Row r : results.all()) {
			list.add(mapRowToVO(r));
		}
		return (List<T>) list;
	}

	@SuppressWarnings("unchecked")
	protected <T extends IndicatorVO> T mapResultSetToSingle(ResultSet results) {
		return (T) mapRowToVO(results.one());
	}

	public <T extends IndicatorVO> void insert(T vo) {
		PreparedStatement preparedStatement = session.prepare(INSERT_SQL);
		session.execute(preparedStatement.bind(generateBindParms(vo)));
	}

	public <T extends IndicatorVO> void insert(List<T> list) {
		BatchStatement batchStatement = new BatchStatement(BatchStatement.Type.UNLOGGED);
		for (IndicatorVO vo : list) {
			PreparedStatement preparedStatement = session.prepare(INSERT_SQL);
			batchStatement.add(preparedStatement.bind(generateBindParms(vo)));
		}
		session.execute(batchStatement);
	}

	public <T extends IndicatorVO> T getSingle(String stockId, String date) {
		PreparedStatement preparedStatement = session.prepare(QUERY_BY_ID_AND_DATE_SQL);
		ResultSet results = session.execute(preparedStatement.bind(stockId, date));
		return mapResultSetToSingle(results);
	}

	public <T extends IndicatorVO> List<T> getAll(String stockId) {
		PreparedStatement preparedStatement = session.prepare(QUERY_ALL_BY_ID_SQL);
		ResultSet results = session.execute(preparedStatement.bind(stockId));
		return mapResultSetToList(results);
	}

	public void delete(String stockId) {
		PreparedStatement preparedStatement = session.prepare(DELETE_BY_STOCKID_SQL);
		session.execute(preparedStatement.bind(stockId));
	}

	public void delete(String stockId, String date) {
		PreparedStatement preparedStatement = session.prepare(DELETE_BY_STOCKID_AND_DATE_SQL);
		session.execute(preparedStatement.bind(stockId, date));
	}

	public <T extends IndicatorVO> List<T> getByIdAndBetweenDate(String stockId, String startDate, String endDate) {
		PreparedStatement preparedStatement = session.prepare(QUERY_BY_STOCKID_AND_BETWEEN_DATE);
		ResultSet results = session.execute(preparedStatement.bind(stockId, startDate, endDate));
		return mapResultSetToList(results);
	}

	public <T extends IndicatorVO> List<T> getByIdAndLatestNDate(String stockId, int day) {
		PreparedStatement preparedStatement = session.prepare(QUERY_LATEST_N_BY_ID_SQL);
		ResultSet results = session.execute(preparedStatement.bind(stockId, day));
		return mapResultSetToList(results);
	}
}
