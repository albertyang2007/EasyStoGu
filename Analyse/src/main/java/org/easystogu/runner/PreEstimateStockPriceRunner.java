package org.easystogu.runner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.EstimateStockTableHelper;
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
import org.easystogu.db.table.EstimateStockVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.runner.AllDailyIndCountAndSaveDBRunner;
import org.easystogu.report.ReportTemplate;
import org.easystogu.sina.runner.DailyWeeklyStockPriceCountAndSaveDBRunner;
import org.easystogu.utils.WeekdayUtil;

//based on latest close price, pre-estimate next working day's price
public class PreEstimateStockPriceRunner implements Runnable {
	private FileConfigurationService config = FileConfigurationService.getInstance();
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private EstimateStockTableHelper estimateStockTable = EstimateStockTableHelper.getInstance();
	private String currentDate = stockPriceTable.getLatestStockDate();
	private String nextDate = WeekdayUtil.nextWorkingDate(currentDate);
	private double nextDatePriceIncPercent = config.getDouble("nextDatePriceIncPercent", 1.04);
	private List<String> allStockIds = stockConfig.getAllStockId();
	private CheckPointDailySelectionTableHelper dailyCheckPointTable = CheckPointDailySelectionTableHelper
			.getInstance();
	private double minEarnPercent = config.getDouble("minEarnPercent_Select_CheckPoint");

	private void injectMockStockPriceDate() {
		System.out.println("Inject Mock StockPrice for nextDate=" + nextDate + ", curDate=" + currentDate);
		for (String stockId : allStockIds) {
			StockPriceVO vo = stockPriceTable.getStockPriceByIdAndDate(stockId, currentDate);
			if (vo != null) {
				StockPriceVO vo2 = new StockPriceVO();
				vo2.stockId = vo.stockId;
				vo2.date = nextDate;
				vo2.open = vo.close * nextDatePriceIncPercent;
				vo2.low = vo2.open;
				vo2.close = vo2.open;
				vo2.high = vo2.open;
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

	private void saveEstimateStockToDB() {
		System.out.println("Save Estimate Stock To DB:");
		// first empty nextDate estimateStock
		estimateStockTable.delete(nextDate);
		List<CheckPointDailySelectionVO> nextDateCPList = dailyCheckPointTable.getDailyCheckPointByDate(nextDate);
		for (CheckPointDailySelectionVO nextDateCP : nextDateCPList) {
			if (DailyCombineCheckPoint.getCheckPointByName(nextDateCP.checkPoint).getEarnPercent() >= this.minEarnPercent) {
				EstimateStockVO vo = new EstimateStockVO(nextDateCP.stockId, nextDateCP.date);
				estimateStockTable.insert(vo);
			}
		}
	}

	// if both date has checkpoint, this must be a NIU gu 牛股!
	private void checkDailyCheckPointForBothDate() {
		System.out.println("CheckPoint for Both Date Report:");
		Collection<String> bothHitIds = new ArrayList<String>();

		List<CheckPointDailySelectionVO> currDateCPList = dailyCheckPointTable.getDailyCheckPointByDate(currentDate);
		List<CheckPointDailySelectionVO> nextDateCPList = dailyCheckPointTable.getDailyCheckPointByDate(nextDate);
		for (CheckPointDailySelectionVO currDateCP : currDateCPList) {
			if (DailyCombineCheckPoint.getCheckPointByName(currDateCP.checkPoint).getEarnPercent() < minEarnPercent)
				continue;
			for (CheckPointDailySelectionVO nextDateCP : nextDateCPList) {
				if (DailyCombineCheckPoint.getCheckPointByName(nextDateCP.checkPoint).getEarnPercent() < minEarnPercent)
					continue;
				if (currDateCP.stockId.equals(nextDateCP.stockId)) {
					if (!bothHitIds.contains(currDateCP.stockId)) {
						bothHitIds.add(currDateCP.stockId);
						break;
					}
				}
			}
		}
		// report to html
		reportToHtml(currentDate, nextDate, bothHitIds);
	}

	public void reportToHtml(String currDate, String nextDate, Collection<String> bothHitIds) {
		String file = config.getString("report.estimate.html.file").replaceAll("nextDate", nextDate);
		System.out.println("\nSaving estimate to " + file);
		try {
			BufferedWriter fout = new BufferedWriter(new FileWriter(file));
			fout.write(ReportTemplate.htmlStart);
			fout.newLine();
			fout.write(ReportTemplate.tableStart);
			fout.newLine();

			for (String stockId : bothHitIds) {
				List<CheckPointDailySelectionVO> currList = dailyCheckPointTable.getCheckPointSelection(stockId,
						currDate);
				List<CheckPointDailySelectionVO> nextList = dailyCheckPointTable.getCheckPointSelection(stockId,
						nextDate);
				StringBuffer sb = new StringBuffer();
				sb.append(stockId + " " + stockConfig.getStockName(stockId) + "<br>");
				System.out.println(stockId + " " + stockConfig.getStockName(stockId));
				for (CheckPointDailySelectionVO currCP : currList) {
					sb.append(currDate + " CheckPoint=" + currCP.checkPoint + "<br>");
					System.out.println(currDate + "  CheckPoint=" + currCP.checkPoint);
				}
				for (CheckPointDailySelectionVO nextCP : nextList) {
					sb.append(nextDate + " CheckPoint=" + nextCP.checkPoint + "<br>");
					System.out.println(nextDate + "  CheckPoint=" + nextCP.checkPoint);
				}

				fout.write(ReportTemplate.tableTrStart);
				fout.newLine();

				fout.write(ReportTemplate.tableTdStart);
				fout.write(sb.toString());
				fout.write(ReportTemplate.tableTdEnd);

				fout.write(ReportTemplate.tableTrEnd);
				fout.newLine();
			}

			fout.write(ReportTemplate.tableEnd);
			fout.newLine();
			fout.write(ReportTemplate.htmlEnd);
			fout.newLine();

			fout.flush();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		String[] args = null;
		try {
			// day
			injectMockStockPriceDate();

			// day ind
			new AllDailyIndCountAndSaveDBRunner().runDailyIndForStockIds(allStockIds);
			// week
			DailyWeeklyStockPriceCountAndSaveDBRunner.main(args);
			// week ind
			new AllDailyIndCountAndSaveDBRunner().runDailyWeekIndForStockIds(allStockIds);

			// analyse
			new DailySelectionRunner().runForStockIds(allStockIds);

			// save next date estimate stock to table
			saveEstimateStockToDB();

			// analyse both date
			checkDailyCheckPointForBothDate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			// clean up all the estimate price data
			cleanupMockData();

			// bug: to fix spList size is not equal to macdList size
			// week nextdate is delete, so re-count the week data
			// recount week
			DailyWeeklyStockPriceCountAndSaveDBRunner.main(args);
			// recount week ind
			new AllDailyIndCountAndSaveDBRunner().runDailyWeekIndForStockIds(allStockIds);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new PreEstimateStockPriceRunner().run();
	}
}
