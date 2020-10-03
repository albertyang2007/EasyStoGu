package org.easystogu.postgresql.access.table;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndBollDBTableHelper extends PostgresqlIndDBHelper {
//	private static IndBollDBTableHelper instance = null;
//
//	public static IndBollDBTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndBollDBTableHelper("ind_boll", BollVO.class);
//		}
//		return instance;
//	}
//
//	protected IndBollDBTableHelper(String tableNameParm, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableNameParm, indicatorVOClass);
//	}

	@Override
	@Value("ind_boll")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.BollVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
