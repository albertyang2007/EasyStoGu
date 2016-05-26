package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.db.access.IndWRTableHelper;
import org.easystogu.db.access.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.table.WRVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.WRHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.utils.Strings;

import com.google.common.primitives.Doubles;

public class HistoryWRCountAndSaveDBRunner {

	protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	protected IndWRTableHelper wrTable = IndWRTableHelper.getInstance();
	protected WRHelper wrHelper = new WRHelper();

	public void deleteWR(String stockId) {
		wrTable.delete(stockId);
	}

	public void deleteWR(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Delete WR for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteWR(stockId);
		}
	}

	public void countAndSaved(String stockId) {
		this.deleteWR(stockId);

		List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

		if (priceList.size() < 1) {
			return;
		}

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);
		List<Double> low = StockPriceFetcher.getLowPrice(priceList);
		List<Double> high = StockPriceFetcher.getHighPrice(priceList);

		double[][] wr = wrHelper.getWRList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high), 42,
				21);

		for (int i = 0; i < wr[0].length; i++) {
			WRVO vo = new WRVO();
			vo.setLonTerm(Strings.convert2ScaleDecimal(wr[0][i], 3));
			vo.setShoTerm(Strings.convert2ScaleDecimal(wr[1][i], 3));
			//vo.setMidTerm(Strings.convert2ScaleDecimal(wr[2][i], 3));
			vo.setStockId(stockId);
			vo.setDate(priceList.get(i).date);

			try {
				// if (vo.date.compareTo("2015-06-29") >= 0)
				// if (qsddTable.getWR(vo.stockId, vo.date) == null) {
				wrTable.insert(vo);
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
				System.out.println("WR countAndSaved: " + stockId + " " + (index) + " of " + stockIds.size());
			this.countAndSaved(stockId);
		}
	}

	// TODO Auto-generated method stub
	// 一次性计算数据库中所有WR数据，入库
	public static void main(String[] args) {
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		HistoryWRCountAndSaveDBRunner runner = new HistoryWRCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		//runner.countAndSaved("000673");
	}

}
