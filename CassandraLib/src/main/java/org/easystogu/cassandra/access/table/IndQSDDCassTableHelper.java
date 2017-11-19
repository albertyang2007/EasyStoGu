package org.easystogu.cassandra.access.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.db.vo.table.QSDDVO;
import org.easystogu.utils.WeekdayUtil;

public class IndQSDDCassTableHelper extends CassandraIndDBHelper {
	private static IndQSDDCassTableHelper instance = null;

	public static IndQSDDCassTableHelper getInstance() {
		if (instance == null) {
			instance = new IndQSDDCassTableHelper("ind_qsdd", QSDDVO.class);
		}
		return instance;
	}

	protected IndQSDDCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
		super(tableName, indicatorVOClass);
	}

	public static void main(String[] args) {
		IndQSDDCassTableHelper cable = IndQSDDCassTableHelper.getInstance();
		List<QSDDVO> list = new ArrayList<QSDDVO>();
		for (int i = 0; i < 10; i++) {
			QSDDVO vo = new QSDDVO();
			vo.stockId = "000001";
			vo.date = WeekdayUtil.nextNDateString(WeekdayUtil.currentDate(), i);
			vo.lonTerm = RandomUtils.nextDouble();
			vo.shoTerm = RandomUtils.nextDouble();
			vo.midTerm = RandomUtils.nextDouble();
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
