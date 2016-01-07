package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;

// shenXian indicator
// 神仙大趋势H1:EMA(CLOSE,6);
// H2:EMA(神仙大趋势H1,18);
// H3:EMA(CLOSE,108),COLORYELLOW;
// STICKLINE(神仙大趋势H1>H2,神仙大趋势H1,H2,1,1),COLORRED;
// STICKLINE(神仙大趋势H1<H2,神仙大趋势H1,H2,1,1),COLORBLUE;
public class ShenXianHelper {
    TALIBWraper talib = new TALIBWraper();

    public double[][] getShenXianList(Double[] close) {
        int length = close.length;
        double[][] shenXian = new double[3][length];
        double[] myClose = new double[length];

        for (int i = 0; i < length; i++) {
            myClose[i] = close[i];
        }

        int h3_len = myClose.length < 108 ? myClose.length : 108;
        shenXian[0] = talib.getEma(myClose, 6);
        shenXian[1] = talib.getEma(shenXian[0], 18);
        shenXian[2] = talib.getEma(myClose, h3_len);//use 108

        //System.out.println(shenXian[0][length - 1]);
        //System.out.println(shenXian[1][length - 1]);
        //System.out.println(shenXian[2][length - 1]);
        return shenXian;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
        List<StockPriceVO> list = stockPriceTable.getStockPriceById("999999");
        TALIBWraper talib = new TALIBWraper();

        // list is order by date
        int length = list.size();
        double[] close = new double[length];
        int index = 0;
        for (StockPriceVO vo : list) {
            close[index++] = vo.close;
        }

        double[] h1 = talib.getEma(close, 6);
        System.out.println("h1=" + h1[h1.length - 1]);

        double[] h2 = talib.getEma(h1, 18);
        System.out.println("h2=" + h2[h2.length - 1]);

        double[] h3 = talib.getEma(close, 108);
        System.out.println("h3=" + h3[h3.length - 1]);

    }

}
