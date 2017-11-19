package org.easystogu.postgresql.access.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.utils.WeekdayUtil;

public class IndWeekKDJDBTableHelper extends PostgresqlIndDBHelper {
	private static IndWeekKDJDBTableHelper instance = null;

	public static IndWeekKDJDBTableHelper getInstance() {
		if (instance == null) {
			instance = new IndWeekKDJDBTableHelper("ind_week_kdj", KDJVO.class);
		}
		return instance;
	}

	protected IndWeekKDJDBTableHelper(String tableNameParm, Class<? extends IndicatorVO> indicatorVOClass) {
		super(tableNameParm, indicatorVOClass);
	}

	public static void main(String[] args) {
		IndWeekKDJDBTableHelper cable = IndWeekKDJDBTableHelper.getInstance();
		List<KDJVO> list = new ArrayList<KDJVO>();
		for (int i = 0; i < 10; i++) {
			KDJVO vo = new KDJVO();
			vo.stockId = "100001";
			vo.date = WeekdayUtil.nextNDateString(WeekdayUtil.currentDate(), i);
			vo.k = RandomUtils.nextDouble();
			vo.d = RandomUtils.nextDouble();
			vo.j = RandomUtils.nextDouble();
			list.add(vo);
		}

		System.out.println("delete");
		cable.delete("100001", WeekdayUtil.currentDate());
		System.out.println("delete");
		cable.delete("100001");
		System.out.println("insert");
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
