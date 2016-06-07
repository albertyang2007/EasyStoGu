package org.easystogu.indicator.runner.history;

import java.util.List;

public class IndicatorHistortOverAllRunner {
    public void countAndSave(List<String> stockIds) {
        //day
        new HistoryMacdCountAndSaveDBRunner().countAndSaved(stockIds);
        new HistoryKDJCountAndSaveDBRunner().countAndSaved(stockIds);
        new HistoryBollCountAndSaveDBRunner().countAndSaved(stockIds);
        new HistoryShenXianCountAndSaveDBRunner().countAndSaved(stockIds);
        new HistoryQSDDCountAndSaveDBRunner().countAndSaved(stockIds);
        new HistoryWRCountAndSaveDBRunner().countAndSaved(stockIds);
        //week
        new HistoryWeeklyMacdCountAndSaveDBRunner().countAndSaved(stockIds);
        new HistoryWeeklyKDJCountAndSaveDBRunner().countAndSaved(stockIds);
    }
    
    public void countAndSave(String stockId) {
        //day
        new HistoryMacdCountAndSaveDBRunner().countAndSaved(stockId);
        new HistoryKDJCountAndSaveDBRunner().countAndSaved(stockId);
        new HistoryBollCountAndSaveDBRunner().countAndSaved(stockId);
        new HistoryShenXianCountAndSaveDBRunner().countAndSaved(stockId);
        new HistoryQSDDCountAndSaveDBRunner().countAndSaved(stockId);
        new HistoryWRCountAndSaveDBRunner().countAndSaved(stockId);
        //week
        new HistoryWeeklyMacdCountAndSaveDBRunner().countAndSaved(stockId);
        new HistoryWeeklyKDJCountAndSaveDBRunner().countAndSaved(stockId);
    }

    public static void main(String[] args) {
        //day
        HistoryMacdCountAndSaveDBRunner.main(args);
        HistoryKDJCountAndSaveDBRunner.main(args);
        HistoryBollCountAndSaveDBRunner.main(args);
        HistoryShenXianCountAndSaveDBRunner.main(args);
        HistoryQSDDCountAndSaveDBRunner.main(args);
        HistoryWRCountAndSaveDBRunner.main(args);
        // week
        HistoryWeeklyMacdCountAndSaveDBRunner.main(args);
        HistoryWeeklyKDJCountAndSaveDBRunner.main(args);
    }
}
