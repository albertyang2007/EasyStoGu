package org.easystogu.postgresql.access.table;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndWeekKDJDBTableHelper extends PostgresqlIndDBHelper implements InitializingBean{
	@Override
	@Value("ind_week_kdj")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	public void afterPropertiesSet() {
		super.setIndicatorVOClass(org.easystogu.db.vo.table.KDJVO.class);
		super.init();
	}
}
