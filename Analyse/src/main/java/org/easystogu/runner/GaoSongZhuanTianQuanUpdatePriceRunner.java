package org.easystogu.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.EventGaoSongZhuanTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.GaoSongZhuanVO;

//if table event_gaosongzhuan has update, please run this runner 
//to update all the gaoSongZhuan price data
public class GaoSongZhuanTianQuanUpdatePriceRunner {
    protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    protected EventGaoSongZhuanTableHelper gaoSongZhuanTable = EventGaoSongZhuanTableHelper.getInstance();

    private void checkIfGaoSongZhuanExist(String stockId) {
        //get latest two day vo
        //List<StockPriceVO> list = stockPriceTable.getStockPriceById(stockId);
        //how to check if GaoSongZhuan exist???
        System.out.println("Not know how to check if GaoSongZhuan exist!!!");
        System.out.println("Currently manually input the event into event_gaosongzhuan!!!");

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
        runner.updatePriceBasedOnGaoSongZhuan(stockIds);
        //runner.updatePriceBasedOnGaoSongZhuan("601727");
    }

}
