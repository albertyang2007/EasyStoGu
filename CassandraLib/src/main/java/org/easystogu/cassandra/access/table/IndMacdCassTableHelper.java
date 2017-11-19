package org.easystogu.cassandra.access.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.utils.WeekdayUtil;

public class IndMacdCassTableHelper extends CassandraIndDBHelper {
	private static IndMacdCassTableHelper instance = null;

	public static IndMacdCassTableHelper getInstance() {
		if (instance == null) {
			instance = new IndMacdCassTableHelper("ind_macd", MacdVO.class);
		}
		return instance;
	}

	protected IndMacdCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
		super(tableName, indicatorVOClass);
	}

	public static void main(String[] args) {
		IndMacdCassTableHelper cable = IndMacdCassTableHelper.getInstance();
		List<MacdVO> list = new ArrayList<MacdVO>();
		for (int i = 0; i < 10; i++) {
			MacdVO vo = new MacdVO();
			vo.stockId = "000001";
			vo.date = WeekdayUtil.nextNDateString(WeekdayUtil.currentDate(), i);
			vo.dif = RandomUtils.nextDouble();
			vo.dea = RandomUtils.nextDouble();
			vo.macd = vo.dea - vo.dif;
			list.add(vo);
		}

		cable.insert(list);
		System.out.println("getAll");
		cable.getAll("000001");
		System.out.println("getSingle");
		cable.getSingle("000001", "2017-11-19");
		System.out.println("getByIdAndBetweenDate");
		cable.getByIdAndBetweenDate("000001", "2017-11-19", "2017-11-20");
		System.out.println("getByIdAndNDate");
		cable.getByIdAndLatestNDate("000001", 5);
		
		System.exit(0);
	}
}
