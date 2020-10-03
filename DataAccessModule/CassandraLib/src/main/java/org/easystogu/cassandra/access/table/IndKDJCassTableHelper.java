package org.easystogu.cassandra.access.table;

import org.easystogu.config.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndKDJCassTableHelper extends CassandraIndDBHelper {
//	private static IndKDJCassTableHelper instance = null;
//
//	public static IndKDJCassTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndKDJCassTableHelper("ind_kdj", KDJVO.class);
//		}
//		return instance;
//	}
//
//	protected IndKDJCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableName, indicatorVOClass);
//	}

	@Override
	@Value(Constants.CassandraKeySpace + "ind_kdj")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.KDJVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
