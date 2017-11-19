package org.easystogu.cassandra.access.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.utils.WeekdayUtil;

public class IndWeekKDJCassTableHelper extends CassandraIndDBHelper {
	private static IndWeekKDJCassTableHelper instance = null;

	public static IndWeekKDJCassTableHelper getInstance() {
		if (instance == null) {
			instance = new IndWeekKDJCassTableHelper("ind_week_kdj", KDJVO.class);
		}
		return instance;
	}

	protected IndWeekKDJCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
		super(tableName, indicatorVOClass);
	}

	public static void main(String[] args) {
		IndWeekKDJCassTableHelper cable = IndWeekKDJCassTableHelper.getInstance();
		List<KDJVO> list = new ArrayList<KDJVO>();
		for (int i = 0; i < 10; i++) {
			KDJVO vo = new KDJVO();
			vo.stockId = "100001";
			vo.date = WeekdayUtil.nextNDateString(WeekdayUtil.currentDate(), i);
			vo.k = RandomUtils.nextDouble();
			vo.d = RandomUtils.nextDouble();
			vo.j = vo.k - vo.d;
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
