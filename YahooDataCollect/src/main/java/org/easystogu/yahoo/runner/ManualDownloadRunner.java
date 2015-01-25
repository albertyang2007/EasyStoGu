package org.easystogu.yahoo.runner;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.yahoo.helper.YahooDataDownloadHelper;

public class ManualDownloadRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 一次性获取yahoo的历史数据，这个函数可能要运行很多次才能将所有数据下载
		// run this main many time unti the total error is 0
		String[] startDate = { "2010", "0", "01" };// 2010-01-01
		String[] endDate = { "2015", "0", "23" };// 2015-01-23

		StockListConfigurationService config = StockListConfigurationService
				.getInstance();
		YahooDataDownloadHelper helper = new YahooDataDownloadHelper();
		helper.downloadAllHistoryData(config.getAllSZStockId(), "sz",
				startDate, endDate);
		helper.downloadAllHistoryData(config.getAllSHStockId(), "ss",
				startDate, endDate);
		System.out.println("TotalError=" + helper.getTotalError());
	}

}
