package org.easystogu.ai.checkpoint;

import java.util.ArrayList;
import java.util.List;

public enum PricePredictResult {
  //in the next N days
  LowPrice_Low_30_Next_N_Day(-10.0, -0.30, false),//the lowest price in the next N days is lower 30% then current price
  LowPrice_Low_20_Next_N_Day(-0.30, -0.20, false),
  LowPrice_Low_10_Next_N_Day(-0.20, -0.10, false),
  LowPrice_Equal_Next_N_Day(-0.10, 0.10, false),
  
  HighPrice_Equal_Next_N_Day(-0.10, 0.10, true),
  HighPrice_High_10_Next_N_Day(0.10, 0.20, true),
  HighPrice_High_20_Next_N_Day(0.20, 0.30, true),
  HighPrice_High_30_Next_N_Day(0.30, 10.0, true);//the highest price in the next N days is higher 30% then current price
  
  private double min;
  private double max;
  private boolean isHighPrice;//true: isHighPrice; false: isLowPrice
  
  public static List<String> csvHeaders = toCsvHeaders();
  
  private static List<String> toCsvHeaders(){
    List<String> headers = new ArrayList();
    for(PricePredictResult value : PricePredictResult.values()) {
      headers.add(value.name());
    }
    return headers;
  }
  
  private PricePredictResult(double min, double max, boolean isHighPrice) {
    this.min = min;
    this.max = max;
    this.isHighPrice = isHighPrice;
  }
  
  public static int countCsvHeaderIndex(double result, boolean isHighPrice) {
    PricePredictResult predictResult = null;
    for(PricePredictResult value : PricePredictResult.values()) {
      if(value.isHighPrice == isHighPrice) {
        if(result >= value.min && result < value.max) {
          predictResult = value;
        }
      }
    }
    
    if(predictResult !=null ) {
      for(int index = 0; index < csvHeaders.size(); index++) {
        if(csvHeaders.get(index).equalsIgnoreCase(predictResult.name())) {
          return index;
        }
      }
    }
    
    return -1;
  }
  
  public static void main(String[] args) {
    System.out.println(PricePredictResult.countCsvHeaderIndex(-0.15, true));
    System.out.println(PricePredictResult.countCsvHeaderIndex(0.0, true));
    System.out.println(PricePredictResult.countCsvHeaderIndex(0.15, true));
    
    System.out.println(PricePredictResult.countCsvHeaderIndex(-0.15, false));
    System.out.println(PricePredictResult.countCsvHeaderIndex(0.0, false));
    System.out.println(PricePredictResult.countCsvHeaderIndex(0.15, false));
  }
}
