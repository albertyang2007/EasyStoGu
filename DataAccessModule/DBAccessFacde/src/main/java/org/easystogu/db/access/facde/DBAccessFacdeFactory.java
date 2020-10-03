package org.easystogu.db.access.facde;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.easystogu.cassandra.access.table.IndBollCassTableHelper;
import org.easystogu.cassandra.access.table.IndKDJCassTableHelper;
import org.easystogu.cassandra.access.table.IndMacdCassTableHelper;
import org.easystogu.cassandra.access.table.IndQSDDCassTableHelper;
import org.easystogu.cassandra.access.table.IndShenXianCassTableHelper;
import org.easystogu.cassandra.access.table.IndWRCassTableHelper;
import org.easystogu.cassandra.access.table.IndWeekKDJCassTableHelper;
import org.easystogu.cassandra.access.table.IndWeekMacdCassTableHelper;
import org.easystogu.config.Constants;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.postgresql.access.table.IndBollDBTableHelper;
import org.easystogu.postgresql.access.table.IndKDJDBTableHelper;
import org.easystogu.postgresql.access.table.IndMacdDBTableHelper;
import org.easystogu.postgresql.access.table.IndQSDDDBTableHelper;
import org.easystogu.postgresql.access.table.IndShenXianDBTableHelper;
import org.easystogu.postgresql.access.table.IndWRDBTableHelper;
import org.easystogu.postgresql.access.table.IndWeekKDJDBTableHelper;
import org.easystogu.postgresql.access.table.IndWeekMacdDBTableHelper;
import org.easystogu.utils.Strings;
import org.springframework.stereotype.Component;

@Component
public class DBAccessFacdeFactory {
	static Map<String, Class<? extends IndicatorDBHelperIF>> sqlFacdeMap = new HashMap<String, Class<? extends IndicatorDBHelperIF>>();
	static Map<String, Class<? extends IndicatorDBHelperIF>> cqlFacdeMap = new HashMap<String, Class<? extends IndicatorDBHelperIF>>();

	static {
		// cql
		cqlFacdeMap.put(Constants.indMacd, IndMacdCassTableHelper.class);
		cqlFacdeMap.put(Constants.indKDJ, IndKDJCassTableHelper.class);
		cqlFacdeMap.put(Constants.indBoll, IndBollCassTableHelper.class);
		cqlFacdeMap.put(Constants.indQSDD, IndQSDDCassTableHelper.class);
		cqlFacdeMap.put(Constants.indWR, IndWRCassTableHelper.class);
		cqlFacdeMap.put(Constants.indShenXian, IndShenXianCassTableHelper.class);
		cqlFacdeMap.put(Constants.indWeekMacd, IndWeekMacdCassTableHelper.class);
		cqlFacdeMap.put(Constants.indWeekKDJ, IndWeekKDJCassTableHelper.class);

		// sql
		sqlFacdeMap.put(Constants.indMacd, IndMacdDBTableHelper.class);
		sqlFacdeMap.put(Constants.indKDJ, IndKDJDBTableHelper.class);
		sqlFacdeMap.put(Constants.indBoll, IndBollDBTableHelper.class);
		sqlFacdeMap.put(Constants.indQSDD, IndQSDDDBTableHelper.class);
		sqlFacdeMap.put(Constants.indWR, IndWRDBTableHelper.class);
		sqlFacdeMap.put(Constants.indShenXian, IndShenXianDBTableHelper.class);
		sqlFacdeMap.put(Constants.indWeekMacd, IndWeekMacdDBTableHelper.class);
		sqlFacdeMap.put(Constants.indWeekKDJ, IndWeekKDJDBTableHelper.class);
	}

	@SuppressWarnings("unchecked")
	public static IndicatorDBHelperIF getInstance(String name) {
		try {
			//for k8s deployment, the indicatorDBType is set in configMap
			//the configMap is save to system environment 
			String indicatorDBType = System.getenv(Constants.indicatorDBType);
			if (Strings.isEmpty(indicatorDBType)) {
				indicatorDBType = "SQL";
			}

			Class clazz = null;
			if ("CQL".equals(indicatorDBType)) {
				clazz = cqlFacdeMap.get(name);
			} else {
				clazz = sqlFacdeMap.get(name);
			}

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
