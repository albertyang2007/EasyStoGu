package org.easystogu.runner;

import java.util.List;

import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.IndBollTableHelper;
import org.easystogu.db.access.IndKDJTableHelper;
import org.easystogu.db.access.IndMacdTableHelper;
import org.easystogu.db.access.IndMai1Mai2TableHelper;
import org.easystogu.db.access.IndShenXianTableHelper;
import org.easystogu.db.access.IndWeekBollTableHelper;
import org.easystogu.db.access.IndWeekKDJTableHelper;
import org.easystogu.db.access.IndWeekMacdTableHelper;
import org.easystogu.db.access.IndWeekMai1Mai2TableHelper;
import org.easystogu.db.access.IndWeekShenXianTableHelper;
import org.easystogu.db.access.IndWeekYiMengBSTableHelper;
import org.easystogu.db.access.IndXueShi2TableHelper;
import org.easystogu.db.access.IndYiMengBSTableHelper;
import org.easystogu.db.access.IndZhuliJinChuTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.db.table.CheckPointDailySelectionVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.runner.DailyBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyMai1Mai2CountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyShenXianCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekMai1Mai2CountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekShenXianCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekYiMengBSCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyXueShi2CountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyYiMengBSCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyZhuliJinChuCountAndSaveDBRunner;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;
import org.easystogu.utils.WeekdayUtil;

//based on latest close price, pre-estimate next working day's price
public class PreEstimateStockPriceRunner implements Runnable {
	private FileConfigurationService config = FileConfigurationService.getInstance();
	private StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private String currentDate = stockPriceTable.getLatestStockDate();
	private String nextDate = WeekdayUtil.nextWorkingDate(currentDate);
	private double nextDatePriceIncPercent = config.getDouble("nextDatePriceIncPercent", 1.04);
	private List<String> stockIds = stockConfig.getAllStockId();
	private CheckPointDailySelectionTableHelper dailyCheckPointTable = CheckPointDailySelectionTableHelper
			.getInstance();
	private double minEarnPercent = config.getDouble("minEarnPercent_Select_CheckPoint");

	private void injectMockStockPriceDate() {
		System.out.println("Inject Mock StockPrice for nextDate=" + nextDate + ", curDate=" + currentDate);
		for (String stockId : stockIds) {
			StockPriceVO vo = stockPriceTable.getStockPriceByIdAndDate(stockId, currentDate);
			if (vo != null) {
				StockPriceVO vo2 = new StockPriceVO();
				vo2.stockId = vo.stockId;
				vo2.date = nextDate;
				vo2.open = vo.close;
				vo2.low = vo.close;
				vo2.close = vo.close * nextDatePriceIncPercent;
				vo2.high = vo.close * nextDatePriceIncPercent;
				vo2.volume = vo.volume + 100;
				vo2.lastClose = vo.close;

				stockPriceTable.insert(vo2);
			}
		}
	}

	private void cleanupMockData() {
		System.out.println("Cleanup Mock Price Data for all tables. nextDate=" + nextDate + ", curDate=" + currentDate);
		stockPriceTable.deleteByDate(nextDate);

		// day table
		IndMacdTableHelper.getInstance().deleteByDate(nextDate);
		IndKDJTableHelper.getInstance().deleteByDate(nextDate);
		IndBollTableHelper.getInstance().deleteByDate(nextDate);
		IndMai1Mai2TableHelper.getInstance().deleteByDate(nextDate);
		IndShenXianTableHelper.getInstance().deleteByDate(nextDate);
		IndXueShi2TableHelper.getInstance().deleteByDate(nextDate);
		IndZhuliJinChuTableHelper.getInstance().deleteByDate(nextDate);
		IndYiMengBSTableHelper.getInstance().deleteByDate(nextDate);

		// week table
		WeekStockPriceTableHelper.getInstance().deleteByDate(nextDate);
		IndWeekMacdTableHelper.getInstance().deleteByDate(nextDate);
		IndWeekKDJTableHelper.getInstance().deleteByDate(nextDate);
		IndWeekBollTableHelper.getInstance().deleteByDate(nextDate);
		IndWeekMai1Mai2TableHelper.getInstance().deleteByDate(nextDate);
		IndWeekYiMengBSTableHelper.getInstance().deleteByDate(nextDate);
		IndWeekShenXianTableHelper.getInstance().deleteByDate(nextDate);

		// daily checkpoint table
		dailyCheckPointTable.deleteByDate(nextDate);
	}

	private void checkDailyCheckPointForBothDate() {
		System.out.println("CheckPoint for Both Date Report:");
		List<CheckPointDailySelectionVO> currDateCPList = dailyCheckPointTable.getDailyCheckPointByDate(currentDate);
		List<CheckPointDailySelectionVO> nextDateCPList = dailyCheckPointTable.getDailyCheckPointByDate(nextDate);
		for (CheckPointDailySelectionVO currDateCP : currDateCPList) {
			if (DailyCombineCheckPoint.getCheckPointByName(currDateCP.checkPoint).getEarnPercent() < minEarnPercent)
				continue;
			for (CheckPointDailySelectionVO nextDateCP : nextDateCPList) {
				if (DailyCombineCheckPoint.getCheckPointByName(nextDateCP.checkPoint).getEarnPercent() < minEarnPercent)
					continue;
				if (currDateCP.stockId.equals(nextDateCP.stockId)) {
					System.out.println(currDateCP.stockId + " " + stockConfig.getStockName(currDateCP.stockId)
							+ "\n   currDate=" + currDateCP.checkPoint + "\n   nextDate=" + nextDateCP.checkPoint);
				}
			}
		}
	}

	public void run() {
		try {
			String[] args = null;
			// day
			injectMockStockPriceDate();

			// day ind
			DailyMacdCountAndSaveDBRunner.main(args);
			DailyKDJCountAndSaveDBRunner.main(args);
			DailyBollCountAndSaveDBRunner.main(args);
			DailyMai1Mai2CountAndSaveDBRunner.main(args);
			DailyShenXianCountAndSaveDBRunner.main(args);
			DailyXueShi2CountAndSaveDBRunner.main(args);
			DailyZhuliJinChuCountAndSaveDBRunner.main(args);
			DailyYiMengBSCountAndSaveDBRunner.main(args);
			// week
			DailyWeeklyStockPriceCountAndSaveDBRunner.main(args);
			// week ind
			DailyWeekMacdCountAndSaveDBRunner.main(args);
			DailyWeekKDJCountAndSaveDBRunner.main(args);
			DailyWeekBollCountAndSaveDBRunner.main(args);
			DailyWeekMai1Mai2CountAndSaveDBRunner.main(args);
			DailyWeekShenXianCountAndSaveDBRunner.main(args);
			DailyWeekYiMengBSCountAndSaveDBRunner.main(args);

			// analyse
			DailySelectionRunner.main(args);

			// analyse both date
			checkDailyCheckPointForBothDate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			// clean up all the estimate price data
			cleanupMockData();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new PreEstimateStockPriceRunner().run();
	}
}
