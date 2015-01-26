package org.easystogu.avg;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.StockPriceTableHelper;

public class AVGAnalyse {
	private StockPriceTableHelper tableHelper = new StockPriceTableHelper();
	private int ma1 = 0, ma2 = 1, ma3 = 2, ma5 = 3, ma10 = 4, ma20 = 5,
			ma30 = 6;

	private void analyseAVGCross(String stockId) {
		double[] avg = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		double[] vol = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

		avg[ma1] = tableHelper.getAvgClosePrice(stockId, 1);
		avg[ma5] = tableHelper.getAvgClosePrice(stockId, 5);
		avg[ma10] = tableHelper.getAvgClosePrice(stockId, 10);
		avg[ma20] = tableHelper.getAvgClosePrice(stockId, 20);
		avg[ma30] = tableHelper.getAvgClosePrice(stockId, 30);

		vol[ma1] = tableHelper.getAvgVolume(stockId, 1);
		vol[ma2] = tableHelper.getAvgVolume(stockId, 2);
		vol[ma5] = tableHelper.getAvgVolume(stockId, 5);
		vol[ma10] = tableHelper.getAvgVolume(stockId, 10);

		// 趋势向上的，近期回调的
		if (avg[ma10] > avg[ma20] && avg[ma20] > avg[ma30]) {
			if (avg[ma5] < avg[ma10]) {
				// 近期成交量上升
				if (vol[ma1] > vol[ma2] && vol[ma1] > vol[ma5]
						&& vol[ma5] > vol[ma10]) {
					System.out.println("stockId=" + stockId);
				}
			}
		}
	}

	private void analyseAVGCross(List<String> stockIds) {
		for (String stockId : stockIds) {
			this.analyseAVGCross(stockId);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockListConfigurationService stockConfig = StockListConfigurationService
				.getInstance();
		AVGAnalyse ins = new AVGAnalyse();
		ins.analyseAVGCross(stockConfig.getAllStockId());
	}
}
