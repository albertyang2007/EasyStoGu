package org.easystogu.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.easystogu.analyse.CombineAnalyseHelper;
import org.easystogu.analyse.util.IndProcessHelper;
import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.CheckPointDailyStatisticsTableHelper;
import org.easystogu.db.access.CheckPointHistoryAnalyseTableHelper;
import org.easystogu.db.access.CheckPointHistorySelectionTableHelper;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.StockSuperVOHelper;
import org.easystogu.db.access.WeekStockSuperVOHelper;
import org.easystogu.db.table.CheckPointDailySelectionVO;
import org.easystogu.db.table.CheckPointDailyStatisticsVO;
import org.easystogu.db.table.CheckPointHistoryAnalyseVO;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.CrossType;
import org.easystogu.utils.SellPointType;

public class HistoryAnalyseReport {
    private FileConfigurationService config = FileConfigurationService.getInstance();
    private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
    private CheckPointHistorySelectionTableHelper checkPointHistorySelectionTable = CheckPointHistorySelectionTableHelper
            .getInstance();
    private CheckPointDailyStatisticsTableHelper checkPointDailyStatisticsTable = CheckPointDailyStatisticsTableHelper
            .getInstance();
    private WeekStockSuperVOHelper weekStockOverAllHelper = new WeekStockSuperVOHelper();
    private CombineAnalyseHelper combineAanalyserHelper = new CombineAnalyseHelper();
    private StockSuperVOHelper stockOverAllHelper = new StockSuperVOHelper();
    private CheckPointHistoryAnalyseTableHelper cpHistoryAnalyse = CheckPointHistoryAnalyseTableHelper.getInstance();
    private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
            .getInstance();
    private String specifySelectCheckPoint = config.getString("specify_Select_CheckPoint", "");
    private String[] specifySelectCheckPoints = specifySelectCheckPoint.split(";");
    private ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();
    private String[] generalCheckPoints = config.getString("general_CheckPoint", "").split(";");
    //date, count
    private Map<String, Integer> generalCheckPointStatisticsMap = new HashMap<String, Integer>();

    public List<HistoryReportDetailsVO> doAnalyseBuySellDate(String stockId, List<DailyCombineCheckPoint> checkPointList) {
        List<HistoryReportDetailsVO> reportList = new ArrayList<HistoryReportDetailsVO>();
        for (DailyCombineCheckPoint checkPoint : checkPointList) {
            reportList.addAll(this.doAnalyseBuySellDate(stockId, checkPoint));
        }
        return reportList;
    }

