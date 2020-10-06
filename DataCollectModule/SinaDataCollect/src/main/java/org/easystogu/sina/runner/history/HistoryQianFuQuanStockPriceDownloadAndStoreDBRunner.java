package org.easystogu.sina.runner.history;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner {
	private static Logger logger = LogHelper.getLogger(HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner.class);
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	private QianFuQuanStockPriceTableHelper qianfuquanStockPriceTable;
	@Autowired
	@Qualifier("stockPriceTable")
	private StockPriceTableHelper stockPriceTable;
	@Autowired
	protected CompanyInfoFileHelper companyInfoHelper;

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
				// logger.debug("chuquan index= " + chuquan_index + " at "
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
		stockIds.parallelStream().forEach(
				stockId -> this.countAndSave(stockId)
		);
		//int index = 0;
		//for (String stockId : stockIds) {
		//	System.out
		//			.println("Process qian fuquan price for " + stockId + ", " + (++index) + " of " + stockIds.size());
		//	this.countAndSave(stockId);
		//}
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

	public void mainWork(String[] args) {
		// must include major indicator
		List<String> stockIds = this.companyInfoHelper.getAllStockId();
		// for all stockIds
		this.countAndSave(stockIds);
		// for specify stockId
		// runner.countAndSave("999999");
		// runner.countAndSave("399001");
		//runner.countAndSave("000049");
	}
}
