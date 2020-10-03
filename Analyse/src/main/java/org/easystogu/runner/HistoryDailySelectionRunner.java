package org.easystogu.runner;

import java.util.ArrayList;
import java.util.List;
import org.easystogu.db.vo.view.FavoritesStockVO;
import org.springframework.stereotype.Component;

// DailySelectionRunner is only run for today, so this job is to go back to
// history and run favorites stockId (not all stockIds) then save into checkpoint_daily_selection
@Component
public class HistoryDailySelectionRunner extends DailySelectionRunner {
  public void runTask(int cpuIndex) {
    System.out.println("HistoryDailySelectionRunner for cpuIndex:" + cpuIndex);
    HistoryDailySelectionRunner runner = new HistoryDailySelectionRunner();
    int totalSotckDay = runner.stockPriceTable.getCounterDaysOfStockDate();
    List<String> allDates = runner.stockPriceTable.getLatestNStockDate(totalSotckDay);
    System.out.println("allDates size: " + allDates.size());
    // System.out.println(dates.get(dates.size() - 1));//first day of stock: 1990-12-19
    // System.out.println(dates.get(0));//current day of stock

    List<String> stockIds = new ArrayList<String>();

    // not count all the stockId since it will cause huge time
    // so just count the favorites stockId
    //List<String> stockIds = runner.stockConfig.getAllStockId();
    List<FavoritesStockVO> favoritesStockIds = runner.favoritesStockHelper.getByUserId("admin");

    for (int index = 0; index < favoritesStockIds.size(); index++) {
      stockIds.add(favoritesStockIds.get(index).stockId);
    }

    // split the date into 4 sub groups for 4 cup run async
    int startIndex = getStartIndexFromDates(allDates, cpuIndex);
    int stopIndex = getStopIndexFromDates(allDates, cpuIndex);
    List<String> sudDateGroups = allDates.subList(startIndex, stopIndex);

    System.out.println(
        "cpuIndex: " + cpuIndex + ", startIndex: " + startIndex + ", stopIndex: " + stopIndex);

    //
    for (int index = 0; index < sudDateGroups.size(); index++) {
      String date = sudDateGroups.get(index);
      System.out.println("Process of data:" + date);
      this.runForDate(date, stockIds);
    }
    System.out.println("HistoryDailySelectionRunner Complete for cpuIndex:" + cpuIndex);
  }

  // split into number of logic CPU async run
  private int getStartIndexFromDates(List<String> allDates, int cpuIndex) {
    return (allDates.size() / getLogicCPUNumber()) * cpuIndex;
  }

  private int getStopIndexFromDates(List<String> allDates, int cpuIndex) {
    if (cpuIndex == getLogicCPUNumber() -1) {
      return allDates.size();
    }
    return (allDates.size() / getLogicCPUNumber()) * (cpuIndex + 1);
  }

  public static int getLogicCPUNumber() {
    return Runtime.getRuntime().availableProcessors();
  }

  public void runAllUsingMultipCpu() {
    List<Integer> cpuList = new ArrayList<Integer>();
    for (int i = 0; i < getLogicCPUNumber() - 1; i++) {
      cpuList.add(new Integer(i));
    }

    cpuList.parallelStream().forEach(cpuIndex -> this.runTask(cpuIndex));
  }
}
