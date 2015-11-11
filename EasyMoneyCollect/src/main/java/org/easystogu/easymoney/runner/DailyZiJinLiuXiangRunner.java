package org.easystogu.easymoney.runner;

import java.util.List;

import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.ZiJinLiuTableHelper;
import org.easystogu.db.table.ZiJinLiuVO;
import org.easystogu.easymoney.helper.DailyZiJinLiuFatchDataHelper;

public class DailyZiJinLiuXiangRunner implements Runnable {
	private FileConfigurationService config = FileConfigurationService.getInstance();
	private DailyZiJinLiuFatchDataHelper fatchDataHelper = new DailyZiJinLiuFatchDataHelper();
	private ZiJinLiuTableHelper zijinliuTableHelper = ZiJinLiuTableHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private String latestDate = stockPriceTable.getLatestStockDate();
	private boolean runInOffice = true;
	private int toPage = 10;

	public DailyZiJinLiuXiangRunner() {
		this.runInOffice = config.getBoolean("runInOffice", false);
		this.toPage = (this.runInOffice) ? config.getInt("real_Time_Get_ZiJin_Liu_PageNumber", 10)
				: DailyZiJinLiuFatchDataHelper.totalPages;
	}

	public void resetToAllPage() {
		this.toPage = DailyZiJinLiuFatchDataHelper.totalPages;
	}

	public void countAndSaved() {
		System.out.println("Fatch ZiJinLiu only toPage = " + toPage);
		List<ZiJinLiuVO> list = fatchDataHelper.getAllStockIdsZiJinLiu(toPage);
		System.out.println("Total Fatch ZiJinLiu size = " + list.size());
		for (ZiJinLiuVO vo : list) {
			vo.setDate(latestDate);
			if (vo.isValidated()) {
				zijinliuTableHelper.delete(vo.stockId, vo.date);
				zijinliuTableHelper.insert(vo);
				// System.out.println(vo.stockId + "=" + vo.name);
			}
		}
	}

	public void run() {
		countAndSaved();
	}

	public static void main(String[] args) {
		DailyZiJinLiuXiangRunner runner = new DailyZiJinLiuXiangRunner();
		runner.countAndSaved();
	}
}
