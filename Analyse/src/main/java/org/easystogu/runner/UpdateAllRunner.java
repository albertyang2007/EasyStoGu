package org.easystogu.runner;

import org.easystogu.indicator.runner.history.IndicatorHistortOverAllRunner;
import org.easystogu.report.HistoryAnalyseReport;
import org.easystogu.sina.runner.history.StockPriceHistoryOverAllRunner;

public class UpdateAllRunner {

    //be careful to run this method, it will cause all price update from web
    //and all indicator will be update and all checkpoint will be recount
    //it will cause much of time to run!!!!
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //StockPriceHistoryOverAllRunner.main(args);
        IndicatorHistortOverAllRunner.main(args);
        HistoryAnalyseReport.main(args);
    }
}
