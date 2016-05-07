package org.easystogu.runner;

import java.io.IOException;

import org.easystogu.indicator.runner.history.IndicatorHistortOverAllRunner;
import org.easystogu.report.HistoryAnalyseReport;
import org.easystogu.sina.runner.history.StockPriceHistoryOverAllRunner;
import org.easystogu.utils.WeekdayUtil;

public class UpdateAllRunner {

	// be careful to run this method, it will cause all price update from web
	// and all indicator will be update and all checkpoint will be recount
	// it will cause much of time to run!!!!
	public static void main(String[] args) throws IOException {
		System.out.println("Are you sure want to update all? (y/n)");
		char anster = (char) System.in.read();
		if (anster != 'y')
			return;

		long startL = System.currentTimeMillis();
		System.out.println("Start datetime:" + WeekdayUtil.currentDateTime());
		// task started

		// StockPriceHistoryOverAllRunner.main(args);
		IndicatorHistortOverAllRunner.main(args);
		HistoryAnalyseReport.main(args);

		// task end
		long endL = System.currentTimeMillis();
		long durationHours = (endL - startL) / (1000 * 60 * 60);
		System.out.println("Completed datetime:" + WeekdayUtil.currentDateTime() + ", it takes hours=" + durationHours);
	}
}
