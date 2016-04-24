package org.easystogu.yahoo.helper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.easystogu.config.FileConfigurationService;
import org.easystogu.http.util.HttpUtil;

public class YahooDataDownloadHelper {

	private FileConfigurationService fileConfile = FileConfigurationService.getInstance();

	private static final String yahooBaseUrl = "http://ichart.yahoo.com/table.csv?s=";
	private int totalError = 0;
	private int totalSkip = 0;
	private int totalSuccess = 0;

	public void downloadHistoryData(String stockId, String area, String[] startDate, String[] endDate) {
		String path = fileConfile.getString("yahoo.csv.file.path");
		String saveFile = path + stockId + ".csv";
		String url = yahooBaseUrl + stockId + "." + area + "&a=" + startDate[1] + "&b=" + startDate[2] + "&c="
				+ startDate[0] + "&d=" + endDate[1] + "&e=" + endDate[2] + "&f=" + endDate[0] + "&g=d";
		try {
			// first check if file already exist
			File file = new File(saveFile);
			if (!file.exists() || file.length() == 0) {
				HttpUtil.downloadFile(saveFile, url);
				this.totalSuccess++;
			} else {
				totalSkip++;
				System.out.println("File " + stockId + " already exist, skip");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			totalError++;
			System.out.println("Can't download stockId=" + stockId + ", error=" + e.getMessage());
			// e.printStackTrace();
		}
	}

	public void downloadAllHistoryData(List<String> stockId, String area, String[] startDate, String[] endDate) {
		for (int i = 0; i < stockId.size(); i++) {
			System.out.println("downloading " + stockId.get(i) + "," + (i + 1) + "/" + stockId.size());
			downloadHistoryData(stockId.get(i), area, startDate, endDate);
		}
	}

	public int getTotalError() {
		return this.totalError;
	}

	public int getTotalSkip() {
		return this.totalSkip;
	}

	public int getTotalSuccess() {
		return this.totalSuccess;
	}

	public static void main(String[] args) {
		YahooDataDownloadHelper helper = new YahooDataDownloadHelper();
		String stockId = "002609";
		String area = "sz";
		String[] startDate = { "2010", "0", "01" };// query last update date
		String[] endDate = { "2016", "3", "24" };// should be today
		helper.downloadHistoryData(stockId, area, startDate, endDate);
	}
}
