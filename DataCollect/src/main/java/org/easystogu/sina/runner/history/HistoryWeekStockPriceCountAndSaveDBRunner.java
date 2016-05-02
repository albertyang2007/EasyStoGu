package org.easystogu.sina.runner.history;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.util.MergeNDaysPriceUtil;
import org.easystogu.file.access.CompanyInfoFileHelper;

//手动将2009年之后的stockprice分成每周入库，weeksotckprice，一次性运行
public class HistoryWeekStockPriceCountAndSaveDBRunner {
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private MergeNDaysPriceUtil weekPriceMergeUtil = new MergeNDaysPriceUtil();
	private WeekStockPriceTableHelper weekStockPriceTable = WeekStockPriceTableHelper.getInstance();

	public void deleteStockPrice(String stockId) {
		weekStockPriceTable.delete(stockId);
	}

	public void deleteStockPrice(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Delete stock price for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteStockPrice(stockId);
		}
	}

	public void countAndSave(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Process weekly price for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.countAndSave(stockId);
		}
	}

	public void countAndSave(String stockId) {
		List<StockPriceVO> spWeekList = weekPriceMergeUtil.generateAllWeekPriceVO(stockId,
				stockPriceTable.getStockPriceById(stockId));
		for (StockPriceVO mergeVO : spWeekList) {
			weekStockPriceTable.delete(mergeVO.stockId, mergeVO.date);
			weekStockPriceTable.insert(mergeVO);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		HistoryWeekStockPriceCountAndSaveDBRunner runner = new HistoryWeekStockPriceCountAndSaveDBRunner();
		runner.countAndSave(stockConfig.getAllStockId());
		//runner.countAndSave("999999");
		//runner.countAndSave("399001");
		//runner.countAndSave("399006");
	}

}
