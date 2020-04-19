package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.KDJHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.utils.Strings;

import com.google.common.primitives.Doubles;

public class DailyKDJCountAndSaveDBRunner implements Runnable {
	protected IndicatorDBHelperIF kdjTable = DBAccessFacdeFactory.getInstance(Constants.indKDJ);
	private KDJHelper kdjHelper = new KDJHelper();
	protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	protected CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();

	public DailyKDJCountAndSaveDBRunner() {

	}

	public void deleteKDJ(String stockId, String date) {
		kdjTable.delete(stockId, date);
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

		if (priceList.size() <= 9) {
			// System.out.println("StockPrice data is less than 9, skip " +
			// stockId);
			return;
		}

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);
		List<Double> low = StockPriceFetcher.getLowPrice(priceList);
		List<Double> high = StockPriceFetcher.getHighPrice(priceList);

		double[][] KDJ = kdjHelper.getKDJList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));

		int index = priceList.size() - 1;
		
		KDJVO vo = new KDJVO();
		vo.setK(Strings.convert2ScaleDecimal(KDJ[0][index]));
		vo.setD(Strings.convert2ScaleDecimal(KDJ[1][index]));
		vo.setJ(Strings.convert2ScaleDecimal(KDJ[2][index]));
		vo.setRsv(Strings.convert2ScaleDecimal(KDJ[3][index]));
		vo.setStockId(stockId);
		vo.setDate(priceList.get(index).date);

		this.deleteKDJ(stockId, vo.date);

		kdjTable.insert(vo);
	}

	public void countAndSaved(List<String> stockIds) {
	  stockIds.stream().forEach(stockId -> {
        this.countAndSaved(stockId);
      });
	  
//		int index = 0;
//		for (String stockId : stockIds) {
//			if (index++ % 500 == 0) {
//				System.out.println("KDJ countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
//			}
//			this.countAndSaved(stockId);
//		}
	}

	public void run() {
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		DailyKDJCountAndSaveDBRunner runner = new DailyKDJCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002609");
	}
}
