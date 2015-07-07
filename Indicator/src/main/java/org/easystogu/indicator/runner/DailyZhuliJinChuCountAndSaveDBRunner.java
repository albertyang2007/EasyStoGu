package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndZhuliJinChuTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.table.ZhuliJinChuVO;
import org.easystogu.indicator.ZhuliJinChuHelper;
import org.easystogu.multirunner.MultThreadRunner;

public class DailyZhuliJinChuCountAndSaveDBRunner implements Runnable {

	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected IndZhuliJinChuTableHelper zhuliJinChuTable = IndZhuliJinChuTableHelper.getInstance();
	private ZhuliJinChuHelper zhuliJinChuHelper = new ZhuliJinChuHelper();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();
	protected StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
	protected MultThreadRunner parentRunner;

	public DailyZhuliJinChuCountAndSaveDBRunner() {

	}

	public DailyZhuliJinChuCountAndSaveDBRunner(MultThreadRunner parentRunner) {
		this.parentRunner = parentRunner;
		this.parentRunner.newTaskInfo(this.getClass().getSimpleName());
	}

	public void deleteZhuliJinChu(String stockId, String date) {
		zhuliJinChuTable.delete(stockId, date);
	}

	public void deleteZhuliJinChu(String stockId) {
		zhuliJinChuTable.delete(stockId);
	}

	public void deleteMai1Mai2(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Delete ZhuliJinChu for " + stockId + " " + (++index) + "/" + stockIds.size());
			this.deleteZhuliJinChu(stockId);
		}
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

		if (priceList.size() <= 34) {
			// System.out.println("StockPrice data is less than 34, skip " +
			// stockId);
			return;
		}

		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updatePrice(stockId, priceList);

		// list is order by date
		int length = priceList.size();
		double[] var1 = new double[length];
		int index = 0;
		for (StockPriceVO vo : priceList) {
			var1[index++] = (2 * vo.close + vo.high + vo.low) / 4;
		}

		double[][] zhuliJinChu = zhuliJinChuHelper.getZhuliJinChuList(var1);

		ZhuliJinChuVO vo = new ZhuliJinChuVO();
		vo.setDuofang(zhuliJinChu[0][length - 1]);
		vo.setKongfang(zhuliJinChu[1][length - 1]);
		vo.setStockId(stockId);
		vo.setDate(priceList.get(length - 1).date);

		this.deleteZhuliJinChu(stockId, vo.date);
		zhuliJinChuTable.insert(vo);

	}

	public void countAndSaved(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			if (index++ % 500 == 0) {
				System.out.println("ZhuliJinChu countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
			}
			this.countAndSaved(stockId);
		}
	}

	public void run() {
		this.parentRunner.startTaskInfo(this.getClass().getSimpleName());
		countAndSaved(stockConfig.getAllStockId());
		this.parentRunner.stopTaskInfo(this.getClass().getSimpleName());
	}

	// TODO Auto-generated method stub
	public static void main(String[] args) {
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		DailyZhuliJinChuCountAndSaveDBRunner runner = new DailyZhuliJinChuCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600000");
	}

}
