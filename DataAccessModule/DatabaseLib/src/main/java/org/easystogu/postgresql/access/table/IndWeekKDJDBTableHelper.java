package org.easystogu.postgresql.access.table;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndWeekKDJDBTableHelper extends PostgresqlIndDBHelper {
//	private static IndWeekKDJDBTableHelper instance = null;
//
//	public static IndWeekKDJDBTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndWeekKDJDBTableHelper("ind_week_kdj", KDJVO.class);
//		}
//		return instance;
//	}
//
//	protected IndWeekKDJDBTableHelper(String tableNameParm, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableNameParm, indicatorVOClass);
//	}

	@Override
	@Value("ind_week_kdj")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.KDJVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
