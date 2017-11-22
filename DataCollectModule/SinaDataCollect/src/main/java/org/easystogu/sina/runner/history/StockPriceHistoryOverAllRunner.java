package org.easystogu.sina.runner.history;

public class StockPriceHistoryOverAllRunner implements Runnable {
	private String startDate;
	private String endDate;

	public StockPriceHistoryOverAllRunner(String _startDate, String _endDate) {
		this.startDate = _startDate;
		this.endDate = _endDate;
	}

	public void run() {
		// had better clean all the data from DB
		// history stock price
		HistoryStockPriceDownloadAndStoreDBRunner.main(new String[] { startDate, endDate });
		// hou fuquan history price
		// HistoryHouFuQuanStockPriceDownloadAndStoreDBRunner.main(args);
		// qian fuquan history price
		HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner.main(null);
		// count week price
		HistoryWeekStockPriceCountAndSaveDBRunner.main(null);

	}

	public static void main(String[] args) {
		// had better clean all the data from DB
		// history stock price
		HistoryStockPriceDownloadAndStoreDBRunner.main(args);
		// hou fuquan history price
		// HistoryHouFuQuanStockPriceDownloadAndStoreDBRunner.main(args);
		// qian fuquan history price
		HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner.main(args);
		// count week price
		HistoryWeekStockPriceCountAndSaveDBRunner.main(args);
	}
}