    public List<HistoryReportDetailsVO> doAnalyseBuySellDate(String stockId, DailyCombineCheckPoint checkPoint) {

        List<HistoryReportDetailsVO> historyReportList = new ArrayList<HistoryReportDetailsVO>();

        List<StockSuperVO> overDayList = stockOverAllHelper.getAllStockSuperVO(stockId);
        List<StockSuperVO> overWeekList = weekStockOverAllHelper.getAllStockSuperVO(stockId);

        // fliter the history data, set the startDate and endDate
        // overDayList = this.getSubDayVOList(overDayList, "2014-04-01",
        // "9999-99-99");

        if (overDayList.size() == 0) {
            // System.out.println("doAnalyseReport overDayList size=0 for " +
            // stockId);
            return historyReportList;
        }

        if (overWeekList.size() == 0) {
            // System.out.println("doAnalyseReport overWeekList size=0 for " +
            // stockId);
            return historyReportList;
        }

        // update price based on chuQuanChuXi event
        chuQuanChuXiPriceHelper.updateSuperPrice(stockId, overDayList);
        chuQuanChuXiPriceHelper.updateSuperPrice(stockId, overWeekList);

        IndProcessHelper.processDayList(overDayList);
        IndProcessHelper.processWeekList(overWeekList);

        HistoryReportDetailsVO reportVO = null;
        for (int index = 120; index < overDayList.size(); index++) {
            StockSuperVO superVO = overDayList.get(index);

            // buy point
            if (reportVO == null) {
                String startDate = overDayList.get(index - 120).priceVO.date;
                String endDate = overDayList.get(index).priceVO.date;
                // System.out.println(startDate + " ~~ " + endDate);
                // include the startDate, not include the endDate
                List<StockSuperVO> subOverWeekList = this.getSubWeekVOList(overWeekList, startDate, endDate);
                // System.out.println(subOverWeekList.get(0).priceVO.date +
                // " week "
                // + subOverWeekList.get(subOverWeekList.size() -
                // 1).priceVO.date);

                List<StockSuperVO> subOverDayList = overDayList.subList(index - 120, index + 1);

                // System.out.println(subOverDayList.get(0).priceVO.date +
                // " day "
                // + subOverDayList.get(subOverDayList.size() -
                // 1).priceVO.date);

                if (combineAanalyserHelper.isConditionSatisfy(checkPoint, subOverDayList, subOverWeekList)) {
                    reportVO = new HistoryReportDetailsVO(overDayList);
                    reportVO.setBuyPriceVO(superVO.priceVO);
                    continue;
                }
            }

            // sell point (MACD dead or KDJ dead point or next day)
            if ((reportVO != null) && (reportVO.buyPriceVO != null) && (reportVO.sellPriceVO == null)) {
                if (checkPoint.getSellPointType().equals(SellPointType.KDJ_Dead)) {
                    if (superVO.kdjCorssType == CrossType.DEAD) {
                        reportVO.setSellPriceVO(superVO.priceVO);
                        historyReportList.add(reportVO);
                        reportVO = null;
                    }
                } else if (checkPoint.getSellPointType().equals(SellPointType.MACD_Dead)) {
                    if (superVO.macdCorssType == CrossType.DEAD) {
                        reportVO.setSellPriceVO(superVO.priceVO);
                        historyReportList.add(reportVO);
                        reportVO = null;
                    }
                } else if (checkPoint.getSellPointType().equals(SellPointType.ShenXian_Dead)) {
                    if (superVO.shenXianCorssType12 == CrossType.DEAD) {
                        reportVO.setSellPriceVO(superVO.priceVO);
                        historyReportList.add(reportVO);
                        reportVO = null;
                    }
                } else if (checkPoint.getSellPointType().equals(SellPointType.Next_Day)) {
                    reportVO.setSellPriceVO(superVO.priceVO);
                    historyReportList.add(reportVO);
                    reportVO = null;
                }
            }
        }

        // if loop to end and no sell point, then set the latest day as sell
        // point
        if ((reportVO != null) && (reportVO.buyPriceVO != null) && (reportVO.sellPriceVO == null)) {
            StockSuperVO superVO = overDayList.get(overDayList.size() - 1);
            reportVO.setSellPriceVO(superVO.priceVO, false);
            historyReportList.add(reportVO);
            reportVO = null;
        }

        return historyReportList;
    }

    //if the checkpoint is meet, then add vo to list
    public List<HistoryReportDetailsVO> doAnalyseStatistics(String stockId, DailyCombineCheckPoint checkPoint) {

        List<HistoryReportDetailsVO> historyReportList = new ArrayList<HistoryReportDetailsVO>();

        List<StockSuperVO> overDayList = stockOverAllHelper.getAllStockSuperVO(stockId);
        List<StockSuperVO> overWeekList = weekStockOverAllHelper.getAllStockSuperVO(stockId);

        // fliter the history data, set the startDate and endDate
        // overDayList = this.getSubDayVOList(overDayList, "2014-04-01",
        // "9999-99-99");

        if (overDayList.size() == 0) {
            // System.out.println("doAnalyseReport overDayList size=0 for " +
            // stockId);
            return historyReportList;
        }

        if (overWeekList.size() == 0) {
            // System.out.println("doAnalyseReport overWeekList size=0 for " +
            // stockId);
            return historyReportList;
        }

        // update price based on chuQuanChuXi event
        chuQuanChuXiPriceHelper.updateSuperPrice(stockId, overDayList);
        chuQuanChuXiPriceHelper.updateSuperPrice(stockId, overWeekList);

        IndProcessHelper.processDayList(overDayList);
        IndProcessHelper.processWeekList(overWeekList);

        for (int index = 120; index < overDayList.size(); index++) {
            StockSuperVO superVO = overDayList.get(index);

            // buy point
            String startDate = overDayList.get(index - 120).priceVO.date;
            String endDate = overDayList.get(index).priceVO.date;
            // System.out.println(startDate + " ~~ " + endDate);
            // include the startDate, not include the endDate
            List<StockSuperVO> subOverWeekList = this.getSubWeekVOList(overWeekList, startDate, endDate);
            // System.out.println(subOverWeekList.get(0).priceVO.date +
            // " week "
            // + subOverWeekList.get(subOverWeekList.size() -
            // 1).priceVO.date);

            List<StockSuperVO> subOverDayList = overDayList.subList(index - 120, index + 1);

            // System.out.println(subOverDayList.get(0).priceVO.date +
            // " day "
            // + subOverDayList.get(subOverDayList.size() -
            // 1).priceVO.date);

            if (combineAanalyserHelper.isConditionSatisfy(checkPoint, subOverDayList, subOverWeekList)) {
                HistoryReportDetailsVO reportVO = new HistoryReportDetailsVO(overDayList);
                reportVO.setBuyPriceVO(superVO.priceVO);//must keep it
                historyReportList.add(reportVO);
            }
        }

        return historyReportList;
    }

