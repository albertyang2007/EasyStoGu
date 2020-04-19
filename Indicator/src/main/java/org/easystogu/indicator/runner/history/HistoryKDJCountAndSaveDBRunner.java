package org.easystogu.indicator.runner.history;

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

public class HistoryKDJCountAndSaveDBRunner {
	protected IndicatorDBHelperIF kdjTable = DBAccessFacdeFactory.getInstance(Constants.indKDJ);
	protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	protected KDJHelper kdjHelper = new KDJHelper();

	public void deleteKDJ(String stockId) {
		kdjTable.delete(stockId);
	}

	public void deleteKDJ(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Delete KDJ for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteKDJ(stockId);
		}
	}

	public void countAndSaved(String stockId) {
        this.deleteKDJ(stockId);
        
		List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

		if (priceList.size() <= 9) {
			System.out.println("StockPrice data is less than 9, skip " + stockId);
			return;
		}

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);
		List<Double> low = StockPriceFetcher.getLowPrice(priceList);
		List<Double> high = StockPriceFetcher.getHighPrice(priceList);

		double[][] KDJ = kdjHelper.getKDJList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));

		for (int i = 0; i < KDJ[0].length; i++) {
			KDJVO vo = new KDJVO();
			vo.setK(Strings.convert2ScaleDecimal(KDJ[0][i], 3));
			vo.setD(Strings.convert2ScaleDecimal(KDJ[1][i], 3));
			vo.setJ(Strings.convert2ScaleDecimal(KDJ[2][i], 3));
			vo.setRsv(Strings.convert2ScaleDecimal(KDJ[3][i], 3));
			vo.setStockId(stockId);
			vo.setDate(priceList.get(i).date);

			try {
				// if (vo.date.compareTo("2015-06-29") >= 0)
				// if (kdjTable.getKDJ(vo.stockId, vo.date) == null) {
				kdjTable.insert(vo);
				// }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void countAndSaved(List<String> stockIds) {
      System.out.println("KDJ countAndSaved start");
      stockIds.parallelStream().forEach(stockId -> {
        this.countAndSaved(stockId);
      });
      
//      int index = 0;
//      for (String stockId : stockIds) {
//          if (index++ % 100 == 0)
//              System.out.println("JDK countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
//          this.countAndSaved(stockId);
//      }
      
      System.out.println("JDK countAndSaved stop");
    }

	// TODO Auto-generated method stub
	// 一次性计算数据库中所有KDJ数据，入库
	public static void main(String[] args) {
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		HistoryKDJCountAndSaveDBRunner runner = new HistoryKDJCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}
}
