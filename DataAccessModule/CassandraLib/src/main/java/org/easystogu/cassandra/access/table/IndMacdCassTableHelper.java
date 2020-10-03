package org.easystogu.cassandra.access.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.easystogu.config.Constants;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.utils.WeekdayUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndMacdCassTableHelper extends CassandraIndDBHelper {
//	private static IndMacdCassTableHelper instance = null;
//
//	public static IndMacdCassTableHelper getInstance() {
//		if (instance == null) {
//			instance = new IndMacdCassTableHelper("ind_macd", MacdVO.class);
//		}
//		return instance;
//	}
//
//	protected IndMacdCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
//		super(tableName, indicatorVOClass);
//	}
	
	@Override
	@Value(Constants.CassandraKeySpace + "ind_macd")
	public void setTableName(String tableName) {
		super.setTableName(tableName);
	}
	
	@Override
	@Value("org.easystogu.db.vo.table.MacdVO")
	public void setIndicatorVOClass(String indicatorVOClass) {
		super.setIndicatorVOClass(indicatorVOClass);
	}
}
