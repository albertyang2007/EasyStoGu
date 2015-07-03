package org.easystogu.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndBollTableHelper;
import org.easystogu.db.access.IndKDJTableHelper;
import org.easystogu.db.access.IndMacdTableHelper;
import org.easystogu.db.access.IndMai1Mai2TableHelper;
import org.easystogu.db.access.IndShenXianTableHelper;
import org.easystogu.db.access.IndWeekBollTableHelper;
import org.easystogu.db.access.IndWeekKDJTableHelper;
import org.easystogu.db.access.IndWeekMacdTableHelper;
import org.easystogu.db.access.IndWeekMai1Mai2TableHelper;
import org.easystogu.db.access.IndWeekShenXianTableHelper;
import org.easystogu.db.access.IndXueShi2TableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;

public class DataBaseSanityCheck {
    protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
    protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
    protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
    protected IndMai1Mai2TableHelper mai1mai2Table = IndMai1Mai2TableHelper.getInstance();
    protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();
    protected IndXueShi2TableHelper xueShi2Table = IndXueShi2TableHelper.getInstance();

    protected WeekStockPriceTableHelper weekStockPriceTable = WeekStockPriceTableHelper.getInstance();
    protected IndWeekMacdTableHelper macdWeekTable = IndWeekMacdTableHelper.getInstance();
    protected IndWeekKDJTableHelper kdjWeekTable = IndWeekKDJTableHelper.getInstance();
    protected IndWeekBollTableHelper bollWeekTable = IndWeekBollTableHelper.getInstance();
    protected IndWeekMai1Mai2TableHelper mai1mai2WeekTable = IndWeekMai1Mai2TableHelper.getInstance();
    protected IndWeekShenXianTableHelper shenXianWeekTable = IndWeekShenXianTableHelper.getInstance();

    public void sanityDailyCheck(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            if (index++ % 100 == 0) {
                System.out.println("Processing " + index + "/" + stockIds.size());
            }
            this.sanityDailyCheck(stockId);
        }
    }

    public void sanityDailyCheck(String stockId) {
        //System.out.println("Sanity Check of " + stockId);

        List<StockPriceVO> spList = stockPriceTable.getStockPriceById(stockId);
        if (spList.size() >= 108)
            for (int index = 0; index < spList.size(); index++) {
                StockPriceVO spVO = spList.get(index);
                if (macdTable.getMacd(stockId, spVO.date) == null) {
                    System.out.println(stockId + " missing macd at " + spVO.date);
                }
                if (kdjTable.getKDJ(stockId, spVO.date) == null) {
                    System.out.println(stockId + " missing kdj at " + spVO.date);
                }
                if (bollTable.getBoll(stockId, spVO.date) == null) {
                    System.out.println(stockId + " missing boll at " + spVO.date);
                }
                if (mai1mai2Table.getMai1Mai2(stockId, spVO.date) == null) {
                    System.out.println(stockId + " missing mai1mai2 at " + spVO.date);
                }
                if (shenXianTable.getShenXian(stockId, spVO.date) == null) {
                    System.out.println(stockId + " missing shenXian at " + spVO.date);
                }
                if (xueShi2Table.getXueShi2(stockId, spVO.date) == null) {
                    System.out.println(stockId + " missing xieShi2 at " + spVO.date);
                }
            }
    }

    public void sanityWeekCheck(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            if (index++ % 100 == 0) {
                System.out.println("Processing " + index + "/" + stockIds.size());
            }
            this.sanityWeekCheck(stockId);
        }
    }

    public void sanityWeekCheck(String stockId) {
        //System.out.println("Sanity Check of " + stockId);

        List<StockPriceVO> spList = weekStockPriceTable.getStockPriceById(stockId);
        if (spList.size() >= 108)
        for (int index = 0; index < spList.size(); index++) {
            StockPriceVO spVO = spList.get(index);
            if (macdWeekTable.getMacd(stockId, spVO.date) == null) {
                System.out.println(stockId + " missing week macd at " + spVO.date);
            }
            if (kdjWeekTable.getKDJ(stockId, spVO.date) == null) {
                System.out.println(stockId + " missing week kdj at " + spVO.date);
            }
            if (bollWeekTable.getBoll(stockId, spVO.date) == null) {
                System.out.println(stockId + " missing week boll at " + spVO.date);
            }
            if (mai1mai2WeekTable.getMai1Mai2(stockId, spVO.date) == null) {
                System.out.println(stockId + " missing week mai1mai2 at " + spVO.date);
            }
                if (shenXianWeekTable.getShenXian(stockId, spVO.date) == null) {
                System.out.println(stockId + " missing week shenXian at " + spVO.date);
            }
        }
    }

    public static void main(String[] args) {
        StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
        DataBaseSanityCheck check = new DataBaseSanityCheck();
        //check.sanityDailyCheck(stockConfig.getAllStockId());
        check.sanityWeekCheck(stockConfig.getAllStockId());
    }
}
