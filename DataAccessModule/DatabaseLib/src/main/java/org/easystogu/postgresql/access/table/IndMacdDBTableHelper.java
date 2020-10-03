package org.easystogu.postgresql.access.table;

import org.easystogu.config.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndMacdDBTableHelper extends PostgresqlIndDBHelper {
//	private static IndMacdDBTableHelper instance = null;
//
//	public static IndMacdDBTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndMacdDBTableHelper("ind_macd", MacdVO.class);
//		}
//		return instance;
//	}
//
//	protected IndMacdDBTableHelper(String tableNameParm, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableNameParm, indicatorVOClass);
//	}
	
	@Override
	@Value("ind_macd")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.MacdVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
