package org.easystogu.db.access.facde;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.easystogu.cassandra.access.table.CassandraIndDBHelper;
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
import org.easystogu.log.LogHelper;
import org.easystogu.postgresql.access.table.IndBollDBTableHelper;
import org.easystogu.postgresql.access.table.IndKDJDBTableHelper;
import org.easystogu.postgresql.access.table.IndMacdDBTableHelper;
import org.easystogu.postgresql.access.table.IndQSDDDBTableHelper;
import org.easystogu.postgresql.access.table.IndShenXianDBTableHelper;
import org.easystogu.postgresql.access.table.IndWRDBTableHelper;
import org.easystogu.postgresql.access.table.IndWeekKDJDBTableHelper;
import org.easystogu.postgresql.access.table.IndWeekMacdDBTableHelper;
import org.easystogu.postgresql.access.table.PostgresqlIndDBHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBAccessFacdeFactory {
	private static Logger logger = LogHelper.getLogger(DBAccessFacdeFactory.class);
	static Map<String, PostgresqlIndDBHelper> sqlFacdeMap = new HashMap<String, PostgresqlIndDBHelper>();
	static Map<String, CassandraIndDBHelper> cqlFacdeMap = new HashMap<String, CassandraIndDBHelper>();

	// Cassandra
	@Autowired
	IndMacdCassTableHelper indMacdCassTableHelper;
	@Autowired
	IndKDJCassTableHelper indKDJCassTableHelper;
	@Autowired
	IndBollCassTableHelper indBollCassTableHelper;
	@Autowired
	IndQSDDCassTableHelper indQSDDCassTableHelper;
	@Autowired
	IndWRCassTableHelper indWRCassTableHelper;
	@Autowired
	IndShenXianCassTableHelper indShenXianCassTableHelper;
	@Autowired
	IndWeekMacdCassTableHelper indWeekMacdCassTableHelper;
	@Autowired
	IndWeekKDJCassTableHelper indWeekKDJCassTableHelper;

	// Postgresql
	@Autowired
	IndMacdDBTableHelper indMacdDBTableHelper;
	@Autowired
	IndKDJDBTableHelper indKDJDBTableHelper;
	@Autowired
	IndBollDBTableHelper indBollDBTableHelper;
	@Autowired
	IndQSDDDBTableHelper indQSDDDBTableHelper;
	@Autowired
	IndWRDBTableHelper indWRDBTableHelper;
	@Autowired
	IndShenXianDBTableHelper indShenXianDBTableHelper;
	@Autowired
	IndWeekMacdDBTableHelper indWeekMacdDBTableHelper;
	@Autowired
	IndWeekKDJDBTableHelper indWeekKDJDBTableHelper;

	@PostConstruct
	public void init() {
		// cql
		cqlFacdeMap.put(Constants.indMacd, indMacdCassTableHelper);
		cqlFacdeMap.put(Constants.indKDJ, indKDJCassTableHelper);
		cqlFacdeMap.put(Constants.indBoll, indBollCassTableHelper);
		cqlFacdeMap.put(Constants.indQSDD, indQSDDCassTableHelper);
		cqlFacdeMap.put(Constants.indWR, indWRCassTableHelper);
		cqlFacdeMap.put(Constants.indShenXian, indShenXianCassTableHelper);
		cqlFacdeMap.put(Constants.indWeekMacd, indWeekMacdCassTableHelper);
		cqlFacdeMap.put(Constants.indWeekKDJ, indWeekKDJCassTableHelper);

		// sql
		sqlFacdeMap.put(Constants.indMacd, indMacdDBTableHelper);
		sqlFacdeMap.put(Constants.indKDJ, indKDJDBTableHelper);
		sqlFacdeMap.put(Constants.indBoll, indBollDBTableHelper);
		sqlFacdeMap.put(Constants.indQSDD, indQSDDDBTableHelper);
		sqlFacdeMap.put(Constants.indWR, indWRDBTableHelper);
		sqlFacdeMap.put(Constants.indShenXian, indShenXianDBTableHelper);
		sqlFacdeMap.put(Constants.indWeekMacd, indWeekMacdDBTableHelper);
		sqlFacdeMap.put(Constants.indWeekKDJ, indWeekKDJDBTableHelper);
	}

	public IndicatorDBHelperIF getInstance(String name) {
		try {
			// for k8s deployment, the indicatorDBType is set in configMap
			// the configMap is save to system environment
			String indicatorDBType = System.getenv(Constants.indicatorDBType);
			if (Strings.isEmpty(indicatorDBType)) {
				indicatorDBType = "SQL";
			}
			
			logger.info("indicatorDBType is: {}", indicatorDBType);

			IndicatorDBHelperIF instance = null;
			if ("CQL".equals(indicatorDBType)) {
				instance = cqlFacdeMap.get(name);
			} else {
				instance = sqlFacdeMap.get(name);
			}

			//Method getInstanceM = instance.getMethod("getInstance", null);
			//return (IndicatorDBHelperIF) getInstanceM.invoke(null, null);
			return instance;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
