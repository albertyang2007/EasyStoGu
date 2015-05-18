package org.easystogu.db.access;

import java.util.List;

import org.easystogu.db.table.ChuQuanChuXiVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.table.StockSuperVO;

public class ChuQuanChuXiPriceHelper {

	protected EventChuQuanChuXiTableHelper chuQuanChuXiTable = EventChuQuanChuXiTableHelper.getInstance();

	public void updateAllPrice(String stockId, List<StockPriceVO> priceList) {
		List<ChuQuanChuXiVO> list = chuQuanChuXiTable.getAllChuQuanChuXiVO(stockId);
		// list is order by date
		for (ChuQuanChuXiVO chuQuanVO : list) {
			for (StockPriceVO priceVO : priceList) {
				if (priceVO.date.compareTo(chuQuanVO.date) < 0) {
					priceVO.close = priceVO.close * chuQuanVO.rate;
					priceVO.open = priceVO.open * chuQuanVO.rate;
					priceVO.high = priceVO.high * chuQuanVO.rate;
					priceVO.low = priceVO.low * chuQuanVO.rate;
				}
			}
		}
	}

	public void updatePrice(String stockId, List<StockPriceVO> priceList) {
		List<ChuQuanChuXiVO> list = chuQuanChuXiTable.getNDateChuQuanChuXiVO(stockId, 1);

		if (list == null || list.size() == 0)
			return;

		ChuQuanChuXiVO chuQuanVO = list.get(0);

		for (StockPriceVO priceVO : priceList) {
			if (priceVO.date.compareTo(chuQuanVO.date) < 0) {
				priceVO.close = priceVO.close * chuQuanVO.rate;
				priceVO.open = priceVO.open * chuQuanVO.rate;
				priceVO.high = priceVO.high * chuQuanVO.rate;
				priceVO.low = priceVO.low * chuQuanVO.rate;
			}
		}
	}

	public void updateWeekPrice(String stockId, List<StockPriceVO> priceList, String firstDate, String lastDate) {
		List<ChuQuanChuXiVO> list = chuQuanChuXiTable.getNDateChuQuanChuXiVO(stockId, 1);

		if (list == null || list.size() == 0)
			return;

		ChuQuanChuXiVO chuQuanVO = list.get(0);

		for (StockPriceVO priceVO : priceList) {
			// only limit the price update when chuQuan happens at that week
			if (chuQuanVO.date.compareTo(firstDate) >= 0 && chuQuanVO.date.compareTo(lastDate) <= 0) {
				if (priceVO.date.compareTo(chuQuanVO.date) < 0) {
					priceVO.close = priceVO.close * chuQuanVO.rate;
					priceVO.open = priceVO.open * chuQuanVO.rate;
					priceVO.high = priceVO.high * chuQuanVO.rate;
					priceVO.low = priceVO.low * chuQuanVO.rate;
				}
			}
		}
	}

	public void updateSuperPrice(String stockId, List<StockSuperVO> superList) {
		List<ChuQuanChuXiVO> list = chuQuanChuXiTable.getNDateChuQuanChuXiVO(stockId, 1);

		if (list == null || list.size() == 0)
			return;

		ChuQuanChuXiVO chuQuanVO = list.get(0);

		for (StockSuperVO superVO : superList) {
			if (superVO.priceVO.date.compareTo(chuQuanVO.date) < 0) {
				superVO.priceVO.close = superVO.priceVO.close * chuQuanVO.rate;
				superVO.priceVO.open = superVO.priceVO.open * chuQuanVO.rate;
				superVO.priceVO.high = superVO.priceVO.high * chuQuanVO.rate;
				superVO.priceVO.low = superVO.priceVO.low * chuQuanVO.rate;
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
