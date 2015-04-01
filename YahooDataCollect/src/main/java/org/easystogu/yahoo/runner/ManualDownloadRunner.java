package org.easystogu.yahoo.runner;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.yahoo.helper.YahooDataDownloadHelper;

public class ManualDownloadRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 涓�鎬ц幏鍙杫ahoo鐨勫巻鍙叉暟鎹紝杩欎釜鍑芥暟鍙兘瑕佽繍琛屽緢澶氭鎵嶈兘灏嗘墍鏈夋暟鎹笅杞�
		// run this main many time unti the total error is 0
		String[] startDate = { "2015", "0", "1" };// 2010-01-01
		String[] endDate = { "2015", "0", "23" };// 2015-01-23

		StockListConfigurationService config = StockListConfigurationService
				.getInstance();
		YahooDataDownloadHelper helper = new YahooDataDownloadHelper();
		helper.downloadAllHistoryData(config.getAllSZStockId(), "sz",
				startDate, endDate);
		helper.downloadAllHistoryData(config.getAllSHStockId(), "ss",
				startDate, endDate);
		System.out.println("TotalSuccess=" + helper.getTotalSuccess());		
		System.out.println("TotalSkip=" + helper.getTotalSkip());
		System.out.println("TotalError=" + helper.getTotalError());
	}

}
