package org.easystogu.sina.runner.history;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.Strings;

public class HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner {
	private QianFuQuanStockPriceTableHelper qianfuquanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private CompanyInfoFileHelper companyInfoHelper = CompanyInfoFileHelper.getInstance();

	// priceList is order by date from stockPrice
	// scan stockprce to count chuquan event and count the qian fuquan
	// stockprice
	// 使用除权事件计算前除权数据
	private List<StockPriceVO> updateQianFuQianPriceBasedOnChuQuanEvent(String stockId, List<StockPriceVO> spList) {
		if (companyInfoHelper.isStockIdAMajorZhiShu(stockId)) {
			return spList;
		}

		List<StockPriceVO> chuQuanSPList = new ArrayList<StockPriceVO>();

		// count the qian fuquan stockprice for stockid from the latest chuquan
		// event
		int chuquan_index = spList.size() - 1;
		double sumRate = 1.0;
		for (int index = spList.size() - 1; index >= 1; index--) {
			StockPriceVO vo = spList.get(index);
			StockPriceVO prevo = spList.get(index - 1);
			double rate = 1.0;
			if (vo.lastClose != 0 && prevo.close != 0 && vo.lastClose != prevo.close) {
				chuquan_index = index - 1;
				rate = prevo.close / vo.lastClose;
				// System.out.println("chuquan index= " + chuquan_index + " at "
				// + prevo.date + " rate=" + rate);
			}
			// add the chuQuan VO
			StockPriceVO cqVO = vo.copy();
			cqVO.open = Strings.convert2ScaleDecimal(vo.open / sumRate);
			cqVO.close = Strings.convert2ScaleDecimal(vo.close / sumRate);
			cqVO.low = Strings.convert2ScaleDecimal(vo.low / sumRate);
			cqVO.high = Strings.convert2ScaleDecimal(vo.high / sumRate);
			chuQuanSPList.add(cqVO);

			// update the sumRate
			sumRate = rate * sumRate;
		}
		// fix a bug
		// add the first vo
		if (spList != null && spList.size() >= 1)
			chuQuanSPList.add(spList.get(0));

		return chuQuanSPList;
	}

	public void countAndSave(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out
					.println("Process qian fuquan price for " + stockId + ", " + (++index) + " of " + stockIds.size());
			this.countAndSave(stockId);
		}
	}

	public void countAndSave(String stockId) {
		try {
			List<StockPriceVO> spList = stockPriceTable.getStockPriceById(stockId);
			List<StockPriceVO> chuQuanSPList = this.updateQianFuQianPriceBasedOnChuQuanEvent(stockId, spList);
			this.qianfuquanStockPriceTable.delete(stockId);
			this.qianfuquanStockPriceTable.insert(chuQuanSPList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner runner = new HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner();
		// must include major indicator
		List<String> stockIds = runner.companyInfoHelper.getAllStockId();
		// for all stockIds
		runner.countAndSave(stockIds);
		// for specify stockId
		// runner.countAndSave("999999");
		// runner.countAndSave("399001");
		// runner.countAndSave("601388");
	}
}
