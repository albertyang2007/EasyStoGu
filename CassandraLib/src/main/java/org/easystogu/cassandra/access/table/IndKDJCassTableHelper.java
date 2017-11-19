package org.easystogu.cassandra.access.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.utils.WeekdayUtil;

public class IndKDJCassTableHelper extends CassandraIndDBHelper {
	private static IndKDJCassTableHelper instance = null;

	public static IndKDJCassTableHelper getInstance() {
		if (instance == null) {
			instance = new IndKDJCassTableHelper("ind_kdj", KDJVO.class);
		}
		return instance;
	}

	protected IndKDJCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
		super(tableName, indicatorVOClass);
	}

	public static void main(String[] args) {
		IndKDJCassTableHelper cable = IndKDJCassTableHelper.getInstance();
		List<KDJVO> list = new ArrayList<KDJVO>();
		for (int i = 0; i < 10; i++) {
			KDJVO vo = new KDJVO();
			vo.stockId = "000001";
			vo.date = WeekdayUtil.nextNDateString(WeekdayUtil.currentDate(), i);
			vo.k = RandomUtils.nextDouble();
			vo.d = RandomUtils.nextDouble();
			vo.j = vo.k - vo.d;
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
