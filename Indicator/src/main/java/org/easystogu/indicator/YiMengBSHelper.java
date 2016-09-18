package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;

import com.google.common.primitives.Doubles;

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
public class YiMengBSHelper extends IND {

	public double[][] getYiMengBSList(double[] close, double[] low, double[] high) {
		double[][] X = new double[2][close.length];
		double[] X1 = DIV(ADD(ADD(close, low), high), 3);
		X[0] = EMA(X1, 6);
		X[1] = EMA(X[0], 5);
		return X;
	}

	public static void main(String[] args) {
		StockPriceTableHelper stockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
		YiMengBSHelper ins = new YiMengBSHelper();
		String stockId = "999999";
		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);
		List<Double> close = StockPriceFetcher.getClosePrice(priceList);
		List<Double> low = StockPriceFetcher.getLowPrice(priceList);
		List<Double> high = StockPriceFetcher.getHighPrice(priceList);

		double[][] X = ins.getYiMengBSList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));
		System.out.println("X1=" + (X[0][close.size() - 1]));
		System.out.println("X2=" + (X[1][close.size() - 1]));
	}
}
