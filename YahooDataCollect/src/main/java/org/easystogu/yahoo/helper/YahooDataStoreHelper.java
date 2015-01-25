package org.easystogu.yahoo.helper;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.yahoo.csv.CSVReader;

public class YahooDataStoreHelper {
	private StockPriceTableHelper tableHelper = new StockPriceTableHelper();
	private StockListConfigurationService stockConfig = StockListConfigurationService
			.getInstance();

	private void storeDataIntoDatabase(List<StockPriceVO> list) {
		try {
			tableHelper.insert(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void storeDataIntoDatabase(String path, String stockId) {
		CSVReader csvReader = new CSVReader(path + stockId + ".csv");
		List<StockPriceVO> list = csvReader.getAllDataList();
		// add stockId and name to vo
		for (StockPriceVO vo : list) {
			vo.setStockId(stockId);
			vo.setName(stockConfig.getStockName(stockId));
		}

		System.out.println("store into database: " + stockId + ", size="
				+ list.size());
		this.storeDataIntoDatabase(list);
	}

	public static void main(String[] args) {
	}
}
