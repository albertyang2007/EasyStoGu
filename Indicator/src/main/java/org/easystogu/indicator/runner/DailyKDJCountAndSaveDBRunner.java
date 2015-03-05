package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndKDJTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.kdj.KDJHelper;

public class DailyKDJCountAndSaveDBRunner {
    private KDJHelper kdjHelper = new KDJHelper();
    protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();

    public void deleteKDJ(String stockId, String date) {
        kdjTable.delete(stockId, date);
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

        int length = KDJ[0].length;

        // for (int i = 0; i < KDJ[0].length; i++) {
        KDJVO vo = new KDJVO();
        vo.setK(KDJ[0][length - 1]);
        vo.setD(KDJ[1][length - 1]);
        vo.setJ(KDJ[2][length - 1]);
        vo.setRsv(KDJ[3][length - 1]);
        vo.setStockId(stockId);
        vo.setDate(spVO.get(length - 1).date);

        this.deleteKDJ(stockId, vo.date);
        kdjTable.insert(vo);

    }

    public void countAndSaved(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            System.out.println("KDJ countAndSaved: " + stockId + " " + (++index) + "/" + stockIds.size());
            this.countAndSaved(stockId);
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
        DailyKDJCountAndSaveDBRunner runner = new DailyKDJCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        //runner.countAndSaved("600825");
    }
}
