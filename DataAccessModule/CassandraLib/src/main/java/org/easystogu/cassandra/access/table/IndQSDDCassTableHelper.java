package org.easystogu.cassandra.access.table;

import org.easystogu.config.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndQSDDCassTableHelper extends CassandraIndDBHelper {
//	private static IndQSDDCassTableHelper instance = null;
//
//	public static IndQSDDCassTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndQSDDCassTableHelper("ind_qsdd", QSDDVO.class);
//		}
//		return instance;
//	}
//
//	protected IndQSDDCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableName, indicatorVOClass);
//	}
	
	@Override
	@Value(Constants.CassandraKeySpace + "ind_qsdd")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.QSDDVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
