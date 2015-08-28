package org.easystogu.analyse.util;

import java.util.List;

import org.easystogu.db.table.StockSuperVO;

public class IndProcessHelper {
    public static void process(List<StockSuperVO> overDayList, List<StockSuperVO> overWeekList) {
        // count and update all ind data
        // day
        IndCrossCheckingHelper.macdCross(overDayList);
        IndCrossCheckingHelper.kdjCross(overDayList);
        IndCrossCheckingHelper.rsvCross(overDayList);
        IndCrossCheckingHelper.bollXueShi2DnCross(overDayList);
        IndCrossCheckingHelper.mai1Mai2Cross(overDayList);
        IndCrossCheckingHelper.shenXianCross12(overDayList);
        IndCrossCheckingHelper.yiMengBSCross(overDayList);
        IndCrossCheckingHelper.zhuliJinChuCross(overDayList);
        VolumeCheckingHelper.volumeIncreasePuls(overDayList);
        VolumeCheckingHelper.avgVolume5(overDayList);
        PriceCheckingHelper.priceHigherThanNday(overDayList, 15);
        PriceCheckingHelper.setLastClosePrice(overDayList);
        PriceCheckingHelper.countAvgMA(overDayList);
        // week
        IndCrossCheckingHelper.macdCross(overWeekList);
        IndCrossCheckingHelper.kdjCross(overWeekList);
        IndCrossCheckingHelper.rsvCross(overWeekList);
        IndCrossCheckingHelper.mai1Mai2Cross(overWeekList);
        IndCrossCheckingHelper.shenXianCross12(overWeekList);
        IndCrossCheckingHelper.yiMengBSCross(overWeekList);
        PriceCheckingHelper.setLastClosePrice(overWeekList);
    }
}
