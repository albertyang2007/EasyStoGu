package org.easystogu.analyse.util;

import java.util.List;

import org.easystogu.db.vo.table.StockSuperVO;

public class PriceCheckingHelper {

    // 判断是否突破平台(当日高于15日均价)
    // the overList is order by date
    public static void priceHigherThanNday(List<StockSuperVO> overList, int day) {
        int length = overList.size();
        StockSuperVO currentSuperVO = overList.get(length - 1);
        if (day > length) {
            System.out.println("There is not enough data to count the priceHigherThanNday for "
                    + currentSuperVO.priceVO.stockId);
            return;
        }
        double currentPrice = currentSuperVO.priceVO.close;
        for (int index = length - 2; index >= 0; index--) {
            if (currentPrice < overList.get(index).priceVO.high) {
                currentSuperVO.priceHigherThanNday = false;
                return;
            }
        }

        currentSuperVO.priceHigherThanNday = true;
    }

    // set lastClose price
    // the overList is order by date
    public static void setLastClosePrice(List<StockSuperVO> overList) {
        for (int index = overList.size() - 1; index > 0; index--) {
            StockSuperVO superVO = overList.get(index);
            StockSuperVO preSuperVO = overList.get(index - 1);
            superVO.priceVO.lastClose = preSuperVO.priceVO.close;
        }
    }

    // count the avg MA5 and MA10, MA20
    // the overList is order by date
    public static void countAvgMA(List<StockSuperVO> overList) {
        for (int index = overList.size() - 1; index > 0; index--) {
            StockSuperVO superVO = overList.get(index);
            if (((index - 4) >= 0) && ((index + 1) <= overList.size())) {
                superVO.avgMA5 = avgClosePrice(overList.subList(index - 4, index + 1));
            }
            if (((index - 9) >= 0) && ((index + 1) <= overList.size())) {
                superVO.avgMA10 = avgClosePrice(overList.subList(index - 9, index + 1));
            }
            if (((index - 18) >= 0) && ((index + 1) <= overList.size())) {
                superVO.avgMA19 = avgClosePrice(overList.subList(index - 18, index + 1));
            }
            if (((index - 19) >= 0) && ((index + 1) <= overList.size())) {
                superVO.avgMA20 = avgClosePrice(overList.subList(index - 19, index + 1));
            }
            if (((index - 29) >= 0) && ((index + 1) <= overList.size())) {
                superVO.avgMA30 = avgClosePrice(overList.subList(index - 29, index + 1));
            }
            if (((index - 42) >= 0) && ((index + 1) <= overList.size())) {
                superVO.avgMA43 = avgClosePrice(overList.subList(index - 42, index + 1));
            }
            if (((index - 59) >= 0) && ((index + 1) <= overList.size())) {
                superVO.avgMA60 = avgClosePrice(overList.subList(index - 59, index + 1));
            }
            if (((index - 85) >= 0) && ((index + 1) <= overList.size())) {
                superVO.avgMA86 = avgClosePrice(overList.subList(index - 85, index + 1));
            }
            if (((index - 119) >= 0) && ((index + 1) <= overList.size())) {
                superVO.avgMA120 = avgClosePrice(overList.subList(index - 119, index + 1));
            }
        }
    }

    public static double avgClosePrice(List<StockSuperVO> subList) {
        double avg = 0.0;
        for (StockSuperVO vo : subList) {
            avg += vo.priceVO.close;
        }
        if (subList.size() > 0) {
            return avg / subList.size();
        }
        return 0.0;
    }
    
    public static void countLLV43(List<StockSuperVO> subList){
    	
    }

    // platform checking 平台整理和突破
    public static void platformChecking(List<StockSuperVO> overList) {
        // TBD
    }
}
