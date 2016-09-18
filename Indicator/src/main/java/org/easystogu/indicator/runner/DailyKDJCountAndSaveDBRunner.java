package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.table.IndKDJTableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.KDJHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.utils.Strings;

import com.google.common.primitives.Doubles;

public class DailyKDJCountAndSaveDBRunner implements Runnable {
    private KDJHelper kdjHelper = new KDJHelper();
    protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
    protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
    protected CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();

    public DailyKDJCountAndSaveDBRunner() {

    }

    public void deleteKDJ(String stockId, String date) {
        kdjTable.delete(stockId, date);
    }

    public void countAndSaved(String stockId) {
        List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

        if (priceList.size() <= 9) {
            // System.out.println("StockPrice data is less than 9, skip " +
            // stockId);
            return;
        }

        List<Double> close = StockPriceFetcher.getClosePrice(priceList);
        List<Double> low = StockPriceFetcher.getLowPrice(priceList);
        List<Double> high = StockPriceFetcher.getHighPrice(priceList);

        double[][] KDJ = kdjHelper.getKDJList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));

        int length = KDJ[0].length;

        // for (int i = 0; i < KDJ[0].length; i++) {
        KDJVO vo = new KDJVO();
        vo.setK(Strings.convert2ScaleDecimal(KDJ[0][length - 1]));
        vo.setD(Strings.convert2ScaleDecimal(KDJ[1][length - 1]));
        vo.setJ(Strings.convert2ScaleDecimal(KDJ[2][length - 1]));
        vo.setRsv(Strings.convert2ScaleDecimal(KDJ[3][length - 1]));
        vo.setStockId(stockId);
        vo.setDate(priceList.get(length - 1).date);

        //System.out.println(vo);
        this.deleteKDJ(stockId, vo.date);
        kdjTable.insert(vo);

    }

    public void countAndSaved(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            if (index++ % 500 == 0) {
                System.out.println("KDJ countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
            }
            this.countAndSaved(stockId);
        }
    }

    public void run() {
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
        DailyKDJCountAndSaveDBRunner runner = new DailyKDJCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        // runner.countAndSaved("002609");
    }
}
