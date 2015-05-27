package org.easystogu.report;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.analyse.CombineAnalyseHelper;
import org.easystogu.analyse.util.IndCrossCheckingHelper;
import org.easystogu.analyse.util.PriceCheckingHelper;
import org.easystogu.analyse.util.VolumeCheckingHelper;
import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.CheckPointHistoryAnalyseTableHelper;
import org.easystogu.db.access.CheckPointHistorySelectionTableHelper;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.StockSuperVOHelper;
import org.easystogu.db.access.WeekStockSuperVOHelper;
import org.easystogu.db.table.CheckPointDailySelectionVO;
import org.easystogu.db.table.CheckPointHistoryAnalyseVO;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.utils.CrossType;
import org.easystogu.utils.SellPointType;

public class HistoryAnalyseReport {
	private FileConfigurationService config = FileConfigurationService.getInstance();
	private StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
	private CheckPointHistorySelectionTableHelper historyReportTableHelper = CheckPointHistorySelectionTableHelper
			.getInstance();
	private WeekStockSuperVOHelper weekStockOverAllHelper = new WeekStockSuperVOHelper();
	protected CombineAnalyseHelper combineAanalyserHelper = new CombineAnalyseHelper();
	private StockSuperVOHelper stockOverAllHelper = new StockSuperVOHelper();
	private CheckPointHistoryAnalyseTableHelper cpHistoryAnalyse = CheckPointHistoryAnalyseTableHelper.getInstance();
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
			.getInstance();
	private String specifySelectCheckPoint = config.getString("specify_Select_CheckPoint", "");
	private String[] specifySelectCheckPoints = specifySelectCheckPoint.split(";");
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();

	public List<HistoryReportDetailsVO> doAnalyseReport(String stockId, List<DailyCombineCheckPoint> checkPointList) {
		List<HistoryReportDetailsVO> reportList = new ArrayList<HistoryReportDetailsVO>();
		for (DailyCombineCheckPoint checkPoint : checkPointList) {
			reportList.addAll(this.doAnalyseReport(stockId, checkPoint));
		}
		return reportList;
	}

