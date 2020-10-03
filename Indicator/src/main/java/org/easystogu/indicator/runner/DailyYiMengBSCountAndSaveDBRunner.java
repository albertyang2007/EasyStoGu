package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.table.IndYiMengBSTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.YiMengBSVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.YiMengBSHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Doubles;

@Component
public class DailyYiMengBSCountAndSaveDBRunner {
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
    protected IndYiMengBSTableHelper yiMengBSTable;
    @Autowired
    protected YiMengBSHelper yiMengBSHelper = new YiMengBSHelper();
    @Autowired
    protected CompanyInfoFileHelper stockConfig;

    public void deleteYiMengBS(String stockId, String date) {
        yiMengBSTable.delete(stockId, date);
    }

    public void deleteYiMengBS(String stockId) {
        yiMengBSTable.delete(stockId);
    }

    public void deleteYiMengBS(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            System.out.println("Delete YiMengBS for " + stockId + " " + (++index) + "/" + stockIds.size());
            this.deleteYiMengBS(stockId);
        }
    }

    public void countAndSaved(String stockId) {
        List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

        if (priceList.size() <= 108) {
            // System.out.println("StockPrice data is less than 108, skip " +
            // stockId);
            return;
        }

        List<Double> close = StockPriceFetcher.getClosePrice(priceList);
        List<Double> low = StockPriceFetcher.getLowPrice(priceList);
        List<Double> high = StockPriceFetcher.getHighPrice(priceList);

        double[][] yiMeng = yiMengBSHelper.getYiMengBSList(Doubles.toArray(close), Doubles.toArray(low),
                Doubles.toArray(high));

        int length = yiMeng[0].length;

        YiMengBSVO vo = new YiMengBSVO();
        vo.setX2(Strings.convert2ScaleDecimal(yiMeng[0][length - 1]));
        vo.setX3(Strings.convert2ScaleDecimal(yiMeng[1][length - 1]));
        vo.setStockId(stockId);
        vo.setDate(priceList.get(length - 1).date);

        this.deleteYiMengBS(stockId, vo.date);
        yiMengBSTable.insert(vo);

    }

    public void countAndSaved(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            if (index++ % 500 == 0) {
                System.out.println("YiMengBS countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
            }
            this.countAndSaved(stockId);
        }
    }
}
