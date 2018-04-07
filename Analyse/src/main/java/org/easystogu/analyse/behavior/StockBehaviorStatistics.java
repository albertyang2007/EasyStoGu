package org.easystogu.analyse.behavior;

import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockBehaviorStatisticsTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;

public class StockBehaviorStatistics {
    private StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
    private StockBehaviorStatisticsTableHelper stockBehaviorStatisticsTable = StockBehaviorStatisticsTableHelper
            .getInstance();

    //跳空高开，当天回补缺口
    public void doAnalyseTiaoKongGaoKaiDay1HuiBu(String stockId, float baseDif) {
        List<StockPriceVO> spList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

        int[] statistics = { 0, 0, 0 };
        for (int index = 1; index < spList.size() - 2; index++) {
            StockPriceVO pre1VO = spList.get(index - 1);
            StockPriceVO curVO = spList.get(index);
            StockPriceVO next1VO = spList.get(index + 1);
            StockPriceVO next2VO = spList.get(index + 2);
            //高开n个点
            if (curVO.open > pre1VO.high) {
                double dif = (curVO.open - pre1VO.close) / (pre1VO.close * 0.1);
                if (dif >= baseDif & dif <= baseDif + 0.1) {
                    statistics[0]++;
                    if (curVO.low <= pre1VO.high) {
                        //当天回补
                        statistics[1]++;
                        //System.out.println("当天回补缺口: pre1VO=" + pre1VO + ", curVO=" + curVO);
                    } else if (next1VO.low <= pre1VO.high || next2VO.low <= pre1VO.high) {
                        //第二天 或者第三天回补
                        statistics[2]++;
                    } else {
                        //当天不回补，高开高走
                        //System.out.println("当天高开高走: pre1VO=" + pre1VO + ", curVO=" + curVO);
                    }
                }
            }
        }

        if (statistics[0] > 0) {
            System.out.println(baseDif + " 当天回补缺口概率=" + ((double) statistics[1] / (double) statistics[0])
                    + " 第二三天回补缺口概率=" + ((double) statistics[2] / (double) statistics[0]) + ", 总样本数=" + statistics[0]);
        } else {
            System.out.println(baseDif + " 没有数据");
        }
    }

    //跳空低开，当天回补缺口
    public void doAnalyseTiaoKongDiKaiDay1HuiBu(String stockId, float baseDif) {
        List<StockPriceVO> spList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

        int[] statistics = { 0, 0, 0 };
        for (int index = 1; index < spList.size() - 2; index++) {
            StockPriceVO pre1VO = spList.get(index - 1);
            StockPriceVO curVO = spList.get(index);
            StockPriceVO next1VO = spList.get(index + 1);
            StockPriceVO next2VO = spList.get(index + 2);
            //低开n个点
            if (curVO.open < pre1VO.low) {
                double dif = (pre1VO.close - curVO.open) / (pre1VO.close * 0.1);
                if (dif >= baseDif & dif <= baseDif + 0.1) {
                    statistics[0]++;
                    if (curVO.high >= pre1VO.low) {
                        //当天回补
                        statistics[1]++;
                        //System.out.println("当天回补缺口: pre1VO=" + pre1VO + ", curVO=" + curVO);
                    } else if (next1VO.high >= pre1VO.low || next2VO.high >= pre1VO.low) {
                        //第二天 或者第三天回补
                        statistics[2]++;
                    } else {
                        //当天不回补，高开高走
                        //System.out.println("当天高开高走: pre1VO=" + pre1VO + ", curVO=" + curVO);
                    }
                }
            }
        }

        if (statistics[0] > 0) {
            System.out.println(baseDif + " 当天回补缺口概率=" + ((double) statistics[1] / (double) statistics[0])
                    + " 第二三天回补缺口概率=" + ((double) statistics[2] / (double) statistics[0]) + ", 总样本数=" + statistics[0]);
        } else {
            System.out.println(baseDif + " 没有数据");
        }
    }

    public static void main(String[] args) {
        StockBehaviorStatistics ins = new StockBehaviorStatistics();
        for (float dif = 0.1f; dif < 1.0; dif += 0.1)
            ins.doAnalyseTiaoKongGaoKaiDay1HuiBu("601318", dif);
        for (float dif = 0.1f; dif < 1.0; dif += 0.1)
            ins.doAnalyseTiaoKongDiKaiDay1HuiBu("601318", dif);
    }
}
