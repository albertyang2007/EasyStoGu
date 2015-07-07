package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;

/*
 VAR4:=EXPMA(CLOSE,9);
 薛斯外上轨:EXPMA(VAR4*1.14,5);
 薛斯外下轨:EXPMA(VAR4*0.86,5);
 */
public class XueShi2Helper {
	public static void main(String[] args) {
		StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
		List<StockPriceVO> list = stockPriceTable.getStockPriceById("000979");
		TALIBWraper talib = new TALIBWraper();

		// list is order by date
		int length = list.size();
		double[] close = new double[length];
		int index = 0;
		for (StockPriceVO vo : list) {
			close[index++] = vo.close;
		}

		double[] var = talib.getEma(close, 9);
		System.out.println("var=" + var[var.length - 1]);

		double[] varUpper = new double[var.length];
		for (int i = 0; i < var.length; i++) {
			varUpper[i] = var[i] * 1.14;
		}

		double[] varLower = new double[var.length];
		for (int i = 0; i < var.length; i++) {
			varLower[i] = var[i] * 0.86;
		}

		double[] xueShi2Upper = talib.getEma(varUpper, 5);
		System.out.println("xueShi2Upper=" + xueShi2Upper[xueShi2Upper.length - 1]);

		double[] xueShi2Low = talib.getEma(varLower, 5);
		System.out.println("xueShi2Low=" + xueShi2Low[xueShi2Low.length - 1]);

	}
}
