package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Doubles;

/*
 RSV:=(CLOSE-LLV(LOW,9))/(HHV(HIGH,9)-LLV(LOW,9))*100;
 K:=SMA(RSV,3,1);
 D:=SMA(K,3,1);
 J:=3*K-2*D;
 */

@Component
public class KDJHelper extends IND {
	public static final double DEFAULT_VALUE = 50.0;

	public double[][] getKDJList(double[] CLOSE, double[] LOW, double[] HIGH) {
		return KDJ(CLOSE, LOW, HIGH, 9, 3, 3);
	}

	private double getRsv(double close, double lown, double highn) {
		return 100.0 * (close - lown) / (highn - lown);
	}

	private double[] RSV(double[] close, double[] llv, double[] hhv) {
		double[] rsv = new double[close.length];
		for (int index = 0; index < close.length; index++) {
			rsv[index] = this.getRsv(close[index], llv[index], hhv[index]);
		}
		return rsv;
	}

	public double[] getJ(double[] k, double[] d) {
		double[] j = new double[k.length];
		for (int index = 0; index < k.length; index++) {
			j[index] = 3 * k[index] - 2 * d[index];
		}
		return j;
	}

	// (N,M1,M2)=(9,3,3)
	public double[][] KDJ(double[] close, double[] low, double[] high, int N, int M1, int M2) {
		int length = close.length;
		double[][] kdj = new double[4][length];
		double[] rsv = this.RSV(close, LLV(low, N), HHV(high, N));

		for (int index = 0; index < N; index++) {
			kdj[0][index] = DEFAULT_VALUE;
			kdj[1][index] = DEFAULT_VALUE;
			kdj[2][index] = DEFAULT_VALUE;
			kdj[3][index] = DEFAULT_VALUE;
		}

		for (int index = N - 1; index < length; index++) {

			kdj[0][index] = (2.0 / 3.0) * kdj[0][index - 1] + (1.0 / 3.0) * rsv[index];// K
			kdj[1][index] = (2.0 / 3.0) * kdj[1][index - 1] + (1.0 / 3.0) * kdj[0][index];// D
			kdj[2][index] = 3.0 * kdj[0][index] - 2.0 * kdj[1][index];// J
			kdj[3][index] = rsv[index];
		}

		return kdj;
	}

	public static void main(String[] args) {
		StockPriceTableHelper stockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
		KDJHelper ins = new KDJHelper();
		String stockId = "000333";
		List<Double> close = stockPriceTable.getAllClosePrice(stockId);
		List<Double> low = stockPriceTable.getAllLowPrice(stockId);
		List<Double> high = stockPriceTable.getAllHighPrice(stockId);

		double[][] newKDJ = ins.getKDJList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));
		System.out.println("K   " + newKDJ[0][close.size() - 1]);
		System.out.println("D   " + newKDJ[1][close.size() - 1]);
		System.out.println("J   " + newKDJ[2][close.size() - 1]);
		System.out.println("RSV " + newKDJ[3][close.size() - 1]);
	}
}
