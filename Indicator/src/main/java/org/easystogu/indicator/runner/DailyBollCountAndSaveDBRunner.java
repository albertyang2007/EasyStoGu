package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.BollVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.BOLLHelper;
import org.easystogu.utils.Strings;

//每日根据最新数据计算当天的boll值，每天运行一次
public class DailyBollCountAndSaveDBRunner implements Runnable {
	protected IndicatorDBHelperIF bollTable = DBAccessFacdeFactory.getInstance(Constants.indBoll);
	protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	protected BOLLHelper bollHelper = new BOLLHelper();
	protected CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();

	public DailyBollCountAndSaveDBRunner() {

	}

	public void deleteBoll(String stockId, String date) {
		bollTable.delete(stockId, date);
	}

	public void countAndSaved(String stockId) {

		List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

		int length = priceList.size();

		if (priceList.size() < 1) {
			return;
		}

		double[] close = new double[length];
		int index = 0;
		for (StockPriceVO vo : priceList) {
			close[index++] = vo.close;
		}

		double[][] boll = bollHelper.getBOLLList(close, 20, 2, 2);

		index = priceList.size() - 1;

		double up = Strings.convert2ScaleDecimal(boll[0][index]);
		double mb = Strings.convert2ScaleDecimal(boll[1][index]);
		double dn = Strings.convert2ScaleDecimal(boll[2][index]);

		BollVO vo = new BollVO();
		vo.setStockId(stockId);
		vo.setDate(priceList.get(index).date);
		vo.setMb(mb);
		vo.setUp(up);
		vo.setDn(dn);

		this.deleteBoll(stockId, vo.date);

		bollTable.insert(vo);
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
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		DailyBollCountAndSaveDBRunner runner = new DailyBollCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002214");
	}
}
