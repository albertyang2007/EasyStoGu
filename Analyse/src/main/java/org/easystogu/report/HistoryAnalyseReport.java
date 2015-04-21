package org.easystogu.report;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.analyse.CombineAnalyseHelper;
import org.easystogu.analyse.util.IndCrossCheckingHelper;
import org.easystogu.analyse.util.PriceCheckingHelper;
import org.easystogu.analyse.util.VolumeCheckingHelper;
import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.CheckPointHistoryAnalyseTableHelper;
import org.easystogu.db.access.CheckPointHistorySelectionTableHelper;
import org.easystogu.db.access.StockSuperVOHelper;
import org.easystogu.db.access.WeekStockSuperVOHelper;
import org.easystogu.db.table.CheckPointHistoryAnalyseVO;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.utils.CrossType;
import org.easystogu.utils.SellPointType;

public class HistoryAnalyseReport {

	private StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
	private CheckPointHistorySelectionTableHelper historyReportTableHelper = CheckPointHistorySelectionTableHelper
			.getInstance();
	private WeekStockSuperVOHelper weekStockOverAllHelper = new WeekStockSuperVOHelper();
	protected CombineAnalyseHelper combineAanalyserHelper = new CombineAnalyseHelper();
	private StockSuperVOHelper stockOverAllHelper = new StockSuperVOHelper();
	private CheckPointHistoryAnalyseTableHelper cpHistoryAnalyse = CheckPointHistoryAnalyseTableHelper.getInstance();

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

		if (overDayList.size() == 0) {
			return historyReportList;
		}

		// count and update all ind data
		// day
		IndCrossCheckingHelper.macdCross(overDayList);
		IndCrossCheckingHelper.kdjCross(overDayList);
		IndCrossCheckingHelper.rsvCross(overDayList);
		IndCrossCheckingHelper.shenXianCross12(overDayList);
		IndCrossCheckingHelper.shenXianCross13(overDayList);
		VolumeCheckingHelper.volumeIncreasePuls(overDayList);
		VolumeCheckingHelper.avgVolume5(overDayList);
		PriceCheckingHelper.priceHigherThanNday(overDayList, 15);
		PriceCheckingHelper.setLastClosePrice(overDayList);
		PriceCheckingHelper.countAvgMA(overDayList);
		// week
		IndCrossCheckingHelper.macdCross(overWeekList);
		IndCrossCheckingHelper.kdjCross(overWeekList);
		IndCrossCheckingHelper.rsvCross(overWeekList);

		HistoryReportDetailsVO reportVO = null;
		for (int index = 120; index < overDayList.size() - 1; index++) {
			StockSuperVO superVO = overDayList.get(index);
			// buy point
			if (reportVO == null
					&& combineAanalyserHelper.isConditionSatisfy(checkPoint,
							overDayList.subList(index - 120, index + 1), overWeekList)) {
				reportVO = new HistoryReportDetailsVO();
				reportVO.setBuyPriceVO(superVO.priceVO);
				continue;
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
			reportVO.setSelPriceVO(superVO.priceVO, false);
			historyReportList.add(reportVO);
			reportVO = null;
		}

		return historyReportList;
	}

	public void UnitTest1() {
		// kdj閲戝弶锛屼笁鍊煎潎鍦�0-50涔嬮棿銆俶acd宸茬粡涓烘銆傚叾浠栨潯浠惰窡2涓�牱銆備緥瀛愭牸鍔涚數鍣�0141124
		List<DailyCombineCheckPoint> checkPointList = new ArrayList<DailyCombineCheckPoint>();
		checkPointList.add(DailyCombineCheckPoint.KDJ_Gordon_3_days_Red);
		String stockId = "000651";
		List<HistoryReportDetailsVO> historyReportList = this.doAnalyseReport(stockId, checkPointList);
		System.out.println("\nUnit1 history report for " + DailyCombineCheckPoint.KDJ_Gordon_3_days_Red + " size="
				+ historyReportList.size());
		for (HistoryReportDetailsVO reportVO : historyReportList) {
			if (reportVO.sellPriceVO != null) {
				reportVO.countData();
				System.out.println(reportVO);
			}
		}
	}

	public void UnitTest2() {
		// kdj閲戝弶锛屼笁鍊煎湪70-80涔嬮棿锛宮acd涓夊�涓烘锛宬绾垮ぇ闃筹紝鎴愪氦閲忓ぇ浜庡墠涓�棩銆備箣鍓嶄笁鏃ュ潎缁匡紝鏄礂鐩樼粨鏉熴�渚嬪瓙鏍煎姏鍦颁骇20150129
		List<DailyCombineCheckPoint> checkPointList = new ArrayList<DailyCombineCheckPoint>();
		checkPointList.add(DailyCombineCheckPoint.KDJ_Gordon_Pre_3_Days_Green);
		String stockId = "600185";
		List<HistoryReportDetailsVO> historyReportList = this.doAnalyseReport(stockId, checkPointList);
		System.out.println("\nUnit2 history report for " + DailyCombineCheckPoint.KDJ_Gordon_Pre_3_Days_Green
				+ " size=" + historyReportList.size());
		for (HistoryReportDetailsVO reportVO : historyReportList) {
			if (reportVO.sellPriceVO != null) {
				reportVO.countData();
				System.out.println(reportVO);
			}
		}
	}

