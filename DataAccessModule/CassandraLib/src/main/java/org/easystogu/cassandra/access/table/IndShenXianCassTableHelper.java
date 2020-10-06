package org.easystogu.cassandra.access.table;

import org.easystogu.config.Constants;
import org.easystogu.db.vo.table.ShenXianVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndShenXianCassTableHelper extends CassandraIndDBHelper<ShenXianVO> implements InitializingBean {
	@Override
	@Value(Constants.CassandraKeySpace + "ind_shenxian")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}

	@Override
	public void afterPropertiesSet() {
		super.setIndicatorVOClass(ShenXianVO.class);
		super.init();
	}
}
