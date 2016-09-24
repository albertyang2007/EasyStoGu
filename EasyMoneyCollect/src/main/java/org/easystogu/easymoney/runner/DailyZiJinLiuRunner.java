package org.easystogu.easymoney.runner;

import java.util.List;

import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.table.ZiJinLiu3DayTableHelper;
import org.easystogu.db.access.table.ZiJinLiu5DayTableHelper;
import org.easystogu.db.access.table.ZiJinLiuTableHelper;
import org.easystogu.db.vo.table.ZiJinLiuVO;
import org.easystogu.easymoney.helper.DailyZiJinLiuFatchDataHelper;
import org.easystogu.utils.Strings;

//zijinliu, ddx use this
public class DailyZiJinLiuRunner implements Runnable {
	private FileConfigurationService config = FileConfigurationService.getInstance();
	private DailyZiJinLiuFatchDataHelper fatchDataHelper = new DailyZiJinLiuFatchDataHelper();
	private ZiJinLiuTableHelper zijinliuTableHelper = ZiJinLiuTableHelper.getInstance();
	private ZiJinLiu3DayTableHelper zijinliu3DayTableHelper = ZiJinLiu3DayTableHelper.getInstance();
	private ZiJinLiu5DayTableHelper zijinliu5DayTableHelper = ZiJinLiu5DayTableHelper.getInstance();
	private int toPage = 10;

	public DailyZiJinLiuRunner() {
		this.toPage = config.getInt("real_Time_Get_ZiJin_Liu_PageNumber", 10);
	}

	public void resetToAllPage() {
		this.toPage = DailyZiJinLiuFatchDataHelper.totalPages;
	}

	public void countAndSaved() {
		System.out.println("Fatch ZiJinLiu only toPage = " + toPage);
		List<ZiJinLiuVO> list = fatchDataHelper.getAllStockIdsZiJinLiu(toPage);
		System.out.println("Total Fatch ZiJinLiu size = " + list.size());

		// first clean today's zijinliu data
		if (list.size() > 0 && Strings.isNotEmpty(fatchDataHelper.currentDate))
			zijinliuTableHelper.deleteByDate(fatchDataHelper.currentDate);

		for (ZiJinLiuVO vo : list) {
			if (vo.isValidated()) {
				// zijinliuTableHelper.delete(vo.stockId, vo.date);
				zijinliuTableHelper.insert(vo);
				// System.out.println(vo.stockId + "=" + vo.name);
			}
		}
	}

	public void countAndSaved_3Day() {
		System.out.println("Fatch ZiJinLiu only toPage = " + toPage);
		List<ZiJinLiuVO> list = fatchDataHelper.get3DayAllStockIdsZiJinLiu(toPage);
		System.out.println("Total Fatch ZiJinLiu size = " + list.size());
		for (ZiJinLiuVO vo : list) {
			if (vo.isValidated()) {
				zijinliu3DayTableHelper.delete(vo.stockId, vo.date);
				zijinliu3DayTableHelper.insert(vo);
				// System.out.println(vo.stockId + "=" + vo.name);
			}
		}
	}

	public void countAndSaved_5Day() {
		System.out.println("Fatch ZiJinLiu only toPage = " + toPage);
		List<ZiJinLiuVO> list = fatchDataHelper.get5DayAllStockIdsZiJinLiu(toPage);
		System.out.println("Total Fatch ZiJinLiu size = " + list.size());
		for (ZiJinLiuVO vo : list) {
			if (vo.isValidated()) {
				zijinliu5DayTableHelper.delete(vo.stockId, vo.date);
				zijinliu5DayTableHelper.insert(vo);
				// System.out.println(vo.stockId + "=" + vo.name);
			}
		}
	}

	public void run() {
		countAndSaved();
		// countAndSaved_3Day();
		// countAndSaved_5Day();
	}

	public static void main(String[] args) {
		DailyZiJinLiuRunner runner = new DailyZiJinLiuRunner();
		runner.run();
	}
}
