package org.easystogu.runner;

import java.util.ArrayList;
import java.util.List;

// DailySelectionRunner is only run for today, so this job is to go back to
// history and run then save into checkpoint_daily_selection
public class HistoryDailySelectionRunner extends DailySelectionRunner {
  public void runTask(int cpuIndex) {
    System.out.println("HistoryDailySelectionRunner for cpuIndex:" + cpuIndex);
    HistoryDailySelectionRunner runner = new HistoryDailySelectionRunner();
    int totalSotckDay = runner.stockPriceTable.getCounterDaysOfStockDate();
    List<String> dates = runner.stockPriceTable.getLatestNStockDate(totalSotckDay);
    List<String> stockIds = runner.stockConfig.getAllStockId();
    //System.out.println(dates.get(dates.size() - 1));//first day of stock: 1990-12-19
    //System.out.println(dates.get(0));//current day of stock
    
    //split the stockIds into 4 sub groups for 4 cup run async
    List<String> stockIdsSubGroups = new ArrayList();
    for(int index = 0; index < stockIds.size(); index ++) {
      if(index % 4 == cpuIndex) {
        stockIdsSubGroups.add(stockIds.get(index));
      }
    }
    
    //index is start from 3 to skip the already run date
    for(int index = 3; index < dates.size(); index++) {
      System.out.println("Process of data:" + dates.get(index));
      new DailySelectionRunner().runForDate(dates.get(index), stockIdsSubGroups);
    }
    System.out.println("HistoryDailySelectionRunner Complete for cpuIndex:" + cpuIndex);
  }
  public static void main(String[] args) {
    HistoryDailySelectionRunner runner = new HistoryDailySelectionRunner();
    //cpuIndex: 0,1,2,3
    runner.runTask(0);
  }
}
