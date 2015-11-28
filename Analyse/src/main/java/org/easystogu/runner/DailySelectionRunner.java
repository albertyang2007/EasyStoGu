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
import org.easystogu.analyse.util.IndProcessHelper;
import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.StockSuperVOHelper;
import org.easystogu.db.access.WeekStockSuperVOHelper;
import org.easystogu.db.access.ZhuLiJingLiuRuTableHelper;
import org.easystogu.db.access.ZiJinLiu3DayTableHelper;
import org.easystogu.db.access.ZiJinLiu5DayTableHelper;
import org.easystogu.db.access.ZiJinLiuTableHelper;
import org.easystogu.db.table.CheckPointDailySelectionVO;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.db.table.ZiJinLiuVO;
import org.easystogu.easymoney.helper.RealTimeZiJinLiuFatchDataHelper;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.report.HistoryAnalyseReport;
import org.easystogu.report.HistoryReportDetailsVO;
import org.easystogu.report.RangeHistoryReportVO;
import org.easystogu.report.ReportTemplate;
import org.easystogu.report.comparator.ZiJinLiuComparator;

//daily select stock that checkpoint is satisfied
public class DailySelectionRunner implements Runnable {
	private FileConfigurationService config = FileConfigurationService.getInstance();
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private StockSuperVOHelper stockOverAllHelper = new StockSuperVOHelper();
	private WeekStockSuperVOHelper weekStockOverAllHelper = new WeekStockSuperVOHelper();
	private String latestDate = stockPriceTable.getLatestStockDate();
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
			.getInstance();
	private RealTimeZiJinLiuFatchDataHelper realTimeZiJinLiuHelper = RealTimeZiJinLiuFatchDataHelper.getInstance();
	private ZiJinLiuTableHelper ziJinLiuTableHelper = ZiJinLiuTableHelper.getInstance();
	private ZhuLiJingLiuRuTableHelper zhuLiJingLiuRuTableHelper = ZhuLiJingLiuRuTableHelper.getInstance();
	private ZiJinLiu3DayTableHelper ziJinLiu3DayTableHelper = ZiJinLiu3DayTableHelper.getInstance();
	private ZiJinLiu5DayTableHelper ziJinLiu5DayTableHelper = ZiJinLiu5DayTableHelper.getInstance();
	private HistoryAnalyseReport historyReportHelper = new HistoryAnalyseReport();
	private CombineAnalyseHelper combineAnalyserHelper = new CombineAnalyseHelper();
	private boolean doHistoryAnalyzeInDailySelection = config.getBoolean("do_History_Analyze_In_Daily_Selection", true);
	private String[] specifySelectCheckPoints = config.getString("specify_Select_CheckPoint", "").split(";");
	private String[] specifyDependCheckPoints = config.getString("specify_Depend_CheckPoint", "").split(";");
	private StringBuffer recommandStr = new StringBuffer();
	// StockPriceVO, CheckPoint list
	private Map<StockSuperVO, List<DailyCombineCheckPoint>> selectedMaps = new HashMap<StockSuperVO, List<DailyCombineCheckPoint>>();
	private ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();
	private Map<String, ZiJinLiuVO> realTimeZiJinLiuMap = new HashMap<String, ZiJinLiuVO>();
	private boolean fetchRealTimeZiJinLiu = false;

