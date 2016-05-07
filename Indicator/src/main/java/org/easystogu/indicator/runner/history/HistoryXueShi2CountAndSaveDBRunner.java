package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.db.access.IndXueShi2TableHelper;
import org.easystogu.db.access.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.table.XueShi2VO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.TALIBWraper;
import org.easystogu.utils.Strings;

public class HistoryXueShi2CountAndSaveDBRunner {

    protected IndXueShi2TableHelper xueShi2Table = IndXueShi2TableHelper.getInstance();
    protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
    protected TALIBWraper talib = new TALIBWraper();

    public void deleteXueShi2(String stockId) {
        xueShi2Table.delete(stockId);
    }

    public void deleteXueShi2(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            System.out.println("Delete YiMengBS for " + stockId + " " + (++index) + " of " + stockIds.size());
            this.deleteXueShi2(stockId);
        }
    }

    public void countAndSaved(String stockId) {
        deleteXueShi2(stockId);

        try {
            List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

            int length = priceList.size();

            if (length < 60) {
                System.out
                        .println(stockId
                                + " price data is not enough to count XueShi2, please wait until it has at least 60 days. Skip");
                return;
            }

            double[] close = new double[length];
            int index = 0;
            for (StockPriceVO vo : priceList) {
                close[index++] = vo.close;
            }

            double[] var = talib.getEma(close, 9);

            double[] varUpper = new double[var.length];
            for (int i = 0; i < var.length; i++) {
                varUpper[i] = var[i] * 1.14;
            }

            double[] varLower = new double[var.length];
            for (int i = 0; i < var.length; i++) {
                varLower[i] = var[i] * 0.86;
            }

            double[] xueShi2Upper = talib.getEma(varUpper, 5);
            double[] xueShi2Low = talib.getEma(varLower, 5);

            for (index = priceList.size() - 1; index >= 0; index--) {
                double up = xueShi2Upper[index];
                double dn = xueShi2Low[index];
                // System.out.println("UP=" + up);
                // System.out.println("DN=" + dn);

                XueShi2VO xueShi2VO = new XueShi2VO();
                xueShi2VO.setStockId(stockId);
                xueShi2VO.setDate(priceList.get(index).date);
                xueShi2VO.setUp(Strings.convert2ScaleDecimal(up));
                xueShi2VO.setDn(Strings.convert2ScaleDecimal(dn));

                // if (xueShi2VO.date.compareTo("2015-06-29") >= 0)
                //if (xueShi2Table.getXueShi2(xueShi2VO.stockId, xueShi2VO.date) == null) {
                xueShi2Table.insert(xueShi2VO);
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void countAndSaved(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            if (index++ % 100 == 0)
                System.out.println("XueShi2 countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
            this.countAndSaved(stockId);
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
        HistoryXueShi2CountAndSaveDBRunner runner = new HistoryXueShi2CountAndSaveDBRunner();
        runner.countAndSaved(stockConfig.getAllStockId());
        // runner.countAndSaved("600750");
    }
}
