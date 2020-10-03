package org.easystogu.postgresql.access.table;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndWRDBTableHelper extends PostgresqlIndDBHelper {
//	private static IndWRDBTableHelper instance = null;
//
//	public static IndWRDBTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndWRDBTableHelper("ind_wr", WRVO.class);
//		}
//		return instance;
//	}
//
//	protected IndWRDBTableHelper(String tableNameParm, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableNameParm, indicatorVOClass);
//	}

	@Override
	@Value("ind_wr")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.WRVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
