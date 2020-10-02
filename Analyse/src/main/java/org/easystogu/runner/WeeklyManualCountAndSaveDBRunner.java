package org.easystogu.runner;

import org.easystogu.indicator.runner.history.HistoryWeeklyKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryWeeklyMacdCountAndSaveDBRunner;
import org.easystogu.sina.runner.history.HistoryWeekStockPriceCountAndSaveDBRunner;

public class WeeklyManualCountAndSaveDBRunner {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        HistoryWeekStockPriceCountAndSaveDBRunner.main(args);
        HistoryWeeklyMacdCountAndSaveDBRunner.mainWork(args);
        HistoryWeeklyKDJCountAndSaveDBRunner.mainWork(args);
    }
}
