package org.easystogu.cassandra.access.table;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.easystogu.cassandra.ks.CassandraKepSpaceFactory;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

@Service
public abstract class CassandraIndDBHelper<T extends IndicatorVO> implements IndicatorDBHelperIF {
	private static Logger logger = LogHelper.getLogger(CassandraIndDBHelper.class);
	@Autowired
	protected CassandraKepSpaceFactory cassandraKepSpaceFactory;
	protected Class<?> indicatorVOClass;
	protected String tableName;// To be set later
	protected String INSERT_SQL;
	protected String QUERY_ALL_BY_ID_SQL;
	protected String QUERY_BY_ID_AND_DATE_SQL;
	protected String QUERY_BY_STOCKID_AND_BETWEEN_DATE;
	protected String QUERY_LATEST_N_BY_ID_SQL;
	protected String DELETE_BY_STOCKID_SQL;
	protected String DELETE_BY_STOCKID_AND_DATE_SQL;

	private static Map<String, PreparedStatement> prepareStmtMap = new ConcurrentHashMap<String, PreparedStatement>();

	protected void init() {
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
	protected T mapRowToVO(Row r) {
		try {
			if (r == null)
				return null;

			IndicatorVO vo = (IndicatorVO) indicatorVOClass.newInstance();
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
			//logger.debug(vo);
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

	private PreparedStatement getPrepareStatement(String CQL) {
		PreparedStatement stmt = prepareStmtMap.get(CQL);
		if (stmt == null) {
			stmt = getCassandraSession().prepare(CQL);
			prepareStmtMap.put(CQL, stmt);
		}
		return stmt;
	}

	@SuppressWarnings("unchecked")
	protected List<T> mapResultSetToList(ResultSet results) {
		List<IndicatorVO> list = new ArrayList<IndicatorVO>();
		for (Row r : results.all()) {
			list.add(mapRowToVO(r));
		}
		return (List<T>) list;
	}

	@SuppressWarnings("unchecked")
	protected T mapResultSetToSingle(ResultSet results) {
		return (T) mapRowToVO(results.one());
	}

	public <T extends IndicatorVO> void insert(T vo) {
		PreparedStatement preparedStatement = getPrepareStatement(INSERT_SQL);
		getCassandraSession().execute(preparedStatement.bind(generateBindParms(vo)));
	}

	public <T extends IndicatorVO> void insert(List<T> list) {
		BatchStatement batchStatement = new BatchStatement(BatchStatement.Type.UNLOGGED);
		PreparedStatement preparedStatement = getPrepareStatement(INSERT_SQL);
		for (IndicatorVO vo : list) {
			batchStatement.add(preparedStatement.bind(generateBindParms(vo)));
		}
		getCassandraSession().execute(batchStatement);
	}

	public T getSingle(String stockId, String date) {
		PreparedStatement preparedStatement = getPrepareStatement(QUERY_BY_ID_AND_DATE_SQL);
		ResultSet results = getCassandraSession().execute(preparedStatement.bind(stockId, date));
		return mapResultSetToSingle(results);
	}

	public List<T> getAll(String stockId) {
		PreparedStatement preparedStatement = getPrepareStatement(QUERY_ALL_BY_ID_SQL);
		ResultSet results = getCassandraSession().execute(preparedStatement.bind(stockId));
		return mapResultSetToList(results);
	}

	public void delete(String stockId) {
		PreparedStatement preparedStatement = getPrepareStatement(DELETE_BY_STOCKID_SQL);
		getCassandraSession().execute(preparedStatement.bind(stockId));
	}

	public void delete(String stockId, String date) {
		PreparedStatement preparedStatement = getPrepareStatement(DELETE_BY_STOCKID_AND_DATE_SQL);
		getCassandraSession().execute(preparedStatement.bind(stockId, date));
	}

	public List<T> getByIdAndBetweenDate(String stockId, String startDate, String endDate) {
		PreparedStatement preparedStatement = getPrepareStatement(QUERY_BY_STOCKID_AND_BETWEEN_DATE);
		ResultSet results = getCassandraSession().execute(preparedStatement.bind(stockId, startDate, endDate));
		return mapResultSetToList(results);
	}

	public List<T> getByIdAndLatestNDate(String stockId, int day) {
		PreparedStatement preparedStatement = getPrepareStatement(QUERY_LATEST_N_BY_ID_SQL);
		ResultSet results = getCassandraSession().execute(preparedStatement.bind(stockId, day));
		return mapResultSetToList(results);
	}

	public Class<?> getIndicatorVOClass() {
		return indicatorVOClass;
	}

	public void setIndicatorVOClass(Class<T> indicatorVOClass) {
		this.indicatorVOClass = indicatorVOClass;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	private Session getCassandraSession(){
		return cassandraKepSpaceFactory.createCluster().connect();
	}
}