    //original analyse all stockId for checkPoint
    //count buyDate, sellDate, maxEarn, minEarn etc, save data into checkpoint_history_selection
    public void searchAllStockIdAnalyseHistoryCheckPoint(DailyCombineCheckPoint checkPoint) {

        this.checkPointHistorySelectionTable.deleteByCheckPoint(checkPoint.name());

        double[] earnPercent = new double[3];
        long holdDays = 0;
        long holdDaysWhenHighPrice = 0;
        long totalCount = 0;
        int totalHighCount = 0;
        int totalLowCount = 0;
        List<String> stockIds = stockConfig.getAllStockId();

        System.out.println("\n===============================" + checkPoint + " (sellPoint:"
                + checkPoint.getSellPointType() + ")==========================");

        for (String stockId : stockIds) {

            //if (!stockId.equals("002609"))
            //    continue;

            List<HistoryReportDetailsVO> historyReportList = this.doAnalyseBuySellDate(stockId, checkPoint);
            for (HistoryReportDetailsVO reportVO : historyReportList) {
                //analyse the original buy and sell data
                if (reportVO.sellPriceVO != null) {
                    reportVO.countData();
                    // print the high earn percent if larger than 25%
                    if ((reportVO.earnPercent[1] >= 50.0) && (reportVO.earnPercent[0] >= 25.0)) {
                        totalHighCount++;
                        // System.out.println("High earn: " + reportVO);
                    } else if ((reportVO.earnPercent[1] <= -10.0) || (reportVO.earnPercent[0] <= -10.0)) {
                        totalLowCount++;
                        // System.out.println("Low  earn: " + reportVO);
                    }

                    if (!reportVO.completed) {
                        System.out.println("Not Completed: " + reportVO);
                        // save to checkpint daily selection table
                        if (isCheckPointSelected(checkPoint)) {
                            this.saveToCheckPointDailySelectionDB(reportVO.stockId, reportVO.buyPriceVO.date,
                                    checkPoint);
                        }
                    } else {
                        // for completed VO
                        // remove it from daily selection
                        System.out.println("Completed: " + reportVO);
                        this.checkPointDailySelectionTable.delete(stockId, reportVO.buyPriceVO.date,
                                checkPoint.toString());
                        // save case into history DB
                        checkPointHistorySelectionTable
                                .insert(reportVO.convertToHistoryReportVO(checkPoint.toString()));
                    }

                    totalCount++;
                    earnPercent[0] += reportVO.earnPercent[0];
                    earnPercent[1] += reportVO.earnPercent[1];
                    earnPercent[2] += reportVO.earnPercent[2];
                    holdDays += reportVO.holdDays;
                    holdDaysWhenHighPrice += reportVO.holdDaysWhenHighPrice;
                }
            }
        }

        if (totalCount == 0) {
            totalCount = 1;
        }

        System.out.println("Total satisfy: " + totalCount + "\t earnPercent[close]=" + (earnPercent[0] / totalCount)
                + "\t earnPercent[high]=" + (earnPercent[1] / totalCount) + "\t earnPercent[low]="
                + (earnPercent[2] / totalCount) + "\noldEarn=" + checkPoint.getEarnPercent());

        System.out.println("Avg hold stock days when sell point: " + (holdDays / totalCount));
        System.out.println("Avg hold stock days when high price: " + (holdDaysWhenHighPrice / totalCount));
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
        cpHistoryAnalyse.insert(vo);
    }

