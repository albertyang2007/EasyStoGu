package org.easystogu.cassandra.access.table;

import org.easystogu.config.Constants;
import org.easystogu.db.vo.table.BollVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndBollCassTableHelper extends CassandraIndDBHelper<BollVO> implements InitializingBean {
	@Override
	@Value(Constants.CassandraKeySpace + "ind_boll")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	public void afterPropertiesSet() {
		super.setIndicatorVOClass(BollVO.class);
		super.init();
	}
}
