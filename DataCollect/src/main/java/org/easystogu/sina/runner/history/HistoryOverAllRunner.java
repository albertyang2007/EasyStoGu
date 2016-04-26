package org.easystogu.sina.runner.history;

public class HistoryOverAllRunner {

    public static void main(String[] args) {
        //had better clean all the data from DB
        //history stock price
        HistoryStockPriceDownloadAndStoreDBRunner.main(args);
        //count week price
        HistoryWeekStockPriceCountAndSaveDBRunner.main(args);
        //fuquan history price
        HistoryFuQuanStockPriceDownloadAndStoreDBRunner.main(args);
        //count the indicator
    }
}
