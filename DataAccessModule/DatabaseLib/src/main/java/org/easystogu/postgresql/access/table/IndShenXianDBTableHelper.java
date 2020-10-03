package org.easystogu.postgresql.access.table;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndShenXianDBTableHelper extends PostgresqlIndDBHelper {
//	private static IndShenXianDBTableHelper instance = null;
//
//	public static IndShenXianDBTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndShenXianDBTableHelper("ind_shenxian", ShenXianVO.class);
//		}
//		return instance;
//	}
//
//	protected IndShenXianDBTableHelper(String tableNameParm, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableNameParm, indicatorVOClass);
//	}

	@Override
	@Value("ind_shenxian")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.ShenXianVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
