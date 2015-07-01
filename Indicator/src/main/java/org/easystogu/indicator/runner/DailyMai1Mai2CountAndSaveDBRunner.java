package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndMai1Mai2TableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.Mai1Mai2VO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.mai1mai2.Mai1Mai2Helper;
import org.easystogu.multirunner.DayMultThreadRunner;

public class DailyMai1Mai2CountAndSaveDBRunner implements Runnable {

	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected IndMai1Mai2TableHelper mai1mai2Table = IndMai1Mai2TableHelper.getInstance();
	private Mai1Mai2Helper mai1mai2Helper = new Mai1Mai2Helper();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();
	protected StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
	protected DayMultThreadRunner parentRunner;

	public DailyMai1Mai2CountAndSaveDBRunner() {

	}

	public DailyMai1Mai2CountAndSaveDBRunner(DayMultThreadRunner parentRunner) {
		this.parentRunner = parentRunner;
		this.parentRunner.newTaskInfo(this.getClass().getSimpleName());
	}

	public void deleteMai1Mai2(String stockId, String date) {
		mai1mai2Table.delete(stockId, date);
	}

	public void deleteMai1Mai2(String stockId) {
		mai1mai2Table.delete(stockId);
	}

	public void deleteMai1Mai2(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Delete Mai1Mai2 for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteMai1Mai2(stockId);
		}
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

		if (priceList.size() <= 20) {
			// System.out.println("StockPrice data is less than 20, skip " +
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
			var1[index++] = (2 * vo.close + vo.open + vo.high + vo.low) / 5;
		}

		double[][] mai1mai2 = mai1mai2Helper.getMai1Mai2List(var1);

		Mai1Mai2VO vo = new Mai1Mai2VO();
		vo.setSd(mai1mai2[0][length - 1]);
		vo.setSk(mai1mai2[1][length - 1]);
		vo.setStockId(stockId);
		vo.setDate(priceList.get(length - 1).date);

		this.deleteMai1Mai2(stockId, vo.date);
		mai1mai2Table.insert(vo);

	}

	public void countAndSaved(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			if (index++ % 500 == 0) {
				System.out.println("Mai1Mai2 countAndSaved: " + stockId + " " + (index) + " of " + stockIds.size());
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
		DailyMai1Mai2CountAndSaveDBRunner runner = new DailyMai1Mai2CountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600084");
	}
}
