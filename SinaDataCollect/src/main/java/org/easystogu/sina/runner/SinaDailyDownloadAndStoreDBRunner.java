package org.easystogu.sina.runner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.sina.common.RealTimePriceVO;
import org.easystogu.sina.helper.SinaDataDownloadHelper;
import org.easystogu.utils.WeekdayUtil;

public class SinaDailyDownloadAndStoreDBRunner {
	private StockListConfigurationService stockConfig = StockListConfigurationService
			.getInstance();
	private StockPriceTableHelper tableHelper = new StockPriceTableHelper();
	private SinaDataDownloadHelper sinaHelper = new SinaDataDownloadHelper();
	private int totalError = 0;
	private int totalSize = 0;

	public void downloadDataAndSaveIntoDB() {
		List<String> shStockIds = stockConfig.getAllSHStockId("sh");
		List<String> szStockIds = stockConfig.getAllSZStockId("sz");

		List<String> totalStockIds = new ArrayList<String>();

		totalStockIds.addAll(shStockIds);
		totalStockIds.addAll(szStockIds);

		int batchSize = 200;
		int batchs = totalStockIds.size() / batchSize;
		totalSize = totalStockIds.size();

		// 分批取数据
		int index = 0;
		for (; index < batchs; index++) {
			List<RealTimePriceVO> list = sinaHelper
					.fetchDataFromWeb(totalStockIds.subList(index * batchSize,
							(index + 1) * batchSize));
			for (RealTimePriceVO vo : list) {
				this.saveIntoDB(vo);
			}
		}
		// 去剩余数据
		List<RealTimePriceVO> list = sinaHelper.fetchDataFromWeb(totalStockIds
				.subList(index * batchSize, totalStockIds.size()));
		for (RealTimePriceVO vo : list) {
			this.saveIntoDB(vo);
		}
	}

	private void saveIntoDB(RealTimePriceVO vo) {
		try {
			StockPriceVO svo = vo.convertToStockPriceVO();
			if (svo.isValidated()) {
				System.out.println("saving into DB, vo=" + svo);
				tableHelper.insert(svo);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Can't save to DB, vo=" + vo + ", error="
					+ e.getMessage());
			e.printStackTrace();
			totalError++;
		}
	}

	private void printResult() {
		System.out.println("totalSize=" + this.totalSize);
		System.out.println("totalError=" + this.totalError);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 每日下载收盘数据和入库
		// 由于http url请求长度不能超过2083，需要将请求分批发送
		if (WeekdayUtil.isCurrentTimeWorkingDayInDealTime()) {
			System.out
					.println("Now is still in deal time. Please wait, or just comment this code.");
			return;
		}

		SinaDailyDownloadAndStoreDBRunner runner = new SinaDailyDownloadAndStoreDBRunner();
		runner.downloadDataAndSaveIntoDB();
		runner.printResult();
	}
}
