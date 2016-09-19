package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;

import com.google.common.primitives.Doubles;

// shenXian indicator
// 神仙大趋势H1:EMA(CLOSE,6);
// H2:EMA(神仙大趋势H1,18);
// H3:EMA(CLOSE,108),COLORYELLOW;
// STICKLINE(神仙大趋势H1>H2,神仙大趋势H1,H2,1,1),COLORRED;
// STICKLINE(神仙大趋势H1<H2,神仙大趋势H1,H2,1,1),COLORBLUE;
public class ShenXianHelper extends IND {
	public double[][] getShenXianList(double[] CLOSE) {
		int length = CLOSE.length + mockLength;
		double[][] shenXian = new double[3][length];

		// always add 120 mock date price before the list
		// append mock data at the begging
		CLOSE = insertBefore(CLOSE, CLOSE[0], mockLength);

		shenXian[0] = EMA(CLOSE, 6);
		shenXian[1] = EMA(shenXian[0], 18);
		shenXian[2] = EMA(CLOSE, 108);

		// exclude the mockLength data
		shenXian[0] = subList(shenXian[0], mockLength, length);
		shenXian[1] = subList(shenXian[1], mockLength, length);
		shenXian[2] = subList(shenXian[2], mockLength, length);

		return shenXian;
	}

	// HC5:(MA(HIGH-H1, 3)+H1)*1.01;
	// sell point: = IF(H1>H2 AND HIGH>HC5,1,0);
	// h3 is replaced by HC5
	public double[][] getShenXianSellPointList(double[] CLOSE, double[] HIGH) {
		int length = CLOSE.length + mockLength;
		double[][] shenXian = new double[3][length];

		// always add 120 mock date price before the list
		// append mock data at the begging
		CLOSE = insertBefore(CLOSE, CLOSE[0], mockLength);
		HIGH = insertBefore(HIGH, HIGH[0], mockLength);

		shenXian[0] = EMA(CLOSE, 6);
		shenXian[1] = EMA(shenXian[0], 18);
		// count HC5
		// h3 is replaced by HC5
		shenXian[2] = MUL(ADD(MA(SUB(HIGH, shenXian[0]), 3), shenXian[0]), 1.01);

		// exclude the mockLength data
		shenXian[0] = subList(shenXian[0], mockLength, length);
		shenXian[1] = subList(shenXian[1], mockLength, length);
		shenXian[2] = subList(shenXian[2], mockLength, length);

		return shenXian;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockPriceTableHelper stockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
		ShenXianHelper ins = new ShenXianHelper();

		String stockId = "002575";
		List<Double> close = stockPriceTable.getAllClosePrice(stockId);
		List<Double> high = stockPriceTable.getAllHighPrice(stockId);

		double[][] h = ins.getShenXianSellPointList(Doubles.toArray(close), Doubles.toArray(high));
		System.out.println("H1 =" + h[0][close.size() - 1]);
		System.out.println("H2 =" + h[1][close.size() - 1]);
		System.out.println("HC5=" + (h[2][close.size() - 1]));
	}

}
