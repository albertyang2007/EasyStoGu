package org.easystogu.cassandra.access.table;

import org.easystogu.config.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndShenXianCassTableHelper extends CassandraIndDBHelper {
//	private static IndShenXianCassTableHelper instance = null;
//
//	public static IndShenXianCassTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndShenXianCassTableHelper("ind_shenxian", ShenXianVO.class);
//		}
//		return instance;
//	}
//
//	protected IndShenXianCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableName, indicatorVOClass);
//	}

	@Override
	@Value(Constants.CassandraKeySpace + "ind_shenxian")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.ShenXianVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
