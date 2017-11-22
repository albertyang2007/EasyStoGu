package org.easystogu.postgresql.access.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.db.vo.table.QSDDVO;
import org.easystogu.utils.WeekdayUtil;

public class IndQSDDDBTableHelper extends PostgresqlIndDBHelper {
	private static IndQSDDDBTableHelper instance = null;

	public static IndQSDDDBTableHelper getInstance() {
		if (instance == null) {
			instance = new IndQSDDDBTableHelper("ind_qsdd", QSDDVO.class);
		}
		return instance;
	}

	protected IndQSDDDBTableHelper(String tableNameParm, Class<? extends IndicatorVO> indicatorVOClass) {
		super(tableNameParm, indicatorVOClass);
	}

	public static void main(String[] args) {
		IndQSDDDBTableHelper cable = IndQSDDDBTableHelper.getInstance();
		List<QSDDVO> list = new ArrayList<QSDDVO>();
		for (int i = 0; i < 10; i++) {
			QSDDVO vo = new QSDDVO();
			vo.stockId = "100001";
			vo.date = WeekdayUtil.nextNDateString(WeekdayUtil.currentDate(), i);
			vo.lonTerm = RandomUtils.nextDouble();
			vo.shoTerm = RandomUtils.nextDouble();
			vo.midTerm = RandomUtils.nextDouble();
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
