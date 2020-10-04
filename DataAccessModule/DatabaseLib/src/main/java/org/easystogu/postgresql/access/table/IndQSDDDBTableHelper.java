package org.easystogu.postgresql.access.table;

import org.easystogu.db.vo.table.QSDDVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndQSDDDBTableHelper extends PostgresqlIndDBHelper<QSDDVO> implements InitializingBean{
	@Override
	@Value("ind_qsdd")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	public void afterPropertiesSet() {
		super.setIndicatorVOClass(QSDDVO.class);
		super.init();
	}
}
