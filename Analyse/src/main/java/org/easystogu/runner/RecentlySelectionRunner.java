package org.easystogu.runner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.ZiJinLiu3DayTableHelper;
import org.easystogu.db.access.ZiJinLiu5DayTableHelper;
import org.easystogu.db.access.ZiJinLiuTableHelper;
import org.easystogu.db.table.CheckPointDailySelectionVO;
import org.easystogu.db.table.CompanyInfoVO;
import org.easystogu.db.table.ZiJinLiuVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.report.ReportTemplate;
import org.easystogu.report.comparator.CheckPointEventAndZiJinLiuComparator;

//recently (10 days) select stock that checkpoint is satisfied
public class RecentlySelectionRunner implements Runnable {
    private FileConfigurationService config = FileConfigurationService.getInstance();
    private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
    private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    private ZiJinLiuTableHelper ziJinLiuTableHelper = ZiJinLiuTableHelper.getInstance();
    private ZiJinLiu3DayTableHelper ziJinLiu3DayTableHelper = ZiJinLiu3DayTableHelper.getInstance();
    private ZiJinLiu5DayTableHelper ziJinLiu5DayTableHelper = ZiJinLiu5DayTableHelper.getInstance();
    private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
            .getInstance();
    private String latestDate = stockPriceTable.getLatestStockDate();
    private List<String> lastNDates = stockPriceTable.getAllLastNDate(stockConfig.getSZZSStockIdForDB(), 10);
    private Map<String, List<CheckPointDailySelectionVO>> checkPointStocks = new HashMap<String, List<CheckPointDailySelectionVO>>();

    private void fetchRecentDaysCheckPointFromDB() {
        // TODO Auto-generated method stub
        List<CheckPointDailySelectionVO> cpList = checkPointDailySelectionTable.getRecentDaysCheckPoint(lastNDates
                .get(lastNDates.size() - 1));
        for (CheckPointDailySelectionVO cpVO : cpList) {
            DailyCombineCheckPoint checkPoint = DailyCombineCheckPoint.getCheckPointByName(cpVO.checkPoint);
            if (checkPoint.isSatisfyMinEarnPercent()) {
                this.addCheckPointStockToMap(cpVO);
            }
        }

        this.checkPointStocks = CheckPointEventAndZiJinLiuComparator.sortMapByValue(checkPointStocks);
    }

    private void addCheckPointStockToMap(CheckPointDailySelectionVO cpVO) {
        if (!this.checkPointStocks.containsKey(cpVO.stockId)) {
            List<CheckPointDailySelectionVO> cps = new ArrayList<CheckPointDailySelectionVO>();
            cps.add(cpVO);
            this.checkPointStocks.put(cpVO.stockId, cps);
        } else {
            List<CheckPointDailySelectionVO> cps = this.checkPointStocks.get(cpVO.stockId);
            cps.add(cpVO);
        }
    }

    private void printRecentCheckPointToConsole() {
        Set<String> stockIds = this.checkPointStocks.keySet();
        Iterator<String> its = stockIds.iterator();
        while (its.hasNext()) {
            String stockId = its.next();
            List<CheckPointDailySelectionVO> cpList = this.checkPointStocks.get(stockId);
            System.out.println(stockId + " " + cpList);
        }
    }