	public List<HistoryReportDetailsVO> doAnalyseReport(String stockId, DailyCombineCheckPoint checkPoint) {

		List<HistoryReportDetailsVO> historyReportList = new ArrayList<HistoryReportDetailsVO>();

		List<StockSuperVO> overDayList = stockOverAllHelper.getAllStockSuperVO(stockId);
		List<StockSuperVO> overWeekList = weekStockOverAllHelper.getAllStockSuperVO(stockId);

		// fliter the history data, set the startDate and endDate
		// overDayList = this.getSubDayVOList(overDayList, "2014-04-01",
		// "9999-99-99");

		if (overDayList.size() == 0) {
			return historyReportList;
		}

		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updateSuperPrice(stockId, overDayList);
		chuQuanChuXiPriceHelper.updateSuperPrice(stockId, overWeekList);

		// count and update all ind data
		// day
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

		HistoryReportDetailsVO reportVO = null;
		for (int index = 120; index < overDayList.size() - 1; index++) {
			StockSuperVO superVO = overDayList.get(index);

			// buy point
			if (reportVO == null) {
				String startDate = overDayList.get(index - 120).priceVO.date;
				String endDate = overDayList.get(index + 1).priceVO.date;
				// include the startDate, not include the endDate
				List<StockSuperVO> subOverWeekList = this.getSubWeekVOList(overWeekList, startDate, endDate);
				if (combineAanalyserHelper.isConditionSatisfy(checkPoint, overDayList.subList(index - 120, index + 1),
						subOverWeekList)) {
					reportVO = new HistoryReportDetailsVO(overDayList);
					reportVO.setBuyPriceVO(superVO.priceVO);
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

	public void searchAllStockIdAccordingToCheckPoint(DailyCombineCheckPoint checkPoint) {

		emptyTableByCheckPoint(checkPoint.toString());

		double[] earnPercent = new double[3];
		long holdDays = 0;
		long holdDaysWhenHighPrice = 0;
		long totalCount = 0;
		int totalHighCount = 0;
		int totalLowCount = 0;
		List<String> stockIds = stockConfig.getAllStockId();

		System.out.println("\n===============================" + checkPoint + " (sellPoint:"
				+ checkPoint.getSellPointType() + ")==========================");
		for (String stockId : stockIds) {

			// if (!stockId.equals("002041"))
			// continue;

			List<HistoryReportDetailsVO> historyReportList = this.doAnalyseReport(stockId, checkPoint);
			for (HistoryReportDetailsVO reportVO : historyReportList) {
				if (reportVO.sellPriceVO != null) {
					reportVO.countData();
					// print the high earn percent if larger than 25%
					if ((reportVO.earnPercent[1] >= 50.0) && (reportVO.earnPercent[0] >= 25.0)) {
						totalHighCount++;
						// System.out.println("High earn: " + reportVO);
					} else if ((reportVO.earnPercent[1] <= -10.0) || (reportVO.earnPercent[0] <= -10.0)) {
						totalLowCount++;
						// System.out.println("Low  earn: " + reportVO);
					}

					if (!reportVO.completed) {
						System.out.println("Not Completed: " + reportVO);
						// save to checkpint daily selection table
						if (isCheckPointSelected(checkPoint)) {
							this.saveToCheckPointDailySelectionDB(reportVO.stockId, reportVO.buyPriceVO.date,
									checkPoint);
						}
					} else {
						// for completed VO
						// remove it from daily selection
						this.checkPointDailySelectionTable.delete(stockId, reportVO.buyPriceVO.date,
								checkPoint.toString());
						// save case into history DB
						historyReportTableHelper.insert(reportVO.convertToHistoryReportVO(checkPoint.toString()));
					}

					totalCount++;
					earnPercent[0] += reportVO.earnPercent[0];
					earnPercent[1] += reportVO.earnPercent[1];
					earnPercent[2] += reportVO.earnPercent[2];
					holdDays += reportVO.holdDays;
					holdDaysWhenHighPrice += reportVO.holdDaysWhenHighPrice;
				}
			}
		}

		if (totalCount == 0) {
			totalCount = 1;
		}

		System.out.println("Total satisfy: " + totalCount + "\t earnPercent[close]=" + (earnPercent[0] / totalCount)
				+ "\t earnPercent[high]=" + (earnPercent[1] / totalCount) + "\t earnPercent[low]="
				+ (earnPercent[2] / totalCount));
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

	private void saveToCheckPointDailySelectionDB(String stockId, String date, DailyCombineCheckPoint checkPoint) {
		CheckPointDailySelectionVO vo = new CheckPointDailySelectionVO();
		vo.setStockId(stockId);
		vo.setDate(date);
		vo.setCheckPoint(checkPoint.toString());
		this.checkPointDailySelectionTable.insertIfNotExist(vo);
	}

	private boolean isCheckPointSelected(DailyCombineCheckPoint checkPoint) {
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

	public void emptyTableByCheckPoint(String checkPoint) {
		this.historyReportTableHelper.deleteByCheckPoint(checkPoint);
	}

	public static void main(String[] args) {
		HistoryAnalyseReport reporter = new HistoryAnalyseReport();

		for (DailyCombineCheckPoint checkPoint : DailyCombineCheckPoint.values()) {
			if (checkPoint.getEarnPercent() <= 0.0) {
				continue;
			}
			// reporter.searchAllStockIdAccordingToCheckPoint(checkPoint);
		}
		reporter.searchAllStockIdAccordingToCheckPoint(DailyCombineCheckPoint.HengPang_Ready_To_Break_Platform_BollUp_BollXueShi2_Dn_Gordon);
		reporter.searchAllStockIdAccordingToCheckPoint(DailyCombineCheckPoint.HengPang_Ready_To_Break_Platform_MA30_Support_MA_RongHe_XiangShang);
	}
}
