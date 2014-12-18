package org.albertyang2007.easystogu.indicator.kdj;

import java.util.List;

import org.albertyang2007.easystogu.csv.CSVReader;
import org.albertyang2007.easystogu.indicator.TALIBWraper;

public class KDJHelper {

    public static void main(String[] args) {
        TALIBWraper talib = new TALIBWraper();
        String csvFilePath = "classpath:/000821.csv";
        CSVReader reader = new CSVReader(csvFilePath);
        List<Double> hightL = reader.getAllHightPrice();
        List<Double> lowL = reader.getAllLowPrice();
        List<Double> closeL = reader.getAllClosedPrice();

        //revert list
        int length = closeL.size();
        double[] close = new double[length];
        double[] hight = new double[length];
        double[] low = new double[length];
        int index = 0;
        for (Double d : closeL) {
            close[length - ++index] = d;
        }
        index = 0;
        for (Double d : hightL) {
            hight[length - ++index] = d;
        }
        index = 0;
        for (Double d : lowL) {
            low[length - ++index] = d;
        }

        int optInFastK_Period = 9;
        int optInFastD_Period = 9;
        int optInSlowK_Period = 5;
        int optInSlowD_Period = 5;
        double[][] kdj = talib.getStochF(hight, low, close, optInFastK_Period, optInFastD_Period);

        System.out.println("=" + kdj[0][length - 1]);
        System.out.println("=" + kdj[1][length - 1]);

    }

}
