package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndShenXianTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.ShenXianVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.ShenXianHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.utils.Strings;

import com.google.common.primitives.Doubles;

public class DailyShenXianCountAndSaveDBRunner implements Runnable {
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();
	private ShenXianHelper shenXianHelper = new ShenXianHelper();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();
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
		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

        if (priceList.size() < 1) {
            return;
        }

		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updateQianFuQianPriceBasedOnHouFuQuan(stockId, priceList);

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);

		double[][] shenXian = shenXianHelper.getShenXianList(Doubles.toArray(close));

		int length = shenXian[0].length;

		ShenXianVO vo = new ShenXianVO();
		vo.setH1(Strings.convert2ScaleDecimal(shenXian[0][length - 1]));
		vo.setH2(Strings.convert2ScaleDecimal(shenXian[1][length - 1]));
		vo.setH3(Strings.convert2ScaleDecimal(shenXian[2][length - 1]));
		vo.setStockId(stockId);
		vo.setDate(priceList.get(length - 1).date);

		// System.out.println(vo);
		this.deleteShenXian(stockId, vo.date);
		shenXianTable.insert(vo);

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
