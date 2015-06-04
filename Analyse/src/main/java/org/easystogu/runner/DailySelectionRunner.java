package org.easystogu.runner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easystogu.analyse.CombineAnalyseHelper;
import org.easystogu.analyse.util.IndCrossCheckingHelper;
import org.easystogu.analyse.util.PriceCheckingHelper;
import org.easystogu.analyse.util.VolumeCheckingHelper;
import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.StockSuperVOHelper;
import org.easystogu.db.access.WeekStockSuperVOHelper;
import org.easystogu.db.table.CheckPointDailySelectionVO;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.easymoney.helper.ReamTimeZiJinLiuXiangHelper;
import org.easystogu.report.HistoryAnalyseReport;
import org.easystogu.report.HistoryReportDetailsVO;
import org.easystogu.report.RangeHistoryReportVO;
import org.easystogu.report.ReportTemplate;
import org.easystogu.report.comparator.ZiJinLiuComparator;

public class DailySelectionRunner {
	private FileConfigurationService config = FileConfigurationService.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private StockSuperVOHelper stockOverAllHelper = new StockSuperVOHelper();
	private WeekStockSuperVOHelper weekStockOverAllHelper = new WeekStockSuperVOHelper();
	private String latestDate = stockPriceTable.getLatestStockDate();
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
			.getInstance();
	private ReamTimeZiJinLiuXiangHelper ziJinLiuHelper = new ReamTimeZiJinLiuXiangHelper();
	private HistoryAnalyseReport historyReportHelper = new HistoryAnalyseReport();
	private CombineAnalyseHelper combineAnalyserHelper = new CombineAnalyseHelper();
	private double minEarnPercent = config.getDouble("minEarnPercent_Select_CheckPoint");
	private String[] specifySelectCheckPoints = config.getString("specify_Select_CheckPoint", "").split(";");
	private String[] specifyDependCheckPoints = config.getString("specify_Depend_CheckPoint", "").split(";");
	private StringBuffer recommandStr = new StringBuffer();
	// StockPriceVO, CheckPoint list
	private Map<StockSuperVO, List<DailyCombineCheckPoint>> selectedMaps = new HashMap<StockSuperVO, List<DailyCombineCheckPoint>>();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();

	public void doAnalyse(String stockId) {
		// LatestN is reverse in date order desc
		List<StockSuperVO> overDayList = stockOverAllHelper.getLatestNStockSuperVO(stockId, 120);
		List<StockSuperVO> overWeekList = weekStockOverAllHelper.getLatestNStockSuperVO(stockId, 30);

		if (overDayList.size() == 0) {
			// System.out.println("No stockprice data for " + stockId);
			return;
		}

		// so must reverse in date order
		Collections.reverse(overDayList);
		Collections.reverse(overWeekList);

		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updateSuperPrice(stockId, overDayList);
		chuQuanChuXiPriceHelper.updateSuperPrice(stockId, overWeekList);

		// day
		// count and update the macd/kdj corss
		IndCrossCheckingHelper.macdCross(overDayList);
		IndCrossCheckingHelper.kdjCross(overDayList);
		IndCrossCheckingHelper.rsvCross(overDayList);
		IndCrossCheckingHelper.bollXueShi2DnCross(overDayList);
		// IndCrossCheckingHelper.shenXianCross12(overDayList);
		// IndCrossCheckingHelper.shenXianCross13(overDayList);
		VolumeCheckingHelper.volumeIncreasePuls(overDayList);
		VolumeCheckingHelper.avgVolume5(overDayList);
		PriceCheckingHelper.priceHigherThanNday(overDayList, 15);
		PriceCheckingHelper.setLastClosePrice(overDayList);
		PriceCheckingHelper.countAvgMA(overDayList);

		// week
		IndCrossCheckingHelper.macdCross(overWeekList);
		IndCrossCheckingHelper.kdjCross(overWeekList);
		IndCrossCheckingHelper.rsvCross(overWeekList);
		PriceCheckingHelper.setLastClosePrice(overWeekList);

		int index = overDayList.size() - 1;
		StockSuperVO superVO = overDayList.get(index);

		if (!superVO.priceVO.date.equals(latestDate)) {
			return;
		}

		// check all combine check point
		for (DailyCombineCheckPoint checkPoint : DailyCombineCheckPoint.values()) {
			if (this.isSelectedCheckPoint(checkPoint)) {
				if (combineAnalyserHelper.isConditionSatisfy(checkPoint, overDayList, overWeekList)) {
					superVO.setZiJinLiuVO(this.ziJinLiuHelper.fetchDataFromWeb(stockId));
					this.saveToCheckPointSelectionDB(superVO, checkPoint);
					this.addToConditionMapForReportDisplay(superVO, checkPoint);
				}
			} else if (this.isDependCheckPoint(checkPoint)) {
				if (combineAnalyserHelper.isConditionSatisfy(checkPoint, overDayList, overWeekList)) {
					superVO.setZiJinLiuVO(this.ziJinLiuHelper.fetchDataFromWeb(stockId));
					// search if other checkpoint already happen in recent days
					CheckPointDailySelectionVO latestCheckPointSelection = checkPointDailySelectionTable
							.getDifferentLatestCheckPointSelection(stockId, checkPoint.toString());
					if (latestCheckPointSelection != null
							&& !latestCheckPointSelection.checkPoint.equals(checkPoint.toString())
							&& !latestCheckPointSelection.date.equals(superVO.priceVO.date)) {
						// check if day is between 10 days
						String lastNDate = stockPriceTable.getLastNDate(stockId, 10);
						if (latestCheckPointSelection.date.compareTo(lastNDate) >= 0) {
							this.saveToCheckPointSelectionDB(superVO, checkPoint);
							this.addToConditionMapForReportDisplay(superVO, checkPoint);
						}
					}
				}
			}
		}
	}

