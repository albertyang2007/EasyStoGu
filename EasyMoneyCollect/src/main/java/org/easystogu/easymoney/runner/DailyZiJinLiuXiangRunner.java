package org.easystogu.easymoney.runner;

import java.util.List;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.ZiJinLiuTableHelper;
import org.easystogu.db.table.ZiJinLiuVO;
import org.easystogu.easymoney.helper.DailyZiJinLiuFatchDataHelper;

public class DailyZiJinLiuXiangRunner implements Runnable {
	private DailyZiJinLiuFatchDataHelper fatchDataHelper = new DailyZiJinLiuFatchDataHelper();
	private ZiJinLiuTableHelper zijinliuTableHelper = ZiJinLiuTableHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private String latestDate = stockPriceTable.getLatestStockDate();

	public void countAndSaved() {
		List<ZiJinLiuVO> list = fatchDataHelper.getAllStockIdsZiJinLiu();
		for (ZiJinLiuVO vo : list) {
			vo.setDate(latestDate);
			zijinliuTableHelper.delete(vo.stockId, vo.date);
			zijinliuTableHelper.insert(vo);
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
