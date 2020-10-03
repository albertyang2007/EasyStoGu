package org.easystogu.postgresql.access.table;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndWeekMacdDBTableHelper extends PostgresqlIndDBHelper {
//	private static IndWeekMacdDBTableHelper instance = null;
//
//	public static IndWeekMacdDBTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndWeekMacdDBTableHelper("ind_week_macd", MacdVO.class);
//		}
//		return instance;
//	}
//
//	protected IndWeekMacdDBTableHelper(String tableNameParm, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableNameParm, indicatorVOClass);
//	}

	@Override
	@Value("ind_week_macd")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.MacdVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
