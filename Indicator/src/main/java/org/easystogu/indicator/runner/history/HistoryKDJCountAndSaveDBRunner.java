package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndKDJTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.KDJHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.utils.Strings;

public class HistoryKDJCountAndSaveDBRunner {
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
	private KDJHelper kdjHelper = new KDJHelper();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();

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
		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

		if (priceList.size() <= 9) {
			System.out.println("StockPrice data is less than 9, skip " + stockId);
			return;
		}

		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updatePrice(stockId, priceList);

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);
		List<Double> low = StockPriceFetcher.getLowPrice(priceList);
		List<Double> high = StockPriceFetcher.getHighPrice(priceList);

		double[][] KDJ = kdjHelper.getKDJList(close.toArray(new Double[0]), low.toArray(new Double[0]),
				high.toArray(new Double[0]));

		for (int i = 0; i < KDJ[0].length; i++) {
			KDJVO vo = new KDJVO();
			vo.setK(Strings.convert2ScaleDecimal(KDJ[0][i]));
			vo.setD(Strings.convert2ScaleDecimal(KDJ[1][i]));
			vo.setJ(Strings.convert2ScaleDecimal(KDJ[2][i]));
			vo.setRsv(Strings.convert2ScaleDecimal(KDJ[3][i]));
			vo.setStockId(stockId);
			vo.setDate(priceList.get(i).date);

			try {
				// if (vo.date.compareTo("2015-06-29") >= 0)
				if (kdjTable.getKDJ(vo.stockId, vo.date) == null) {
					kdjTable.insert(vo);
				}
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
				System.out.println("KDJ countAndSaved: " + stockId + " " + (index) + " of " + stockIds.size());
			this.countAndSaved(stockId);
		}
	}

	// TODO Auto-generated method stub
	// 一次性计算数据库中所有KDJ数据，入库
	public static void main(String[] args) {
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		HistoryKDJCountAndSaveDBRunner runner = new HistoryKDJCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}
}
