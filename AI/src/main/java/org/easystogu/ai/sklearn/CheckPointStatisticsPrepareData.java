package org.easystogu.ai.sklearn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.db.access.table.CheckPointDailyStatisticsTableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.CheckPointDailyStatisticsVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.CSVFileHelper;
import org.easystogu.utils.Strings;

// the python scripts is: EasyStock_Predict_High.py
public class CheckPointStatisticsPrepareData {
  private StockPriceTableHelper stockPriceTableHelper = StockPriceTableHelper.getInstance();
  private CheckPointDailyStatisticsTableHelper checkPointDailyStatisticsTable =
      CheckPointDailyStatisticsTableHelper.getInstance();
  
  private static String path = "C:/Users/eyaweiw/github/EasyStoGu/AI/mytest/AI/";
  
  private void prepareStockPriceForSkLearnClassifier(String stockId, int dayRange) {
    List<StockPriceVO> spList = stockPriceTableHelper.queryByStockId(stockId);
    int[] indexs = new int[spList.size()];
    for (int index = dayRange; index < spList.size() - dayRange; index++) {
      //System.out.println("search index: " + index + ", date:" + spList.get(index).date + ", vo:" + spList.get(index).toString());
      List<StockPriceVO> subSpList1 = spList.subList(index - dayRange, index + 1);
      List<StockPriceVO> subSpList2 = spList.subList(index, index + dayRange);
      //search the highest index in both sub list
      int highIndex1 = getHighestPriceIndex(subSpList1);
      int highIndex2 = getHighestPriceIndex(subSpList2);
      //if the index is highest same as current index, then this is the highest in dayRange
      if((highIndex1 == subSpList1.size() -1) && (highIndex2 == 0)) {
        indexs[index] = 1;
        //System.out.println("find one highest index: " + index + ", date:" + spList.get(index).date);
      }
      
      //search the highest index in both sub list
      int lowIndex1 = getLowestPriceIndex(subSpList1);
      int lowIndex2 = getLowestPriceIndex(subSpList2);
      //if the index is highest same as current index, then this is the highest in dayRange
      if((lowIndex1 == subSpList1.size() - 1) && (lowIndex2 == 0)) {
        //System.out.println("find one lowest index: " + index + ", date:" + spList.get(index).date);
        indexs[index] = -1;
      }
    }
    //
    String[] csvHeader = new String[] { "date", "flags" };
    List<String[]> csvData = new ArrayList<String[]>(spList.size());
    for (int i = 0; i < indexs.length; i++) {
        csvData.add(new String[] { spList.get(i).getDate(), Integer.toString(indexs[i]) });
    }

    CSVFileHelper.write(path+ stockId +"_high_low.csv", csvHeader, csvData);
  }

  private void prepareCheckPointStatisticsForSkLearnClassifier() {
    List<String> dates = stockPriceTableHelper.getAllSZZSDealDate();
    Collections.reverse(dates);//order by date
    List<String[]> macdContents = new ArrayList();
    List<String[]> qsddContents = new ArrayList();
    List<String[]> wrContents = new ArrayList();
    List<String[]> sxContents = new ArrayList();
    List<String[]> lzCrossContents = new ArrayList();
    List<String[]> lzTrendContents = new ArrayList();
    int count = 0;
    for (String date : dates) {
      //count the stock company has deal at that date
      int totalCompanyDeal = stockPriceTableHelper.countByDate(date);
      
      if(count++ % 100 == 0){
        System.out.println("CheckPointStatisticsPrepareData Process count " + count + " of " + dates.size());
      }
      
      //count MACD Cross Statistics
      {
      CheckPointDailyStatisticsVO macdG =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.MACD_Gordon.name());
      CheckPointDailyStatisticsVO macdD =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.MACD_Dead.name());
      String[] content = new String[3];
      content[0] = date;
      content[1] = convertToDecimalOrZeroStr(macdG, totalCompanyDeal);
      content[2] = convertToDecimalOrZeroStr(macdD, totalCompanyDeal);
      macdContents.add(content);
      }
       
