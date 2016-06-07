package org.easystogu.runner;

import java.util.Collections;
import java.util.List;

import org.easystogu.db.access.EventChuQuanChuXiTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.ChuQuanChuXiVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;

//if table event_gaosongzhuan has update, please run this runner 
//to update all the gaoSongZhuan price data
//manually to update gaoSongZhuan table, pls refer to 
//http://www.cninfo.com.cn/search/memo.jsp?datePara=2015-05-13

//do not use this class now, do not use the table EventChuQuanChuXiTableHelper now
public class ChuQuanChuXiCheckerRunner implements Runnable {
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected EventChuQuanChuXiTableHelper chuQuanChuXiTable = EventChuQuanChuXiTableHelper.getInstance();
	protected CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();

	public void historyCheckChuQuanEvent(String stockId) {
		// get all vo
		// list is order by date
		List<StockPriceVO> list = stockPriceTable.getStockPriceById(stockId);
		// revert the list order. now order by date desc
		Collections.reverse(list);

		// first delete old data
		chuQuanChuXiTable.delete(stockId);

		for (int index = 0; index < list.size() - 1; index++) {
			StockPriceVO spvo = list.get(index);
			StockPriceVO yesterday_spvo = list.get(index + 1);
			// System.out.println(cur);
			// System.out.println(pre);

			if ((spvo.close > 0 && yesterday_spvo.close > 0) && (spvo.close / yesterday_spvo.close <= 0.85)) {
				// chuQuan happen!
				ChuQuanChuXiVO vo = new ChuQuanChuXiVO();
				vo.setStockId(spvo.stockId);
				vo.setDate(spvo.date);
				vo.setRate(spvo.lastClose / yesterday_spvo.close);
				vo.setAlreadyUpdatePrice(false);

				System.out.println("ChuQuan happen for " + vo);
				chuQuanChuXiTable.insert(vo);
			}
		}
	}

	public void historyCheckChuQuanEvent(List<String> stockIds) {
		System.out.println("Run chuQuan for all stocks.");
		for (String stockId : stockIds) {
			if (stockId.equals(stockConfig.getSZZSStockIdForDB()) || stockId.equals(stockConfig.getSZCZStockIdForDB())
					|| stockId.equals(stockConfig.getCYBZStockIdForDB())) {
				continue;
			}
			this.historyCheckChuQuanEvent(stockId);
		}
	}

	public void dailyCheckChuQuanEvent(String stockId) {
		// get latest two day vo
		// list is order by date desc
		List<StockPriceVO> list = stockPriceTable.getNdateStockPriceById(stockId, 2);
		if (list != null && list.size() == 2) {
			StockPriceVO spvo = list.get(0);
			StockPriceVO yesterday_spvo = list.get(1);
			// System.out.println(cur);
			// System.out.println(pre);
			if ((spvo.close > 0 && yesterday_spvo.close > 0) && (spvo.close / yesterday_spvo.close <= 0.85)) {
				// chuQuan happen!
				ChuQuanChuXiVO vo = chuQuanChuXiTable.getChuQuanChuXiVO(stockId, spvo.date);
				if (vo == null) {
					vo = new ChuQuanChuXiVO();
					vo.setStockId(spvo.stockId);
					vo.setDate(spvo.date);
					vo.setRate(spvo.lastClose / yesterday_spvo.close);
					vo.setAlreadyUpdatePrice(false);

					// System.out.println("ChuQuan happen for " + vo);
					chuQuanChuXiTable.insert(vo);
				}
			}
		}

	}

	public void dailyCheckChuQuanEvent(List<String> stockIds) {
		System.out.println("Run chuQuan for all stocks.");
		for (String stockId : stockIds) {
			if (stockId.equals(stockConfig.getSZZSStockIdForDB()) || stockId.equals(stockConfig.getSZCZStockIdForDB())
					|| stockId.equals(stockConfig.getCYBZStockIdForDB())) {
				continue;
			}
			this.dailyCheckChuQuanEvent(stockId);
		}
	}

	public void run() {
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		ChuQuanChuXiCheckerRunner runner = new ChuQuanChuXiCheckerRunner();
		runner.historyCheckChuQuanEvent(stockConfig.getAllStockId());
		// runner.historyCheckChuQuanEvent("002609");
	}
}
