package org.easystogu.indicator.kdj;

import java.util.List;

import org.easystogu.indicator.TALIBWraper;
import org.easystogu.yahoo.csv.CSVReader;

public class KDJHelper {

	public static void main(String[] args) {
		TALIBWraper talib = new TALIBWraper();
		String csvFilePath = "classpath:/600388.csv";
		CSVReader reader = new CSVReader(csvFilePath);
		List<Double> hightL = reader.getAllHightPrice();
		List<Double> lowL = reader.getAllLowPrice();
		List<Double> closeL = reader.getAllClosedPrice();

		// revert list
		int length = closeL.size();
		double[] close = new double[length];
		double[] hight = new double[length];
		double[] low = new double[length];
		int index = 0;
		for (Double d : closeL) {
			close[length - ++index] = d;
		}
		index = 0;
		for (Double d : hightL) {
			hight[length - ++index] = d;
		}
		index = 0;
		for (Double d : lowL) {
			low[length - ++index] = d;
		}

		int optInFastK_Period = 9;
		int optInFastD_Period = 3;
		int optInSlowK_Period = 3;
		int optInSlowD_Period = 3;
		double[][] kdj = talib.getStoch(hight, low, close, optInFastK_Period,
				optInSlowK_Period, optInSlowD_Period);

		System.out.println("=" + kdj[0][length - 1]);
		System.out.println("=" + kdj[1][length - 1]);
		
		kdj = talib.getStochF(hight, low, close, optInFastK_Period,
				optInFastD_Period);

		System.out.println("=" + kdj[0][length - 1]);
		System.out.println("=" + kdj[1][length - 1]);
	}

}
