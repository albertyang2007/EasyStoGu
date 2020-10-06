package org.easystogu.indicator;

public class MAHelper extends IND {

	public double[] getMAList(double[] CLOSE, int day) {
		return MA(CLOSE, day);
	}
}
