package org.easystogu.postgresql.access.table;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndShenXianDBTableHelper extends PostgresqlIndDBHelper implements InitializingBean{
	@Override
	@Value("ind_shenxian")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	public void afterPropertiesSet() {
		super.setIndicatorVOClass(org.easystogu.db.vo.table.ShenXianVO.class);
		super.init();
	}
}
