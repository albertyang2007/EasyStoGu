package org.easystogu.indicator.runner;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.ShenXianVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.ShenXianHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.utils.Strings;

import com.google.common.primitives.Doubles;

public class DailyShenXianCountAndSaveDBRunner implements Runnable {
	protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	protected IndicatorDBHelperIF shenXianTable = DBAccessFacdeFactory.getInstance(Constants.indShenXian);

	private ShenXianHelper shenXianHelper = new ShenXianHelper();
	protected CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();

	public DailyShenXianCountAndSaveDBRunner() {

	}

	public void deleteShenXian(String stockId, String date) {
		shenXianTable.delete(stockId, date);
	}

	public void deleteShenXian(String stockId) {
		shenXianTable.delete(stockId);
	}

	public void deleteShenXian(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Delete ShenXian for " + stockId + " " + (++index) + "/" + stockIds.size());
			this.deleteShenXian(stockId);
		}
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

		if (priceList.size() < 1) {
			return;
		}

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);

		double[][] shenXian = shenXianHelper.getShenXianList(Doubles.toArray(close));

		// int length = shenXian[0].length;
		List<ShenXianVO> indList = new ArrayList<ShenXianVO>();
		for (int index = 0; index < priceList.size() - 1; index++) {
			ShenXianVO vo = new ShenXianVO();
			vo.setH1(Strings.convert2ScaleDecimal(shenXian[0][index]));
			vo.setH2(Strings.convert2ScaleDecimal(shenXian[1][index]));
			vo.setH3(Strings.convert2ScaleDecimal(shenXian[2][index]));
			vo.setStockId(stockId);
			vo.setDate(priceList.get(index).date);
			
			indList.add(vo);

			// if using cassandra, do not need to delete it, it will overwrite them
			// this.deleteShenXian(stockId, vo.date);
		}
		shenXianTable.insert(indList);

	}

	public void countAndSaved(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			if (index++ % 500 == 0) {
				System.out.println("ShenXian countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
			}
			this.countAndSaved(stockId);
		}
	}

	public void run() {

	}

	// TODO Auto-generated method stub
	public static void main(String[] args) {
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		List<String> stockIds = stockConfig.getAllStockId();
		DailyShenXianCountAndSaveDBRunner runner = new DailyShenXianCountAndSaveDBRunner();
		runner.countAndSaved(stockIds);
		// runner.countAndSaved("999999");
	}
}
