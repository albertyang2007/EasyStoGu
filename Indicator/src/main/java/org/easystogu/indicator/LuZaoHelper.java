package org.easystogu.indicator;


/*
 {鲁兆大趋势}
 MA19:MA(CLOSE, 19);
 MA43:EMA(MA19, 43);
 MA86:MA(CLOSE, 86);
 */
public class LuZaoHelper extends IND {
	public double[][] getLuZaoList(double[] CLOSE) {
		int length = CLOSE.length;
		double[][] lz = new double[3][length];

		lz[0] = MA(CLOSE, 19);
		lz[1] = MA(CLOSE, 43);
		lz[2] = MA(CLOSE, 86);

		return lz;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
