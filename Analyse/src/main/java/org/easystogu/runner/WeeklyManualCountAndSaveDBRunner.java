package org.easystogu.runner;

import org.easystogu.indicator.runner.history.WeeklyBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.WeeklyKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.WeeklyMacdCountAndSaveDBRunner;
import org.easystogu.sina.runner.history.WeeklyStockPriceManualCountAndSaveDBRunner;

public class WeeklyManualCountAndSaveDBRunner {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        WeeklyStockPriceManualCountAndSaveDBRunner.main(args);
        WeeklyMacdCountAndSaveDBRunner.main(args);
        WeeklyKDJCountAndSaveDBRunner.main(args);
        WeeklyBollCountAndSaveDBRunner.main(args);
    }
}
