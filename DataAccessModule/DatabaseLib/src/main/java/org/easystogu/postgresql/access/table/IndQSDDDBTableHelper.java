package org.easystogu.postgresql.access.table;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndQSDDDBTableHelper extends PostgresqlIndDBHelper {
//	private static IndQSDDDBTableHelper instance = null;
//
//	public static IndQSDDDBTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndQSDDDBTableHelper("ind_qsdd", QSDDVO.class);
//		}
//		return instance;
//	}
//
//	protected IndQSDDDBTableHelper(String tableNameParm, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableNameParm, indicatorVOClass);
//	}

	@Override
	@Value("ind_qsdd")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.QSDDVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
