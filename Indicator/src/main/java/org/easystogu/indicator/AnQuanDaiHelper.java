package org.easystogu.indicator;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.access.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;

//安全带 (黑马通道)
public class AnQuanDaiHelper {

	public static void main(String[] args) {
		StockPriceTableHelper stockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
		SimpleMovingAverages maHelper = new SimpleMovingAverages();
		List<StockPriceVO> list = stockPriceTable.getStockPriceById("601919");

		double[] open = new double[list.size()];
		List<Double> var1 = new ArrayList<Double>();
		int index = 0;
		for (StockPriceVO vo : list) {
			open[index++] = vo.open;
			var1.add(new Double(vo.open + vo.low + vo.high + 2 * vo.close) / 5);
		}

		List<Double> a8s = maHelper.getFullEXPMA(var1, 2.78);
		double[] a9s = maHelper.getSma(open, (int) 3.2);

		System.out.println("A8=" + a8s.get(0) * 1.028 + ", A9=" + a9s[list.size() - 1] * 1.028);
		System.out.println("A8=" + a8s.get(1) * 1.028 + ", A9=" + a9s[list.size() - 2] * 1.028);
		System.out.println("A8=" + a8s.get(2) * 1.028 + ", A9=" + a9s[list.size() - 3] * 1.028);
		System.out.println("A8=" + a8s.get(3) * 1.028 + ", A9=" + a9s[list.size() - 4] * 1.028);
	}
}
