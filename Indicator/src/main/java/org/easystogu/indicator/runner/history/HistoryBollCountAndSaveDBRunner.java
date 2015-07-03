package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndBollTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.TALIBWraper;

//计算数据库中所有boll值，包括最新和历史的，一次性运行
public class HistoryBollCountAndSaveDBRunner {

	protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private TALIBWraper talib = new TALIBWraper();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();

	public void countAndSaved(String stockId) {
		try {
			List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

			int length = priceList.size();

			if (length < 20) {
				System.out.println(stockId
						+ " price data is not enough to count Boll, please wait until it has at least 20 days. Skip");
				return;
			}

			// update price based on chuQuanChuXi event
			chuQuanChuXiPriceHelper.updatePrice(stockId, priceList);

			double[] close = new double[length];
			int index = 0;
			for (StockPriceVO vo : priceList) {
				close[index++] = vo.close;
			}

			double[][] boll = talib.getBbands(close, 20, 2, 2);

			for (index = priceList.size() - 1; index >= 0; index--) {
				double up = boll[0][index];
				double mb = boll[1][index];
				double dn = boll[2][index];
				// System.out.println("MB=" + mb);
				// System.out.println("UP=" + up);
				// System.out.println("DN=" + dn);

				BollVO bollVO = new BollVO();
				bollVO.setStockId(stockId);
				bollVO.setDate(priceList.get(index).date);
				bollVO.setMb(mb);
				bollVO.setUp(up);
				bollVO.setDn(dn);

				if (bollTable.getBoll(bollVO.stockId, bollVO.date) == null) {
					bollTable.insert(bollVO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void countAndSaved(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Boll countAndSaved: " + stockId + " " + (++index) + "/" + stockIds.size());
			this.countAndSaved(stockId);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		HistoryBollCountAndSaveDBRunner runner = new HistoryBollCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		//runner.countAndSaved("600750");
	}

}
