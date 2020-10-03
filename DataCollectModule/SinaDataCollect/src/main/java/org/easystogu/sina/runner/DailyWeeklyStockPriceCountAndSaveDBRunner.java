package org.easystogu.sina.runner;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.WeekdayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//每日stockprice入库之后计算本周的stockprice，入库
@Component
public class DailyWeeklyStockPriceCountAndSaveDBRunner {
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
    private QianFuQuanStockPriceTableHelper qianFuQuanStockPriceTable;
	@Autowired
	@Qualifier("weekStockPriceTable")
    private WeekStockPriceTableHelper weekStockPriceTable;
	@Autowired
	protected CompanyInfoFileHelper stockConfig;

	public void deleteWeekStockPrice(String stockId, String date) {
		weekStockPriceTable.delete(stockId, date);
	}

	public void countAndSave(List<String> stockIds) {
	String latestDate = qianFuQuanStockPriceTable.getLatestStockDate();
		
	  stockIds.parallelStream().forEach(stockId -> {
        this.countAndSaved(stockId, latestDate);
      });
	  
//		int index = 0;
//		for (String stockId : stockIds) {
//			if (index++ % 500 == 0) {
//				System.out.println("Process weekly price " + (index) + "/" + stockIds.size());
//			}
//			this.countAndSaved(stockId);
//		}
	}

	public void countAndSaved(String stockId, String latestDate) {
		// first clean one tuple in week_stockprice table
		// loop all this week's date, in fact, only one tuple match and
		// del		
		List<String> dates = WeekdayUtil.getWeekWorkingDates(latestDate);
		for (String date : dates) {
			this.deleteWeekStockPrice(stockId, date);
		}

		if ((dates != null) && (dates.size() >= 1)) {
			String firstDate = dates.get(0);
			String lastDate = dates.get(dates.size() - 1);
			List<StockPriceVO> spList = this.getStockPriceByIdAndBetweenDate(stockId, firstDate, lastDate);
			if ((spList != null) && (spList.size() >= 1)) {

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
				// System.out.println(mergeVO);
				weekStockPriceTable.insert(mergeVO);
			}
		}
	}

	private List<StockPriceVO> getStockPriceByIdAndBetweenDate(String stockId, String firstDate, String lastDate) {
		List<StockPriceVO> rtnList = new ArrayList<StockPriceVO>();
		List<StockPriceVO> spList = qianFuQuanStockPriceTable.getStockPriceById(stockId);
		for (StockPriceVO vo : spList) {
			if (vo.date.compareTo(firstDate) >= 0 && vo.date.compareTo(lastDate) <= 0) {
				rtnList.add(vo);
			}
		}
		return rtnList;
	}

	public void run() {
		countAndSave(stockConfig.getAllStockId());
	}
}
