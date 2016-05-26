package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;

import com.google.common.primitives.Doubles;

//WR:=100-100*(HHV(HIGH,N)-CLOSE)/(HHV(HIGH,N)-LLV(LOW,N));
public class WRHelper extends IND {
	public double[][] getWRList(double[] CLOSE, double[] LOW, double[] HIGH, int N1, int N2) {
		int length = CLOSE.length + mockLength;
		double[][] wr = new double[2][length];

		// always add 120 mock date price before the list
		// append mock data at the begging
		CLOSE = insertBefore(CLOSE, LOW[0], mockLength);
		LOW = insertBefore(LOW, LOW[0], mockLength);
		HIGH = insertBefore(HIGH, LOW[0], mockLength);

		wr[0] = SUB(100, MUL(100, DIV(SUB(HHV(HIGH, N1), CLOSE), SUB(HHV(HIGH, N1), LLV(LOW, N1)))));
		wr[1] = SUB(100, MUL(100, DIV(SUB(HHV(HIGH, N2), CLOSE), SUB(HHV(HIGH, N2), LLV(LOW, N2)))));

		// exclude the mockLength data
		wr[0] = subList(wr[0], mockLength, length);
		wr[1] = subList(wr[1], mockLength, length);

		return wr;
	}

	public static void main(String[] args) {
		StockPriceTableHelper stockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
		WRHelper ins = new WRHelper();
		String stockId = "000673";
		List<Double> close = stockPriceTable.getAllClosePrice(stockId);
		List<Double> low = stockPriceTable.getAllLowPrice(stockId);
		List<Double> high = stockPriceTable.getAllHighPrice(stockId);

		int len = close.size();
		// System.out.println("close=" + close.get(len - 1) + ", high=" +
		// high.get(len - 1) + ", low=" + low.get(len - 1));

		double[][] wr = ins.getWRList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high), 42, 21);

		for (int i = 0; i < 10; i++) {
			System.out.print("WR[0]=" + (wr[0][len - 1 - i]));
			System.out.println(", WR[1]=" + (wr[1][len - 1 - i]));
		}

		// System.out.println("WR[0]=" + (wr[0][1]));
		// System.out.println("WR[1]=" + (wr[1][1]));
	}
}
