package org.albertyang2007.easystogu.macd;

import java.util.List;

import org.albertyang2007.easystogu.csv.CSVReader;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class MACDHelper2 {
	private static Core lib = new Core();
	private static MInteger outBegIdx = new MInteger();
	private static MInteger outNbElement = new MInteger();
	private static RetCode retCode;
	private static int lookback;

	static {
		outBegIdx.value = -1;
		outNbElement.value = -1;
		retCode = RetCode.InternalError;
		lookback = -1;
	}

	public static void getMACD(List<Double> list, int shortPeriod,
			int longPeriod, int midPeriod) {
		int length = list.size();
		double[] close = new double[length];
		int index = 0;
		for (Double d : list) {
			close[length - ++index] = d;
			//close[index++] = d;
		}
		double macd[] = new double[length];
		double signal[] = new double[length];
		double hist[] = new double[length];
		lookback = lib.macdLookback(shortPeriod, longPeriod, midPeriod);
		retCode = lib.macd(0, length - 1, close, shortPeriod, longPeriod,
				midPeriod, outBegIdx, outNbElement, macd, signal, hist);
		
		outBegIdx.value = -1;
		outNbElement.value = -1;

		double ema15[] = new double[length];
		lookback = lib.emaLookback(shortPeriod);
		retCode = lib.ema(0, length - 1, close, shortPeriod, outBegIdx,
				outNbElement, ema15);
		
		outBegIdx.value = -1;
		outNbElement.value = -1;

		double ema26[] = new double[length];
		lookback = lib.emaLookback(longPeriod);
		retCode = lib.ema(0, length - 1, close, longPeriod, outBegIdx,
				outNbElement, ema26);

		double dif[] = new double[length];
		for (int i = 0; i < length; i++) {
			dif[i] = ema15[i] - ema26[i];
		}
		
		outBegIdx.value = -1;
		outNbElement.value = -1;

		double ema9[] = new double[length];
		lookback = lib.emaLookback(midPeriod);
		retCode = lib.ema(0, length - 1, dif, midPeriod, outBegIdx,
				outNbElement, ema9);
		
		System.out.println("macd[0]=" + macd[0]);
		System.out.println("signal[0]=" + signal[0]);
		System.out.println("hist[0]=" + hist[0]);

		// DIFF = EMA(CLOSE, SHORT) - EMA(CLOSE, LONG)
		double diff = ema15[0] - ema26[0];
		System.out.println("DIF=" + diff);
		// DEA= EMA(DIFF, M)
		double dea = ema9[0];
		System.out.println("DEA=" + dea);
		// MACD= 2*(DIFF - DEA)
		double macdI = 2 * (diff - dea);
		System.out.println("MACD=" + macdI);

		// Correct DIF=0.622 DEA=0.574 MACD=0.096
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String csvFilePath = "classpath:/000821.csv";
		CSVReader reader = new CSVReader();
		List<Double> list = reader.getAllClosedPrice(csvFilePath);
		MACDHelper2.getMACD(list, 12, 26, 9);
	}

}
