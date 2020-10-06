package org.easystogu.cassandra.access.table;

import org.easystogu.config.Constants;
import org.easystogu.db.vo.table.WRVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndWRCassTableHelper extends CassandraIndDBHelper<WRVO> implements InitializingBean {
	@Override
	@Value(Constants.CassandraKeySpace + "ind_wr")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	public void afterPropertiesSet() {
		super.setIndicatorVOClass(WRVO.class);
		super.init();
	}
}
