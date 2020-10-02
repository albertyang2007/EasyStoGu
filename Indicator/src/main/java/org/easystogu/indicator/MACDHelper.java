package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Doubles;

/*
 {MACD}
 DIF := EMA(CLOSE,12) - EMA(CLOSE,26);
 DEA := EMA(DIF,9);
 MACD:= 2*(DIF-DEA);
 */

@Component
public class MACDHelper extends IND {

	public double[][] getMACDList(double[] CLOSE) {
		int length = CLOSE.length + mockLength;
		double[][] macd = new double[3][length];

		// always add 120 mock date price before the list
		// append mock data at the begging
		CLOSE = insertBefore(CLOSE, CLOSE[0], mockLength);

		macd[0] = SUB(EMA(CLOSE, 12), EMA(CLOSE, 26));
		macd[1] = EMA(macd[0], 9);
		macd[2] = MUL(SUB(macd[0], macd[1]), 2);

		// exclude the mockLength data
		macd[0] = subList(macd[0], mockLength, length);
		macd[1] = subList(macd[1], mockLength, length);
		macd[2] = subList(macd[2], mockLength, length);

		return macd;
	}

	public static void main(String[] args) {
		StockPriceTableHelper stockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
		MACDHelper ins = new MACDHelper();
		String stockId = "002789";
		List<Double> close = stockPriceTable.getAllClosePrice(stockId);

		double[][] macd = ins.getMACDList(Doubles.toArray(close));
		System.out.println("DIF=" + (macd[0][close.size() - 1]));
		System.out.println("DEA=" + (macd[1][close.size() - 1]));
		System.out.println("MACD=" + (macd[2][close.size() - 1]));
		
		System.out.println("DIF=" + (macd[0][1]));
		System.out.println("DEA=" + (macd[1][1]));
		System.out.println("MACD=" + (macd[2][1]));
	}
}
