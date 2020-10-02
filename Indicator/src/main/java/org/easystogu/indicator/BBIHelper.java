package org.easystogu.indicator;

import org.springframework.stereotype.Component;

@Component
public class BBIHelper extends IND {
	public double[][] getBBIList(double[] CLOSE) {
		double[][] bbi = new double[2][CLOSE.length];

		bbi[0] = DIV((ADD(MA(CLOSE, 3), MA(CLOSE, 6), MA(CLOSE, 12), MA(CLOSE, 24))), 4);
		bbi[1] = CLOSE;
		return bbi;
	}
}
