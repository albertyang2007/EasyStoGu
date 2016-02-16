package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;

import com.google.common.primitives.Doubles;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

public class IND {
    private static final double DEFAULT_VALUE = 0.0;
    private Core core = new Core();

    public double[] LLV(double[] low, int n) {

        double[] llv = new double[low.length];

        for (int index = 0; index < low.length; index++) {
            llv[index] = this.lown(low, index - n + 1, index);
        }

        return llv;
    }

    public double[] HHV(double[] high, int n) {

        double[] hhv = new double[high.length];

        for (int index = 0; index < high.length; index++) {
            hhv[index] = this.highn(high, index - n + 1, index);
        }

        return hhv;
    }

    //same as TALIBWraper.getSma
    public double[] SMA(double[] prices, int N) {
        double[] tempOutPut = new double[prices.length];
        double[] output = new double[prices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        begin.value = -1;
        length.value = -1;

        core.sma(0, prices.length - 1, prices, N, begin, length, tempOutPut);

        for (int i = 0; i < N - 1; i++) {
            output[i] = 0;
        }
        for (int i = N - 1; 0 < i && i < (prices.length); i++) {
            output[i] = tempOutPut[i - N + 1];
        }

        return output;
    }

    // same as TALIBWraper.getEma
    // sometimes not so incorrect!
    public double[] EMA(double[] prices, int N) {
        double[] tempOutPut = new double[prices.length];
        double[] output = new double[prices.length];

        MInteger begin = new MInteger();
        MInteger length = new MInteger();
        begin.value = -1;
        length.value = -1;

        core.ema(0, prices.length - 1, prices, N, begin, length, tempOutPut);
        // Ema(int startIdx, int endIdx, double[] inReal, int optInTimePeriod,
        // out int outBegIdx, out int outNBElement, double[] outReal);
        for (int i = 0; i < N - 1; i++) {
            output[i] = 0;
        }
        for (int i = N - 1; 0 < i && i < (prices.length); i++) {
            output[i] = tempOutPut[i - N + 1];
        }
        return output;
    }

    // result of EMA should be same as EXPMA
    // same as SimpleMovingAverages.getFullEXPMA
    // sometimes not so incorrect! 
    public double[] EXPMA(double[] prices, int N) {
        double[] ema = new double[prices.length];
        for (int index = 0; index < prices.length; index++) {
            ema[index] = this.getEXPMA(this.subList(prices, 0, prices.length - index), N);
        }
        return ema;
    }

    private double getEXPMA(double[] prices, int N) {
        double k = 2.0 / (N + 1.0);
        double ema = prices[0];
        for (int index = 1; index < prices.length; index++) {
            ema = prices[index] * k + ema * (1 - k);
        }
        return ema;
    }

    // return double[] + double[]
    public double[] ADD(double[] a, double[] b) {
        double[] rtn = new double[a.length];
        for (int index = 0; index < a.length; index++) {
            rtn[index] = a[index] + b[index];
        }
        return rtn;
    }

    // return double[] + double
    public double[] ADD(double[] a, double b) {
        double[] rtn = new double[a.length];
        for (int index = 0; index < a.length; index++) {
            rtn[index] = a[index] + b;
        }
        return rtn;
    }

    public double[] ADD(double a, double[] b) {
        return ADD(b, a);
    }

    // return double[] - double[]
    public double[] SUB(double[] a, double[] b) {
        double[] rtn = new double[a.length];
        for (int index = 0; index < a.length; index++) {
            rtn[index] = a[index] - b[index];
        }
        return rtn;
    }

    // return double[] * double[]
    public double[] MUL(double[] a, double[] b) {
        double[] rtn = new double[a.length];
        for (int index = 0; index < a.length; index++) {
            rtn[index] = a[index] * b[index];
        }
        return rtn;
    }

    // return double[] / double[]
    public double[] DIV(double[] a, double[] b) {
        double[] rtn = new double[a.length];
        for (int index = 0; index < a.length; index++) {
            rtn[index] = a[index] / b[index];
        }
        return rtn;
    }

    // return double[] * double
    public double[] MUL(double[] a, double b) {
        double[] rtn = new double[a.length];
        for (int index = 0; index < a.length; index++) {
            rtn[index] = a[index] * b;
        }
        return rtn;
    }

    // return double * double[]
    public double[] MUL(double a, double[] b) {
        return MUL(b, a);
    }

    public double[] MA(double[] price, int N) {
        int length = price.length;
        double[] ma = new double[length];
        for (int index = length - 1; index > 0; index--) {
            if (((index - (N - 1)) >= 0) && ((index + 1) <= length)) {
                ma[index] = AVG(subList(price, index - (N - 1), index + 1));
            }
        }
        return ma;
    }

    public double AVG(double[] d) {
        double avg = 0.0;
        for (double v : d) {
            avg += v;
        }
        if (d.length > 0) {
            return avg / d.length;
        }
        return 0.0;
    }

    public double SUM(double[] d) {
        double sum = 0.0;
        for (double v : d) {
            sum += v;
        }
        return sum;
    }

    private double[] subList(double[] d, int start, int end) {
        double[] rtn = new double[end - start];
        for (int index = start; index < end; index++) {
            rtn[index - start] = d[index];
        }
        return rtn;
    }

    private double lown(double[] low, int start, int end) {
        if (start < 0)
            return DEFAULT_VALUE;

        double lown = low[start];

        for (int i = start + 1; i <= end; i++) {
            if (lown > low[i]) {
                lown = low[i];
            }
        }

        return lown;
    }

    private double highn(double[] high, int start, int end) {
        if (start < 0)
            return DEFAULT_VALUE;

        double highn = high[start];

        for (int i = start; i <= end; i++) {
            if (highn < high[i]) {
                highn = high[i];
            }
        }

        return highn;
    }

    public static void main(String[] args) {
        IND ind = new IND();
        StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
        String stockId = "999999";
        List<Double> close = stockPriceTable.getAllClosePrice(stockId);
        double[] ma1 = ind.EMA(Doubles.toArray(close), 19);
        double[] ma2 = ind.EXPMA(Doubles.toArray(close), 19);
        System.out.println("ma1=" + ma1[close.size() - 1]);
        System.out.println("ma2=" + ma2[0]);
    }
}
