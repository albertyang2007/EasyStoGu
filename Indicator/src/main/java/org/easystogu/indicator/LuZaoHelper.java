package org.easystogu.indicator;

import org.springframework.stereotype.Component;

/*
 {鲁兆大趋势}
 MA19:MA(CLOSE, 19);
 MA43:EMA(MA19, 43);
 MA86:MA(CLOSE, 86);
 */

@Component
public class LuZaoHelper extends IND {
	public double[][] getLuZaoList(double[] CLOSE) {
		int length = CLOSE.length + mockLength;
		double[][] luzao = new double[3][length];

		// always add 120 mock date price before the list
		// append mock data at the begging
		CLOSE = insertBefore(CLOSE, CLOSE[0], mockLength);

		luzao[0] = MA(CLOSE, 19);
		luzao[1] = MA(CLOSE, 43);
		luzao[2] = MA(CLOSE, 86);

		// exclude the mockLength data
		luzao[0] = subList(luzao[0], mockLength, length);
		luzao[1] = subList(luzao[1], mockLength, length);
		luzao[2] = subList(luzao[2], mockLength, length);

		return luzao;
	}
}
