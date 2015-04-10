package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndShenXianTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.ShenXianVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.shenxian.ShenXianHelper;

public class HistoryShenXianCountAndSaveDBRunner {

	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();
	private ShenXianHelper shenXianHelper = new ShenXianHelper();

	public void deleteShenXian(String stockId) {
		shenXianTable.delete(stockId);
	}

	public void deleteShenXian(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Delete ShenXian for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteShenXian(stockId);
		}
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> spVO = stockPriceTable.getStockPriceById(stockId);

		if (spVO.size() <= 108) {
			System.out.println("StockPrice data is less than 108, skip " + stockId);
			return;
		}

		List<Double> close = stockPriceTable.getAllClosePrice(stockId);

		double[][] shenXian = shenXianHelper.getShenXianList(close.toArray(new Double[0]));

		for (int i = 0; i < shenXian[0].length; i++) {
			ShenXianVO vo = new ShenXianVO();
			vo.setH1(shenXian[0][i]);
			vo.setH2(shenXian[1][i]);
			vo.setH3(shenXian[2][i]);
			vo.setStockId(stockId);
			vo.setDate(spVO.get(i).date);

			try {
				if (shenXianTable.getShenXian(vo.stockId, vo.date) == null) {
					shenXianTable.insert(vo);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void countAndSaved(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("ShenXian countAndSaved: " + stockId + " " + (++index) + " of " + stockIds.size());
			this.countAndSaved(stockId);
		}
	}

	// TODO Auto-generated method stub
	// 一次性计算数据库中所有ShenXian数据，入库
	public static void main(String[] args) {
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		List<String> stockIds = stockConfig.getAllStockId();
		HistoryShenXianCountAndSaveDBRunner runner = new HistoryShenXianCountAndSaveDBRunner();
		runner.countAndSaved(stockIds);
		//runner.countAndSaved("002194");
	}

}
