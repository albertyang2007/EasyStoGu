package org.easystogu.easymoney.runner;

import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.ZiJinLiuTableHelper;
import org.easystogu.db.table.ZiJinLiuVO;
import org.easystogu.easymoney.helper.DailyZiJinLiuFatchDataHelper;
import org.easystogu.utils.Strings;

public class DailyZiJinLiuXiangRunner implements Runnable {
	private FileConfigurationService config = FileConfigurationService.getInstance();
	private DailyZiJinLiuFatchDataHelper fatchDataHelper = new DailyZiJinLiuFatchDataHelper();
	private ZiJinLiuTableHelper zijinliuTableHelper = ZiJinLiuTableHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private String latestDate = stockPriceTable.getLatestStockDate();
	private boolean runInOffice = true;
	private int toPage = 5;

	public DailyZiJinLiuXiangRunner() {
		this.runInOffice = Strings.isNotEmpty(config.getString(Constants.httpProxyServer)) ? true : false;
		this.toPage = (this.runInOffice) ? config.getInt("real_Time_Get_ZiJin_Liu_PageNumber", 5)
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
			if (vo.isValidated()) {
				vo.setDate(latestDate);
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