    //statistics all stockIds and count checkpoint, save into checkpoint_daily_statistics
    public void searchAllStockIdStatisticsCheckPoint(DailyCombineCheckPoint checkPoint) {

        this.checkPointDailyStatisticsTable.deleteByCheckPoint(checkPoint.name());
        this.generalCheckPointStatisticsMap.clear();

        int index = 0;
        List<String> stockIds = stockConfig.getAllStockId();

        System.out.println("\n===============================" + checkPoint + " (sellPoint:"
                + checkPoint.getSellPointType() + ")==========================");

        for (String stockId : stockIds) {

            //if (!stockId.equals("002609") && !stockId.equals("300248") && !stockId.equals("300037"))
            //    continue;

            if (index++ % 100 == 0) {
                System.out.println("Analyse of " + index + "/" + stockIds.size());
            }

            List<HistoryReportDetailsVO> historyReportList = this.doAnalyseStatistics(stockId, checkPoint);
            for (HistoryReportDetailsVO reportVO : historyReportList) {
                //statistics the checkPoint and count at date               
                Integer count = this.generalCheckPointStatisticsMap.get(reportVO.buyPriceVO.date);
                if (count == null) {
                    count = new Integer(0);
                }
                //update the count
                this.generalCheckPointStatisticsMap.put(new String(reportVO.buyPriceVO.date), new Integer(count + 1));
            }
        }

        //save statistics the checkPoint and count at date into DB
        Iterator it = this.generalCheckPointStatisticsMap.entrySet().iterator();
        while (it.hasNext()) {
            CheckPointDailyStatisticsVO cpdsvo = new CheckPointDailyStatisticsVO();
            Map.Entry entry = (Map.Entry) it.next();
            cpdsvo.checkPoint = checkPoint.name();
            cpdsvo.date = entry.getKey().toString();
            cpdsvo.count = (Integer) entry.getValue();

            checkPointDailyStatisticsTable.insert(cpdsvo);
        }
    }

    private void saveToCheckPointDailySelectionDB(String stockId, String date, DailyCombineCheckPoint checkPoint) {
        CheckPointDailySelectionVO vo = new CheckPointDailySelectionVO();
        vo.setStockId(stockId);
        vo.setDate(date);
        vo.setCheckPoint(checkPoint.toString());
        this.checkPointDailySelectionTable.insertIfNotExist(vo);
    }

    private boolean isCheckPointSelected(DailyCombineCheckPoint checkPoint) {
        for (String cp : specifySelectCheckPoints) {
            if (cp.equals(checkPoint.toString())) {
                return true;
            }
        }
        return false;
    }

    public List<StockSuperVO> getSubWeekVOList(List<StockSuperVO> overWeekList, String startDate, String endDate) {
        List<StockSuperVO> subList = new ArrayList<StockSuperVO>();

        for (StockSuperVO vo : overWeekList) {
            // include the startDate, not include the endDate
            if (vo.priceVO.date.compareTo(startDate) >= 0 && vo.priceVO.date.compareTo(endDate) <= 0) {
                subList.add(vo);
            }
        }

        return subList;
    }

    public List<StockSuperVO> getSubDayVOList(List<StockSuperVO> overDayList, String startDate, String endDate) {
        List<StockSuperVO> subList = new ArrayList<StockSuperVO>();

        for (StockSuperVO vo : overDayList) {
            // include the startDate, not include the endDate
            if (vo.priceVO.date.compareTo(startDate) >= 0 && vo.priceVO.date.compareTo(endDate) < 0) {
                subList.add(vo);
            }
        }

        return subList;
    }

    public static void main(String[] args) {
        HistoryAnalyseReport reporter = new HistoryAnalyseReport();
        FileConfigurationService config = FileConfigurationService.getInstance();
        // for (DailyCombineCheckPoint checkPoint :
        // DailyCombineCheckPoint.values()) {
        // if (config.getString("general_CheckPoint",
        // "").contains(checkPoint.name())) {
        // reporter.searchAllStockIdAnalyseHistoryCheckPoint(checkPoint);
        // }
        // }
        reporter.searchAllStockIdStatisticsCheckPoint(DailyCombineCheckPoint.Trend_PhaseI_GuanCha);
        reporter.searchAllStockIdStatisticsCheckPoint(DailyCombineCheckPoint.Trend_PhaseII_JianCang);
        reporter.searchAllStockIdStatisticsCheckPoint(DailyCombineCheckPoint.Trend_PhaseIII_ChiGu);
        reporter.searchAllStockIdStatisticsCheckPoint(DailyCombineCheckPoint.Trend_PhaseVI_JianCang);
    }
}
