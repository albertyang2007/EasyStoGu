package org.easystogu.cassandra.access.table;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.easystogu.db.vo.table.BollVO;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.utils.WeekdayUtil;

public class IndBollCassTableHelper extends CassandraIndDBHelper {
	private static IndBollCassTableHelper instance = null;

	public static IndBollCassTableHelper getInstance() {
		if (instance == null) {
			instance = new IndBollCassTableHelper("ind_boll", BollVO.class);
		}
		return instance;
	}

	protected IndBollCassTableHelper(String tableName, Class<? extends IndicatorVO> indicatorVOClass) {
		super(tableName, indicatorVOClass);
	}

	public static void main(String[] args) {
		IndBollCassTableHelper cable = IndBollCassTableHelper.getInstance();
		List<BollVO> list = new ArrayList<BollVO>();
		for (int i = 0; i < 10; i++) {
			BollVO vo = new BollVO();
			vo.stockId = "000001";
			vo.date = WeekdayUtil.nextNDateString(WeekdayUtil.currentDate(), i);
			vo.dn = RandomUtils.nextDouble();
			vo.mb = RandomUtils.nextDouble();
			vo.up = RandomUtils.nextDouble();
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