	private void saveToCheckPointSelectionDB(StockSuperVO superVO, DailyCombineCheckPoint checkPoint) {
		CheckPointDailySelectionVO vo = new CheckPointDailySelectionVO();
		vo.setStockId(superVO.priceVO.stockId);
		vo.setDate(superVO.priceVO.date);
		vo.setCheckPoint(checkPoint.toString());
		this.checkPointDailySelectionTable.insertIfNotExist(vo);
	}

	private void addToConditionMapForReportDisplay(StockSuperVO superVO, DailyCombineCheckPoint checkPoint) {
		List<DailyCombineCheckPoint> checkPointList = selectedMaps.get(superVO);
		if (checkPointList == null) {
			checkPointList = new ArrayList<DailyCombineCheckPoint>();
			checkPointList.add(checkPoint);
			selectedMaps.put(superVO, checkPointList);
		} else {
			checkPointList.add(checkPoint);
		}
	}

	private boolean isSelectedCheckPoint(DailyCombineCheckPoint checkPoint) {
		for (String cp : specifySelectCheckPoints) {
			if (cp.equals(checkPoint.toString())) {
				return true;
			}
		}
		return false;
	}

	private boolean isDependCheckPoint(DailyCombineCheckPoint checkPoint) {
		for (String cp : specifyDependCheckPoints) {
			if (cp.equals(checkPoint.toString())) {
				return true;
			}
		}
		return false;
	}

	public void reportSelectedStockIds() {
		Set<StockSuperVO> keys = this.selectedMaps.keySet();
		Iterator<StockSuperVO> keysIt = keys.iterator();
		while (keysIt.hasNext()) {
			StockSuperVO superVO = keysIt.next();
			List<DailyCombineCheckPoint> checkPointList = this.selectedMaps.get(superVO);
			for (DailyCombineCheckPoint checkPoint : checkPointList) {
				if (checkPoint.getEarnPercent() >= this.minEarnPercent) {
					recommandStr.append(superVO.priceVO.stockId + " select :" + checkPointList.toString() + " "
							+ superVO.getZiJinLiuVO().toNetPerString() + "\n");
				}
			}
		}

		System.out.println("\nRecommand to select below checkPoint that earnPercent >=" + minEarnPercent);
		System.out.println(recommandStr.toString());
	}

