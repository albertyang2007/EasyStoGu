package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndMacdTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.TALIBWraper;
import org.easystogu.multirunner.MultThreadRunner;
import org.easystogu.utils.Strings;

//每日根据最新数据计算当天的macd值，每天运行一次
public class DailyMacdCountAndSaveDBRunner implements Runnable {
	protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected TALIBWraper talib = new TALIBWraper();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();
	protected CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	protected MultThreadRunner parentRunner;

	public DailyMacdCountAndSaveDBRunner() {

	}

	public DailyMacdCountAndSaveDBRunner(MultThreadRunner parentRunner) {
		this.parentRunner = parentRunner;
		this.parentRunner.newTaskInfo(this.getClass().getSimpleName());
	}

	public void deleteMacd(String stockId, String date) {
		macdTable.delete(stockId, date);
	}

	public void countAndSaved(String stockId) {

		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);
		// List<StockPriceVO> list =
		// stockPriceTable.getNdateStockPriceById(stockId, minLength);
		// Collections.reverse(list);

		int length = priceList.size();

		if (length < 26) {
			// System.out.println(stockId
			// +
			// " price data is not enough to count MACD, please wait until it has at least 26 days. Skip");
			return;
		}

		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updatePrice(stockId, priceList);

		double[] close = new double[length];
		int index = 0;
		for (StockPriceVO vo : priceList) {
			close[index++] = vo.close;
		}

		double[][] macd = talib.getMacdExt(close, 12, 26, 9);

		index = priceList.size() - 1;
		double dif = macd[0][index];
		double dea = macd[1][index];
		double macdRtn = (dif - dea) * 2;
		// System.out.println("date=" + list.get(index).date);
		// System.out.println("DIF=" + dif);
		// System.out.println("DEA=" + dea);
		// System.out.println("MACD=" + macdRtn);

		MacdVO vo = new MacdVO();
		vo.setStockId(stockId);
		vo.setDate(priceList.get(index).date);
		vo.setDif(Strings.convert2ScaleDecimal(dif));
		vo.setDea(Strings.convert2ScaleDecimal(dea));
		vo.setMacd(Strings.convert2ScaleDecimal(macdRtn));

		// System.out.println(vo);
		this.deleteMacd(stockId, vo.date);
		macdTable.insert(vo);

	}

	public void countAndSaved(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			if (index++ % 500 == 0) {
				System.out.println("MACD countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
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
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		DailyMacdCountAndSaveDBRunner runner = new DailyMacdCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002214");
	}
}
