package org.easystogu.cassandra.access.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.utils.WeekdayUtil;

public class IndWeekMacdCassTableHelper extends CassandraIndDBHelper {
	private static IndWeekMacdCassTableHelper instance = null;

	public static IndWeekMacdCassTableHelper getInstance() {
		if (instance == null) {
			instance = new IndWeekMacdCassTableHelper("ind_week_macd", MacdVO.class);
		}
		return instance;
	}

	protected IndWeekMacdCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
		super(tableName, indicatorVOClass);
	}

	public static void main(String[] args) {
		IndWeekMacdCassTableHelper cable = IndWeekMacdCassTableHelper.getInstance();
		List<MacdVO> list = new ArrayList<MacdVO>();
		for (int i = 0; i < 10; i++) {
			MacdVO vo = new MacdVO();
			vo.stockId = "100001";
			vo.date = WeekdayUtil.nextNDateString(WeekdayUtil.currentDate(), i);
			vo.dif = RandomUtils.nextDouble();
			vo.dea = RandomUtils.nextDouble();
			vo.macd = vo.dea - vo.dif;
			list.add(vo);
		}

		cable.insert(list);
		System.out.println("getAll");
		cable.getAll("100001");
		System.out.println("getSingle");
		cable.getSingle("100001", "2017-11-19");
		System.out.println("getByIdAndBetweenDate");
		cable.getByIdAndBetweenDate("100001", "2017-11-19", "2017-11-20");
		System.out.println("getByIdAndNDate");
		cable.getByIdAndLatestNDate("100001", 5);

		System.exit(0);
	}
}
