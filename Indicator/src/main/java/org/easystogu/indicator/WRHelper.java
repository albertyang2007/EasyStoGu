package org.easystogu.indicator;

import org.springframework.stereotype.Component;

//WR:=100-100*(HHV(HIGH,N)-CLOSE)/(HHV(HIGH,N)-LLV(LOW,N));

@Component
public class WRHelper extends IND {
	public double[][] getWRList(double[] CLOSE, double[] LOW, double[] HIGH, int N1, int N2, int N3) {
		int length = CLOSE.length + mockLength;
		double[][] wr = new double[3][length];

		// always add 120 mock date price before the list
		// append mock data at the begging
		CLOSE = insertBefore(CLOSE, LOW[0], mockLength);
		LOW = insertBefore(LOW, LOW[0], mockLength);
		HIGH = insertBefore(HIGH, LOW[0], mockLength);

		wr[0] = SUB(100, MUL(100, DIV(SUB(HHV(HIGH, N1), CLOSE), SUB(HHV(HIGH, N1), LLV(LOW, N1)))));
		wr[1] = SUB(100, MUL(100, DIV(SUB(HHV(HIGH, N2), CLOSE), SUB(HHV(HIGH, N2), LLV(LOW, N2)))));
		wr[2] = SUB(100, MUL(100, DIV(SUB(HHV(HIGH, N3), CLOSE), SUB(HHV(HIGH, N3), LLV(LOW, N3)))));

		// exclude the mockLength data
		wr[0] = subList(wr[0], mockLength, length);
		wr[1] = subList(wr[1], mockLength, length);
		wr[2] = subList(wr[2], mockLength, length);

		return wr;
	}
}
