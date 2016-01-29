package org.easystogu.sina.runner.history;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.easystogu.config.FileConfigurationService;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

//从sina下载季度数据到文件中
public class HistoryStockPriceDownloadRunner {
	private static Logger logger = LogHelper.getLogger(HistoryStockPriceDownloadRunner.class);
	private String baseUrl = "http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/";
	private String szzsUrl = "http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/000001/type/S.phtml";// 上证指数
	private FileConfigurationService configure = FileConfigurationService.getInstance();
	private String saveToPath = configure.getString("sina.history.file.path");
	private CompanyInfoFileHelper companyInfoHelper = CompanyInfoFileHelper.getInstance();

	// ��ȡstockIdĳ��ĳ���ȵ����
	public void getFromWebAndSaveToFile(String stockId, int year, int jidu) {
		URL url;
		String fileName = stockId + "_" + year + "_" + jidu + ".html";
		try {
			// save to this filename
			File file = new File(saveToPath + fileName);

			// if (file.exists() && (file.length() > 0)) {
			// System.out.println("File already exist, skip: " + fileName);
			// return;
			// }

			file.createNewFile();

			// get URL content
			url = new URL(baseUrl + stockId + ".phtml?year=" + year + "&jidu=" + jidu);
			if (stockId.equals(companyInfoHelper.getSZZSStockIdForDB()))
				url = new URL(szzsUrl + ".phtml?year=" + year + "&jidu=" + jidu);

			URLConnection conn = url.openConnection();

			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String inputLine;

			// use FileWriter to write file
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			while ((inputLine = br.readLine()) != null) {
				bw.write(inputLine + "\n");
			}

			bw.close();
			br.close();

			System.out.println("Saved file success: " + fileName);
			logger.debug("Saved file success: " + fileName);

		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger.error("Can not download and save file for " + fileName, e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Can not download and save file for " + fileName, e);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		HistoryStockPriceDownloadRunner runner = new HistoryStockPriceDownloadRunner();

		List<String> stockIds = stockConfig.getAllStockId();
		int count = 0;
		for (String stockId : stockIds) {
			if (stockId.equals("999999")) {
				System.out.println("Process " + ++count + " of " + stockIds.size());
				for (int year = 1991; year <= 2016; year++) {
					if (year == 2016) {
						runner.getFromWebAndSaveToFile(stockId, year, 1);
					} else {
						runner.getFromWebAndSaveToFile(stockId, year, 1);
						runner.getFromWebAndSaveToFile(stockId, year, 2);
						runner.getFromWebAndSaveToFile(stockId, year, 3);
						runner.getFromWebAndSaveToFile(stockId, year, 4);
					}
				}
			}
		}
	}
}