	public void reportSelectedHistoryReport() {
		List<RangeHistoryReportVO> rangeList = new ArrayList<RangeHistoryReportVO>();
		Set<StockSuperVO> keys = this.selectedMaps.keySet();
		Iterator<StockSuperVO> keysIt = keys.iterator();
		while (keysIt.hasNext()) {
			StockSuperVO superVO = keysIt.next();
			List<DailyCombineCheckPoint> checkPointList = this.selectedMaps.get(superVO);

			for (DailyCombineCheckPoint checkPoint : checkPointList) {
				if (checkPoint.getEarnPercent() >= this.minEarnPercent) {
					List<HistoryReportDetailsVO> hisReport = historyReportHelper.doAnalyseReport(
							superVO.priceVO.stockId, checkPoint);
					RangeHistoryReportVO rangeVO = new RangeHistoryReportVO(superVO, hisReport, checkPoint);
					rangeList.add(rangeVO);
				}
			}
		}

		this.sortRangeHistoryReport(rangeList);
		this.reportToConsole(rangeList);
		this.reportToHtml(rangeList);
	}

	public void reportToConsole(List<RangeHistoryReportVO> rangeList) {
		System.out.println("\nHistory range report: ");
		for (RangeHistoryReportVO rangeVO : rangeList) {
			System.out.println(rangeVO.toSimpleString() + " WeekLen(" + rangeVO.currentSuperVO.hengPanWeekLen
					+ ") KDJ(" + (int) rangeVO.currentSuperVO.kdjVO.k + ") "
					+ rangeVO.currentSuperVO.ziJinLiuVO.toNetPerString());
		}
	}

	public void reportToHtml(List<RangeHistoryReportVO> rangeList) {
		String file = config.getString("report.analyse.html.file").replaceAll("currentDate", latestDate);
		System.out.println("\nSaving report to " + file);
		try {
			BufferedWriter fout = new BufferedWriter(new FileWriter(file));
			fout.write(ReportTemplate.htmlStart);
			fout.newLine();
			fout.write(ReportTemplate.tableStart);
			fout.newLine();

			// fout.write(recommandStr.toString().replaceAll("\n", "<br>"));
			// fout.newLine();

			for (RangeHistoryReportVO rangeVO : rangeList) {

				if (rangeVO.checkPoint.getEarnPercent() < this.minEarnPercent) {
					continue;
				}

				String stockId = rangeVO.stockId;
				String pre = stockId.startsWith("6") ? "sh" : "sz";

				fout.write(ReportTemplate.tableTrStart);
				fout.newLine();

				fout.write(ReportTemplate.tableTdStart);
				fout.write(rangeVO.toSimpleString() + "&nbsp; WeekLen(" + rangeVO.currentSuperVO.hengPanWeekLen
						+ ") &nbsp; KDJ(" + (int) rangeVO.currentSuperVO.kdjVO.k + ") &nbsp;"
						+ rangeVO.currentSuperVO.ziJinLiuVO.toNetPerString());
				fout.write(ReportTemplate.tableTdEnd);
				fout.newLine();

				fout.write(ReportTemplate.tableTdLink1Start.replace("[stockId]", stockId));
				fout.write("Details 1");
				fout.write(ReportTemplate.tableTdLinkEnd);
				fout.newLine();

				fout.write(ReportTemplate.tableTdLink2Start.replace("[prestockId]", pre + stockId));
				fout.write("Details 2");
				fout.write(ReportTemplate.tableTdLinkEnd);
				fout.newLine();

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

	// sort by avgHighEarnPercent
	@SuppressWarnings("unchecked")
	public void sortRangeHistoryReport(List<RangeHistoryReportVO> rangeList) {
		// Collections.sort(rangeList, new CheckPointEarnPercentComparator());
		Collections.sort(rangeList, new ZiJinLiuComparator());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DailySelectionRunner runner = new DailySelectionRunner();
		CheckPointDailySelectionTableHelper eventTable = CheckPointDailySelectionTableHelper.getInstance();
		// System.out.println("Delete latest's Select Event");
		// eventTable.deleteByDate(runner.latestDate);

		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		List<String> stockIds = stockConfig.getAllStockId();

		for (String stockId : stockIds) {
			// if (stockId.equals("000979"))
			runner.doAnalyse(stockId);
		}

		runner.reportSelectedStockIds();
		runner.reportSelectedHistoryReport();
	}

}
