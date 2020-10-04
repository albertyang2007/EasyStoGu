package org.easystogu.postgresql.access.table;

import org.easystogu.db.vo.table.MacdVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndMacdDBTableHelper extends PostgresqlIndDBHelper<MacdVO> implements InitializingBean{
	@Override
	@Value("ind_macd")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	public void afterPropertiesSet() {
		super.setIndicatorVOClass(MacdVO.class);
		super.init();
	}
}
