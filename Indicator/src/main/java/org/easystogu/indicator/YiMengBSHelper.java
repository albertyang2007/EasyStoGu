package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;

//yiMengBS indicator
//{网上两种说法1:}
//X1:=(C+L+H)/3;
//X2:EMA(X1,6);
//X3:EMA(X2,5);
//
//{网上两种说法2:}
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
public class YiMengBSHelper {
	TALIBWraper talib = new TALIBWraper();

	public double[][] getYiMengBSList(List<StockPriceVO> list) {
		// list is order by date
		int length = list.size();
		double[] close = new double[length];
		double[] low = new double[length];
		double[] high = new double[length];
		int index = 0;
		for (StockPriceVO vo : list) {
			close[index] = vo.close;
			low[index] = vo.low;
			high[index] = vo.high;
			index++;
		}

		double[][] yiMeng = new double[3][length];
		for (int i = 0; i < length; i++) {
			yiMeng[0][i] = (close[i] + low[i] + high[i]) / 3;
		}

		yiMeng[1] = talib.getEma(yiMeng[0], 6);
		yiMeng[2] = talib.getEma(yiMeng[1], 5);

		// System.out.println(yiMeng[1][length - 1]);
		// System.out.println(yiMeng[2][length - 1]);
		return yiMeng;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		YiMengBSHelper helper = new YiMengBSHelper();
		StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
		List<StockPriceVO> list = stockPriceTable.getStockPriceById("002194");
		helper.getYiMengBSList(list);
	}
}
