package org.easystogu.analyse.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.StockSuperVO;

public class IndProcessHelper {
  private static Map<IndKey, ValueWrapper> dayListMap =
      new java.util.concurrent.ConcurrentHashMap<IndKey, ValueWrapper>();
  private static Map<IndKey, ValueWrapper> weekListMap =
      new java.util.concurrent.ConcurrentHashMap<IndKey, ValueWrapper>();

  public static void processDayList(List<StockSuperVO> overDayList) {
    // count and update all ind data
    // day
//    IndKey indKey = new IndKey(overDayList);
//
//    ValueWrapper valueObj = dayListMap.get(indKey);
//    if (valueObj != null && valueObj.isYangEnough()) {
//      overDayList = valueObj.overList;
//      return;
//    }

    IndCrossCheckingHelper.macdCross(overDayList);
    IndCrossCheckingHelper.kdjCross(overDayList);
    IndCrossCheckingHelper.rsvCross(overDayList);
    IndCrossCheckingHelper.shenXianCross12(overDayList);
    IndCrossCheckingHelper.qsddCross(overDayList);
    // IndCrossCheckingHelper.yiMengBSCross(overDayList);
    IndCountHelper.countAvgWR(overDayList);
    VolumeCheckingHelper.volumeIncreasePuls(overDayList);
    VolumeCheckingHelper.avgVolume5(overDayList);
    PriceCheckingHelper.priceHigherThanNday(overDayList, 15);
    PriceCheckingHelper.setLastClosePrice(overDayList);
    PriceCheckingHelper.countAvgMA(overDayList);

//    dayListMap.put(indKey, new ValueWrapper(overDayList));
  }

  public static void processWeekList(List<StockSuperVO> overWeekList) {
    // count and update all ind data
    // week
//    IndKey indKey = new IndKey(overWeekList);
//
//    ValueWrapper valueObj = dayListMap.get(indKey);
//    if (valueObj != null && valueObj.isYangEnough()) {
//      overWeekList = valueObj.overList;
//      return;
//    }

    PriceCheckingHelper.setLastClosePrice(overWeekList);
    IndCrossCheckingHelper.macdCross(overWeekList);
    IndCrossCheckingHelper.kdjCross(overWeekList);
    // IndCrossCheckingHelper.qsddCross(overWeekList);
    // IndCrossCheckingHelper.shenXianCross12(overWeekList);

//    weekListMap.put(indKey, new ValueWrapper(overWeekList));
  }
}


class IndKey {
  private StockPriceVO startPriceVO;
  private StockPriceVO endPriceVO;
  private int length;

  public IndKey(List<StockSuperVO> overList) {
    this.startPriceVO = overList.get(0).priceVO;
    this.endPriceVO = overList.get(overList.size() - 1).priceVO;
    this.length = overList.size();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((endPriceVO == null) ? 0 : endPriceVO.hashCode());
    result = prime * result + length;
    result = prime * result + ((startPriceVO == null) ? 0 : startPriceVO.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    IndKey other = (IndKey) obj;
    if (endPriceVO == null) {
      if (other.endPriceVO != null)
        return false;
    } else if (!endPriceVO.equals(other.endPriceVO))
      return false;
    if (length != other.length)
      return false;
    if (startPriceVO == null) {
      if (other.startPriceVO != null)
        return false;
    } else if (!startPriceVO.equals(other.startPriceVO))
      return false;
    return true;
  }
}


class ValueWrapper {
  long putTimeStamp;
  List<StockSuperVO> overList;

  public ValueWrapper(List<StockSuperVO> overList) {
    this.putTimeStamp = System.currentTimeMillis();
    this.overList = new ArrayList<StockSuperVO>(overList);
  }

  public boolean isYangEnough() {
    // if the time is still within 1 hours
    if ((System.currentTimeMillis() - this.putTimeStamp) <= (1000 * 3600)) {
      return true;
    }
    return false;
  }
}
