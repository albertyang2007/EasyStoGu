package org.easystogu.postgresql.access.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.db.vo.table.ShenXianVO;
import org.easystogu.utils.WeekdayUtil;

public class IndShenXianDBTableHelper extends PostgresqlIndDBHelper {
	private static IndShenXianDBTableHelper instance = null;

	public static IndShenXianDBTableHelper getInstance() {
		if (instance == null) {
			instance = new IndShenXianDBTableHelper("ind_shenxian", ShenXianVO.class);
		}
		return instance;
	}

	protected IndShenXianDBTableHelper(String tableNameParm, Class<? extends IndicatorVO> indicatorVOClass) {
		super(tableNameParm, indicatorVOClass);
	}

	public static void main(String[] args) {
		IndShenXianDBTableHelper cable = IndShenXianDBTableHelper.getInstance();
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
