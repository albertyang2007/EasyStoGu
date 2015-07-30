package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndMai1Mai2TableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.Mai1Mai2VO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.Mai1Mai2Helper;
import org.easystogu.utils.Strings;

public class HistoryMai1Mai2CountAndSaveDBRunner {

	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected IndMai1Mai2TableHelper mai1mai2Table = IndMai1Mai2TableHelper.getInstance();
	private Mai1Mai2Helper mai1mai2Helper = new Mai1Mai2Helper();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();

	public void deleteMai1Mai2(String stockId) {
		mai1mai2Table.delete(stockId);
	}

	public void deleteMai1Mai2(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Delete Mai1Mai2 for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteMai1Mai2(stockId);
		}
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

		if (priceList.size() <= 20) {
			System.out.println("StockPrice data is less than 20, skip " + stockId);
			return;
		}

		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updatePrice(stockId, priceList);

		// list is order by date
		int length = priceList.size();
		double[] var1 = new double[length];
		int index = 0;
		for (StockPriceVO vo : priceList) {
			var1[index++] = (2 * vo.close + vo.open + vo.high + vo.low) / 5;
		}

		double[][] mai1mai2 = mai1mai2Helper.getMai1Mai2List(var1);

		for (int i = 0; i < mai1mai2[0].length; i++) {
			Mai1Mai2VO vo = new Mai1Mai2VO();
			vo.setSd(Strings.convert2ScaleDecimal(mai1mai2[0][i]));
			vo.setSk(Strings.convert2ScaleDecimal(mai1mai2[1][i]));
			vo.setStockId(stockId);
			vo.setDate(priceList.get(i).date);

			try {
				// if (vo.date.compareTo("2015-06-29") >= 0)
				if (mai1mai2Table.getMai1Mai2(vo.stockId, vo.date) == null) {
					mai1mai2Table.insert(vo);
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
				System.out.println("Mai1Mai2 countAndSaved: " + stockId + " " + (index) + " of " + stockIds.size());
			this.countAndSaved(stockId);
		}
	}

	// TODO Auto-generated method stub
	// 一次性计算数据库中所有ShenXian数据，入库
	public static void main(String[] args) {
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		HistoryMai1Mai2CountAndSaveDBRunner runner = new HistoryMai1Mai2CountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}
}
