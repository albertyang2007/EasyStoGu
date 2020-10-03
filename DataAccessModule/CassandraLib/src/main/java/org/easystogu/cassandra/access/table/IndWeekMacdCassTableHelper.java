package org.easystogu.cassandra.access.table;

import org.easystogu.config.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndWeekMacdCassTableHelper extends CassandraIndDBHelper {
//	private static IndWeekMacdCassTableHelper instance = null;
//
//	public static IndWeekMacdCassTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndWeekMacdCassTableHelper("ind_week_macd", MacdVO.class);
//		}
//		return instance;
//	}
//
//	protected IndWeekMacdCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableName, indicatorVOClass);
//	}
	
	@Override
	@Value(Constants.CassandraKeySpace + "ind_week_macd")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.MacdVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
