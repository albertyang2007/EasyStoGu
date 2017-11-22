package org.easystogu.cassandra.access.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.db.vo.table.ShenXianVO;
import org.easystogu.utils.WeekdayUtil;

public class IndShenXianCassTableHelper extends CassandraIndDBHelper {
	private static IndShenXianCassTableHelper instance = null;

	public static IndShenXianCassTableHelper getInstance() {
		if (instance == null) {
			instance = new IndShenXianCassTableHelper("ind_shenxian", ShenXianVO.class);
		}
		return instance;
	}

	protected IndShenXianCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
		super(tableName, indicatorVOClass);
	}

	public static void main(String[] args) {
		IndShenXianCassTableHelper cable = IndShenXianCassTableHelper.getInstance();
		List<ShenXianVO> list = new ArrayList<ShenXianVO>();
		for (int i = 0; i < 10; i++) {
			ShenXianVO vo = new ShenXianVO();
			vo.stockId = "100001";
			vo.date = WeekdayUtil.nextNDateString(WeekdayUtil.currentDate(), i);
			vo.h1 = RandomUtils.nextDouble();
			vo.h2 = RandomUtils.nextDouble();
			vo.h3 = RandomUtils.nextDouble();
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
