package org.easystogu.yahoo.runner;

import java.io.File;

import org.easystogu.config.FileConfigurationService;
import org.easystogu.yahoo.helper.YahooDataStoreHelper;

public class ManualDataStoreRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 数据入库，一次性运行就好，多次运行会提示数据冲突
		// 给定目录下面所有csv文件入库
		FileConfigurationService fileConfile = FileConfigurationService
				.getInstance();
		YahooDataStoreHelper ins = new YahooDataStoreHelper();
		String csvPath = fileConfile.getString("yahoo.csv.file.path");
		File path = new File(csvPath);
		File[] files = path.listFiles();
		for (File file : files) {
			String stockId = file.getName().split(".csv")[0];
			ins.storeDataIntoDatabase(csvPath, stockId);
		}
	}
}
