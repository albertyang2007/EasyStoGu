package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;

//绝对顶底
public class JueDuiDingDiHelper {

	public static double hhv(double[] list, int before) {
		int startIndex = (list.length - before) > 0 ? (list.length - 360) : 0;
		double max = 0;
		for (int i = startIndex; i < list.length; i++) {
			if (max < list[i]) {
				max = list[i];
			}
		}
		return max;
	}

	public static void main(String[] args) {
		StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
		SimpleMovingAverages maHelper = new SimpleMovingAverages();
		List<StockPriceVO> list = stockPriceTable.getStockPriceById("601919");

		double[] close = new double[list.size()];
		double[] hld = new double[list.size()];

		for (int i = 0; i < list.size(); i++) {
			close[i] = list.get(i).close;
		}

		double[] sq = maHelper.getSma(close, 20);

		for (int i = 0; i < list.size(); i++) {
			hld[i] = (close[i] - sq[i]) / sq[i];
		}

	}

}
