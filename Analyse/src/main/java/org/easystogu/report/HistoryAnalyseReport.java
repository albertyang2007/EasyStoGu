package org.easystogu.report;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.analyse.CombineAnalyseHelper;
import org.easystogu.analyse.util.IndCrossCheckingHelper;
import org.easystogu.analyse.util.PriceCheckingHelper;
import org.easystogu.analyse.util.VolumeCheckingHelper;
import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.CheckPointHistoryAnalyseTableHelper;
import org.easystogu.db.access.CheckPointHistorySelectionTableHelper;
import org.easystogu.db.access.StockSuperVOHelper;
import org.easystogu.db.access.WeekStockSuperVOHelper;
import org.easystogu.db.table.CheckPointHistoryAnalyseVO;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.utils.CrossType;

public class HistoryAnalyseReport {

    private StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
    private CheckPointHistorySelectionTableHelper historyReportTableHelper = CheckPointHistorySelectionTableHelper
            .getInstance();
    private WeekStockSuperVOHelper weekStockOverAllHelper = new WeekStockSuperVOHelper();
    protected CombineAnalyseHelper combineAanalyserHelper = new CombineAnalyseHelper();
    private StockSuperVOHelper stockOverAllHelper = new StockSuperVOHelper();
    private CheckPointHistoryAnalyseTableHelper cpHistoryAnalyse = CheckPointHistoryAnalyseTableHelper.getInstance();

    public List<HistoryReportDetailsVO> doAnalyseReport(String stockId, List<DailyCombineCheckPoint> checkPointList) {
        List<HistoryReportDetailsVO> reportList = new ArrayList<HistoryReportDetailsVO>();
        for (DailyCombineCheckPoint checkPoint : checkPointList) {
            reportList.addAll(this.doAnalyseReport(stockId, checkPoint));
        }
        return reportList;
    }

    public List<HistoryReportDetailsVO> doAnalyseReport(String stockId, DailyCombineCheckPoint checkPoint) {

        List<HistoryReportDetailsVO> historyReportList = new ArrayList<HistoryReportDetailsVO>();

        List<StockSuperVO> overDayList = stockOverAllHelper.getAllStockSuperVO(stockId);
        List<StockSuperVO> overWeekList = weekStockOverAllHelper.getAllStockSuperVO(stockId);

        if (overDayList.size() == 0) {
            return historyReportList;
        }

        // count and update all ind data
        // day
        IndCrossCheckingHelper.macdCross(overDayList);
        IndCrossCheckingHelper.kdjCross(overDayList);
        IndCrossCheckingHelper.rsvCross(overDayList);
        VolumeCheckingHelper.volumeIncreasePuls(overDayList);
        VolumeCheckingHelper.avgVolume5(overDayList);
        PriceCheckingHelper.priceHigherThanNday(overDayList, 15);
        PriceCheckingHelper.setLastClosePrice(overDayList);
        PriceCheckingHelper.countAvgMA(overDayList);
        // week
        IndCrossCheckingHelper.macdCross(overWeekList);
        IndCrossCheckingHelper.kdjCross(overWeekList);

        HistoryReportDetailsVO reportVO = null;
        for (int index = 120; index < (overDayList.size() - 1); index++) {
            StockSuperVO superVO = overDayList.get(index);
            // buy point
            if (combineAanalyserHelper.isConditionSatisfy(checkPoint, overDayList.subList(index - 120, index + 1),
                    overWeekList)) {
                reportVO = new HistoryReportDetailsVO();
                reportVO.setBuyPriceVO(superVO.priceVO);
                continue;
            }

            // sell point (MACD dead or KDJ dead point)
            if ((superVO.kdjCorssType == CrossType.DEAD) || (superVO.macdCorssType == CrossType.DEAD)) {
                if ((reportVO != null) && (reportVO.buyPriceVO != null) && (reportVO.sellPriceVO == null)) {
                    reportVO.setSelPriceVO(superVO.priceVO);
                    historyReportList.add(reportVO);
                    reportVO = null;
                }
            }
        }
        return historyReportList;
    }

