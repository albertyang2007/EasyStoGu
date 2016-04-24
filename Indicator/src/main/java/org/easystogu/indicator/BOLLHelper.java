package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;

import com.tictactec.ta.lib.MAType;
import com.tictactec.ta.lib.MInteger;

/*
 {布林中轨（20日均线）}
 MA20:=MA(CLOSE,20);
 {布林上下轨}
 UPPER:=MA20+2*STD(CLOSE,20);
 LOWER:=MA20-2*STD(CLOSE,20);
 */
public class BOLLHelper extends IND {

	public double[][] getBOLLList(double[] prices, int optInTimePeriod, double optInNbDevUp, double optInNbDevDn) {
		int length = prices.length + mockLength;
		// always add 120 mock date price before the list
		// append mock data at the begging
		prices = insertBefore(prices, prices[0], mockLength);

		double[][] boll = this.getBOLLListOrig(prices, optInTimePeriod, optInNbDevUp, optInNbDevDn);

		// exclude the mockLength data
		boll[0] = subList(boll[0], mockLength, length);
		boll[1] = subList(boll[1], mockLength, length);
		boll[2] = subList(boll[2], mockLength, length);
		
		return boll;
	}

	// same as TALIBWraper.getBbands
	public double[][] getBOLLListOrig(double[] prices, int optInTimePeriod, double optInNbDevUp, double optInNbDevDn) {
		MAType optInMAType = MAType.Sma;

		double[] tempoutput1 = new double[prices.length];
		double[] tempoutput2 = new double[prices.length];
		double[] tempoutput3 = new double[prices.length];
		double[][] output = { new double[prices.length], new double[prices.length], new double[prices.length] };

		double[] result1 = new double[prices.length];
		double[] result2 = new double[prices.length];
		double[] result3 = new double[prices.length];

		MInteger begin = new MInteger();
		MInteger length = new MInteger();
		begin.value = -1;
		length.value = -1;

		core.bbands(0, prices.length - 1, prices, optInTimePeriod, optInNbDevUp, optInNbDevDn, optInMAType, begin,
				length, tempoutput1, tempoutput2, tempoutput3);

		for (int i = 0; i < optInTimePeriod - 1; i++) {
			result1[i] = 0;
			result2[i] = 0;
			result3[i] = 0;
		}
		for (int i = optInTimePeriod - 1; 0 < i && i < (prices.length); i++) {
			result1[i] = tempoutput1[i - optInTimePeriod + 1];
			result2[i] = tempoutput2[i - optInTimePeriod + 1];
			result3[i] = tempoutput3[i - optInTimePeriod + 1];
		}

		for (int i = 0; i < prices.length; i++) {
			output[0][i] = result1[i];
			output[1][i] = result2[i];
			output[2][i] = result3[i];
		}
		return output;
	}

	public static void main(String[] args) {
		StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
		List<StockPriceVO> list = stockPriceTable.getStockPriceById("002789");
		BOLLHelper ins = new BOLLHelper();
		// list is order by date
		int length = list.size();
		double[] close = new double[length];
		int index = 0;
		for (StockPriceVO vo : list) {
			close[index++] = vo.close;
		}

		double[][] boll = ins.getBOLLList(close, 20, 2.0, 2.0);

		double up = boll[0][list.size() - 1];
		double mb = boll[1][list.size() - 1];
		double dn = boll[2][list.size() - 1];

		System.out.println("MB=" + mb);
		System.out.println("UP=" + up);
		System.out.println("DN=" + dn);
		// output:
		// MB=34.13050000000002
		// UP=37.170114942718456
		// DN=31.09088505728158
		// Refer:
		// MB=34.13
		// UP=37.17
		// DN=31.09
	}

}
