package org.easystogu.sina.runner.history;

import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.db.util.MergeNDaysPriceUtil;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//手动将2009年之后的stockprice分成每周入库，weeksotckprice，一次性运行
@Component
public class HistoryWeekStockPriceCountAndSaveDBRunner {
	private static Logger logger = LogHelper.getLogger(HistoryWeekStockPriceCountAndSaveDBRunner.class);
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
    private QianFuQuanStockPriceTableHelper qianFuQuanStockPriceTable;
	@Autowired
	@Qualifier("weekStockPriceTable")
    private WeekStockPriceTableHelper weekStockPriceTable;
	@Autowired
    private MergeNDaysPriceUtil nDaysPriceMergeUtil;
	@Autowired
	private CompanyInfoFileHelper stockConfig;

    public void deleteStockPrice(String stockId) {
        weekStockPriceTable.delete(stockId);
    }

    public void deleteStockPrice(List<String> stockIds) {
        int index = 0;
        for (String stockId : stockIds) {
            logger.debug("Delete stock price for " + stockId + " " + (++index) + " of " + stockIds.size());
            this.deleteStockPrice(stockId);
        }
    }

    public void countAndSave(List<String> stockIds) {
        stockIds.parallelStream().forEach(
                stockId -> this.countAndSave(stockId)
        );
        //int index = 0;
        //for (String stockId : stockIds) {
        //    logger.debug("Process weekly price for " + stockId + " " + (++index) + " of " + stockIds.size());
        //    this.countAndSave(stockId);
        //}
    }

    public void countAndSave(String stockId) {
        // update price based on chuQuanChuXi event
        weekStockPriceTable.delete(stockId);
        
        List<StockPriceVO> spList = qianFuQuanStockPriceTable.getStockPriceById(stockId);
        List<StockPriceVO> spWeekList = nDaysPriceMergeUtil.generateAllWeekPriceVO(stockId, spList);
        for (StockPriceVO mergeVO : spWeekList) {
            weekStockPriceTable.insert(mergeVO);
        }
    }

    public void mainWork(String[] args) {
        this.countAndSave(stockConfig.getAllStockId());
        //runner.countAndSave("999999");
        //runner.countAndSave("399001");
        //runner.countAndSave("399006");
        //runner.countAndSave("000049");
    }

}
