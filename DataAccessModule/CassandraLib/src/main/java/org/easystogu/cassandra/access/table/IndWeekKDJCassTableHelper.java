package org.easystogu.cassandra.access.table;

import org.easystogu.config.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndWeekKDJCassTableHelper extends CassandraIndDBHelper {
//	private static IndWeekKDJCassTableHelper instance = null;
//
//	public static IndWeekKDJCassTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndWeekKDJCassTableHelper("ind_week_kdj", KDJVO.class);
//		}
//		return instance;
//	}
//
//	protected IndWeekKDJCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableName, indicatorVOClass);
//	}
	
	@Override
	@Value(Constants.CassandraKeySpace + "ind_week_kdj")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.KDJVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
