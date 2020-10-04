package org.easystogu.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.easystogu.analyse.CombineAnalyseHelper;
import org.easystogu.analyse.util.IndProcessHelper;
import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.DBConfigurationService;
import org.easystogu.db.access.table.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.table.CheckPointDailyStatisticsTableHelper;
import org.easystogu.db.access.table.CheckPointHistoryAnalyseTableHelper;
import org.easystogu.db.access.table.CheckPointHistorySelectionTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.table.StockSuperVOHelper;
import org.easystogu.db.access.table.WeekStockSuperVOHelper;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.db.vo.table.CheckPointDailyStatisticsVO;
import org.easystogu.db.vo.table.CheckPointHistoryAnalyseVO;
import org.easystogu.db.vo.table.StockSuperVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.CrossType;
import org.easystogu.utils.SellPointType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class HistoryAnalyseReport {
	@Autowired
	private DBConfigurationService config;
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	private CompanyInfoFileHelper stockConfig;
	@Autowired
	private CheckPointHistorySelectionTableHelper checkPointHistorySelectionTable;
	@Autowired
	private CheckPointDailyStatisticsTableHelper checkPointDailyStatisticsTable;
	@Autowired
	@Qualifier("weekStockSuperVOHelper")
	private WeekStockSuperVOHelper weekStockOverAllHelper;
	@Autowired
	private CombineAnalyseHelper combineAanalyserHelper;
	@Autowired
	@Qualifier("stockSuperVOHelper")
	private StockSuperVOHelper stockOverAllHelper;
	@Autowired
	private CheckPointHistoryAnalyseTableHelper cpHistoryAnalyse;
	@Autowired
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable;

	public List<HistoryReportDetailsVO> doAnalyseBuySellDate(String stockId,
			List<DailyCombineCheckPoint> checkPointList) {
		List<HistoryReportDetailsVO> reportList = new ArrayList<HistoryReportDetailsVO>();
		for (DailyCombineCheckPoint checkPoint : checkPointList) {
			reportList.addAll(this.doAnalyseBuySellDate(stockId, checkPoint));
		}
		return reportList;
	}

	public List<HistoryReportDetailsVO> doAnalyseBuySellDate(String stockId, DailyCombineCheckPoint checkPoint) {

		List<HistoryReportDetailsVO> historyReportList = new ArrayList<HistoryReportDetailsVO>();

		List<StockSuperVO> overDayList = stockOverAllHelper.getAllStockSuperVO(stockId);
		List<StockSuperVO> overWeekList = weekStockOverAllHelper.getAllStockSuperVO(stockId);

		if (overDayList.size() == 0) {
			// System.out.println("doAnalyseReport overDayList size=0 for " + stockId);
			return historyReportList;
		}

		if (overWeekList.size() == 0) {
			// System.out.println("doAnalyseReport overWeekList size=0 for " + stockId);
			return historyReportList;
		}

		IndProcessHelper.processDayList(overDayList);
		IndProcessHelper.processWeekList(overWeekList);

		HistoryReportDetailsVO reportVO = null;
		for (int index = 120; index < overDayList.size(); index++) {
			StockSuperVO superVO = overDayList.get(index);
			// buy point
			if (reportVO == null) {
				String startDate = overDayList.get(index - 120).priceVO.date;
				String endDate = overDayList.get(index).priceVO.date;
				// System.out.println(startDate + " ~~ " + endDate);
				// include the startDate, not include the endDate
				List<StockSuperVO> subOverWeekList = this.getSubWeekVOList(overWeekList, startDate, endDate);
				// System.out.println(subOverWeekList.get(0).priceVO.date +
				// " week "
				// + subOverWeekList.get(subOverWeekList.size() -
				// 1).priceVO.date);

				List<StockSuperVO> subOverDayList = overDayList.subList(index - 120, index + 1);

				// System.out.println(subOverDayList.get(0).priceVO.date +
				// " day "
				// + subOverDayList.get(subOverDayList.size() -
				// 1).priceVO.date);

				if (combineAanalyserHelper.isConditionSatisfy(checkPoint, subOverDayList, subOverWeekList)) {
					reportVO = new HistoryReportDetailsVO(overDayList);
					reportVO.setBuyPriceVO(superVO.priceVO);
					// System.out.println(superVO.priceVO.date + " buy");
					continue;
				}
			}

			// sell point (MACD dead or KDJ dead point or next day)
			if ((reportVO != null) && (reportVO.buyPriceVO != null) && (reportVO.sellPriceVO == null)) {
				if (checkPoint.getSellPointType().equals(SellPointType.KDJ_Dead)) {
					if (superVO.kdjCorssType == CrossType.DEAD) {
						reportVO.setSellPriceVO(superVO.priceVO);
						historyReportList.add(reportVO);
						reportVO = null;
					}
				} else if (checkPoint.getSellPointType().equals(SellPointType.MACD_Dead)) {
					if (superVO.macdCorssType == CrossType.DEAD) {
						reportVO.setSellPriceVO(superVO.priceVO);
						historyReportList.add(reportVO);
						reportVO = null;
					}
				} else if (checkPoint.getSellPointType().equals(SellPointType.ShenXian_Dead)) {
					if (superVO.shenXianCorssType12 == CrossType.DEAD) {
						reportVO.setSellPriceVO(superVO.priceVO);
						historyReportList.add(reportVO);
						reportVO = null;
					}
				} else if (checkPoint.getSellPointType().equals(SellPointType.Next_Day)) {
					reportVO.setSellPriceVO(superVO.priceVO);
					historyReportList.add(reportVO);
					reportVO = null;
				}
			}
		}

		// if loop to end and no sell point, then set the latest day as sell
		// point
		if ((reportVO != null) && (reportVO.buyPriceVO != null) && (reportVO.sellPriceVO == null)) {
			StockSuperVO superVO = overDayList.get(overDayList.size() - 1);
			reportVO.setSellPriceVO(superVO.priceVO, false);
			historyReportList.add(reportVO);
			reportVO = null;
		}

		return historyReportList;
	}

	// if the checkpoint is meet, then add vo to list
	public List<HistoryReportDetailsVO> doAnalyseStatistics(String stockId, DailyCombineCheckPoint checkPoint) {

		List<HistoryReportDetailsVO> historyReportList = new ArrayList<HistoryReportDetailsVO>();

		List<StockSuperVO> overDayList = stockOverAllHelper.getAllStockSuperVO(stockId);
		List<StockSuperVO> overWeekList = weekStockOverAllHelper.getAllStockSuperVO(stockId);

		if (overDayList.size() == 0) {
			// System.out.println("doAnalyseReport overDayList size=0 for " +
			// stockId);
			return historyReportList;
		}

		if (overWeekList.size() == 0) {
			// System.out.println("doAnalyseReport overWeekList size=0 for " +
			// stockId);
			return historyReportList;
		}

		IndProcessHelper.processDayList(overDayList);
		IndProcessHelper.processWeekList(overWeekList);

		for (int index = 120; index < overDayList.size(); index++) {
			StockSuperVO superVO = overDayList.get(index);

			// buy point
			String startDate = overDayList.get(index - 120).priceVO.date;
			String endDate = overDayList.get(index).priceVO.date;
			// System.out.println(startDate + " ~~ " + endDate);
			// include the startDate, not include the endDate
			List<StockSuperVO> subOverWeekList = this.getSubWeekVOList(overWeekList, startDate, endDate);
			// System.out.println(subOverWeekList.get(0).priceVO.date +
			// " week "
			// + subOverWeekList.get(subOverWeekList.size() -
			// 1).priceVO.date);

			List<StockSuperVO> subOverDayList = overDayList.subList(index - 120, index + 1);

			// System.out.println(subOverDayList.get(0).priceVO.date +
			// " day "
			// + subOverDayList.get(subOverDayList.size() -
			// 1).priceVO.date);

			if (combineAanalyserHelper.isConditionSatisfy(checkPoint, subOverDayList, subOverWeekList)) {
				HistoryReportDetailsVO reportVO = new HistoryReportDetailsVO(overDayList);
				reportVO.setBuyPriceVO(superVO.priceVO);// must keep it
				historyReportList.add(reportVO);
			}
		}

		return historyReportList;
	}

	// original analyse all stockId for checkPoint
	// count buyDate, sellDate, maxEarn, minEarn etc, save data into
	// checkpoint_history_selection
	public void searchAllStockIdAnalyseHistoryBuySellCheckPoint(DailyCombineCheckPoint checkPoint) {

		this.checkPointHistorySelectionTable.deleteByCheckPoint(checkPoint.name());

		double[] earnPercent = new double[3];
		long holdDays = 0;
		long holdDaysWhenHighPrice = 0;
		long totalCount = 0;
		int totalHighCount = 0;
		int totalLowCount = 0;
		int index = 0;
		List<String> stockIds = stockConfig.getAllStockId();

		System.out.println("\n===============================" + checkPoint + " (sellPoint:"
				+ checkPoint.getSellPointType() + ")==========================");

		for (String stockId : stockIds) {
			index++;

			// if (!stockId.equals("600837")) {// 600837 002797
			// continue;
			// }

			if (index++ % 100 == 0) {
				System.out.println(checkPoint.name() + " Analyse of " + index + "/" + stockIds.size());
			}

			List<HistoryReportDetailsVO> historyReportList = this.doAnalyseBuySellDate(stockId, checkPoint);
			for (HistoryReportDetailsVO reportVO : historyReportList) {
				// analyse the original buy and sell data
				if (reportVO.sellPriceVO != null) {
					reportVO.countData(stockPriceTable);

					// skip the abmornal data
					if (reportVO.earnPercent[1] >= 1000.0) {
						continue;
					}

					totalCount++;
					earnPercent[0] += reportVO.earnPercent[0];
					earnPercent[1] += reportVO.earnPercent[1];
					earnPercent[2] += reportVO.earnPercent[2];
					holdDays += reportVO.holdDays;
					holdDaysWhenHighPrice += reportVO.holdDaysWhenHighPrice;

					// print the high earn percent if larger than 25%
					if ((reportVO.earnPercent[1] >= 50.0) && (reportVO.earnPercent[0] >= 25.0)) {
						totalHighCount++;
						// System.out.println("High earn: " + reportVO);
					} else if ((reportVO.earnPercent[1] <= -10.0) || (reportVO.earnPercent[0] <= -10.0)) {
						totalLowCount++;
						// System.out.println("Low earn: " + reportVO);
					}

					if (!reportVO.completed) {
						// System.out.println("Not Completed: " + reportVO + "\tIndex=" + index +
						// "\tCurrent
						// highPercent="
						// + (earnPercent[1] / totalCount));
						// save to checkpint daily selection table
						if (isCheckPointSelected(checkPoint)) {
							this.saveToCheckPointDailySelectionDB(reportVO.stockId, reportVO.buyPriceVO.date,
									checkPoint);
						}
					} else {
						// for completed VO
						// remove it from daily selection
						// System.out.println("Completed: " + reportVO);
						// this.checkPointDailySelectionTable.delete(stockId, reportVO.buyPriceVO.date,
						// checkPoint.toString());
						// save case into history DB
						if (isCheckPointSelected(checkPoint)) {
							this.saveToCheckPointDailySelectionDB(reportVO.stockId, reportVO.buyPriceVO.date,
									checkPoint);
						}
						checkPointHistorySelectionTable
								.insert(reportVO.convertToHistoryReportVO(checkPoint.toString()));
					}
				}
			}
		}

		if (totalCount == 0) {
			totalCount = 1;
		}

		System.out.println("Total satisfy: " + totalCount + "\t earnPercent[close]=" + (earnPercent[0] / totalCount)
				+ "\t earnPercent[high]=" + (earnPercent[1] / totalCount) + "\t earnPercent[low]="
				+ (earnPercent[2] / totalCount) + "\noldEarn=" + checkPoint.getEarnPercent());

		System.out.println("Avg hold stock days when sell point: " + (holdDays / totalCount));
		System.out.println("Avg hold stock days when high price: " + (holdDaysWhenHighPrice / totalCount));
		System.out.println("Total high earn between (25, 50): " + totalHighCount);
		System.out.println("Total low  earn between (10, 10): " + totalLowCount);

		CheckPointHistoryAnalyseVO vo = new CheckPointHistoryAnalyseVO();
		vo.setCheckPoint(checkPoint.toString());
		vo.setTotalSatisfy(totalCount);
		vo.setCloseEarnPercent(earnPercent[0] / totalCount);
		vo.setHighEarnPercent(earnPercent[1] / totalCount);
		vo.setLowEarnPercent(earnPercent[2] / totalCount);
		vo.setAvgHoldDays(holdDays / totalCount);
		vo.setTotalHighEarn(totalHighCount);
		cpHistoryAnalyse.insert(vo);
	}

	// statistics all stockIds and count checkpoint, save into
	// checkpoint_daily_statistics
	// not test it, pls do not use it before testing
	public void searchAllStockIdStatisticsCheckPoint(List<DailyCombineCheckPoint> checkPoints) {
		// checkPoints.parallelStream().forEach(s ->
		// searchAllStockIdStatisticsCheckPoint(s));
	}

	// statistics all stockIds and count checkpoint, save into
	// checkpoint_daily_statistics
	public void searchAllStockIdStatisticsCheckPoint(DailyCombineCheckPoint checkPoint) {
		System.out.println("\n===============================" + checkPoint + " (sellPoint:"
				+ checkPoint.getSellPointType() + ")==========================");

		// date, count
		Map<String, Integer> generalCheckPointStatisticsMap = new ConcurrentHashMap<String, Integer>();

		this.checkPointDailyStatisticsTable.deleteByCheckPoint(checkPoint.name());
		generalCheckPointStatisticsMap.clear();

		int index = 0;
		List<String> stockIds = stockConfig.getAllStockId();

		// run it as parallelStream
		// try {
		// myForkJoinPool.submit(() -> stockIds.parallelStream().forEach(s ->
		// analyseOneStock(checkPoint, s))).get();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		// run it as normal
		for (String stockId : stockIds) {

			// if (!stockId.equals("002797")) {
			// continue;
			// }

			if (index++ % 100 == 0) {
				System.out.println(checkPoint.name() + " Analyse of " + index + "/" + stockIds.size());
			}

			analyseOneStock(checkPoint, stockId, generalCheckPointStatisticsMap);
		}

		// save statistics the checkPoint and count at date into DB
		Iterator it = generalCheckPointStatisticsMap.entrySet().iterator();
		while (it.hasNext()) {
			CheckPointDailyStatisticsVO cpdsvo = new CheckPointDailyStatisticsVO();
			Map.Entry entry = (Map.Entry) it.next();
			cpdsvo.checkPoint = checkPoint.name();
			cpdsvo.date = entry.getKey().toString();
			cpdsvo.count = (Integer) entry.getValue();

			// System.out.println(cpdsvo);
			checkPointDailyStatisticsTable.insert(cpdsvo);
		}

		System.out.println(
				"===============================Job done for " + checkPoint + "===============================");
	}

	private void analyseOneStock(DailyCombineCheckPoint checkPoint, String stockId,
			Map<String, Integer> generalCheckPointStatisticsMap) {
		List<HistoryReportDetailsVO> historyReportList = this.doAnalyseStatistics(stockId, checkPoint);
		for (HistoryReportDetailsVO reportVO : historyReportList) {
			// statistics the checkPoint and count at date
			Integer count = generalCheckPointStatisticsMap.get(reportVO.buyPriceVO.date);
			if (count == null) {
				count = new Integer(0);
			}
			// update the count
			generalCheckPointStatisticsMap.put(new String(reportVO.buyPriceVO.date), new Integer(count + 1));
		}
	}

	private void saveToCheckPointDailySelectionDB(String stockId, String date, DailyCombineCheckPoint checkPoint) {
		CheckPointDailySelectionVO vo = new CheckPointDailySelectionVO();
		vo.setStockId(stockId);
		vo.setDate(date);
		vo.setCheckPoint(checkPoint.toString());
		this.checkPointDailySelectionTable.insertIfNotExist(vo);
	}

	private boolean isCheckPointSelected(DailyCombineCheckPoint checkPoint) {
		String specifySelectCheckPoint = config.getString("specify_Select_CheckPoint", "");
		String[] specifySelectCheckPoints = specifySelectCheckPoint.split(";");
		for (String cp : specifySelectCheckPoints) {
			if (cp.equals(checkPoint.toString())) {
				return true;
			}
		}
		return false;
	}

	public List<StockSuperVO> getSubWeekVOList(List<StockSuperVO> overWeekList, String startDate, String endDate) {
		List<StockSuperVO> subList = new ArrayList<StockSuperVO>();

		for (StockSuperVO vo : overWeekList) {
			// include the startDate, not include the endDate
			if (vo.priceVO.date.compareTo(startDate) >= 0 && vo.priceVO.date.compareTo(endDate) <= 0) {
				subList.add(vo);
			}
		}

		return subList;
	}

	public List<StockSuperVO> getSubDayVOList(List<StockSuperVO> overDayList, String startDate, String endDate) {
		List<StockSuperVO> subList = new ArrayList<StockSuperVO>();

		for (StockSuperVO vo : overDayList) {
			// include the startDate, not include the endDate
			if (vo.priceVO.date.compareTo(startDate) >= 0 && vo.priceVO.date.compareTo(endDate) < 0) {
				subList.add(vo);
			}
		}

		return subList;
	}

	public void countAllStockIdAnalyseHistoryBuySellCheckPoint() {
		List<DailyCombineCheckPoint> dailyCPs = new ArrayList<DailyCombineCheckPoint>();
		dailyCPs.add(DailyCombineCheckPoint.Trend_PhaseI_GuanCha);
		dailyCPs.add(DailyCombineCheckPoint.Trend_PhaseII_JianCang);
		dailyCPs.add(DailyCombineCheckPoint.Trend_PhaseIII_ChiGu);
		dailyCPs.add(DailyCombineCheckPoint.Trend_PhaseVI_JianCang);

		dailyCPs.add(DailyCombineCheckPoint.QSDD_Top_Area);
		dailyCPs.add(DailyCombineCheckPoint.QSDD_Bottom_Area);
		dailyCPs.add(DailyCombineCheckPoint.QSDD_Bottom_Gordon);

		dailyCPs.add(DailyCombineCheckPoint.LuZao_GordonO_MA43_DownCross_MA86);
		dailyCPs.add(DailyCombineCheckPoint.LuZao_GordonI_MA19_UpCross_MA43);
		dailyCPs.add(DailyCombineCheckPoint.LuZao_GordonII_MA19_UpCross_MA86);
		dailyCPs.add(DailyCombineCheckPoint.LuZao_DeadI_MA43_UpCross_MA86);
		dailyCPs.add(DailyCombineCheckPoint.LuZao_DeadII_MA19_DownCross_MA43);

		dailyCPs.add(DailyCombineCheckPoint.ShenXian_Gordon);
		dailyCPs.add(DailyCombineCheckPoint.ShenXian_Dead);

		dailyCPs.add(DailyCombineCheckPoint.MACD_Gordon);
		dailyCPs.add(DailyCombineCheckPoint.MACD_Dead);

		dailyCPs.add(DailyCombineCheckPoint.WR_Bottom_Area);
		dailyCPs.add(DailyCombineCheckPoint.WR_Top_Area);
		dailyCPs.add(DailyCombineCheckPoint.WR_Bottom_Gordon);

		dailyCPs.add(DailyCombineCheckPoint.LuZao_PhaseII_MACD_WEEK_GORDON_MACD_DAY_DIF_CROSS_0);
		dailyCPs.add(DailyCombineCheckPoint.LuZao_PhaseIII_MACD_WEEK_GORDON_MACD_DAY_DIF_CROSS_0);
		dailyCPs.add(DailyCombineCheckPoint.LuZao_PhaseII_MACD_WEEK_GORDON_KDJ_WEEK_GORDON);
		dailyCPs.add(DailyCombineCheckPoint.LuZao_PhaseIII_MACD_WEEK_GORDON_KDJ_WEEK_GORDON);
		dailyCPs.add(DailyCombineCheckPoint.MACD_TWICE_GORDON_W_Botton_MACD_DI_BEILI);
		dailyCPs.add(DailyCombineCheckPoint.MACD_TWICE_GORDON_W_Botton_TiaoKong_ZhanShang_Bull);

		// using the parallel stream to process the check point
		dailyCPs.parallelStream().forEach(cp -> searchAllStockIdAnalyseHistoryBuySellCheckPoint(cp));
	}

	public void countAllStockIdStatisticsCheckPoint() {
		List<DailyCombineCheckPoint> dailyCPs = new ArrayList<DailyCombineCheckPoint>();
		dailyCPs.add(DailyCombineCheckPoint.Trend_PhaseI_GuanCha);
		dailyCPs.add(DailyCombineCheckPoint.Trend_PhaseII_JianCang);
		dailyCPs.add(DailyCombineCheckPoint.Trend_PhaseIII_ChiGu);
		dailyCPs.add(DailyCombineCheckPoint.Trend_PhaseVI_JianCang);

		dailyCPs.add(DailyCombineCheckPoint.QSDD_Top_Area);
		dailyCPs.add(DailyCombineCheckPoint.QSDD_Bottom_Area);
		dailyCPs.add(DailyCombineCheckPoint.QSDD_Bottom_Gordon);

		dailyCPs.add(DailyCombineCheckPoint.LuZao_GordonO_MA43_DownCross_MA86);
		dailyCPs.add(DailyCombineCheckPoint.LuZao_GordonI_MA19_UpCross_MA43);
		dailyCPs.add(DailyCombineCheckPoint.LuZao_GordonII_MA19_UpCross_MA86);
		dailyCPs.add(DailyCombineCheckPoint.LuZao_DeadI_MA43_UpCross_MA86);
		dailyCPs.add(DailyCombineCheckPoint.LuZao_DeadII_MA19_DownCross_MA43);

		dailyCPs.add(DailyCombineCheckPoint.ShenXian_Gordon);
		dailyCPs.add(DailyCombineCheckPoint.ShenXian_Dead);

		dailyCPs.add(DailyCombineCheckPoint.MACD_Gordon);
		dailyCPs.add(DailyCombineCheckPoint.MACD_Dead);

		dailyCPs.add(DailyCombineCheckPoint.WR_Bottom_Area);
		dailyCPs.add(DailyCombineCheckPoint.WR_Top_Area);
		dailyCPs.add(DailyCombineCheckPoint.WR_Bottom_Gordon);

		dailyCPs.add(DailyCombineCheckPoint.MAGIC_NIGHT_DAYS_SHANG_ZHANG);
		dailyCPs.add(DailyCombineCheckPoint.MAGIC_NIGHT_DAYS_XIA_DIE);

		// using the parallel stream to process the check point
		dailyCPs.parallelStream().forEach(cp -> searchAllStockIdStatisticsCheckPoint(cp));
	}

	public static void main(String[] args) {
		HistoryAnalyseReport reporter = new HistoryAnalyseReport();

		// reporter.searchAllStockIdAnalyseHistoryBuySellCheckPoint(DailyCombineCheckPoint.MAGIC_NIGHT_DAYS_SHANG_ZHANG);

		// reporter.searchAllStockIdStatisticsCheckPoint(DailyCombineCheckPoint.MAGIC_NIGHT_DAYS_SHANG_ZHANG);
		// reporter.searchAllStockIdStatisticsCheckPoint(DailyCombineCheckPoint.MAGIC_NIGHT_DAYS_XIA_DIE);

		// if run at the first time, split into 4 thread to run at the same time
		// reporter.countAllStockIdStatisticsCheckPoint(0);
		// reporter.countAllStockIdAnalyseHistoryBuySellCheckPoint(0);
	}
}
