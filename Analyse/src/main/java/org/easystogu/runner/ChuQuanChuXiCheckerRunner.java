package org.easystogu.runner;

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

public class ChuQuanChuXiCheckerRunner implements Runnable {
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected EventChuQuanChuXiTableHelper chuQuanChuXiTable = EventChuQuanChuXiTableHelper.getInstance();
	protected CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();

	private void checkIfGaoSongZhuanExist(String stockId) {
		// get latest two day vo
		List<StockPriceVO> list = stockPriceTable.getNdateStockPriceById(stockId, 2);
		if (list != null && list.size() == 2) {
			StockPriceVO cur = list.get(0);
			StockPriceVO pre = list.get(1);
			// System.out.println(cur);
			// System.out.println(pre);
			if (cur.lastClose != 0 && cur.lastClose != pre.close) {
				// chuQuan happen!
				ChuQuanChuXiVO vo = chuQuanChuXiTable.getChuQuanChuXiVO(stockId, cur.date);
				if (vo == null) {
					vo = new ChuQuanChuXiVO();
					vo.setStockId(cur.stockId);
					vo.setDate(cur.date);
					vo.setRate(cur.lastClose / pre.close);
					vo.setAlreadyUpdatePrice(false);

					// System.out.println("ChuQuan happen for " + vo);
					chuQuanChuXiTable.insert(vo);
				}
			}
		}

	}

	private void checkIfChuQuanChuXiExist(List<String> stockIds) {
		System.out.println("Run chuQuan for all stocks.");
		for (String stockId : stockIds) {
			if (stockId.equals(stockConfig.getSZZSStockIdForDB()) 
					|| stockId.equals(stockConfig.getSZCZStockIdForDB())
					|| stockId.equals(stockConfig.getCYBZStockIdForDB())) {
				continue;
			}
			this.checkIfGaoSongZhuanExist(stockId);
		}
	}

	public void runForStockIds(List<String> stockIds) {
		checkIfChuQuanChuXiExist(stockIds);
	}

	public void run() {
		checkIfChuQuanChuXiExist(stockConfig.getAllStockId());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		ChuQuanChuXiCheckerRunner runner = new ChuQuanChuXiCheckerRunner();
		runner.checkIfChuQuanChuXiExist(stockConfig.getAllStockId());
	}
}
