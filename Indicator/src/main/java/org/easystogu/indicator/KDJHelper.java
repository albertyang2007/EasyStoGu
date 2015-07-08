package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;

public class KDJHelper {

    public double RSV(double close, double lown, double highn) {
        return 100.0 * (close - lown) / (highn - lown);
    }

    public double lown(Double[] low, int start, int end) {
        double lown = low[start];

        for (int i = start + 1; i <= end; i++) {
            if (lown > low[i]) {
                lown = low[i];
            }
        }

        return lown;
    }

    public double highn(Double[] high, int start, int end) {
        double highn = high[start];

        for (int i = start + 1; i <= end; i++) {
            if (highn < high[i]) {
                highn = high[i];
            }
        }

        return highn;
    }

    // �ӵ�һ����ʼ����JDK,������
    public double[][] getKDJList(Double[] close, Double[] low, Double[] high) {
        int length = close.length;

        double[][] KDJ = new double[4][length];// KDJ����:0��K����,1��D����,2��J����, RSV�����

        int start = 8;// start from index 8
        // ǰ��8�� KDJֵ��Ϊ0,�ӵ�9����ʼ����
        for (int index = 0; index < start; index++) {
            KDJ[0][index] = 50.0;
            KDJ[1][index] = 50.0;
            KDJ[2][index] = 50.0;
            KDJ[3][index] = 50.0;
        }

        for (int index = start; index < length; index++) {

            double lown = this.lown(low, index - 8, index);
            double highn = this.highn(high, index - 8, index);
            double RSV = this.RSV(close[index], lown, highn);

            KDJ[0][index] = (2.0 / 3.0) * KDJ[0][index - 1] + (1.0 / 3.0) * RSV;// K
            KDJ[1][index] = (2.0 / 3.0) * KDJ[1][index - 1] + (1.0 / 3.0) * KDJ[0][index];// D
            KDJ[2][index] = 3.0 * KDJ[0][index] - 2.0 * KDJ[1][index];// J	
            KDJ[3][index] = RSV;
        }

        return KDJ;
    }

    // ����һ��KDJ
    public double[] getKDJ(double[] preKDJ, double close, double lown, double highn) {
        double[] KDJ = new double[4];// KDJ����:0��K����,1��D����,2��J����

        double RSV = this.RSV(close, lown, highn);
        KDJ[0] = (2.0 / 3.0) * preKDJ[0] + (1.0 / 3.0) * RSV;// K
        KDJ[1] = (2.0 / 3.0) * preKDJ[1] + (1.0 / 3.0) * KDJ[0];// D
        KDJ[2] = 3.0 * KDJ[0] - 2.0 * KDJ[1];// J  
        KDJ[3] = RSV;

        return KDJ;
    }

    public static void main(String[] args) {
        StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
        KDJHelper ins = new KDJHelper();
        String stockId = "002194";
        List<Double> close = stockPriceTable.getAllClosePrice(stockId);
        List<Double> low = stockPriceTable.getAllLowPrice(stockId);
        List<Double> high = stockPriceTable.getAllHighPrice(stockId);
        double[][] KDJ = ins.getKDJList((Double[]) close.toArray(new Double[0]), (Double[]) low.toArray(new Double[0]),
                (Double[]) high.toArray(new Double[0]));

        System.out.println(KDJ[0][close.size() - 1]);
        System.out.println(KDJ[1][close.size() - 1]);
        System.out.println(KDJ[2][close.size() - 1]);
        System.out.println(KDJ[3][close.size() - 1]);
    }
}
