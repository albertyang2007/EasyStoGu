package org.easystogu.analyse.util;

import java.util.List;

import org.easystogu.db.table.StockSuperVO;

public class IndProcessHelper {
	public static void processDayList(List<StockSuperVO> overDayList) {
		// count and update all ind data
		// day
		IndCrossCheckingHelper.macdCross(overDayList);
		IndCrossCheckingHelper.kdjCross(overDayList);
		IndCrossCheckingHelper.rsvCross(overDayList);
		IndCrossCheckingHelper.shenXianCross12(overDayList);
		IndCrossCheckingHelper.qsddCross(overDayList);
		//IndCrossCheckingHelper.yiMengBSCross(overDayList);
		IndCountHelper.countAvgWR(overDayList);
		VolumeCheckingHelper.volumeIncreasePuls(overDayList);
		VolumeCheckingHelper.avgVolume5(overDayList);
		PriceCheckingHelper.priceHigherThanNday(overDayList, 15);
		PriceCheckingHelper.setLastClosePrice(overDayList);
		PriceCheckingHelper.countAvgMA(overDayList);
	}

	public static void processWeekList(List<StockSuperVO> overWeekList) {
		// count and update all ind data
		// week
		PriceCheckingHelper.setLastClosePrice(overWeekList);
		IndCrossCheckingHelper.macdCross(overWeekList);
		IndCrossCheckingHelper.kdjCross(overWeekList);
		//IndCrossCheckingHelper.qsddCross(overWeekList);
		//IndCrossCheckingHelper.shenXianCross12(overWeekList);
	}
}
