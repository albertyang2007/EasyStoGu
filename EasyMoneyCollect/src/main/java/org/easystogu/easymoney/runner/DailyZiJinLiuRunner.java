package org.easystogu.easymoney.runner;

import java.util.List;

import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.ZiJinLiu3DayTableHelper;
import org.easystogu.db.access.ZiJinLiu5DayTableHelper;
import org.easystogu.db.access.ZiJinLiuTableHelper;
import org.easystogu.db.table.ZiJinLiuVO;
import org.easystogu.easymoney.helper.DailyZiJinLiuFatchDataHelper;

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
		zijinliuTableHelper.deleteByDate(fatchDataHelper.currentDate);
		for (ZiJinLiuVO vo : list) {
			if (vo.isValidated()) {
				zijinliuTableHelper.insert(vo);
				// System.out.println(vo.stockId + "=" + vo.name);
			}
		}
	}

	public void countAndSaved_3Day() {
		System.out.println("Fatch ZiJinLiu only toPage = " + toPage);
		List<ZiJinLiuVO> list = fatchDataHelper.get3DayAllStockIdsZiJinLiu(toPage);
		System.out.println("Total Fatch ZiJinLiu size = " + list.size());
		zijinliu3DayTableHelper.deleteByDate(fatchDataHelper.currentDate);
		for (ZiJinLiuVO vo : list) {
			if (vo.isValidated()) {
				zijinliu3DayTableHelper.insert(vo);
				// System.out.println(vo.stockId + "=" + vo.name);
			}
		}
	}

	public void countAndSaved_5Day() {
		System.out.println("Fatch ZiJinLiu only toPage = " + toPage);
		List<ZiJinLiuVO> list = fatchDataHelper.get5DayAllStockIdsZiJinLiu(toPage);
		System.out.println("Total Fatch ZiJinLiu size = " + list.size());
		zijinliu5DayTableHelper.deleteByDate(fatchDataHelper.currentDate);
		for (ZiJinLiuVO vo : list) {
			if (vo.isValidated()) {
				zijinliu5DayTableHelper.insert(vo);
				// System.out.println(vo.stockId + "=" + vo.name);
			}
		}
	}

	public void run() {
		countAndSaved();
		countAndSaved_3Day();
		countAndSaved_5Day();
	}

	public static void main(String[] args) {
		DailyZiJinLiuRunner runner = new DailyZiJinLiuRunner();
		runner.run();
	}
}
