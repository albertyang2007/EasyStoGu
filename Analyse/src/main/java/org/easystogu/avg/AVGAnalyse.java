package org.easystogu.avg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.StockPriceTableHelper;

public class AVGAnalyse {
	private StockPriceTableHelper tableHelper = new StockPriceTableHelper();
	private int avg1 = 0, avg2 = 1, avg3 = 2, avg5 = 3, avg10 = 4, avg20 = 5,
			avg30 = 6;
	private Map<String, List<String>> select = new HashMap<String, List<String>>();
	private static String[] reasons = { "趋势向上的，近期回调,5日支撑,成交量放大",
			"趋势向上的，近期回调,10日支撑,成交量放大", "成交量稳步放大" };

	private void analyseAVGCross(String stockId) {
		double[] ma = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		double[] vol = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };

		ma[avg1] = tableHelper.getAvgClosePrice(stockId, 1);
		ma[avg5] = tableHelper.getAvgClosePrice(stockId, 5);
		ma[avg10] = tableHelper.getAvgClosePrice(stockId, 10);
		ma[avg20] = tableHelper.getAvgClosePrice(stockId, 20);
		ma[avg30] = tableHelper.getAvgClosePrice(stockId, 30);

		vol[avg1] = tableHelper.getAvgVolume(stockId, 1);
		vol[avg2] = tableHelper.getAvgVolume(stockId, 2);
		vol[avg3] = tableHelper.getAvgVolume(stockId, 3);
		vol[avg5] = tableHelper.getAvgVolume(stockId, 5);
		vol[avg10] = tableHelper.getAvgVolume(stockId, 10);

		String reason = reasons[0];
		if (ma[avg10] > ma[avg20] && ma[avg20] > ma[avg30]) {
			if (ma[avg5] >= ma[avg1]) {
				if (vol[avg1] > vol[avg2] && vol[avg1] > vol[avg5]
						&& vol[avg5] > vol[avg10]) {
					this.addToList(reason, stockId);
				}
			}
		}

		reason = reasons[1];
		if (ma[avg10] > ma[avg20] && ma[avg20] > ma[avg30]) {
			if (ma[avg10] >= ma[avg5]) {
				if (vol[avg1] > vol[avg2] && vol[avg1] > vol[avg5]
						&& vol[avg5] > vol[avg10]) {
					this.addToList(reason, stockId);
				}
			}
		}

		reason = reasons[2];
		if (vol[avg1] > vol[avg2] && vol[avg2] > vol[avg3]
				&& vol[avg3] > vol[avg5] && vol[avg5] > vol[avg10]) {
			this.addToList(reason, stockId);
		}
	}

	private void addToList(String reason, String stockId) {
		List<String> ids = select.get(reason);
		if (ids == null) {
			ids = new ArrayList<String>();
			ids.add(new String(stockId));
			select.put(new String(reason), ids);
		} else {
			ids.add(new String(stockId));
		}
	}

	private void analyseAVGCross(List<String> stockIds) {
		for (String stockId : stockIds) {
			this.analyseAVGCross(stockId);
		}
	}

	public void displaySelected() {
		Set<String> keys = select.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String reason = it.next();
			List<String> ids = select.get(reason);
			System.out.println(reason + " : " + ids.toString());
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockListConfigurationService stockConfig = StockListConfigurationService
				.getInstance();
		AVGAnalyse ins = new AVGAnalyse();
		ins.analyseAVGCross(stockConfig.getAllStockId());
		ins.displaySelected();
	}
}
