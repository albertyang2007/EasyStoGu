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
    public void doAnalyseTiaoKongGaoKaiDay1HuiBu(String stockId, int[] difRange) {
        List<StockPriceVO> spList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

        int[] statistics = { 0, 0, 0 };
        for (int index = 1; index < spList.size() - 2; index++) {
            StockPriceVO pre1VO = spList.get(index - 1);
            StockPriceVO curVO = spList.get(index);
            StockPriceVO next1VO = spList.get(index + 1);
            StockPriceVO next2VO = spList.get(index + 2);
            //高开n个点
            if (curVO.open > pre1VO.high) {
                double dif = 10 * (curVO.open - pre1VO.close) / (pre1VO.close * 0.1);
                if (dif >= difRange[0] & dif <= difRange[1]) {
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
            System.out.println(stockId + "高开: " + difRange[0] + "~" + difRange[1] + " 当天回补缺口概率="
                    + formatNumber((double) statistics[1] / (double) statistics[0]) + " 第二三天回补缺口概率="
                    + formatNumber((double) statistics[2] / (double) statistics[0]) + ", 总样本数=" + statistics[0]);
        }
    }

    //跳空低开，当天回补缺口
    public void doAnalyseTiaoKongDiKaiDay1HuiBu(String stockId, int[] difRange) {
        List<StockPriceVO> spList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

        int[] statistics = { 0, 0, 0 };
        for (int index = 1; index < spList.size() - 2; index++) {
            StockPriceVO pre1VO = spList.get(index - 1);
            StockPriceVO curVO = spList.get(index);
            StockPriceVO next1VO = spList.get(index + 1);
            StockPriceVO next2VO = spList.get(index + 2);
            //低开n个点
            if (curVO.open < pre1VO.low) {
                double dif = 10 * (pre1VO.close - curVO.open) / (pre1VO.close * 0.1);
                if (dif >= difRange[0] & dif <= difRange[1]) {
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
            System.out.println(stockId + "低开: " + difRange[0] + "~" + difRange[1] + " 当天回补缺口概率="
                    + formatNumber((double) statistics[1] / (double) statistics[0]) + " 第二三天回补缺口概率="
                    + formatNumber((double) statistics[2] / (double) statistics[0]) + ", 总样本数=" + statistics[0]);
        }
    }

    private String formatNumber(double d) {
        return String.format("%.2f", d);
    }

    public static void main(String[] args) {
        StockBehaviorStatistics ins = new StockBehaviorStatistics();
        String stockId = "601318";
        int[][] difRanges = new int[][] { { 1, 3 }, { 3, 5 }, { 5, 8 } };
        for (int[] difRange : difRanges) {
            ins.doAnalyseTiaoKongGaoKaiDay1HuiBu(stockId, difRange);
        }
        for (int[] difRange : difRanges) {
            ins.doAnalyseTiaoKongDiKaiDay1HuiBu(stockId, difRange);
        }
    }
}
