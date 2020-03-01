package org.easystogu.ai.sklearn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.easystogu.db.access.table.CheckPointDailyStatisticsTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.CheckPointDailyStatisticsVO;
import org.easystogu.file.CSVFileHelper;

// the python scripts is: EasyStock_Predict_High.py
public class CheckPointStatisticsPrepareData {
  private StockPriceTableHelper stockPriceTableHelper = StockPriceTableHelper.getInstance();
  private CheckPointDailyStatisticsTableHelper checkPointDailyStatisticsTable =
      CheckPointDailyStatisticsTableHelper.getInstance();
  
  private static String path = "C:/Users/eyaweiw/github/EasyStoGu/AI/mytest/AI/";

  private void prepareDataForSkLearnClassifier() {
    List<String> dates = stockPriceTableHelper.getAllSZZSDealDate();
    Collections.reverse(dates);//order by date
    List<String[]> macdContents = new ArrayList();
    for (String date : dates) {
      //count the stock company has deal at that date
      int totalCompanyDeal = stockPriceTableHelper.countByDate(date);
      CheckPointDailyStatisticsVO macdG =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, "MACD_Gordon");
      CheckPointDailyStatisticsVO macdD =
          checkPointDailyStatisticsTable.getByCheckPointAndDate(date, "MACD_Dead");
      macdContents.add(new String[] {date, macdG!=null?macdG.count+"":"0", macdD!=null?macdD.count+"":"0"});
    }
    
    CSVFileHelper.write(path + "MACD.csv", "date,MACD_Gordon,MACD_Dead".split(","), macdContents);
  }

  public static void main(String[] args) {
    CheckPointStatisticsPrepareData ins = new CheckPointStatisticsPrepareData();
    ins.prepareDataForSkLearnClassifier();
  }
}
