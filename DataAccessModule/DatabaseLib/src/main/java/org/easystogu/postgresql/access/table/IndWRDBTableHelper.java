package org.easystogu.postgresql.access.table;

import org.easystogu.db.vo.table.WRVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndWRDBTableHelper extends PostgresqlIndDBHelper<WRVO> implements InitializingBean {
	@Override
	@Value("ind_wr")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	public void afterPropertiesSet() {
		super.setIndicatorVOClass(WRVO.class);
		super.init();
	}
}
