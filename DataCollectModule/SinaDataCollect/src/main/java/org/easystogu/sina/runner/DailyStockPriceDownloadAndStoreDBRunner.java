package org.easystogu.sina.runner;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.sina.common.RealTimePriceVO;
import org.easystogu.sina.helper.DailyStockPriceDownloadHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//daily get real time stock price from http://hq.sinajs.cn/list=
//it need sotckIds as parameter
@Component
public class DailyStockPriceDownloadAndStoreDBRunner {
	private static Logger logger = LogHelper.getLogger(DailyStockPriceDownloadAndStoreDBRunner.class);
	@Autowired
    private CompanyInfoFileHelper stockConfig;
	@Autowired
	@Qualifier("stockPriceTable")
    private StockPriceTableHelper stockPriceTable;
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
    private QianFuQuanStockPriceTableHelper qianFuQuanStockPriceTable;
	@Autowired
    private DailyStockPriceDownloadHelper sinaHelper;
    @Autowired
	protected CompanyInfoFileHelper companyInfoHelper;


    public void downloadDataAndSaveIntoDB(List<String> allStockIds) {
        int batchSize = 200;
        int batchs = allStockIds.size() / batchSize;
        int totalSize = allStockIds.size();
        logger.debug("Process daily price, totalSize= " + totalSize);
        // 分批取数据
        int index = 0;
        for (; index < batchs; index++) {
            logger.debug("Process daily price " + index + "/" + batchs);
            List<RealTimePriceVO> list = sinaHelper.fetchDataFromWeb(allStockIds.subList(index * batchSize, (index + 1)
                    * batchSize));
            for (RealTimePriceVO vo : list) {
                this.saveIntoDB(vo.convertToStockPriceVO());
            }
        }
        // 去剩余数据
        logger.debug("Process daily price " + index + "/" + batchs);
        List<RealTimePriceVO> list = sinaHelper.fetchDataFromWeb(allStockIds.subList(index * batchSize,
                allStockIds.size()));
        for (RealTimePriceVO vo : list) {
            this.saveIntoDB(vo.convertToStockPriceVO());
        }
    }

    public void saveIntoDB(StockPriceVO vo) {
        try {
            if (vo.isValidated()) {
                // logger.debug("saving into DB, vo=" + vo);
                stockPriceTable.delete(vo.stockId, vo.date);
                qianFuQuanStockPriceTable.delete(vo.stockId, vo.date);
                //houFuQuanStockPriceTable.delete(vo.stockId, vo.date);

                if (vo.stockId.equals(this.companyInfoHelper.getSZCZStockIdForDB())
                        || vo.stockId.equals(this.companyInfoHelper.getCYBZStockIdForDB()))
                    vo.volume = vo.volume / 100;

                stockPriceTable.insert(vo);
                qianFuQuanStockPriceTable.insert(vo);
                //houFuQuanStockPriceTable.insert(vo);
            } else {
                logger.debug("vo invalidate: " + vo);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.debug("Can't save to DB, vo=" + vo + ", error=" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() {
        List<String> shStockIds = stockConfig.getAllSHStockId("sh");
        List<String> szStockIds = stockConfig.getAllSZStockId("sz");

        List<String> allStockIds = new ArrayList<String>();

        allStockIds.add(stockConfig.getSZZSStockIdForSina());
        allStockIds.add(stockConfig.getSZCZStockIdForSina());
        allStockIds.add(stockConfig.getCYBZStockIdForSina());
        allStockIds.addAll(shStockIds);
        allStockIds.addAll(szStockIds);

        downloadDataAndSaveIntoDB(allStockIds);
    }
}
