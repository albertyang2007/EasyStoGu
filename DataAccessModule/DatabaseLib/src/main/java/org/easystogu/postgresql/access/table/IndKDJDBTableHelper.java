package org.easystogu.postgresql.access.table;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndKDJDBTableHelper extends PostgresqlIndDBHelper {
//	private static IndKDJDBTableHelper instance = null;
//
//	public static IndKDJDBTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndKDJDBTableHelper("ind_kdj", KDJVO.class);
//		}
//		return instance;
//	}
//
//	protected IndKDJDBTableHelper(String tableNameParm, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableNameParm, indicatorVOClass);
//	}

	@Override
	@Value("ind_kdj")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.KDJVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
