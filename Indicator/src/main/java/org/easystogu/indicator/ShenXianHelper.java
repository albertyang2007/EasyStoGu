package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;

import com.google.common.primitives.Doubles;

// shenXian indicator
// 神仙大趋势H1:EMA(CLOSE,6);
// H2:EMA(神仙大趋势H1,18);
// H3:EMA(CLOSE,108),COLORYELLOW;
// STICKLINE(神仙大趋势H1>H2,神仙大趋势H1,H2,1,1),COLORRED;
// STICKLINE(神仙大趋势H1<H2,神仙大趋势H1,H2,1,1),COLORBLUE;
public class ShenXianHelper extends IND {
    public double[][] getShenXianList(double[] close) {
        int length = close.length;
        double[][] shenXian = new double[3][length];

        int len108 = (close.length < 108) ? close.length : 108;
        shenXian[0] = EMA(close, 6);
        shenXian[1] = EMA(shenXian[0], 18);
        shenXian[2] = EMA(close, len108);

        return shenXian;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
        ShenXianHelper ins = new ShenXianHelper();

        String stockId = "999999";
        List<Double> close = stockPriceTable.getAllClosePrice(stockId);

        double[][] sx = ins.getShenXianList(Doubles.toArray(close));
        System.out.println("H1=" + (sx[0][close.size() - 1]));
        System.out.println("H2=" + (sx[1][close.size() - 1]));
        System.out.println("H3=" + (sx[2][close.size() - 1]));
    }

}
