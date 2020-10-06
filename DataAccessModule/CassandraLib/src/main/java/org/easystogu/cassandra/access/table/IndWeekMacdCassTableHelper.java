package org.easystogu.cassandra.access.table;

import org.easystogu.config.Constants;
import org.easystogu.db.vo.table.MacdVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndWeekMacdCassTableHelper extends CassandraIndDBHelper<MacdVO> implements InitializingBean {
	@Override
	@Value(Constants.CassandraKeySpace + "ind_week_macd")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	public void afterPropertiesSet() {
		super.setIndicatorVOClass(MacdVO.class);
		super.init();
	}
}
