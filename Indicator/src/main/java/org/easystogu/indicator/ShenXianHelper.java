package org.easystogu.indicator;

import org.springframework.stereotype.Component;

// shenXian indicator
// 神仙大趋势H1:EMA(CLOSE,6);
// H2:EMA(神仙大趋势H1,18);
// H3:EMA(CLOSE,108),COLORYELLOW;
// STICKLINE(神仙大趋势H1>H2,神仙大趋势H1,H2,1,1),COLORRED;
// STICKLINE(神仙大趋势H1<H2,神仙大趋势H1,H2,1,1),COLORBLUE;

@Component
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

	// HC5:(MA(HIGH-H1, 3)+H1)*1.0075;
	// HC6:(H1-MA(H1-LOW, 3))*(1-0.0075);
	// sell point: = IF(H1>H2 AND HIGH>HC5,1,0);
	// h3 is replaced by HC5
	// buy point: = IF(H1<H2 AND LOW<=HC6,1,0);
	public double[][] getShenXianSellPointList(double[] CLOSE, double[] HIGH, double[] LOW) {
		int length = CLOSE.length + mockLength;
		double[][] shenXian = new double[4][length];

		// always add 120 mock date price before the list
		// append mock data at the begging
		CLOSE = insertBefore(CLOSE, CLOSE[0], mockLength);
		HIGH = insertBefore(HIGH, HIGH[0], mockLength);
		LOW = insertBefore(LOW, LOW[0], mockLength);

		shenXian[0] = EMA(CLOSE, 6);
		shenXian[1] = EMA(shenXian[0], 18);
		// count HC5
		// h3 is replaced by HC5
		shenXian[2] = MUL(ADD(MA(SUB(HIGH, shenXian[0]), 3), shenXian[0]), 1.0075);
		// HC6
		shenXian[3] = MUL(SUB(shenXian[0], MA(SUB(shenXian[0], LOW), 3)), (1.0 - 0.0075));

		// exclude the mockLength data
		shenXian[0] = subList(shenXian[0], mockLength, length);
		shenXian[1] = subList(shenXian[1], mockLength, length);
		shenXian[2] = subList(shenXian[2], mockLength, length);
		shenXian[3] = subList(shenXian[3], mockLength, length);

		return shenXian;
	}
}
