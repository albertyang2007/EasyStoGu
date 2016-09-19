package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.table.IndQSDDTableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.QSDDVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.QSDDHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.utils.Strings;

import com.google.common.primitives.Doubles;

public class DailyQSDDCountAndSaveDBRunner implements Runnable {
    private QSDDHelper qsddHelper = new QSDDHelper();
    protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
    protected IndQSDDTableHelper qsddTable = IndQSDDTableHelper.getInstance();
    protected CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();

    public DailyQSDDCountAndSaveDBRunner() {

    }

    public void deleteQSDD(String stockId, String date) {
        qsddTable.delete(stockId, date);
    }

    public void countAndSaved(String stockId) {
        List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

        if (priceList.size() < 1) {
            return;
        }

        List<Double> close = StockPriceFetcher.getClosePrice(priceList);
        List<Double> low = StockPriceFetcher.getLowPrice(priceList);
        List<Double> high = StockPriceFetcher.getHighPrice(priceList);

        double[][] qsdd = qsddHelper.getQSDDList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));

        int length = qsdd[0].length;

        // for (int i = 0; i < KDJ[0].length; i++) {
        QSDDVO vo = new QSDDVO();
        vo.setLonTerm(Strings.convert2ScaleDecimal(qsdd[0][length - 1]));
        vo.setShoTerm(Strings.convert2ScaleDecimal(qsdd[1][length - 1]));
        vo.setMidTerm(Strings.convert2ScaleDecimal(qsdd[2][length - 1]));
        vo.setStockId(stockId);
        vo.setDate(priceList.get(length - 1).date);

        // System.out.println(vo);
        this.deleteQSDD(stockId, vo.date);
        qsddTable.insert(vo);

    }

    public void countAndSaved(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            if (index++ % 500 == 0) {
                System.out.println("QSDD countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
            }
            this.countAndSaved(stockId);
        }
    }

    public void run() {

    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
        DailyQSDDCountAndSaveDBRunner runner = new DailyQSDDCountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        // runner.countAndSaved("999999");
    }
}
