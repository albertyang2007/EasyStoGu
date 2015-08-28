package org.easystogu.runner;

import org.easystogu.indicator.runner.history.HistoryWeeklyBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryWeeklyKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryWeeklyMacdCountAndSaveDBRunner;
import org.easystogu.sina.runner.history.WeeklyStockPriceManualCountAndSaveDBRunner;

public class WeeklyManualCountAndSaveDBRunner {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        WeeklyStockPriceManualCountAndSaveDBRunner.main(args);
        HistoryWeeklyMacdCountAndSaveDBRunner.main(args);
        HistoryWeeklyKDJCountAndSaveDBRunner.main(args);
        HistoryWeeklyBollCountAndSaveDBRunner.main(args);
    }
}
