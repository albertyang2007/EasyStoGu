package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.db.access.table.IndXueShi2TableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.XueShi2VO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.TALIBWraper;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class HistoryXueShi2CountAndSaveDBRunner {
	private static Logger logger = LogHelper.getLogger(HistoryXueShi2CountAndSaveDBRunner.class);
	@Autowired
    protected IndXueShi2TableHelper xueShi2Table;
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	private CompanyInfoFileHelper stockConfig;
    @Autowired
    protected TALIBWraper talibWraper;

    public void deleteXueShi2(String stockId) {
        xueShi2Table.delete(stockId);
    }

    public void deleteXueShi2(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            logger.debug("Delete YiMengBS for " + stockId + " " + (++index) + " of " + stockIds.size());
            this.deleteXueShi2(stockId);
        }
    }

    public void countAndSaved(String stockId) {
        deleteXueShi2(stockId);

        try {
            List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

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

            double[] var = talibWraper.getEma(close, 9);

            double[] varUpper = new double[var.length];
            for (int i = 0; i < var.length; i++) {
                varUpper[i] = var[i] * 1.14;
            }

            double[] varLower = new double[var.length];
            for (int i = 0; i < var.length; i++) {
                varLower[i] = var[i] * 0.86;
            }

            double[] xueShi2Upper = talibWraper.getEma(varUpper, 5);
            double[] xueShi2Low = talibWraper.getEma(varLower, 5);

            for (index = priceList.size() - 1; index >= 0; index--) {
                double up = xueShi2Upper[index];
                double dn = xueShi2Low[index];
                // logger.debug("UP=" + up);
                // logger.debug("DN=" + dn);

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
                logger.debug("XueShi2 countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
            this.countAndSaved(stockId);
        }
    }

    public void mainWork(String[] args) {
        // TODO Auto-generated method stub
        this.countAndSaved(stockConfig.getAllStockId());
        // runner.countAndSaved("600750");
    }
}