	public void doAnalyse(String stockId) {
		try {
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

			IndProcessHelper.process(overDayList, overWeekList);

			int index = overDayList.size() - 1;
			StockSuperVO superVO = overDayList.get(index);

			if (!superVO.priceVO.date.equals(latestDate)) {
				// System.out.println("priveVO date is not equal latestDate");
				return;
			}

			// check all combine check point
			for (DailyCombineCheckPoint checkPoint : DailyCombineCheckPoint.values()) {
				if (this.isSelectedCheckPoint(checkPoint)) {
					if (combineAnalyserHelper.isConditionSatisfy(checkPoint, overDayList, overWeekList)) {
						this.setZiJinLiuVO(superVO);
						this.saveToCheckPointSelectionDB(superVO, checkPoint);
						this.addToConditionMapForReportDisplay(superVO, checkPoint);
					}
				} else if (this.isDependCheckPoint(checkPoint)) {
					if (combineAnalyserHelper.isConditionSatisfy(checkPoint, overDayList, overWeekList)) {
						this.setZiJinLiuVO(superVO);
						// search if other checkpoint already happen in recent
						// days
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
		} catch (Exception e) {
			System.out.println("Exception for " + stockId);
			e.printStackTrace();
		}
	}

	private void setZiJinLiuVO(StockSuperVO superVO) {

		// if real time zijinliu is not collect, then find it from total range
		// zijinliu (59 pages)
		if (this.fetchRealTimeZiJinLiu) {
			String stockId = superVO.priceVO.stockId;
			ZiJinLiuVO realTimeVO = null;
			if (!this.realTimeZiJinLiuMap.containsKey(stockId)) {
				realTimeVO = realTimeZiJinLiuHelper.fetchDataFromWeb(stockId);
				this.realTimeZiJinLiuMap.put(stockId, realTimeVO);
			} else {
				realTimeVO = this.realTimeZiJinLiuMap.get(stockId);
			}

			// put ziJinLiu VO to list
			superVO.putZiJinLiuVO(ZiJinLiuVO.RealTime, realTimeVO);
		}

		// also get zijinliu from DB if exist
		superVO.putZiJinLiuVO(ZiJinLiuVO._1Day, ziJinLiuTableHelper.getZiJinLiu(superVO.priceVO.stockId, latestDate));
		superVO.putZiJinLiuVO(ZiJinLiuVO._3Day,
				ziJinLiu3DayTableHelper.getZiJinLiu(superVO.priceVO.stockId, latestDate));
		superVO.putZiJinLiuVO(ZiJinLiuVO._5Day,
				ziJinLiu5DayTableHelper.getZiJinLiu(superVO.priceVO.stockId, latestDate));

		// get zhuLiJingLiuRu from DB
		superVO.setZhuLiJingLiuRuVO(zhuLiJingLiuRuTableHelper.getZhuLiJingLiuRu(superVO.priceVO.stockId, latestDate));
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
		if (specifySelectCheckPoints != null && specifySelectCheckPoints.length > 0) {
			for (String cp : specifySelectCheckPoints) {
				if (cp.equals(checkPoint.toString())) {
					return true;
				}
			}
		} else if (checkPoint.isSatisfyMinEarnPercent()) {
			return true;
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
				if (checkPoint.isSatisfyMinEarnPercent()) {
					recommandStr.append(superVO.priceVO.stockId + " select :" + checkPointList.toString() + " "
							+ superVO.genZiJinLiuInfo() + " " + superVO.getZhuLiJingLiuRu() + "\n");
				}
			}
		}

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
				if (checkPoint.isSatisfyMinEarnPercent()) {
					List<HistoryReportDetailsVO> hisReport = new ArrayList<HistoryReportDetailsVO>();
					if (doHistoryAnalyzeInDailySelection) {
						hisReport = historyReportHelper.doAnalyseReport(superVO.priceVO.stockId, checkPoint);
					}
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
			if (rangeVO.currentSuperVO.isAllMajorNetPerIn()) {
				System.out.println(rangeVO.toSimpleString() + " WeekLen(" + rangeVO.currentSuperVO.hengPanWeekLen
						+ ") KDJ(" + (int) rangeVO.currentSuperVO.kdjVO.k + ") "
						+ rangeVO.currentSuperVO.genZiJinLiuInfo() + " " + rangeVO.currentSuperVO.getZhuLiJingLiuRu());
			}
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

				if (!rangeVO.checkPoint.isSatisfyMinEarnPercent()) {
					continue;
				}

				if (!rangeVO.currentSuperVO.isAllMajorNetPerIn()) {
					continue;
				}

				String stockId = rangeVO.stockId;
				String pre = stockId.startsWith("6") ? "sh" : "sz";

				fout.write(ReportTemplate.tableTrStart);
				fout.newLine();

				fout.write(ReportTemplate.tableTdStart);
				fout.write(rangeVO.toSimpleString() + "&nbsp; WeekLen(" + rangeVO.currentSuperVO.hengPanWeekLen
						+ ") &nbsp; KDJ(" + (int) rangeVO.currentSuperVO.kdjVO.k + ") <br> "
						+ rangeVO.currentSuperVO.genZiJinLiuInfo() + rangeVO.currentSuperVO.getZhuLiJingLiuRu());
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

	public boolean isFetchRealTimeZiJinLiu() {
		return fetchRealTimeZiJinLiu;
	}

	public void setFetchRealTimeZiJinLiu(boolean fetchRealTimeZiJinLiu) {
		this.fetchRealTimeZiJinLiu = fetchRealTimeZiJinLiu;
	}

	public void runForStockIds(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			// if (!stockId.equals("600680"))
			// continue;
			if (index++ % 500 == 0) {
				System.out.println("Analyse of " + index + "/" + stockIds.size());
			}
			doAnalyse(stockId);
		}

		reportSelectedStockIds();
		reportSelectedHistoryReport();
	}

	public void run() {
		List<String> stockIds = stockConfig.getAllStockId();
		this.runForStockIds(stockIds);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new DailySelectionRunner().run();
	}
}
