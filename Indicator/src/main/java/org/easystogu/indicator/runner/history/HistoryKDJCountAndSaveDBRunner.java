package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndKDJTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.kdj.KDJHelper;

public class HistoryKDJCountAndSaveDBRunner {
    protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
    private KDJHelper kdjHelper = new KDJHelper();

    public void deleteKDJ(String stockId) {
        kdjTable.delete(stockId);
    }

    public void deleteKDJ(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            System.out.println("Delete KDJ for " + stockId + " " + (++index) + " of " + stockIds.size());
            this.deleteKDJ(stockId);
        }
    }

    public void countAndSaved(String stockId) {
        List<StockPriceVO> spVO = stockPriceTable.getStockPriceById(stockId);

        if (spVO.size() <= 9) {
            System.out.println("StockPrice data is less than 9, skip " + stockId);
            return;
        }

        List<Double> close = stockPriceTable.getAllClosePrice(stockId);
        List<Double> low = stockPriceTable.getAllLowPrice(stockId);
        List<Double> high = stockPriceTable.getAllHighPrice(stockId);

        double[][] KDJ = kdjHelper.getKDJList(close.toArray(new Double[0]), low.toArray(new Double[0]),
                high.toArray(new Double[0]));

        for (int i = 0; i < KDJ[0].length; i++) {
            KDJVO vo = new KDJVO();
            vo.setK(KDJ[0][i]);
            vo.setD(KDJ[1][i]);
            vo.setJ(KDJ[2][i]);
            vo.setRsv(KDJ[3][i]);
            vo.setStockId(stockId);
            vo.setDate(spVO.get(i).date);

            try {
                if (kdjTable.getKDJ(vo.stockId, vo.date) == null) {
                    kdjTable.insert(vo);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void countAndSaved(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            System.out.println("KDJ countAndSaved: " + stockId + " " + (++index) + " of " + stockIds.size());
            this.countAndSaved(stockId);
        }
    }

    // TODO Auto-generated method stub
    // 一次性计算数据库中所有KDJ数据，入库
    public static void main(String[] args) {
        StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
        List<String> stockIds = stockConfig.getAllStockId();
        HistoryKDJCountAndSaveDBRunner runner = new HistoryKDJCountAndSaveDBRunner();
        runner.countAndSaved(stockIds);
        //runner.countAndSaved("002194");
    }
}
