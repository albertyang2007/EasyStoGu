package org.albertyang2007.easystogu.indicator.macd;

import java.util.List;

import org.albertyang2007.easystogu.csv.CSVReader;
import org.albertyang2007.easystogu.indicator.TALIBWraper;

public class MACDHelper2 {

    public static void main(String[] args) {
        TALIBWraper talib = new TALIBWraper();
        String csvFilePath = "classpath:/000821.csv";
        CSVReader reader = new CSVReader(csvFilePath);
        List<Double> list = reader.getAllClosedPrice();

        //revert list
        int length = list.size();
        double[] close = new double[length];
        int index = 0;
        for (Double d : list) {
            close[length - ++index] = d;
        }

        double[][] macd = talib.getMacdExt(close, 12, 26, 9);

        double dif = macd[0][list.size() - 1];
        double dea = macd[1][list.size() - 1];
        double macdRtn = (dif - dea) * 2;
        System.out.println("DIF=" + dif);
        System.out.println("DEA=" + dea);
        System.out.println("MACD=" + macdRtn);
        //output:
        //DIF=0.621911206552296
        //DEA=0.5740987235058063
        //MACD=0.09562496609297955
        // Refer:
        //DIF=0.622 
        //DEA=0.574 
        //MACD=0.096
    }
}