      //count QSDD Cross Statistics
      {
      CheckPointDailyStatisticsVO qsddTopArea =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.QSDD_Top_Area.name());
      CheckPointDailyStatisticsVO qsddBottomGordon =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.QSDD_Bottom_Gordon.name());
      CheckPointDailyStatisticsVO qsddBottomArea =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.QSDD_Bottom_Area.name());
      String[] content = new String[4];
      content[0] = date;
      content[1] = convertToDecimalOrZeroStr(qsddTopArea, totalCompanyDeal);
      content[2] = convertToDecimalOrZeroStr(qsddBottomGordon, totalCompanyDeal);
      content[3] = convertToDecimalOrZeroStr(qsddBottomArea, totalCompanyDeal);
      qsddContents.add(content);
      }
      
      //count WR Cross Statistics
      {
      CheckPointDailyStatisticsVO wrTopArea =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.WR_Top_Area.name());
      CheckPointDailyStatisticsVO wrBottomGordon =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.WR_Bottom_Gordon.name());
      CheckPointDailyStatisticsVO wrBottomArea =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.WR_Bottom_Area.name());
      String[] content = new String[4];
      content[0] = date;
      content[1] = convertToDecimalOrZeroStr(wrTopArea, totalCompanyDeal);
      content[2] = convertToDecimalOrZeroStr(wrBottomGordon, totalCompanyDeal);
      content[3] = convertToDecimalOrZeroStr(wrBottomArea, totalCompanyDeal);
      wrContents.add(content);
      }
      
      //count ShenXian Cross Statistics
      {
      CheckPointDailyStatisticsVO sxG =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.ShenXian_Gordon.name());
      CheckPointDailyStatisticsVO sxD =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.ShenXian_Dead.name());
      String[] content = new String[3];
      content[0] = date;
      content[1] = convertToDecimalOrZeroStr(sxG, totalCompanyDeal);
      content[2] = convertToDecimalOrZeroStr(sxD, totalCompanyDeal);
      sxContents.add(content);
      }
      
      //count LuZaoCross Statistics
      {
      CheckPointDailyStatisticsVO lzG0 =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.LuZao_GordonO_MA43_DownCross_MA86.name());
      CheckPointDailyStatisticsVO lzG1 =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.LuZao_GordonI_MA19_UpCross_MA43.name());
      CheckPointDailyStatisticsVO lzG2 =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.LuZao_GordonII_MA19_UpCross_MA86.name());
      CheckPointDailyStatisticsVO lzD1 =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.LuZao_DeadI_MA43_UpCross_MA86.name());
      CheckPointDailyStatisticsVO lzD2 =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.LuZao_DeadII_MA19_DownCross_MA43.name());
      String[] content = new String[6];
      content[0] = date;
      content[1] = convertToDecimalOrZeroStr(lzG0, totalCompanyDeal);
      content[2] = convertToDecimalOrZeroStr(lzG1, totalCompanyDeal);
      content[3] = convertToDecimalOrZeroStr(lzG2, totalCompanyDeal);
      content[4] = convertToDecimalOrZeroStr(lzD1, totalCompanyDeal);
      content[5] = convertToDecimalOrZeroStr(lzD2, totalCompanyDeal);
      lzCrossContents.add(content);
      }

      //count LuZaoTrend Statistics
      {
      CheckPointDailyStatisticsVO lzT1 =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.Trend_PhaseI_GuanCha.name());
      CheckPointDailyStatisticsVO lzT2 =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.Trend_PhaseII_JianCang.name());
      CheckPointDailyStatisticsVO lzT3 =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.Trend_PhaseIII_ChiGu.name());
      CheckPointDailyStatisticsVO lzT4 =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, DailyCombineCheckPoint.Trend_PhaseVI_JianCang.name());
      String[] content = new String[5];
      content[0] = date;
      content[1] = convertToDecimalOrZeroStr(lzT1, totalCompanyDeal);
      content[2] = convertToDecimalOrZeroStr(lzT2, totalCompanyDeal);
      content[3] = convertToDecimalOrZeroStr(lzT3, totalCompanyDeal);
      content[4] = convertToDecimalOrZeroStr(lzT4, totalCompanyDeal);
      lzTrendContents.add(content);
      }
    }
    
    //MACD
    CSVFileHelper.write(path + "MACD.csv", "date,MACD_Gordon,MACD_Dead".split(","), macdContents);
    //QSDD
    CSVFileHelper.write(path + "QSDD.csv", "date,QSDD_Top_Area,QSDD_Bottom_Gordon,QSDD_Bottom_Area".split(","), qsddContents);
    //WR
    CSVFileHelper.write(path + "WR.csv", "date,WR_Top_Area,WR_Bottom_Gordon,WR_Bottom_Area".split(","), wrContents);
    //ShenXian
    CSVFileHelper.write(path + "ShenXian.csv", "date,ShenXian_Gordon,ShenXian_Dead".split(","), sxContents);
    //LuZaoCross
    CSVFileHelper.write(path + "LuZaoCross.csv", "date,LuZao_GordonO_MA43_DownCross_MA86,LuZao_GordonI_MA19_UpCross_MA43,LuZao_GordonII_MA19_UpCross_MA86,LuZao_DeadI_MA43_UpCross_MA86,LuZao_DeadII_MA19_DownCross_MA43".split(","), 
        lzCrossContents);
    //LuZaoTrend
    CSVFileHelper.write(path + "LuZaoTrend.csv", "date,Trend_PhaseI_GuanCha,Trend_PhaseII_JianCang,Trend_PhaseIII_ChiGu,Trend_PhaseVI_JianCang".split(","), 
        lzTrendContents);
  }
  
  private String convertToDecimalOrZeroStr(CheckPointDailyStatisticsVO vo, int totalCompanyDeal) {
    if(vo != null) {
      return Strings.convert2ScaleDecimal((vo.count *1.0 / totalCompanyDeal), 3) + "";
    }
    return "0.0";
  }
  
  private int getHighestPriceIndex(List<StockPriceVO> subSpList) {
    double highest = subSpList.get(0).high;
    int index = 0;
    for (int i = 1; i < subSpList.size(); i++) {
        StockPriceVO vo = subSpList.get(i);
        if (highest < vo.high) {
            highest = vo.high;
            index = i;
        }
    }
    return index;
}

  private int getLowestPriceIndex(List<StockPriceVO> subSpList) {
    double lowest = subSpList.get(0).low;
    int index = 0;
    for (int i = 1; i < subSpList.size(); i++) {
        StockPriceVO vo = subSpList.get(i);
        if (lowest > vo.low) {
            lowest = vo.low;
            index = i;
        }
    }
    return index;
}

  public static void main(String[] args) {
    CheckPointStatisticsPrepareData ins = new CheckPointStatisticsPrepareData();
    ins.prepareCheckPointStatisticsForSkLearnClassifier();
    ins.prepareStockPriceForSkLearnClassifier("999999", 20);
  }
}
