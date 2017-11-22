package org.easystogu.cassandra.access.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.db.vo.table.WRVO;
import org.easystogu.utils.WeekdayUtil;

public class IndWRCassTableHelper extends CassandraIndDBHelper {
	private static IndWRCassTableHelper instance = null;

	public static IndWRCassTableHelper getInstance() {
		if (instance == null) {
			instance = new IndWRCassTableHelper("ind_wr", WRVO.class);
		}
		return instance;
	}

	protected IndWRCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
		super(tableName, indicatorVOClass);
	}

	public static void main(String[] args) {
		IndWRCassTableHelper cable = IndWRCassTableHelper.getInstance();
		List<WRVO> list = new ArrayList<WRVO>();
		for (int i = 0; i < 10; i++) {
			WRVO vo = new WRVO();
			vo.stockId = "100001";
			vo.date = WeekdayUtil.nextNDateString(WeekdayUtil.currentDate(), i);
			vo.lonTerm = RandomUtils.nextDouble();
			vo.shoTerm = RandomUtils.nextDouble();
			vo.midTerm = RandomUtils.nextDouble();
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
