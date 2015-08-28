package org.easystogu.db.access;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.Mai1Mai2VO;
import org.easystogu.db.table.ShenXianVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.db.table.XueShi2VO;
import org.easystogu.db.table.YiMengBSVO;
import org.easystogu.db.table.ZhuliJinChuVO;

public class StockSuperVOHelper {

    protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
    protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
    protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
    protected IndMai1Mai2TableHelper mai1mai2Table = IndMai1Mai2TableHelper.getInstance();
    protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();
    protected IndXueShi2TableHelper xueShi2Table = IndXueShi2TableHelper.getInstance();
    protected IndZhuliJinChuTableHelper zhuliJinChuTable = IndZhuliJinChuTableHelper.getInstance();
    protected IndYiMengBSTableHelper yiMengBSTable = IndYiMengBSTableHelper.getInstance();

    public List<StockSuperVO> getLatestNStockSuperVO(String stockId, int day) {
        // merge them into one overall VO
        List<StockSuperVO> overList = new ArrayList<StockSuperVO>();

        List<StockPriceVO> spList = stockPriceTable.getNdateStockPriceById(stockId, day);
        List<MacdVO> macdList = macdTable.getNDateMacd(stockId, day);
        List<KDJVO> kdjList = kdjTable.getNDateKDJ(stockId, day);
        List<BollVO> bollList = bollTable.getNDateBoll(stockId, day);
        List<ShenXianVO> shenXianList = shenXianTable.getNDateShenXian(stockId, day);
        List<XueShi2VO> xueShie2List = xueShi2Table.getNDateXueShi2(stockId, day);
        List<Mai1Mai2VO> mai1mai2List = mai1mai2Table.getNDateMai1Mai2(stockId, day);
        List<ZhuliJinChuVO> zhuliJinChuList = zhuliJinChuTable.getNDateZhuliJinChu(stockId, day);
        List<YiMengBSVO> yiMengBSList = yiMengBSTable.getNDateYiMengBS(stockId, day);

        if ((spList.size() != day) || (macdList.size() != day) || (kdjList.size() != day) || (bollList.size() != day)
                || (xueShie2List.size() != day) || (shenXianList.size() != day) || (mai1mai2List.size() != day)
                || (zhuliJinChuList.size() != day) || (yiMengBSList.size() != day)) {
            // System.out.println(stockId + " size of spList(" + spList.size() +
            // "), macdList(" + macdList.size()
            // + ") and kdjList(" + kdjList.size() + ") and xueShie2List(" +
            // xueShie2List.size()
            // + ") is not equal, the database must meet fatel error!");
            return overList;
        }

        if (!spList.get(0).date.equals(macdList.get(0).date) || !spList.get(0).date.equals(kdjList.get(0).date)
                || !spList.get(0).date.equals(bollList.get(0).date)
                || !spList.get(0).date.equals(xueShie2List.get(0).date)
                || !spList.get(0).date.equals(shenXianList.get(0).date)
                || !spList.get(0).date.equals(mai1mai2List.get(0).date)
                || !spList.get(0).date.equals(zhuliJinChuList.get(0).date)
                || !spList.get(0).date.equals(yiMengBSList.get(0).date)) {
            // System.out.println(stockId
            // +
            // " date of spList, macdList and kdjList is not equal, the database must meet fatel error!");
            return overList;
        }

        if (!spList.get(day - 1).date.equals(macdList.get(day - 1).date)
                || !spList.get(day - 1).date.equals(kdjList.get(day - 1).date)
                || !spList.get(day - 1).date.equals(bollList.get(day - 1).date)
                || !spList.get(day - 1).date.equals(xueShie2List.get(day - 1).date)
                || !spList.get(day - 1).date.equals(shenXianList.get(day - 1).date)
                || !spList.get(day - 1).date.equals(mai1mai2List.get(day - 1).date)
                || !spList.get(day - 1).date.equals(zhuliJinChuList.get(day - 1).date)
                || !spList.get(day - 1).date.equals(yiMengBSList.get(day - 1).date)) {
            // System.out.println(stockId + " Date of spList(" + spList.get(day
            // - 1).date + "), macdList("
            // + macdList.get(day - 1).date + "),kdjList(" + kdjList.get(day -
            // 1).date + "),bollList("
            // + bollList.get(day - 1).date + "),xueShie2List(" +
            // xueShie2List.get(day - 1).date
            // + ") is not equal, the database must meet fatel error!");
            return overList;
        }

        for (int index = 0; index < spList.size(); index++) {
            StockSuperVO superVO = new StockSuperVO(spList.get(index), macdList.get(index), kdjList.get(index),
                    bollList.get(index));
            superVO.setShenXianVO(shenXianList.get(index));
            superVO.setXueShi2VO(xueShie2List.get(index));
            superVO.setMai1Mai2VO(mai1mai2List.get(index));
            superVO.setZhuliJinChuVO(zhuliJinChuList.get(index));
            superVO.setYiMengBSVO(yiMengBSList.get(index));
            overList.add(superVO);
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
        List<ShenXianVO> shenXianList = shenXianTable.getAllShenXian(stockId);
        List<XueShi2VO> xueShie2List = xueShi2Table.getAllXueShi2(stockId);
        List<Mai1Mai2VO> mai1mai2List = mai1mai2Table.getAllMai1Mai2(stockId);
        List<ZhuliJinChuVO> zhuliJinChuList = zhuliJinChuTable.getAllZhuliJinChu(stockId);
        List<YiMengBSVO> yiMengBSList = yiMengBSTable.getAllYiMengBS(stockId);

        if ((spList.size() != macdList.size()) || (macdList.size() != kdjList.size())
                || (kdjList.size() != spList.size()) || (bollList.size() != spList.size())
                || (xueShie2List.size() != spList.size()) || (shenXianList.size() != spList.size())
                || (mai1mai2List.size() != spList.size()) || (zhuliJinChuList.size() != spList.size())
                || (yiMengBSList.size() != spList.size())) {
            // System.out.println(stockId + " size of spList(" + spList.size() +
            // "), macdList(" + macdList.size()
            // + ") and kdjList(" + kdjList.size() + ") and shenXianList(" +
            // xueShie2List.size()
            // + ") is not equal, the database must meet fatel error!");
            return overList;
        }

        if ((spList.size() == 0) || (macdList.size() == 0) || (kdjList.size() == 0) || (bollList.size() == 0)
                || (xueShie2List.size() == 0) || (shenXianList.size() == 0) || (mai1mai2List.size() == 0)
                || (zhuliJinChuList.size() == 0) || (yiMengBSList.size() == 0)) {
            return overList;
        }

        if (!spList.get(0).date.equals(macdList.get(0).date) || !spList.get(0).date.equals(kdjList.get(0).date)
                || !spList.get(0).date.equals(bollList.get(0).date)
                || !spList.get(0).date.equals(xueShie2List.get(0).date)
                || !spList.get(0).date.equals(shenXianList.get(0).date)
                || !spList.get(0).date.equals(mai1mai2List.get(0).date)
                || !spList.get(0).date.equals(zhuliJinChuList.get(0).date)
                || !spList.get(0).date.equals(yiMengBSList.get(0).date)) {
            // System.out
            // .println("Date of spList, macdList and kdjList is not equal, the database must meet fatel error!");
            return overList;
        }

        for (int index = 0; index < spList.size(); index++) {
            StockSuperVO superVO = new StockSuperVO(spList.get(index), macdList.get(index), kdjList.get(index),
                    bollList.get(index));
            superVO.setShenXianVO(shenXianList.get(index));
            superVO.setXueShi2VO(xueShie2List.get(index));
            superVO.setMai1Mai2VO(mai1mai2List.get(index));
            superVO.setZhuliJinChuVO(zhuliJinChuList.get(index));
            superVO.setYiMengBSVO(yiMengBSList.get(index));
            overList.add(superVO);
        }

        return overList;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}
