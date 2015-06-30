package org.easystogu.indicator.runner;

import java.util.Collections;
import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndXueShi2TableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.table.XueShi2VO;
import org.easystogu.indicator.TALIBWraper;
import org.easystogu.multirunner.DayMultThreadRunner;

public class DailyXueShi2CountAndSaveDBRunner implements Runnable {
	protected IndXueShi2TableHelper xueShi2Table = IndXueShi2TableHelper.getInstance();
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private TALIBWraper talib = new TALIBWraper();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();
	protected StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
	protected DayMultThreadRunner parentRunner;

	public DailyXueShi2CountAndSaveDBRunner() {

	}

	public DailyXueShi2CountAndSaveDBRunner(DayMultThreadRunner parentRunner) {
		this.parentRunner = parentRunner;
		this.parentRunner.newTaskInfo(this.getClass().getSimpleName());
	}

	public void deleteXueShi2(String stockId, String date) {
		xueShi2Table.delete(stockId, date);
	}

	public XueShi2VO countAndSaved(String stockId) {

		// List<StockPriceVO> list =
		// stockPriceTable.getStockPriceById(stockId);
		List<StockPriceVO> priceList = stockPriceTable.getNdateStockPriceById(stockId, 60);
		Collections.reverse(priceList);

		int length = priceList.size();

		if (length < 60) {
			// System.out.println(stockId
			// +
			// " price data is not enough to count XueShi2, please wait until it has at least 60 days. Skip");
			return null;
		}

		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updatePrice(stockId, priceList);

		double[] close = new double[length];
		int index = 0;
		for (StockPriceVO vo : priceList) {
			close[index++] = vo.close;
		}

		double[] var = talib.getEma(close, 9);

		double[] varUpper = new double[var.length];
		for (int i = 0; i < var.length; i++) {
			varUpper[i] = var[i] * 1.14;
		}

		double[] varLower = new double[var.length];
		for (int i = 0; i < var.length; i++) {
			varLower[i] = var[i] * 0.86;
		}

		double[] xueShi2Upper = talib.getEma(varUpper, 5);
		double[] xueShi2Low = talib.getEma(varLower, 5);

		double up = xueShi2Upper[length - 1];
		double dn = xueShi2Low[length - 1];
		// System.out.println("UP=" + up);
		// System.out.println("DN=" + dn);

		XueShi2VO xueShi2VO = new XueShi2VO();
		xueShi2VO.setStockId(stockId);
		xueShi2VO.setDate(priceList.get(length - 1).date);
		xueShi2VO.setUp(up);
		xueShi2VO.setDn(dn);

		this.deleteXueShi2(stockId, xueShi2VO.date);
		xueShi2Table.insert(xueShi2VO);

		return xueShi2VO;
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
		DailyXueShi2CountAndSaveDBRunner runner = new DailyXueShi2CountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("000979");
	}
}
