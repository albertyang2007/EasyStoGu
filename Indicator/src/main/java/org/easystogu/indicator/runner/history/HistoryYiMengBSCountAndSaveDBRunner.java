package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndYiMengBSTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.table.YiMengBSVO;
import org.easystogu.indicator.YiMengBSHelper;
import org.easystogu.utils.Strings;

public class HistoryYiMengBSCountAndSaveDBRunner {

	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected IndYiMengBSTableHelper yiMengBSTable = IndYiMengBSTableHelper.getInstance();
	private YiMengBSHelper yiMengBSHelper = new YiMengBSHelper();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();

	public void deleteYiMengBS(String stockId) {
		yiMengBSTable.delete(stockId);
	}

	public void deleteYiMengBS(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Delete YiMengBS for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteYiMengBS(stockId);
		}
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

		if (priceList.size() <= 108) {
			System.out.println("StockPrice data is less than 108, skip " + stockId);
			return;
		}

		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updatePrice(stockId, priceList);

		double[][] yiMeng = yiMengBSHelper.getYiMengBSList(priceList);

		for (int i = 0; i < yiMeng[0].length; i++) {
			YiMengBSVO vo = new YiMengBSVO();
			vo.setX2(Strings.convert2ScaleDecimal(yiMeng[1][i]));
			vo.setX3(Strings.convert2ScaleDecimal(yiMeng[2][i]));
			vo.setStockId(stockId);
			vo.setDate(priceList.get(i).date);

			try {
				if (yiMengBSTable.getYiMengBS(vo.stockId, vo.date) == null) {
					yiMengBSTable.insert(vo);
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
			if (index++ % 100 == 0)
				System.out.println("YiMengBS countAndSaved: " + stockId + " " + (index) + " of " + stockIds.size());
			this.countAndSaved(stockId);
		}
	}

	// TODO Auto-generated method stub
	// 一次性计算数据库中所有ShenXian数据，入库
	public static void main(String[] args) {
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		HistoryYiMengBSCountAndSaveDBRunner runner = new HistoryYiMengBSCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002194");
	}

}
