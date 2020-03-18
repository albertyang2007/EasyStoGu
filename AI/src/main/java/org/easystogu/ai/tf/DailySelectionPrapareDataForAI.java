package org.easystogu.ai.tf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.easystogu.ai.checkpoint.PricePredictResult;
import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.db.access.table.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.file.CSVFileHelper;

public class DailySelectionPrapareDataForAI implements Runnable {
  private CheckPointDailySelectionTableHelper checkPointDailySelectionTableHelper =
      CheckPointDailySelectionTableHelper.getInstance();
  private StockPriceTableHelper stockPriceTableHelper = StockPriceTableHelper.getInstance();
  private QianFuQuanStockPriceTableHelper qianFuQuanStockPriceTableHelper = QianFuQuanStockPriceTableHelper.getInstance();
  //below can be save into wsfconfig
  private static String checkPointDataPath = "C:/Users/eyaweiw/github/EasyStoGu/AI/mytest/checkPointData/";
  private static int predictPriceWithinNextNDays = 19;

  private static List<String> checkPoint4AiAnalyse = new ArrayList();
  private static List<String> pricePredict4AiResult = PricePredictResult.csvHeaders;

  static {    
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.QSDD_Bottom_Area.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.QSDD_Bottom_Gordon.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.QSDD_Top_Area.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.WR_Bottom_Area.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.WR_Bottom_Gordon.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.WR_Top_Area.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.WR_DI_BeiLi.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.ShenXian_Gordon.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.ShenXian_Dead.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.MACD_Gordon.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.MACD_Dead.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.MACD_TWICE_GORDON_W_Botton_MACD_DI_BEILI.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.MACD_TWICE_GORDON_W_Botton_TiaoKong_ZhanShang_Bull.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.LuZao_KDJ_Gordon_TiaoKongGaoKai.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.LuZao_GordonO_MA43_DownCross_MA86.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.LuZao_GordonI_MA19_UpCross_MA43.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.LuZao_GordonII_MA19_UpCross_MA86.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.LuZao_DeadI_MA43_UpCross_MA86.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.LuZao_DeadII_MA19_DownCross_MA43.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.LuZao_DeadIII_MA43_DownCross_MA86.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.LuZao_PhaseII_MACD_WEEK_GORDON_MACD_DAY_DIF_CROSS_0.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.LuZao_PhaseII_MACD_WEEK_GORDON_KDJ_WEEK_GORDON.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.LuZao_PhaseIII_MACD_WEEK_GORDON_MACD_DAY_DIF_CROSS_0.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.LuZao_PhaseIII_MACD_WEEK_GORDON_KDJ_WEEK_GORDON.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.MAGIC_NIGHT_DAYS_SHANG_ZHANG.name());
    checkPoint4AiAnalyse.add(DailyCombineCheckPoint.MAGIC_NIGHT_DAYS_XIA_DIE.name());
  }
  
  private static int getCheckPointIndex(String checkPointName) {
    for(int index=0; index < checkPoint4AiAnalyse.size(); index ++) {
      if(checkPoint4AiAnalyse.get(index).equalsIgnoreCase(checkPointName)) {
        return index;
      }
    }
    return -1;
  }

  // search CHECKPOINT_DAILY_SELECTION and get checkout to csv file
  private void prepareCsvData(String stockId) {    
    List<String> dates = stockPriceTableHelper.getAllDealDate(stockId);
    Collections.reverse(dates);
    List<String[]> rowContents = new ArrayList();
    for (String date : dates) {
      List<CheckPointDailySelectionVO> cpDailySelections =
          checkPointDailySelectionTableHelper.getCheckPointSelection(stockId, date);
      //add date before the flagArray as csv content
      //add result after the date as csv content
      String[] withDateContent = new String[1 + checkPoint4AiAnalyse.size() + pricePredict4AiResult.size()];
      withDateContent[0] = date;
      
      //add checkpoint selection array
      String[] checkPointArray = toCheckPointDailySelectionArray(cpDailySelections);
      for(int index = 0; index < checkPointArray.length; index++) {
        withDateContent[index + 1] = checkPointArray[index];
      }
      
      //add close low/high price in next N days
      if(cpDailySelections!=null && cpDailySelections.size()>0) {
        String[] pricePredictResult = this.toPricePredictResultArray(stockId, date);
        for(int index = 0; index < pricePredictResult.length; index++) {
          withDateContent[index + 1 + checkPoint4AiAnalyse.size()] = pricePredictResult[index];
        }
      }else {
        //add empty array
        String[] array = generateArrayWithContent(pricePredict4AiResult.size(), "0");
        //add empty array to content
        for(int index = 0; index < array.length; index++) {
          withDateContent[index + 1 + checkPoint4AiAnalyse.size()] = array[index];
        }
      }
      
      //
      rowContents.add(withDateContent);
    }
    
    //out put the stockId and int array to csv file
    writeToCsvFile(stockId, rowContents);
  }

  // convert one days CheckPointDailySelectionVO array
  // 稀疏矩阵
  private String[] toCheckPointDailySelectionArray(
      List<CheckPointDailySelectionVO> cpDailySelectionVos) {    
    String[] array = generateArrayWithContent(checkPoint4AiAnalyse.size(), "0");
    
    for (CheckPointDailySelectionVO cpDsVo : cpDailySelectionVos) {
      int index = getCheckPointIndex(cpDsVo.checkPoint);
      if(index != -1 && index < array.length) {
        array[index] = "1";
      }
    }
    return array;
  }
  
  // convert price predict result to array
  // 稀疏矩阵
  private String[] toPricePredictResultArray(String stockId, String date) {
    String[] array = generateArrayWithContent(pricePredict4AiResult.size(), "0");
    
    //current close price
    double closePrice = qianFuQuanStockPriceTableHelper.getStockPriceByIdAndDate(stockId, date).getClose();

    //high price
    double highPrice = qianFuQuanStockPriceTableHelper.getHighPriceStartDate(stockId, date, predictPriceWithinNextNDays);
    if(highPrice == 0.0) {
      return array;
    }
    int arrayIndexHighPrice = PricePredictResult.countCsvHeaderIndex((highPrice - closePrice)/closePrice, true);
    if(arrayIndexHighPrice != -1) {
      array[arrayIndexHighPrice] = "1";
    }

    //low price
    double lowPrice = qianFuQuanStockPriceTableHelper.getLowPriceStartDate(stockId, date, predictPriceWithinNextNDays);
    if(lowPrice == 0.0) {
      return array;
    }
    int arrayIndexLowPrice = PricePredictResult.countCsvHeaderIndex((lowPrice - closePrice)/closePrice, false);
    if(arrayIndexLowPrice != -1) {
      array[arrayIndexLowPrice] = "1";
    }

    return array;
  }
  
  private String[] generateArrayWithContent(int size, String content) {
    String[] array = new String[size];
    for(int i=0; i<array.length; i++) {
      array[i] = content;
    }
    return array;
  }
  
  //csv file format:
  //date,checkPoint1,checkPoint2,....,checkPointN,Flag_To_Be_Added
  private void writeToCsvFile(String stockId, List<String[]> rowContents) {
    //out put the stockId and int array to csv file
    String fileName = checkPointDataPath + stockId + "_stockCheckPoint.csv";
    List<String> headers = new ArrayList();
    headers.add("Date");
    headers.addAll(checkPoint4AiAnalyse);
    headers.addAll(pricePredict4AiResult);
    CSVFileHelper.write(fileName, headers.toArray(new String[headers.size()]), rowContents);
  }

  @Override
  public void run() {
    prepareCsvData("002352");
  }

  public static void main(String[] args) {
    new DailySelectionPrapareDataForAI().run();
  }
}
