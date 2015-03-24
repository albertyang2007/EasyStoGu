package org.easystogu.indicator.zhulixichou;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.SimpleMovingAverages;
import org.easystogu.indicator.TALIBWraper;

//主力吸筹
public class ZhuLiXiChouHelper {

	private TALIBWraper talibHelper = new TALIBWraper();
	private SimpleMovingAverages smaHelper = new SimpleMovingAverages();

	// 返回筹码
	// list is order by date
	// VAR2:=REF(LOW,1);
	// VAR3:=SMA(ABS(LOW-VAR2),3,1)/SMA(MAX(LOW-VAR2,0),3,1)*100;
	// VAR4:=EMA(IF(CLOSE*1.3,VAR3*10,VAR3/10),3);
	// VAR5:=LLV(LOW,13);
	// VAR6:=HHV(VAR4,13);
	// VAR7:=IF(MA(CLOSE,34),1,0);
	// VAR8:=EMA(IF(LOW<=VAR5,(VAR4+VAR6*2)/2,0),3)/618*VAR7;
	// VAR9:=IF(VAR8>100,100,VAR8);
	// 吸筹:VAR9,LINETHICK2,COLORF00FF0;
	// STICKLINE(VAR9>-120,0,VAR9,8,1),COLORF00FF0;
	public double getChouMa(List<StockPriceVO> list) {
		double chouMa = 0.0;
		int length = list.size();
		StockPriceVO curVO = list.get(length - 1);

		int displayIndex = length - 1;

		double[] var2 = new double[length];
		for (int i = 1; i < length; i++) {
			var2[i] = list.get(i - 1).low;
		}

		System.out.println("REF " + var2[displayIndex]);

		double[] abs1 = new double[length];
		for (int i = 1; i < length; i++) {
			abs1[i] = Math.abs(list.get(i).low - var2[i]);
		}
		System.out.println("ABS1 " + abs1[displayIndex]);

		double[] abs = new double[length - 2];
		for (int i = 0; i < length - 2; i++) {
			abs[i] = abs1[i + 2];
		}
		// System.out.println("ABS " + abs[displayIndex - 2]);

		double[] max1 = new double[length];
		for (int i = 1; i < length; i++) {
			max1[i] = Math.max(list.get(i).low - var2[i], 0);
		}
		System.out.println("MAX1 " + max1[displayIndex]);

		double[] max = new double[length - 2];
		for (int i = 0; i < length - 2; i++) {
			max[i] = max1[i + 2];
		}
		// System.out.println("MAX " + max[displayIndex - 2]);

		double[] sma1 = talibHelper.getSma(abs1, 3);

		System.out.println("SMA1 " + sma1[displayIndex]);

		double[] sma2 = talibHelper.getSma(max1, 3);

		System.out.println("SMA2 " + sma2[displayIndex]);

		double[] var3 = new double[length];
		for (int i = 0; i < length; i++) {
			var3[i] = sma1[i] / sma2[i] * 100.0;
		}

		System.out.println("VAR3 " + var3[displayIndex]);

		double[] close = new double[length];
		for (int i = 0; i < length; i++) {
			if (list.get(i).close > 0) {
				close[i] = var3[i] * 10.0;
			} else {
				close[i] = var3[i] / 10.0;
			}
		}

		System.out.println("Close " + close[displayIndex]);

		double[] var4 = talibHelper.getEma(close, 3);

		System.out.println("VAR4 " + var4[displayIndex]);

		return chouMa;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ZhuLiXiChouHelper helper = new ZhuLiXiChouHelper();
		StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
		List<StockPriceVO> spList = stockPriceTable.getStockPriceById("601088");//603799
		// Collections.reverse(spList);
		helper.getChouMa(spList);
	}

}