    public void printRecentCheckPointToHtml() {
        String file = config.getString("report.recent.analyse.html.file").replaceAll("currentDate", latestDate);
        System.out.println("\nSaving report to " + file);
        try {
            BufferedWriter fout = new BufferedWriter(new FileWriter(file));
            fout.write(ReportTemplate.htmlStart);
            fout.newLine();
            fout.write(ReportTemplate.tableStart);
            fout.newLine();

            Set<String> stockIds = this.checkPointStocks.keySet();
            Iterator<String> its = stockIds.iterator();
            while (its.hasNext()) {
                String stockId = its.next();

                fout.write(ReportTemplate.tableTrStart);
                fout.newLine();

                fout.write(ReportTemplate.tableTdStart);
                fout.write(stockId + "&nbsp;" + this.getLiuTongShiZhi(stockId) + "<br>");
                fout.write("<img src='http://image.sinajs.cn/newchart/daily/n/" + withPrefixStockId(stockId)
                        + ".gif'/>");
                fout.write(ReportTemplate.tableTdEnd);
                fout.newLine();

                fout.write(ReportTemplate.tableTdStart);
                fout.write(ReportTemplate.tableStart);
                fout.newLine();

                for (String date : lastNDates) {
                    fout.write(ReportTemplate.tableTrStart);
                    fout.newLine();

                    fout.write(ReportTemplate.tableTdStart);
                    fout.write(date);
                    fout.write(ReportTemplate.tableTdEnd);
                    fout.newLine();

                    fout.write(ReportTemplate.tableTdStart);
                    fout.write(this.getCheckPointOnDate(date, this.checkPointStocks.get(stockId)));
                    fout.write(this.getZiJinLiu(stockId, date));
                    fout.write(ReportTemplate.tableTdEnd);
                    fout.newLine();

                    fout.write(ReportTemplate.tableTrEnd);
                    fout.newLine();
                }

                fout.write(ReportTemplate.tableEnd);
                fout.newLine();

                fout.write(ReportTemplate.tableTdEnd);
                fout.newLine();

                fout.write(ReportTemplate.tableTrEnd);
                fout.newLine();
            }

            fout.write(ReportTemplate.tableEnd);
            fout.newLine();
            fout.write(ReportTemplate.htmlEnd);
            fout.newLine();

            fout.flush();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String withPrefixStockId(String stockId) {
        if (stockId.startsWith("0") || stockId.startsWith("3")) {
            return "sz" + stockId;
        } else if (stockId.startsWith("6")) {
            return "sh" + stockId;
        }
        return stockId;
    }

    private String getCheckPointOnDate(String date, List<CheckPointDailySelectionVO> cpList) {
        StringBuffer sb = new StringBuffer();
        for (CheckPointDailySelectionVO cp : cpList) {
            if (date.equals(cp.date)) {
                sb.append(cp.checkPoint + "<br>");
            }
        }
        return sb.toString();
    }

    private String getZiJinLiu(String stockId, String date) {
        StringBuffer sb = new StringBuffer();
        ZiJinLiuVO _1dayVO = ziJinLiuTableHelper.getZiJinLiu(stockId, date);
        ZiJinLiuVO _3dayVO = ziJinLiu3DayTableHelper.getZiJinLiu(stockId, date);
        ZiJinLiuVO _5dayVO = ziJinLiu5DayTableHelper.getZiJinLiu(stockId, date);

        if (_1dayVO != null) {
            sb.append(ZiJinLiuVO._1Day + _1dayVO.toNetPerString() + "<br>");
        }
        if (_3dayVO != null) {
            sb.append(ZiJinLiuVO._3Day + _3dayVO.toNetPerString() + "<br>");
        }
        if (_5dayVO != null) {
            sb.append(ZiJinLiuVO._5Day + _5dayVO.toNetPerString() + "<br>");
        }

        return sb.toString();
    }

    // 盘子大小 (流通市值)
    private String getLiuTongShiZhi(String stockId) {
        CompanyInfoVO companyVO = stockConfig.getByStockId(stockId);
        double liuTongShiZhi = 0.0;
        if (companyVO != null) {
            double close = stockPriceTable.getAvgClosePrice(stockId, 1);
            liuTongShiZhi = companyVO.liuTongAGu * close;
        }
        return (int) liuTongShiZhi + "亿";
    }

    public void run() {
        fetchRecentDaysCheckPointFromDB();
        printRecentCheckPointToConsole();
        printRecentCheckPointToHtml();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        RecentlySelectionRunner runner = new RecentlySelectionRunner();
        runner.fetchRecentDaysCheckPointFromDB();
        runner.printRecentCheckPointToConsole();
        runner.printRecentCheckPointToHtml();
    }
}
