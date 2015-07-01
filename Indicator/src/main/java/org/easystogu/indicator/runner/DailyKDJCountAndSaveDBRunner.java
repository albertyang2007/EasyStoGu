package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndKDJTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.kdj.KDJHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.multirunner.DayMultThreadRunner;

public class DailyKDJCountAndSaveDBRunner implements Runnable {
	private KDJHelper kdjHelper = new KDJHelper();
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();
	protected StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
	protected DayMultThreadRunner parentRunner;

	public DailyKDJCountAndSaveDBRunner() {

	}

	public DailyKDJCountAndSaveDBRunner(DayMultThreadRunner parentRunner) {
		this.parentRunner = parentRunner;
		this.parentRunner.newTaskInfo(this.getClass().getSimpleName());
	}

	public void deleteKDJ(String stockId, String date) {
		kdjTable.delete(stockId, date);
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

		if (priceList.size() <= 9) {
			// System.out.println("StockPrice data is less than 9, skip " +
			// stockId);
			return;
		}

		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updatePrice(stockId, priceList);

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);
		List<Double> low = StockPriceFetcher.getLowPrice(priceList);
		List<Double> high = StockPriceFetcher.getHighPrice(priceList);

		double[][] KDJ = kdjHelper.getKDJList(close.toArray(new Double[0]), low.toArray(new Double[0]),
				high.toArray(new Double[0]));

		int length = KDJ[0].length;

		// for (int i = 0; i < KDJ[0].length; i++) {
		KDJVO vo = new KDJVO();
		vo.setK(KDJ[0][length - 1]);
		vo.setD(KDJ[1][length - 1]);
		vo.setJ(KDJ[2][length - 1]);
		vo.setRsv(KDJ[3][length - 1]);
		vo.setStockId(stockId);
		vo.setDate(priceList.get(length - 1).date);

		// System.out.println(vo);
		this.deleteKDJ(stockId, vo.date);
		kdjTable.insert(vo);

	}

	public void countAndSaved(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			if (index++ % 500 == 0) {
				System.out.println("KDJ countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
			}
			this.countAndSaved(stockId);
		}
	}

	public void run() {
		this.parentRunner.startTaskInfo(this.getClass().getSimpleName());
		countAndSaved(stockConfig.getAllStockId());
		this.parentRunner.stopTaskInfo(this.getClass().getSimpleName());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		DailyKDJCountAndSaveDBRunner runner = new DailyKDJCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002214");
	}
}
