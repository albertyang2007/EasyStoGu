package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndShenXianTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.ShenXianVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.indicator.shenxian.ShenXianHelper;

public class DailyShenXianCountAndSaveDBRunner {

	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();
	private ShenXianHelper shenXianHelper = new ShenXianHelper();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();

	public void deleteShenXian(String stockId, String date) {
		shenXianTable.delete(stockId, date);
	}

	public void deleteShenXian(String stockId) {
		shenXianTable.delete(stockId);
	}

	public void deleteShenXian(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Delete ShenXian for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteShenXian(stockId);
		}
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

		if (priceList.size() <= 108) {
			System.out.println("StockPrice data is less than 108, skip " + stockId);
			return;
		}
		
		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updatePrice(stockId, priceList);

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);

		double[][] shenXian = shenXianHelper.getShenXianList(close.toArray(new Double[0]));

		int length = shenXian[0].length;

		ShenXianVO vo = new ShenXianVO();
		vo.setH1(shenXian[0][length - 1]);
		vo.setH2(shenXian[1][length - 1]);
		vo.setH3(shenXian[2][length - 1]);
		vo.setStockId(stockId);
		vo.setDate(priceList.get(length - 1).date);

		this.deleteShenXian(stockId, vo.date);
		shenXianTable.insert(vo);

	}

	public void countAndSaved(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("ShenXian countAndSaved: " + stockId + " " + (++index) + " of " + stockIds.size());
			this.countAndSaved(stockId);
		}
	}

	// TODO Auto-generated method stub
	public static void main(String[] args) {
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		List<String> stockIds = stockConfig.getAllStockId();
		DailyShenXianCountAndSaveDBRunner runner = new DailyShenXianCountAndSaveDBRunner();
		runner.countAndSaved(stockIds);
		// runner.countAndSaved("002194");
	}
}
