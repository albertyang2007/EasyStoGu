package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.MACDHelper;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//每日根据最新数据计算当天的macd值，每天运行一次
@Component
public class DailyMacdCountAndSaveDBRunner implements Runnable {
	protected IndicatorDBHelperIF macdTable = DBAccessFacdeFactory.getInstance(Constants.indMacd);
	protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	@Autowired
	protected MACDHelper macdHelper = new MACDHelper();
	protected CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();

	public DailyMacdCountAndSaveDBRunner() {

	}

	public void deleteMacd(String stockId, String date) {
		macdTable.delete(stockId, date);
	}

	public void countAndSaved(String stockId) {

		List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

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

		MacdVO vo = new MacdVO();
		vo.setStockId(stockId);
		vo.setDate(priceList.get(index).date);
		vo.setDif(Strings.convert2ScaleDecimal(dif));
		vo.setDea(Strings.convert2ScaleDecimal(dea));
		vo.setMacd(Strings.convert2ScaleDecimal(macdRtn));

		this.deleteMacd(stockId, vo.date);

		macdTable.insert(vo);
	}

	public void countAndSaved(List<String> stockIds) {
	  stockIds.parallelStream().forEach(stockId -> {
	    this.countAndSaved(stockId);
	  });
	  
		//int index = 0;
//		for (String stockId : stockIds) {
//			if (index++ % 500 == 0) {
//				System.out.println("MACD countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
//			}
//			this.countAndSaved(stockId);
//		}
	}

	public void run() {

	}

	public void mainWork(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		this.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("999999");
		// System.exit(0);
	}
}
