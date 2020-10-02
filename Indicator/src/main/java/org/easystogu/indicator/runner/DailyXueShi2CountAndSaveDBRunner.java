package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.table.IndXueShi2TableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.XueShi2VO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.TALIBWraper;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DailyXueShi2CountAndSaveDBRunner implements Runnable {
    protected IndXueShi2TableHelper xueShi2Table = IndXueShi2TableHelper.getInstance();
    protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
    @Autowired
    private TALIBWraper talibHelper;
    protected CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();

    public DailyXueShi2CountAndSaveDBRunner() {

    }

    public void deleteXueShi2(String stockId, String date) {
        xueShi2Table.delete(stockId, date);
    }

    public void countAndSaved(String stockId) {

        List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

        int length = priceList.size();

        if (length < 60) {
            // System.out.println(stockId
            // +
            // " price data is not enough to count XueShi2, please wait until it has at least 60 days. Skip");
            return;
        }

        double[] close = new double[length];
        int index = 0;
        for (StockPriceVO vo : priceList) {
            close[index++] = vo.close;
        }

        double[] var = talibHelper.getEma(close, 9);

        double[] varUpper = new double[var.length];
        for (int i = 0; i < var.length; i++) {
            varUpper[i] = var[i] * 1.14;
        }

        double[] varLower = new double[var.length];
        for (int i = 0; i < var.length; i++) {
            varLower[i] = var[i] * 0.86;
        }

        double[] xueShi2Upper = talibHelper.getEma(varUpper, 5);
        double[] xueShi2Low = talibHelper.getEma(varLower, 5);

        double up = xueShi2Upper[length - 1];
        double dn = xueShi2Low[length - 1];
        // System.out.println("UP=" + up);
        // System.out.println("DN=" + dn);

        XueShi2VO xueShi2VO = new XueShi2VO();
        xueShi2VO.setStockId(stockId);
        xueShi2VO.setDate(priceList.get(length - 1).date);
        xueShi2VO.setUp(Strings.convert2ScaleDecimal(up));
        xueShi2VO.setDn(Strings.convert2ScaleDecimal(dn));

        this.deleteXueShi2(stockId, xueShi2VO.date);
        xueShi2Table.insert(xueShi2VO);
    }

    public void countAndSaved(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            if (index++ % 500 == 0) {
                System.out.println("Boll countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
            }
            this.countAndSaved(stockId);
        }
    }

    public void run() {

    }

    public void mainWork(String[] args) {
        // TODO Auto-generated method stub
        CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
        this.countAndSaved(stockConfig.getAllStockId());
        // runner.countAndSaved("000979");
    }
}
