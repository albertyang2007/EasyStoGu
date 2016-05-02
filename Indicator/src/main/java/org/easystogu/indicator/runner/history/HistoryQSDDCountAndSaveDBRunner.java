package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndQSDDTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.QSDDVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.QSDDHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.utils.Strings;

import com.google.common.primitives.Doubles;

public class HistoryQSDDCountAndSaveDBRunner {
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected IndQSDDTableHelper qsddTable = IndQSDDTableHelper.getInstance();
	protected QSDDHelper qsddHelper = new QSDDHelper();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();

	public void deleteQSDD(String stockId) {
		qsddTable.delete(stockId);
	}

	public void deleteQSDD(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Delete KDJ for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteQSDD(stockId);
		}
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

		if (priceList.size() < 1) {
			return;
		}

		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updateQianFuQianPriceBasedOnHouFuQuan(stockId, priceList);

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);
		List<Double> low = StockPriceFetcher.getLowPrice(priceList);
		List<Double> high = StockPriceFetcher.getHighPrice(priceList);

		double[][] qsdd = qsddHelper.getQSDDList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));

		for (int i = 0; i < qsdd[0].length; i++) {
			QSDDVO vo = new QSDDVO();
			vo.setLonTerm(Strings.convert2ScaleDecimal(qsdd[0][i], 3));
			vo.setShoTerm(Strings.convert2ScaleDecimal(qsdd[1][i], 3));
			vo.setMidTerm(Strings.convert2ScaleDecimal(qsdd[2][i], 3));
			vo.setStockId(stockId);
			vo.setDate(priceList.get(i).date);

			try {
				// if (vo.date.compareTo("2015-06-29") >= 0)
				// if (qsddTable.getQSDD(vo.stockId, vo.date) == null) {
				qsddTable.insert(vo);
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void countAndSaved(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			if (index++ % 100 == 0)
				System.out.println("QSDD countAndSaved: " + stockId + " " + (index) + " of " + stockIds.size());
			this.deleteQSDD(stockId);
			this.countAndSaved(stockId);
		}
	}

	// TODO Auto-generated method stub
	// 一次性计算数据库中所有KDJ数据，入库
	public static void main(String[] args) {
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		HistoryQSDDCountAndSaveDBRunner runner = new HistoryQSDDCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("999999");
	}
}
