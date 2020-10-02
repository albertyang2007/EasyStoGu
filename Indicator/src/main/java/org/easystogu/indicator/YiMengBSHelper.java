package org.easystogu.indicator;

import org.springframework.stereotype.Component;

//yiMengBS indicator
//{网上两种说法1: (用这个)}
//X1:=(C+L+H)/3;
//X2:EMA(X1,6);
//X3:EMA(X2,5);
//
//{网上两种说法2: SLOPE不会破译!! 不采用}
//{X2:=EMA(C,2);
//X3:=EMA(SLOPE(C,21)*20+C,42);}
//
//STICKLINE(X2>X3,X2,X3,1,1),COLORRED;
//STICKLINE(X2<X3,X2,X3,1,1),COLORBLUE;
//
//买: IF(CROSS(X2,X3), 1, 0);
//卖: IF(CROSS(X3,X2), 1, 0);
//DRAWTEXT(买,L*0.98,'B');
//DRAWTEXT(卖,H*1.05,'S');

@Component
public class YiMengBSHelper extends IND {

	public double[][] getYiMengBSList(double[] close, double[] low, double[] high) {
		double[][] X = new double[2][close.length];
		double[] X1 = DIV(ADD(ADD(close, low), high), 3);
		X[0] = EMA(X1, 6);
		X[1] = EMA(X[0], 5);
		return X;
	}
}
