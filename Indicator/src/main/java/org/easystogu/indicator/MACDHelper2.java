package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;

public class MACDHelper2 {

	public static void main(String[] args) {
		StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
		List<StockPriceVO> list = stockPriceTable.getStockPriceById("002194");
		TALIBWraper talib = new TALIBWraper();

		// list is order by date
		int length = list.size();
		double[] close = new double[length];
		int index = 0;
		for (StockPriceVO vo : list) {
			close[index++] = vo.close;
		}

		double[][] macd = talib.getMacdExt(close, 12, 26, 9);

		double dif = macd[0][list.size() - 1];
		double dea = macd[1][list.size() - 1];
		double macdRtn = (dif - dea) * 2;
		System.out.println("DIF=" + dif);
		System.out.println("DEA=" + dea);
		System.out.println("MACD=" + macdRtn);
		// output:
		// DIF=0.621911206552296
		// DEA=0.5740987235058063
		// MACD=0.09562496609297955
		// Refer:
		// DIF=0.622
		// DEA=0.574
		// MACD=0.096
	}
}