    public void UnitTest1() {
        // kdj金叉，三值均在40-50之间。macd已经为正。其他条件跟2一样。例子格力电器20141124
        List<DailyCombineCheckPoint> checkPointList = new ArrayList<DailyCombineCheckPoint>();
        checkPointList.add(DailyCombineCheckPoint.KDJ_Gordon_3_days_Red);
        String stockId = "000651";
        List<HistoryReportDetailsVO> historyReportList = this.doAnalyseReport(stockId, checkPointList);
        System.out.println("\nUnit1 history report for " + DailyCombineCheckPoint.KDJ_Gordon_3_days_Red + " size="
                + historyReportList.size());
        for (HistoryReportDetailsVO reportVO : historyReportList) {
            if (reportVO.sellPriceVO != null) {
                reportVO.countData();
                System.out.println(reportVO);
            }
        }
    }

    public void UnitTest2() {
        // kdj金叉，三值在70-80之间，macd三值为正，k线大阳，成交量大于前一日。之前三日均绿，是洗盘结束。例子格力地产20150129
        List<DailyCombineCheckPoint> checkPointList = new ArrayList<DailyCombineCheckPoint>();
        checkPointList.add(DailyCombineCheckPoint.KDJ_Gordon_Pre_3_Days_Green);
        String stockId = "600185";
        List<HistoryReportDetailsVO> historyReportList = this.doAnalyseReport(stockId, checkPointList);
        System.out.println("\nUnit2 history report for " + DailyCombineCheckPoint.KDJ_Gordon_Pre_3_Days_Green
                + " size=" + historyReportList.size());
        for (HistoryReportDetailsVO reportVO : historyReportList) {
            if (reportVO.sellPriceVO != null) {
                reportVO.countData();
                System.out.println(reportVO);
            }
        }
    }

    public void UnitTest3() {
        // kdj，macd双金叉，kdj值均在50左右。三日均为红k线，成交量一日比一日大，收盘价逐步提高。例子美尔雅20150122
        List<DailyCombineCheckPoint> checkPointList = new ArrayList<DailyCombineCheckPoint>();
        checkPointList.add(DailyCombineCheckPoint.MACD_KDJ_Gordon_3_Days_Red);
        String stockId = "600107";
        List<HistoryReportDetailsVO> historyReportList = this.doAnalyseReport(stockId, checkPointList);
        System.out.println("\nUnit3 history report for " + DailyCombineCheckPoint.MACD_KDJ_Gordon_3_Days_Red + " size="
                + historyReportList.size());
        for (HistoryReportDetailsVO reportVO : historyReportList) {
            if (reportVO.sellPriceVO != null) {
                reportVO.countData();
                System.out.println(reportVO);
            }
        }
    }

    public void UnitTest4() {
        // kdj金叉，成交量一日比一日大，金叉前两日红，当日绿，但是收盘价并不低于前一日，说明金叉当日是洗盘继续上行 .
        // 例子石基信息20150204
        List<DailyCombineCheckPoint> checkPointList = new ArrayList<DailyCombineCheckPoint>();
        checkPointList.add(DailyCombineCheckPoint.KDJ_Green_Gordon_Pre_2_Days_Red);
        String stockId = "002153";
        List<HistoryReportDetailsVO> historyReportList = this.doAnalyseReport(stockId, checkPointList);
        System.out.println("\nUnit4 history report for " + DailyCombineCheckPoint.KDJ_Green_Gordon_Pre_2_Days_Red
                + " size=" + historyReportList.size());
        for (HistoryReportDetailsVO reportVO : historyReportList) {
            if (reportVO.sellPriceVO != null) {
                reportVO.countData();
                System.out.println(reportVO);
            }
        }
    }

    public void UnitTestForSpecifyStockId() {
        // make sure UnitTest1~4 are correct, can select the stockId
        this.UnitTest1();
        this.UnitTest2();
        this.UnitTest3();
        this.UnitTest4();
    }

