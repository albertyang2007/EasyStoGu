package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;

import com.google.common.primitives.Doubles;

public class MAHelper extends IND {

	public double[] getMAList(double[] CLOSE, int day) {
		return MA(CLOSE, day);
	}

	public static void main(String[] args) {
		MAHelper helper = new MAHelper();
		StockPriceTableHelper stockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
		List<StockPriceVO> spList = stockPriceTable.getStockPriceById("600873");
		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		double ma[] = helper.getMAList(Doubles.toArray(close), 19);
		
		for (double a : ma) {
			System.out.println(a);
		}
		
		System.out.println("Length="+ma.length);
	}
}
