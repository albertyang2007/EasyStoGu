package org.easystogu.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.EventGaoSongZhuanTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.GaoSongZhuanVO;
import org.easystogu.db.table.StockPriceVO;

//if table event_gaosongzhuan has update, please run this runner 
//to update all the gaoSongZhuan price data
//manually to update gaoSongZhuan table, pls refer to 
//http://www.cninfo.com.cn/search/memo.jsp?datePara=2015-05-13

public class GaoSongZhuanTianQuanUpdatePriceRunner {
    protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    protected EventGaoSongZhuanTableHelper gaoSongZhuanTable = EventGaoSongZhuanTableHelper.getInstance();

    private void checkIfGaoSongZhuanExist(String stockId) {
        //get latest two day vo
        List<StockPriceVO> list = stockPriceTable.getNdateStockPriceById(stockId, 2);
        if (list != null && list.size() == 2) {
            StockPriceVO cur = list.get(0);
            StockPriceVO pre = list.get(1);
            //System.out.println(cur);
            //System.out.println(pre);
            if (cur.lastClose != 0 && cur.lastClose != pre.close) {
                //chuQuan happen! 
                GaoSongZhuanVO avo = gaoSongZhuanTable.getGaoSongZhuanVO(stockId, cur.date);
                if (avo == null) {
                    GaoSongZhuanVO vo = new GaoSongZhuanVO();
                    vo.setStockId(cur.stockId);
                    vo.setDate(cur.date);
                    vo.setRate(cur.lastClose / pre.close);
                    vo.setAlreadyUpdatePrice(false);

                    System.out.println("ChuQuan happen for " + vo);
                    gaoSongZhuanTable.insertIfNotExist(vo);
                }
            }
        }

    }

    private void checkIfGaoSongZhuanExist(List<String> stockIds) {
        for (String stockId : stockIds) {
            this.checkIfGaoSongZhuanExist(stockId);
        }
    }

    private void updatePriceBasedOnGaoSongZhuan(List<String> stockIds) {
        for (String stockId : stockIds) {
            this.updatePriceBasedOnGaoSongZhuan(stockId);
        }
    }

    private void updatePriceBasedOnGaoSongZhuan(String stockId) {
        List<GaoSongZhuanVO> list = gaoSongZhuanTable.getAllGaoSongZhuanVO(stockId);
        //list is order by date
        for (GaoSongZhuanVO vo : list) {
            if (!vo.isAlreadyUpdatePrice()) {
                //update price before the date
                System.out.println("Update price for " + stockId + " before " + vo.date + ", rate=" + vo.rate);
                stockPriceTable.updateGaoSongZhuanPrice(vo);
                //then update the gaoSongZhuanVO to true
                vo.setAlreadyUpdatePrice(true);
                gaoSongZhuanTable.updateGaoSongZhuanVO(vo);
            }
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();

        List<String> stockIds = stockConfig.getAllStockId();
        GaoSongZhuanTianQuanUpdatePriceRunner runner = new GaoSongZhuanTianQuanUpdatePriceRunner();

        runner.checkIfGaoSongZhuanExist(stockIds);
        runner.updatePriceBasedOnGaoSongZhuan(stockIds);
    }

}
