package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.table.IndMai1Mai2TableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.vo.table.Mai1Mai2VO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.Mai1Mai2Helper;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DailyMai1Mai2CountAndSaveDBRunner {
	private static Logger logger = LogHelper.getLogger(DailyMai1Mai2CountAndSaveDBRunner.class);
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
    protected QianFuQuanStockPriceTableHelper qianFuQuanStockPriceTable;
	@Autowired
    protected IndMai1Mai2TableHelper mai1mai2Table;

    private Mai1Mai2Helper mai1mai2Helper = new Mai1Mai2Helper();
    @Autowired
    protected CompanyInfoFileHelper stockConfig;

    public void deleteMai1Mai2(String stockId, String date) {
        mai1mai2Table.delete(stockId, date);
    }

    public void deleteMai1Mai2(String stockId) {
        mai1mai2Table.delete(stockId);
    }

    public void deleteMai1Mai2(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            logger.debug("Delete Mai1Mai2 for " + stockId + " " + (++index) + "/" + stockIds.size());
            this.deleteMai1Mai2(stockId);
        }
    }

    public void countAndSaved(String stockId) {
        List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

        if (priceList.size() <= 20) {
            // logger.debug("StockPrice data is less than 20, skip " +
            // stockId);
            return;
        }

        // list is order by date
        int length = priceList.size();
        double[] var1 = new double[length];
        int index = 0;
        for (StockPriceVO vo : priceList) {
            var1[index++] = (2 * vo.close + vo.open + vo.high + vo.low) / 5;
        }

        double[][] mai1mai2 = mai1mai2Helper.getMai1Mai2List(var1);

        Mai1Mai2VO vo = new Mai1Mai2VO();
        vo.setSd(Strings.convert2ScaleDecimal(mai1mai2[0][length - 1]));
        vo.setSk(Strings.convert2ScaleDecimal(mai1mai2[1][length - 1]));
        vo.setStockId(stockId);
        vo.setDate(priceList.get(length - 1).date);

        this.deleteMai1Mai2(stockId, vo.date);
        mai1mai2Table.insert(vo);

    }

    public void countAndSaved(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            if (index++ % 500 == 0) {
                logger.debug("Mai1Mai2 countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
            }
            this.countAndSaved(stockId);
        }
    }
}
