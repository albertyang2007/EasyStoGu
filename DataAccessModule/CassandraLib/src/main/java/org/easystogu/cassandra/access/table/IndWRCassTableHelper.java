package org.easystogu.cassandra.access.table;

import org.easystogu.config.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndWRCassTableHelper extends CassandraIndDBHelper {
//	private static IndWRCassTableHelper instance = null;
//
//	public static IndWRCassTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndWRCassTableHelper("ind_wr", WRVO.class);
//		}
//		return instance;
//	}
//
//	protected IndWRCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableName, indicatorVOClass);
//	}
	
	@Override
	@Value(Constants.CassandraKeySpace + "ind_wr")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.WRVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
