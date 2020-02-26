package org.easystogu.runner;

import java.util.ArrayList;
import java.util.List;
import org.easystogu.db.vo.view.FavoritesStockVO;

// DailySelectionRunner is only run for today, so this job is to go back to
// history and run then save into checkpoint_daily_selection
public class HistoryDailySelectionRunner extends DailySelectionRunner {
  public void runTask(int cpuIndex) {
    System.out.println("HistoryDailySelectionRunner for cpuIndex:" + cpuIndex);
    HistoryDailySelectionRunner runner = new HistoryDailySelectionRunner();
    int totalSotckDay = runner.stockPriceTable.getCounterDaysOfStockDate();
    List<String> allDates = runner.stockPriceTable.getLatestNStockDate(totalSotckDay);
    System.out.println("allDates size: " + allDates.size());
    //System.out.println(dates.get(dates.size() - 1));//first day of stock: 1990-12-19
    //System.out.println(dates.get(0));//current day of stock
    
    //List<String> stockIds = runner.stockConfig.getAllStockId();
    List<FavoritesStockVO> favoritesStockIds = runner.favoritesStockHelper.getByUserId("admin");
    
    List<String> stockIds = new ArrayList();
    for(int index = 0; index < favoritesStockIds.size(); index ++) {
        stockIds.add(favoritesStockIds.get(index).stockId);
    }
    
    //split the date into 4 sub groups for 4 cup run async
    int startIndex = getStartIndexFromDates(allDates, cpuIndex);
    int stopIndex = getStopIndexFromDates(allDates, cpuIndex);
    List<String> sudDateGroups = allDates.subList(startIndex, stopIndex);

    System.out.println("cpuIndex: " + cpuIndex + ", startIndex: " +startIndex + ", stopIndex: " + stopIndex);
    
    //
    for(int index = 0; index < sudDateGroups.size(); index++) {
      System.out.println("Process of data:" + sudDateGroups.get(index));
      new DailySelectionRunner().runForDate(sudDateGroups.get(index), stockIds);
    }
    System.out.println("HistoryDailySelectionRunner Complete for cpuIndex:" + cpuIndex);
  }
  
  //split into 4 cpu async run
  private int getStartIndexFromDates(List<String> allDates, int cpuIndex) {
    return (allDates.size() / 4) * cpuIndex;
  }
  
  private int getStopIndexFromDates(List<String> allDates, int cpuIndex) {
    if(cpuIndex == 3) {
      return allDates.size();
    }
    return (allDates.size() / 4) * (cpuIndex + 1);
  }
  
  public static void main(String[] args) {
    HistoryDailySelectionRunner runner = new HistoryDailySelectionRunner();
    //cpuIndex: 0,1,2,3
    runner.runTask(0);
    runner.runTask(1);
    runner.runTask(2);
    runner.runTask(3);
  }
}
