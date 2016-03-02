package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;

import com.google.common.primitives.Doubles;

/*
 {MACD}
 DIF := EMA(CLOSE,12) - EMA(CLOSE,26);
 DEA := EMA(DIF,9);
 MACD:= 2*(DIF-DEA);
 */
public class MACDHelper extends IND {

	public double[][] getMACDList(double[] CLOSE) {
		int length = CLOSE.length;
		double[][] macd = new double[3][length];

		macd[0] = SUB(EMA(CLOSE, 12), EMA(CLOSE, 26));
		macd[1] = EMA(macd[0], 9);
		macd[2] = MUL(SUB(macd[0], macd[1]), 2);

		return macd;
	}

	public static void main(String[] args) {
		StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
		MACDHelper ins = new MACDHelper();
		String stockId = "999999";
		List<Double> close = stockPriceTable.getAllClosePrice(stockId);

		double[][] macd = ins.getMACDList(Doubles.toArray(close));
		System.out.println("DIF=" + (macd[0][close.size() - 1]));
		System.out.println("DEA=" + (macd[1][close.size() - 1]));
		System.out.println("MACD=" + (macd[2][close.size() - 1]));
	}

}
