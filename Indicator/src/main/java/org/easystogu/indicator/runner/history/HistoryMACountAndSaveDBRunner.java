package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.db.access.table.IndMATableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.MAVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.MAHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.utils.Strings;

import com.google.common.primitives.Doubles;

public class HistoryMACountAndSaveDBRunner {

	protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	protected IndMATableHelper maTable = IndMATableHelper.getInstance();
	protected MAHelper maHelper = new MAHelper();

	public void deleteMA(String stockId) {
		maTable.delete(stockId);
	}

	public void deleteMA(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Delete MA for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteMA(stockId);
		}
	}

	public void countAndSaved(String stockId) {
		this.deleteMA(stockId);

		List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);

		double[] cls = maHelper.getMAList(Doubles.toArray(close), 1);
		double[] ma5 = maHelper.getMAList(Doubles.toArray(close), 5);
		double[] ma10 = maHelper.getMAList(Doubles.toArray(close), 10);
		double[] ma19 = maHelper.getMAList(Doubles.toArray(close), 19);
		double[] ma20 = maHelper.getMAList(Doubles.toArray(close), 20);
		double[] ma30 = maHelper.getMAList(Doubles.toArray(close), 30);
		double[] ma43 = maHelper.getMAList(Doubles.toArray(close), 43);
		double[] ma60 = maHelper.getMAList(Doubles.toArray(close), 60);
		double[] ma86 = maHelper.getMAList(Doubles.toArray(close), 86);
		double[] ma120 = maHelper.getMAList(Doubles.toArray(close), 120);
		double[] ma250 = maHelper.getMAList(Doubles.toArray(close), 250);

		for (int index = 0; index < close.size(); index++) {
			MAVO vo = new MAVO();
			vo.setStockId(stockId);
			vo.setDate(priceList.get(index).date);
			vo.setMa5(Strings.convert2ScaleDecimal(ma5[index], 2));
			vo.setMa10(Strings.convert2ScaleDecimal(ma10[index], 2));
			vo.setMa19(Strings.convert2ScaleDecimal(ma19[index], 2));
			vo.setMa20(Strings.convert2ScaleDecimal(ma20[index], 2));
			vo.setMa30(Strings.convert2ScaleDecimal(ma30[index], 2));
			vo.setMa43(Strings.convert2ScaleDecimal(ma43[index], 2));
			vo.setMa60(Strings.convert2ScaleDecimal(ma60[index], 2));
			vo.setMa86(Strings.convert2ScaleDecimal(ma86[index], 2));
			vo.setMa120(Strings.convert2ScaleDecimal(ma120[index], 2));
			vo.setMa250(Strings.convert2ScaleDecimal(ma250[index], 2));
			vo.setClose(Strings.convert2ScaleDecimal(cls[index], 2));

			try {
				// if (vo.date.compareTo("2015-06-29") >= 0)
				// if (kdjTable.getKDJ(vo.stockId, vo.date) == null) {
				maTable.insert(vo);
				// }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void countAndSaved(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			if (index++ % 100 == 0)
				System.out.println("MA countAndSaved: " + stockId + " " + (index) + " of " + stockIds.size());
			this.countAndSaved(stockId);
		}
	}

	// TODO Auto-generated method stub
	// 一次性计算数据库中所有KDJ数据，入库
	public static void main(String[] args) {
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		HistoryMACountAndSaveDBRunner runner = new HistoryMACountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("999999");
	}

}
