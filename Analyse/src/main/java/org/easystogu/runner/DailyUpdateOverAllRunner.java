package org.easystogu.runner;

import org.easystogu.indicator.runner.DailyBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyShenXianCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekShenXianCountAndSaveDBRunner;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;

public class DailyUpdateOverAllRunner {

    public static void main(String[] args) {
        // day
        DailyStockPriceDownloadAndStoreDBRunner.main(args);
        ChuQuanChuXiUpdatePriceRunner.main(args);
        DailyMacdCountAndSaveDBRunner.main(args);
        DailyKDJCountAndSaveDBRunner.main(args);
        DailyBollCountAndSaveDBRunner.main(args);
        DailyShenXianCountAndSaveDBRunner.main(args);
        // week
        DailyWeeklyStockPriceCountAndSaveDBRunner.main(args);
        DailyWeekMacdCountAndSaveDBRunner.main(args);
        DailyWeekKDJCountAndSaveDBRunner.main(args);
        DailyWeekBollCountAndSaveDBRunner.main(args);
        DailyWeekShenXianCountAndSaveDBRunner.main(args);
        // analyse
        DailySelectionRunner.main(args);
    }
}
