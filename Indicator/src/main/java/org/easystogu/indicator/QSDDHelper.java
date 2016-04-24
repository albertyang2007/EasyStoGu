package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;

import com.google.common.primitives.Doubles;

/*
 {趋势顶底}
 A:= MA(-100*(HHV(HIGH,34)-CLOSE)/(HHV(HIGH,34)-LLV(LOW,34)),19);
 B:=    -100*(HHV(HIGH,14)-CLOSE)/(HHV(HIGH,14)-LLV(LOW,14));
 D:=EMA(-100*(HHV(HIGH,34)-CLOSE)/(HHV(HIGH,34)-LLV(LOW,34)),4);

 长期线:A+100,COLOR9900FF;
 短期线:B+100,COLOR888888;
 中期线:D+100,COLORYELLOW,LINETHICK2;
 */
public class QSDDHelper extends IND {

	public double[][] getQSDDList(double[] CLOSE, double[] LOW, double[] HIGH) {
		int length = CLOSE.length + mockLength;
		double[][] qsdd = new double[3][length];
		
		// always add 120 mock date price before the list
		// append mock data at the begging
		CLOSE = insertBefore(CLOSE, LOW[0], mockLength);
		LOW = insertBefore(LOW, LOW[0], mockLength);
		HIGH = insertBefore(HIGH, LOW[0], mockLength);

		double[] tmp = DIV(MUL(-100, (SUB(HHV(HIGH, 34), CLOSE))), (SUB(HHV(HIGH, 34), LLV(LOW, 34))));
		qsdd[0] = MA(tmp, 19);
		qsdd[1] = DIV(MUL(-100, (SUB(HHV(HIGH, 14), CLOSE))), SUB(HHV(HIGH, 14), LLV(LOW, 14)));
		// it should use EMA(d, 4), but I don't know why EMA return NaN, so use
		// MA(MA(d,4),2)
		// qsdd[2] = EMA(tmp, 4);
		qsdd[2] = MA(MA(tmp, 4), 2);

		// finally add 100
		qsdd[0] = ADD(qsdd[0], 100.0);// longTerm
		qsdd[1] = ADD(qsdd[1], 100.0);// shortTerm
		qsdd[2] = ADD(qsdd[2], 100.0);// midTerm

		// exclude the mockLength data
		qsdd[0] = subList(qsdd[0], mockLength, length);
		qsdd[1] = subList(qsdd[1], mockLength, length);
		qsdd[2] = subList(qsdd[2], mockLength, length);

		return qsdd;
	}

	public static void main(String[] args) {
		StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
		QSDDHelper ins = new QSDDHelper();
		String stockId = "002790";
		List<Double> close = stockPriceTable.getAllClosePrice(stockId);
		List<Double> low = stockPriceTable.getAllLowPrice(stockId);
		List<Double> high = stockPriceTable.getAllHighPrice(stockId);

		double[][] qsdd = ins.getQSDDList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));

		System.out.println("长期线=" + (qsdd[0][close.size() - 1]));
		System.out.println("中期线=" + (qsdd[2][close.size() - 1]));
		System.out.println("短期线=" + (qsdd[1][close.size() - 1]));

		System.out.println("长期线=" + (qsdd[0][1]));
		System.out.println("中期线=" + (qsdd[2][1]));
		System.out.println("短期线=" + (qsdd[1][1]));
	}

}
