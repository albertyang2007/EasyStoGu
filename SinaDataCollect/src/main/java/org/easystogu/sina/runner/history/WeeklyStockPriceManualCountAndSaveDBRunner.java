package org.easystogu.sina.runner.history;

import java.util.List;

import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.WeekdayUtil;

//手动将2009年之后的stockprice分成每周入库，weeksotckprice，一次性运行
public class WeeklyStockPriceManualCountAndSaveDBRunner {
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private WeekStockPriceTableHelper weekStockPriceTable = WeekStockPriceTableHelper.getInstance();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();

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
		for (int year = 2015; year <= 2015; year++) {
			for (int week = 25; week <= 29; week++) {
				List<String> dates = WeekdayUtil.getWorkingDaysOfWeek(year, week);
				if ((dates != null) && (dates.size() >= 1)) {
					String firstDate = dates.get(0);
					String lastDate = dates.get(dates.size() - 1);
					List<StockPriceVO> spList = stockPriceTable.getStockPriceByIdAndBetweenDate(stockId, firstDate,
							lastDate);
					if ((spList != null) && (spList.size() >= 1)) {

						// update price based on chuQuanChuXi event
						chuQuanChuXiPriceHelper.updateWeekPrice(stockId, spList, firstDate, lastDate);

						int last = spList.size() - 1;
						// first day
						StockPriceVO mergeVO = spList.get(0).copy();
						// last day
						mergeVO.close = spList.get(last).close;
						mergeVO.date = spList.get(last).date;

						if (spList.size() > 1) {
							for (int j = 1; j < spList.size(); j++) {
								StockPriceVO vo = spList.get(j);
								mergeVO.volume += vo.volume;
								if (mergeVO.high < vo.high) {
									mergeVO.high = vo.high;
								}
								if (mergeVO.low > vo.low) {
									mergeVO.low = vo.low;
								}
							}
						}
						weekStockPriceTable.delete(stockId, mergeVO.date);
						weekStockPriceTable.insert(mergeVO);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		WeeklyStockPriceManualCountAndSaveDBRunner runner = new WeeklyStockPriceManualCountAndSaveDBRunner();
		runner.countAndSave(stockConfig.getAllStockId());
		// runner.countAndSave("002214");
	}

}
