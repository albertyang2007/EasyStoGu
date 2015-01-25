package org.easystogu.yahoo.runner;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.yahoo.helper.YahooDataDownloadHelper;

public class AutoDownloadRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 根据当前时间检查数据库是否缺少近期数据，缺少则下载
		// run this main many time unti the total error is 0
		String[] startDate = { "2010", "0", "01" };// query last update date from db
		String[] endDate = { "2015", "0", "23" };// should be today

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
