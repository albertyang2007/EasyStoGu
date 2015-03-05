package org.easystogu.db.access;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.table.StockSuperVO;

public class StockSuperVOHelper {

    protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
    protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
    protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();

    public List<StockSuperVO> getLatestNStockSuperVO(String stockId, int day) {
        // merge them into one overall VO
        List<StockSuperVO> overList = new ArrayList<StockSuperVO>();

        List<StockPriceVO> spList = stockPriceTable.getNdateStockPriceById(stockId, day);
        List<MacdVO> macdList = macdTable.getNDateMacd(stockId, day);
        List<KDJVO> kdjList = kdjTable.getNDateKDJ(stockId, day);
        List<BollVO> bollList = bollTable.getNDateBoll(stockId, day);

        if ((spList.size() != day) || (macdList.size() != day) || (kdjList.size() != day) || (bollList.size() != day)) {
            //System.out.println(stockId + " size of spList(" + spList.size() + "), macdList(" + macdList.size()
            //        + ") and kdjList(" + kdjList.size() + ") is not equal, the database must meet fatel error!");
            return overList;
        }

        if (!spList.get(0).date.equals(macdList.get(0).date) || !spList.get(0).date.equals(kdjList.get(0).date)
                || !spList.get(0).date.equals(bollList.get(0).date)) {
            //System.out.println(stockId
            //        + " date of spList, macdList and kdjList is not equal, the database must meet fatel error!");
            return overList;
        }

        if (!spList.get(day - 1).date.equals(macdList.get(day - 1).date)
                || !spList.get(day - 1).date.equals(kdjList.get(day - 1).date)
                || !spList.get(day - 1).date.equals(bollList.get(day - 1).date)) {
            System.out.println(stockId + " Date of spList(" + spList.get(day - 1).date + "), macdList("
                    + macdList.get(day - 1).date + "),kdjList(" + kdjList.get(day - 1).date + "),bollList"
                    + bollList.get(day - 1).date + " is not equal, the database must meet fatel error!");
            return overList;
        }

        for (int index = 0; index < spList.size(); index++) {
            overList.add(new StockSuperVO(spList.get(index), macdList.get(index), kdjList.get(index), bollList
                    .get(index)));
        }

        return overList;
    }

    public List<StockSuperVO> getAllStockSuperVO(String stockId) {
        // merge them into one overall VO
        List<StockSuperVO> overList = new ArrayList<StockSuperVO>();

        List<StockPriceVO> spList = stockPriceTable.getStockPriceById(stockId);
        List<MacdVO> macdList = macdTable.getAllMacd(stockId);
        List<KDJVO> kdjList = kdjTable.getAllKDJ(stockId);
        List<BollVO> bollList = bollTable.getAllBoll(stockId);

        if ((spList.size() != macdList.size()) || (macdList.size() != kdjList.size())
                || (kdjList.size() != spList.size()) || (bollList.size() != spList.size())) {
            // System.out
            // .println("size of spList, macdList and kdjList is not equal, the database must meet fatel error!");
            return overList;
        }

        if ((spList.size() == 0) || (macdList.size() == 0) || (kdjList.size() == 0) || (bollList.size() == 0)) {
            return overList;
        }

        if (!spList.get(0).date.equals(macdList.get(0).date) || !spList.get(0).date.equals(kdjList.get(0).date)
                || !spList.get(0).date.equals(bollList.get(0).date)) {
            // System.out
            // .println("Date of spList, macdList and kdjList is not equal, the database must meet fatel error!");
            return overList;
        }

        for (int index = 0; index < spList.size(); index++) {
            overList.add(new StockSuperVO(spList.get(index), macdList.get(index), kdjList.get(index), bollList
                    .get(index)));
        }

        return overList;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
