package org.easystogu.db.access.facde;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cassandra.access.table.IndBollCassTableHelper;
import org.easystogu.cassandra.access.table.IndKDJCassTableHelper;
import org.easystogu.cassandra.access.table.IndMacdCassTableHelper;
import org.easystogu.cassandra.access.table.IndQSDDCassTableHelper;
import org.easystogu.cassandra.access.table.IndShenXianCassTableHelper;
import org.easystogu.cassandra.access.table.IndWRCassTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.postgresql.access.table.IndBollDBTableHelper;
import org.easystogu.postgresql.access.table.IndKDJDBTableHelper;
import org.easystogu.postgresql.access.table.IndMacdDBTableHelper;
import org.easystogu.postgresql.access.table.IndQSDDDBTableHelper;
import org.easystogu.postgresql.access.table.IndShenXianDBTableHelper;
import org.easystogu.postgresql.access.table.IndWRDBTableHelper;

public class DBAccessFacdeFactory {
	static ConfigurationServiceCache config = ConfigurationServiceCache.getInstance();

	static Map<String, Class<? extends IndicatorDBHelperIF>> sqlFacdeMap = new HashMap<String, Class<? extends IndicatorDBHelperIF>>();
	static Map<String, Class<? extends IndicatorDBHelperIF>> cqlFacdeMap = new HashMap<String, Class<? extends IndicatorDBHelperIF>>();

	static {
		// cql
		cqlFacdeMap.put("Macd", IndMacdCassTableHelper.class);
		cqlFacdeMap.put("Kdj", IndKDJCassTableHelper.class);
		cqlFacdeMap.put("Boll", IndBollCassTableHelper.class);
		cqlFacdeMap.put("Qsdd", IndQSDDCassTableHelper.class);
		cqlFacdeMap.put("Wr", IndWRCassTableHelper.class);
		cqlFacdeMap.put("Shenxian", IndShenXianCassTableHelper.class);

		// sql
		sqlFacdeMap.put("Macd", IndMacdDBTableHelper.class);
		sqlFacdeMap.put("Kdj", IndKDJDBTableHelper.class);
		sqlFacdeMap.put("Boll", IndBollDBTableHelper.class);
		sqlFacdeMap.put("Qsdd", IndQSDDDBTableHelper.class);
		sqlFacdeMap.put("Wr", IndWRDBTableHelper.class);
		sqlFacdeMap.put("Shenxian", IndShenXianDBTableHelper.class);
	}

	@SuppressWarnings("unchecked")
	public static IndicatorDBHelperIF getInstance(String name) {
		try {
			String indicatorDBType = config.getString("indicatorDBType", "CQL");
			Class clazz = cqlFacdeMap.get(name);
			if ("SQL".equals(indicatorDBType))
				clazz = sqlFacdeMap.get(name);
			
			Method getInstanceM = clazz.getMethod("getInstance", null);
			return (IndicatorDBHelperIF) getInstanceM.invoke(null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		IndicatorDBHelperIF boll = DBAccessFacdeFactory.getInstance("Boll");
		System.out.println(boll.getClass());
	}
}