    public void searchAllStockIdAccordingToCheckPoint(DailyCombineCheckPoint checkPoint) {
        double[] earnPercent = new double[3];
        long holdDays = 0;
        long totalCount = 0;
        int totalHighCount = 0;
        int totalLowCount = 0;
        List<String> stockIds = stockConfig.getAllStockId();

        System.out.println("\n===================" + checkPoint + "  earn between (25, 50)%====================");
        for (String stockId : stockIds) {
            List<HistoryReportDetailsVO> historyReportList = this.doAnalyseReport(stockId, checkPoint);
            for (HistoryReportDetailsVO reportVO : historyReportList) {
                if (reportVO.sellPriceVO != null) {
                    reportVO.countData();
                    // print the high earn percent if larger than 25%
                    if ((reportVO.earnPercent[1] >= 50.0) && (reportVO.earnPercent[0] >= 25.0)) {
                        totalHighCount++;
                        //System.out.println("High earn: " + reportVO);
                        // save the high earnPercent case into DB
                        //historyReportTableHelper.insert(reportVO.convertToHistoryReportVO(checkPoint.toString()));
                    }else if((reportVO.earnPercent[1] <= -10.0) || (reportVO.earnPercent[0] <= -10.0)){
                    	totalLowCount++;
                    	//System.out.println("Low  earn: " + reportVO);
                    }
                    totalCount++;
                    earnPercent[0] += reportVO.earnPercent[0];
                    earnPercent[1] += reportVO.earnPercent[1];
                    earnPercent[2] += reportVO.earnPercent[2];

                    holdDays += reportVO.holdDays;
                } else {
                    //sell point is null, means those event is happen in those days, kdj dead point is not seen yet
                    System.out.println(reportVO.buyPriceVO.stockId + " at " + reportVO.buyPriceVO.date
                            + " still not KDJ dead point yet!");
                }
            }
        }

        if (totalCount == 0) {
            totalCount = 1;
        }

        System.out.println("Total satisfy: " + totalCount + "\t earnPercent[close]=" + (earnPercent[0] / totalCount)
                + "\t earnPercent[high]=" + (earnPercent[1] / totalCount) + "\t earnPercent[low]="
                + (earnPercent[2] / totalCount));
        System.out.println("Avg hold stock days: " + (holdDays / totalCount));
        System.out.println("Total high earn between (25, 50): " + totalHighCount);
        System.out.println("Total low  earn between (10, 10): " + totalLowCount);

        CheckPointHistoryAnalyseVO vo = new CheckPointHistoryAnalyseVO();
        vo.setCheckPoint(checkPoint.toString());
        vo.setTotalSatisfy(totalCount);
        vo.setCloseEarnPercent(earnPercent[0] / totalCount);
        vo.setHighEarnPercent(earnPercent[1] / totalCount);
        vo.setLowEarnPercent(earnPercent[2] / totalCount);
        vo.setAvgHoldDays(holdDays / totalCount);
        vo.setTotalHighEarn(totalHighCount);

        //cpHistoryAnalyse.insert(vo);

    }

    public void emptyTableByCheckPoint(String checkPoint) {
        this.historyReportTableHelper.deleteByCheckPoint(checkPoint);
    }

    public static void main(String[] args) {
        HistoryAnalyseReport reporter = new HistoryAnalyseReport();

        for (DailyCombineCheckPoint checkPoint : DailyCombineCheckPoint.values()) {
            //if(checkPoint.getEarnPercent()>=7.5)
            //System.out.println(checkPoint);
            reporter.emptyTableByCheckPoint(checkPoint.toString());
            reporter.searchAllStockIdAccordingToCheckPoint(checkPoint);
        }

        //reporter.searchAllStockIdAccordingToCheckPoint(DailyCombineCheckPoint.Phase2_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_RSV_KDJ_Gordon);
        //reporter.searchAllStockIdAccordingToCheckPoint(DailyCombineCheckPoint.Phase2_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_RSV_Gordon);
        //reporter.searchAllStockIdAccordingToCheckPoint(DailyCombineCheckPoint.Phase3_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_KDJ_Gordon);
        //reporter.searchAllStockIdAccordingToCheckPoint(DailyCombineCheckPoint.Phase4_Previous_Under_Zero_MACD_Gordon_Now_MACD_Gordon_Volume_Bigger);

        //reporter.UnitTestForSpecifyStockId();
    }
}
