package org.easystogu.runner;

import java.util.List;
import org.easystogu.db.access.table.CheckPointDailyStatisticsTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.CheckPointDailyStatisticsVO;
import org.easystogu.utils.Strings;

//one time to update the CheckPointDailyStatistics table to set the rate (count/totalCompanyDeal)
public class UpdateCheckPointDailyStatistics {
  private static StockPriceTableHelper stockPriceTableHelper = StockPriceTableHelper.getInstance();
  private static CheckPointDailyStatisticsTableHelper checkPointDailyStatisticsTable =
      CheckPointDailyStatisticsTableHelper.getInstance();
  
  private static void runTask() {
    List<String> dates = stockPriceTableHelper.getAllSZZSDealDate();
    for (String date : dates) {
      if(!date.equals("2020-03-18")) {
        continue;
      }
      System.out.println("process "+ date);
      //count the stock company has deal at that date
      int totalCompanyDeal = stockPriceTableHelper.countByDate(date);
      List<CheckPointDailyStatisticsVO> cpDSList = checkPointDailyStatisticsTable.getByDate(date);
      
      for(CheckPointDailyStatisticsVO vo : cpDSList) {
        vo.rate = Strings.convert2ScaleDecimal(vo.count * 1.0 / totalCompanyDeal, 4);
        checkPointDailyStatisticsTable.updateRate(vo);
      }
    }
  }
  public static void main(String[] args) {
    UpdateCheckPointDailyStatistics.runTask();
  }

}
