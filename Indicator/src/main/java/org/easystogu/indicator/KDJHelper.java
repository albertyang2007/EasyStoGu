package org.easystogu.indicator;

/*
 RSV:=(CLOSE-LLV(LOW,9))/(HHV(HIGH,9)-LLV(LOW,9))*100;
 K:=SMA(RSV,3,1);
 D:=SMA(K,3,1);
 J:=3*K-2*D;
 */
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
}
