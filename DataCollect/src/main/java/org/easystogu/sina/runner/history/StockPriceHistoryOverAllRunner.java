package org.easystogu.sina.runner.history;

public class StockPriceHistoryOverAllRunner {

    public static void main(String[] args) {
        //had better clean all the data from DB
        //history stock price
        HistoryStockPriceDownloadAndStoreDBRunner.main(args);
        //hou fuquan history price
        HistoryHouFuQuanStockPriceDownloadAndStoreDBRunner.main(args);
        //qian fuquan history price
        HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner.main(args);
        //count week price
        HistoryWeekStockPriceCountAndSaveDBRunner.main(args);
    }
}
