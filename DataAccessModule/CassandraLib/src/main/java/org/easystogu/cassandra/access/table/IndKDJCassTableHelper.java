package org.easystogu.cassandra.access.table;

import org.easystogu.config.Constants;
import org.easystogu.db.vo.table.KDJVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndKDJCassTableHelper extends CassandraIndDBHelper<KDJVO> implements InitializingBean {
	@Override
	@Value(Constants.CassandraKeySpace + "ind_kdj")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	public void afterPropertiesSet() {
		super.setIndicatorVOClass(KDJVO.class);
		super.init();
	}
}
