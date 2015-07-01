package org.easystogu.indicator.runner;

import java.util.Collections;
import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndBollTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.TALIBWraper;
import org.easystogu.multirunner.DayMultThreadRunner;

//每日根据最新数据计算当天的boll值，每天运行一次
public class DailyBollCountAndSaveDBRunner implements Runnable {
	protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private TALIBWraper talib = new TALIBWraper();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();
	protected StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
	protected DayMultThreadRunner parentRunner;

	public DailyBollCountAndSaveDBRunner() {

	}

	public DailyBollCountAndSaveDBRunner(DayMultThreadRunner parentRunner) {
		this.parentRunner = parentRunner;
		this.parentRunner.newTaskInfo(this.getClass().getSimpleName());
	}

	public void deleteBoll(String stockId, String date) {
		bollTable.delete(stockId, date);
	}

	public BollVO countAndSaved(String stockId) {

		// List<StockPriceVO> list =
		// stockPriceTable.getStockPriceById(stockId);
		List<StockPriceVO> priceList = stockPriceTable.getNdateStockPriceById(stockId, 20);
		Collections.reverse(priceList);

		int length = priceList.size();

		if (length < 20) {
			// System.out.println(stockId
			// +
			// " price data is not enough to count Boll, please wait until it has at least 20 days. Skip");
			return null;
		}

		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updatePrice(stockId, priceList);

		double[] close = new double[length];
		int index = 0;
		for (StockPriceVO vo : priceList) {
			close[index++] = vo.close;
		}

		double[][] boll = talib.getBbands(close, 20, 2, 2);

		index = priceList.size() - 1;
		double up = boll[0][index];
		double mb = boll[1][index];
		double dn = boll[2][index];
		// System.out.println("MB=" + mb);
		// System.out.println("UP=" + up);
		// System.out.println("DN=" + dn);

		BollVO vo = new BollVO();
		vo.setStockId(stockId);
		vo.setDate(priceList.get(index).date);
		vo.setMb(mb);
		vo.setUp(up);
		vo.setDn(dn);

		// System.out.println(vo);
		this.deleteBoll(stockId, vo.date);
		bollTable.insert(vo);
		return vo;
	}

	public void countAndSaved(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			if (index++ % 500 == 0) {
				System.out.println("Boll countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
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
		DailyBollCountAndSaveDBRunner runner = new DailyBollCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002214");
	}
}
