package org.easystogu.sina.runner.history;

public class HistoryOverAllRunner {

	public static void main(String[] args) {
		HistoryStockPriceDownloadAndStoreDBRunner.main(args);
		HistoryWeekStockPriceCountAndSaveDBRunner.main(args);
	}
}