	public void UnitTest3() {
		// kdj锛宮acd鍙岄噾鍙夛紝kdj鍊煎潎鍦�0宸﹀彸銆備笁鏃ュ潎涓虹孩k绾匡紝鎴愪氦閲忎竴鏃ユ瘮涓�棩澶э紝鏀剁洏浠烽�姝ユ彁楂樸�渚嬪瓙缇庡皵闆�0150122
		List<DailyCombineCheckPoint> checkPointList = new ArrayList<DailyCombineCheckPoint>();
		checkPointList.add(DailyCombineCheckPoint.MACD_KDJ_Gordon_3_Days_Red_MA_Ronghe_XiangShang);
		String stockId = "600107";
		List<HistoryReportDetailsVO> historyReportList = this.doAnalyseReport(stockId, checkPointList);
		System.out.println("\nUnit3 history report for "
				+ DailyCombineCheckPoint.MACD_KDJ_Gordon_3_Days_Red_MA_Ronghe_XiangShang + " size="
				+ historyReportList.size());
		for (HistoryReportDetailsVO reportVO : historyReportList) {
			if (reportVO.sellPriceVO != null) {
				reportVO.countData();
				System.out.println(reportVO);
			}
		}
	}

	public void UnitTest4() {
		// kdj閲戝弶锛屾垚浜ら噺涓�棩姣斾竴鏃ュぇ锛岄噾鍙夊墠涓ゆ棩绾紝褰撴棩缁匡紝浣嗘槸鏀剁洏浠峰苟涓嶄綆浜庡墠涓�棩锛岃鏄庨噾鍙夊綋鏃ユ槸娲楃洏缁х画涓婅
		// .
		// 渚嬪瓙鐭冲熀淇℃伅20150204
		List<DailyCombineCheckPoint> checkPointList = new ArrayList<DailyCombineCheckPoint>();
		checkPointList.add(DailyCombineCheckPoint.KDJ_Green_Gordon_Pre_2_Days_Red);
		String stockId = "002153";
		List<HistoryReportDetailsVO> historyReportList = this.doAnalyseReport(stockId, checkPointList);
		System.out.println("\nUnit4 history report for " + DailyCombineCheckPoint.KDJ_Green_Gordon_Pre_2_Days_Red
				+ " size=" + historyReportList.size());
		for (HistoryReportDetailsVO reportVO : historyReportList) {
			if (reportVO.sellPriceVO != null) {
				reportVO.countData();
				System.out.println(reportVO);
			}
		}
	}

	public void UnitTestForSpecifyStockId() {
		// make sure UnitTest1~4 are correct, can select the stockId
		this.UnitTest1();
		this.UnitTest2();
		this.UnitTest3();
		this.UnitTest4();
	}

	public void searchAllStockIdAccordingToCheckPoint(DailyCombineCheckPoint checkPoint) {
		double[] earnPercent = new double[3];
		long holdDays = 0;
		long totalCount = 0;
		int totalHighCount = 0;
		int totalLowCount = 0;
		List<String> stockIds = stockConfig.getAllStockId();

		System.out.println("\n===============================" + checkPoint + " (sellPoint:"
				+ checkPoint.getSellPointType() + ")==========================");
		for (String stockId : stockIds) {
			List<HistoryReportDetailsVO> historyReportList = this.doAnalyseReport(stockId, checkPoint);
			for (HistoryReportDetailsVO reportVO : historyReportList) {
				if (reportVO.sellPriceVO != null) {
					reportVO.countData();
					// print the high earn percent if larger than 25%
					if ((reportVO.earnPercent[1] >= 50.0) && (reportVO.earnPercent[0] >= 25.0)) {
						totalHighCount++;
						// System.out.println("High earn: " + reportVO);
						// save the high earnPercent case into DB
						// historyReportTableHelper.insert(reportVO.convertToHistoryReportVO(checkPoint.toString()));
					} else if ((reportVO.earnPercent[1] <= -10.0) || (reportVO.earnPercent[0] <= -10.0)) {
						totalLowCount++;
						// System.out.println("Low  earn: " + reportVO);
					}

					if (!reportVO.completed) {
						System.out.println("Not Completed: " + reportVO);
					}

					totalCount++;
					earnPercent[0] += reportVO.earnPercent[0];
					earnPercent[1] += reportVO.earnPercent[1];
					earnPercent[2] += reportVO.earnPercent[2];
					holdDays += reportVO.holdDays;
				}
			}
		}

		if (totalCount == 0) {
			totalCount = 1;
		}

		System.out.println("Total satisfy: " + totalCount + "\t earnPercent[close]=" + (earnPercent[0] / totalCount)
				+ "\t earnPercent[high]=" + (earnPercent[1] / totalCount) + "\t earnPercent[low]="
				+ (earnPercent[2] / totalCount));
		System.out.println("Avg hold stock days: " + (holdDays / totalCount));
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

		// cpHistoryAnalyse.insert(vo);

	}

	public void emptyTableByCheckPoint(String checkPoint) {
		this.historyReportTableHelper.deleteByCheckPoint(checkPoint);
	}

	public static void main(String[] args) {
		HistoryAnalyseReport reporter = new HistoryAnalyseReport();

		for (DailyCombineCheckPoint checkPoint : DailyCombineCheckPoint.values()) {
			// if(checkPoint.getEarnPercent()>=7.5)
			// System.out.println(checkPoint);
			// reporter.emptyTableByCheckPoint(checkPoint.toString());
			// reporter.searchAllStockIdAccordingToCheckPoint(checkPoint);
		}

		reporter.searchAllStockIdAccordingToCheckPoint(DailyCombineCheckPoint.HengPan_2_Weeks_2_Days_Green_RSV_KDJ_Gordon_RongHe_XiangShang_Break_Platform);
		// reporter.UnitTestForSpecifyStockId();
	}
}
