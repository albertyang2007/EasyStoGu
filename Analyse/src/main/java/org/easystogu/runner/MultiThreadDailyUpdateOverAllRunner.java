package org.easystogu.runner;

import java.util.Date;

import org.easystogu.multirunner.MultThreadRunner;
import org.easystogu.multirunner.WeekMultThreadRunner;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;

public class MultiThreadDailyUpdateOverAllRunner {

	public static void main(String[] args) {
		System.out.println("start at " + new Date());
		// day
		DailyStockPriceDownloadAndStoreDBRunner.main(args);
		// chuquan
		ChuQuanChuXiCheckerRunner.main(args);
		// week
		DailyWeeklyStockPriceCountAndSaveDBRunner.main(args);
		// day ind
		Thread t_day = new MultThreadRunner();
		t_day.start();

		// week ind
		Thread t_week = new WeekMultThreadRunner();
		t_week.start();

		try {
			t_day.join();
			t_week.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// analyse
		DailySelectionRunner.main(args);

		System.out.println("stop at " + new Date());
	}
}
