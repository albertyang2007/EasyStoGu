package org.albertyang2007.easystogu.macd;

import java.util.List;

import org.albertyang2007.easystogu.csv.CSVReader;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class MACDHelper2 {
    private static Core lib = new Core();
    private static MInteger outBegIdx = new MInteger();
    private static MInteger outNbElement = new MInteger();
    private static RetCode retCode;
    private static int lookback;

    static {
        outBegIdx.value = -1;
        outNbElement.value = -1;
        retCode = RetCode.InternalError;
        lookback = -1;
    }

    public static void getMACD(List<Double> list, int shortPeriod, int longPeriod, int midPeriod) {
        int length = list.size();
        double[] close = new double[length];
        int index = 0;
        for (Double d : list) {
            close[index++] = d;
        }
        double macd[] = new double[length];
        double signal[] = new double[length];
        double hist[] = new double[length];
        lookback = lib.macdLookback(15, 26, 9);
        retCode = lib.macd(0, length - 1, close, 15, 26, 9, outBegIdx, outNbElement, macd, signal, hist);

        System.out.println("lookback=" + lookback);
        System.out.println("retCode=" + retCode.toString());

        double ema15[] = new double[length];
        lookback = lib.emaLookback(15);
        retCode = lib.ema(0, length - 1, close, 15, outBegIdx, outNbElement, ema15);

        System.out.println("lookback=" + lookback);
        System.out.println("retCode=" + retCode.toString());

        double ema26[] = new double[length];
        lookback = lib.emaLookback(26);
        retCode = lib.ema(0, length - 1, close, 26, outBegIdx, outNbElement, ema26);

        System.out.println("lookback=" + lookback);
        System.out.println("retCode=" + retCode.toString());
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String csvFilePath = "classpath:/000821.csv";
        CSVReader reader = new CSVReader();
        List<Double> list = reader.getAllClosedPrice(csvFilePath);
        MACDHelper2.getMACD(list, 15, 26, 9);
    }

}
