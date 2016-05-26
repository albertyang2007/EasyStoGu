package org.easystogu.indicator.runner;

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

public class DailyWRCountAndSaveDBRunner implements Runnable {

	private WRHelper wrHelper = new WRHelper();
	protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	protected IndWRTableHelper wrTable = IndWRTableHelper.getInstance();
	protected CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();

	public DailyWRCountAndSaveDBRunner() {

	}

	public void deleteWR(String stockId, String date) {
		wrTable.delete(stockId, date);
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

		if (priceList.size() < 1) {
			return;
		}

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);
		List<Double> low = StockPriceFetcher.getLowPrice(priceList);
		List<Double> high = StockPriceFetcher.getHighPrice(priceList);

		double[][] wr = wrHelper.getWRList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high), 42, 21);

		int length = wr[0].length;

		// for (int i = 0; i < KDJ[0].length; i++) {
		WRVO vo = new WRVO();
		vo.setLonTerm(Strings.convert2ScaleDecimal(wr[0][length - 1]));
		vo.setShoTerm(Strings.convert2ScaleDecimal(wr[1][length - 1]));
		// vo.setMidTerm(Strings.convert2ScaleDecimal(wr[2][length - 1]));
		vo.setStockId(stockId);
		vo.setDate(priceList.get(length - 1).date);

		// System.out.println(vo);
		this.deleteWR(stockId, vo.date);
		wrTable.insert(vo);

	}

	public void countAndSaved(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			if (index++ % 500 == 0) {
				System.out.println("WR countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
			}
			this.countAndSaved(stockId);
		}
	}

	public void run() {

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		DailyWRCountAndSaveDBRunner runner = new DailyWRCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("999999");
	}

}
