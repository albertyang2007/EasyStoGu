package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.cassandra.access.table.IndMacdCassTableHelper;
import org.easystogu.db.access.table.IndMacdTableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.MACDHelper;
import org.easystogu.utils.Strings;

//每日根据最新数据计算当天的macd值，每天运行一次
public class DailyMacdCountAndSaveDBRunner implements Runnable {
	protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
	protected IndMacdCassTableHelper macdCable = IndMacdCassTableHelper.getInstance();
	protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	protected MACDHelper macdHelper = new MACDHelper();
	protected CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();

	public DailyMacdCountAndSaveDBRunner() {

	}

	public void deleteMacd(String stockId, String date) {
		macdTable.delete(stockId, date);
		macdCable.delete(stockId, date);
	}

	public void countAndSaved(String stockId) {

		List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);
		// List<StockPriceVO> list =
		// stockPriceTable.getNdateStockPriceById(stockId, minLength);
		// Collections.reverse(list);

		int length = priceList.size();

		if (length < 1) {
			return;
		}

		double[] close = new double[length];
		int index = 0;
		for (StockPriceVO vo : priceList) {
			close[index++] = vo.close;
		}

		double[][] macd = macdHelper.getMACDList(close);

		index = priceList.size() - 1;
		double dif = macd[0][index];
		double dea = macd[1][index];
		double macdRtn = macd[2][index];
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
		macdCable.insert(vo);
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

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		DailyMacdCountAndSaveDBRunner runner = new DailyMacdCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("999999");
	}
}
