package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;

import com.google.common.primitives.Doubles;

/*
 {趋势顶底}
 A:=MA(-100*(HHV(HIGH,34)-CLOSE)/(HHV(HIGH,34)-LLV(LOW,34)),19);
 B:=-100*(HHV(HIGH,14)-CLOSE)/(HHV(HIGH,14)-LLV(LOW,14));
 D:=EMA(-100*(HHV(HIGH,34)-CLOSE)/(HHV(HIGH,34)-LLV(LOW,34)),4);

 长期线:A+100,COLOR9900FF;
 短期线:B+100,COLOR888888;
 中期线:D+100,COLORYELLOW,LINETHICK2;
 */
public class QuShiDingDiHelper {
    private IND ind = new IND();

    public double[][] getQuShiDingDi(double[] CLOSE, double[] LOW, double[] HIGH) {
        int length = CLOSE.length;
        double[][] qsdd = new double[4][length];

        qsdd[0] = ind.MA2(
                ind.DIV(ind.MUL(-100, (ind.SUB(ind.HHV(HIGH, 34), CLOSE))),
                        (ind.SUB(ind.HHV(HIGH, 34), ind.LLV(LOW, 34)))), 19);

        return qsdd;
    }

    public static void main(String[] args) {
        StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
        QuShiDingDiHelper ins = new QuShiDingDiHelper();
        String stockId = "999999";
        List<Double> close = stockPriceTable.getAllClosePrice(stockId);
        List<Double> low = stockPriceTable.getAllLowPrice(stockId);
        List<Double> high = stockPriceTable.getAllHighPrice(stockId);

        double[][] qsdd = ins.getQuShiDingDi(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));
        System.out.println("长期线=" + (qsdd[0][close.size() - 1] + 100));
    }

}
