package org.easystogu.sina.runner.history;

public class HistoryOverAllRunner {

    public static void main(String[] args) {
        // TODO Auto-generated method stub    
        //modify:year and season  
        HistoryStockPriceDownloadRunner.main(args);
        //modify:startDay and year_season
        HistoryStockPriceManualParseHtmlAndSaveToDB.main(args);
        //modify:year and week number
        WeeklyStockPriceManualCountAndSaveDBRunner.main(args);
    }

}
