package org.easystogu.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easystogu.analyse.CombineAnalyseHelper;
import org.easystogu.analyse.util.IndProcessHelper;
import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.CheckPointHistoryAnalyseTableHelper;
import org.easystogu.db.access.CheckPointHistorySelectionTableHelper;
import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.StockSuperVOHelper;
import org.easystogu.db.access.WeekStockSuperVOHelper;
import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.CheckPointDailySelectionVO;
import org.easystogu.db.table.CheckPointHistoryAnalyseVO;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.ShenXianVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.db.util.MergeNDaysPriceUtil;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.BOLLHelper;
import org.easystogu.indicator.KDJHelper;
import org.easystogu.indicator.MACDHelper;
import org.easystogu.indicator.ShenXianHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.utils.CrossType;
import org.easystogu.utils.SellPointType;
import org.easystogu.utils.Strings;

import com.google.common.primitives.Doubles;

public class HistoryAnalyseReportNew {
	private FileConfigurationService config = FileConfigurationService.getInstance();
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private CheckPointHistorySelectionTableHelper historyReportTableHelper = CheckPointHistorySelectionTableHelper
			.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private WeekStockSuperVOHelper weekStockOverAllHelper = new WeekStockSuperVOHelper();
	private CombineAnalyseHelper combineAanalyserHelper = new CombineAnalyseHelper();
	private StockSuperVOHelper stockOverAllHelper = new StockSuperVOHelper();
	private CheckPointHistoryAnalyseTableHelper cpHistoryAnalyse = CheckPointHistoryAnalyseTableHelper.getInstance();
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
			.getInstance();
	private MergeNDaysPriceUtil mergeNdaysPriceHeloer = new MergeNDaysPriceUtil();
	private MACDHelper macdHelper = new MACDHelper();
	private KDJHelper kdjHelper = new KDJHelper();
	private ShenXianHelper shenXianHelper = new ShenXianHelper();
	private BOLLHelper bollHelper = new BOLLHelper();
	private MergeNDaysPriceUtil weekPriceMergeUtil = new MergeNDaysPriceUtil();
	private String specifySelectCheckPoint = config.getString("specify_Select_CheckPoint", "");
	private String[] specifySelectCheckPoints = specifySelectCheckPoint.split(";");
	private ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();
	private String[] generalCheckPoints = config.getString("general_CheckPoint", "").split(";");
	// map to contain 5 days spList (real week), the key is the spList.size % 5
	private Map<Integer, List<StockSuperVO>> nDaysOffWeekStockSuperMap = null;

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

		this.initNDaysOffWeekStockSuperMap(stockId);

		// List<StockSuperVO> overWeekList =
		// weekStockOverAllHelper.getAllStockSuperVO(stockId);

		// fliter the history data, set the startDate and endDate
		// overDayList = this.getSubDayVOList(overDayList, "2014-04-01",
		// "9999-99-99");

		if (overDayList.size() == 0) {
			// System.out.println("doAnalyseReport overDayList size=0 for " +
			// stockId);
			return historyReportList;
		}

		// if (overWeekList.size() == 0) {
		// System.out.println("doAnalyseReport overWeekList size=0 for " +
		// stockId);
		// return historyReportList;
		// }

		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updateSuperPrice(stockId, overDayList);
		// chuQuanChuXiPriceHelper.updateSuperPrice(stockId, overWeekList);

		// IndProcessHelper.process(overDayList, overWeekList);
		IndProcessHelper.processDayList(overDayList);

		HistoryReportDetailsVO reportVO = null;
		for (int index = 120; index < overDayList.size(); index++) {
			StockSuperVO superVO = overDayList.get(index);

			// buy point
			if (reportVO == null) {
				String startDate = overDayList.get(index - 120).priceVO.date;
				String endDate = overDayList.get(index).priceVO.date;

				// System.out.println(startDate + " ~~ " + endDate);

				// include the startDate, not include the endDate
				List<StockSuperVO> subOverWeekList = this.getSubWeekVOList(stockId, startDate, endDate, index);

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

			if (!stockId.equals("002609"))
				continue;

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
						System.out.println("Completed: " + reportVO);
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

	public List<StockSuperVO> getSubWeekVOList(String stockId, String startDate, String endDate, int index) {
		// use startDate and endDate to determinate the overWeekList from
		// nDaysOffStockSuperMap
		List<StockSuperVO> subweekList = new ArrayList<StockSuperVO>();
		List<StockSuperVO> nDaysSpList = this.nDaysOffWeekStockSuperMap.get(new Integer((index + 1) % 5));
		// List<StockSuperVO> nDaysSpList = this.getNDaysSpList(endDate);

		// then select all super vo between startDate and endDate
		for (StockSuperVO vo : nDaysSpList) {
			if (vo.priceVO.date.compareTo(startDate) >= 0 && vo.priceVO.date.compareTo(endDate) <= 0) {
				subweekList.add(vo);
			}
		}

		return subweekList;
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

	private List<StockSuperVO> generateStockSuperVOList(String stockId, List<StockPriceVO> spWeekList) {
		// merge them into one overall VO
		List<StockSuperVO> overList = new ArrayList<StockSuperVO>();

		// re-count week macd
		List<MacdVO> macdList = new ArrayList<MacdVO>();
		List<Double> close = StockPriceFetcher.getClosePrice(spWeekList);
		double[][] macd = macdHelper.getMACDList(Doubles.toArray(close));
		for (int i = 0; i < macd[0].length; i++) {
			MacdVO vo = new MacdVO();
			vo.setDif(Strings.convert2ScaleDecimal(macd[0][i]));
			vo.setDea(Strings.convert2ScaleDecimal(macd[1][i]));
			vo.setMacd(Strings.convert2ScaleDecimal(macd[2][i]));
			vo.setStockId(stockId);
			vo.setDate(spWeekList.get(i).date);
			macdList.add(vo);
		}

		// re-count week kdj
		List<KDJVO> kdjList = new ArrayList<KDJVO>();
		List<Double> low = StockPriceFetcher.getLowPrice(spWeekList);
		List<Double> high = StockPriceFetcher.getHighPrice(spWeekList);
		double[][] kdj = kdjHelper.getKDJList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));
		for (int i = 0; i < kdj[0].length; i++) {
			KDJVO vo = new KDJVO();
			vo.setK(Strings.convert2ScaleDecimal(kdj[0][i]));
			vo.setD(Strings.convert2ScaleDecimal(kdj[1][i]));
			vo.setJ(Strings.convert2ScaleDecimal(kdj[2][i]));
			vo.setRsv(Strings.convert2ScaleDecimal(kdj[3][i]));
			vo.setStockId(stockId);
			vo.setDate(spWeekList.get(i).date);
			kdjList.add(vo);
		}

		// re-count week boll
		List<BollVO> bollList = new ArrayList<BollVO>();
		double[][] boll = bollHelper.getBOLLList(Doubles.toArray(close), 20, 2.0, 2.0);
		for (int i = 0; i < boll[0].length; i++) {
			BollVO vo = new BollVO();
			vo.setUp(Strings.convert2ScaleDecimal(boll[0][i]));
			vo.setMb(Strings.convert2ScaleDecimal(boll[1][i]));
			vo.setDn(Strings.convert2ScaleDecimal(boll[2][i]));
			vo.setStockId(stockId);
			vo.setDate(spWeekList.get(i).date);
			bollList.add(vo);
		}

		// re-count week shenxian
		List<ShenXianVO> shenXianList = new ArrayList<ShenXianVO>();
		double[][] shenXian = shenXianHelper.getShenXianList(Doubles.toArray(close));
		for (int i = 0; i < shenXian[0].length; i++) {
			ShenXianVO vo = new ShenXianVO();
			vo.setH1(Strings.convert2ScaleDecimal(shenXian[0][i]));
			vo.setH2(Strings.convert2ScaleDecimal(shenXian[1][i]));
			vo.setH3(Strings.convert2ScaleDecimal(shenXian[2][i]));
			vo.setStockId(stockId);
			vo.setDate(spWeekList.get(i).date);
			shenXianList.add(vo);
		}

		// set all ind list to super vo
		for (int index = 0; index < spWeekList.size(); index++) {
			StockSuperVO superVO = new StockSuperVO(spWeekList.get(index), macdList.get(index), kdjList.get(index),
					bollList.get(index));
			superVO.setShenXianVO(shenXianList.get(index));
			overList.add(superVO);
		}

		return overList;
	}

	private List<StockSuperVO> getNDaysSpList(String endDate) {
		for (int i = 0; i < 5; i++) {
			List<StockSuperVO> tmpList = this.nDaysOffWeekStockSuperMap.get(new Integer(i));
			for (StockSuperVO spvo : tmpList) {
				if (spvo.priceVO.date.equals(endDate)) {
					return tmpList;
				}
			}
		}
		return null;
	}

	private void initNDaysOffWeekStockSuperMap(String stockId) {

		// first empty the map for stockId
		this.nDaysOffWeekStockSuperMap = null;
		this.nDaysOffWeekStockSuperMap = new HashMap<Integer, List<StockSuperVO>>();

		List<StockPriceVO> spDayList = stockPriceTable.getStockPriceById(stockId);
		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updatePrice(stockId, spDayList);

		// System.out.println("original startDay=" + spDayList.get(0).date +
		// ", size=" + spDayList.size());

		int offSet = spDayList.size() % 5;

		for (int i = 0; i < 5; i++) {
			int excludeSize = (5 - i + offSet) % 5;
			List<StockPriceVO> spWeekList_i_off = mergeNdaysPriceHeloer.generateNDaysPriceVOInDescOrder(5,
					spDayList.subList(i, spDayList.size() - excludeSize));
			// System.out.println("map: i=" + i + ", dat startDay=" +
			// spWeekList_i_off.get(0).date + ", endDay="
			// + spWeekList_i_off.get(spWeekList_i_off.size() - 1).date);
			List<StockSuperVO> overWeekList = this.generateStockSuperVOList(stockId, spWeekList_i_off);
			// System.out.println("map: i=" + i + ", week startDay=" +
			// overWeekList.get(0).priceVO.date + ", endDay="
			// + overWeekList.get(overWeekList.size() - 1).priceVO.date);

			IndProcessHelper.processWeekList(overWeekList);
			this.nDaysOffWeekStockSuperMap.put(new Integer(i), overWeekList);
		}
	}

	public static void main(String[] args) {
		HistoryAnalyseReportNew reporter = new HistoryAnalyseReportNew();
		FileConfigurationService config = FileConfigurationService.getInstance();
		// for (DailyCombineCheckPoint checkPoint :
		// DailyCombineCheckPoint.values()) {
		// if (config.getString("general_CheckPoint",
		// "").contains(checkPoint.name())) {
		// reporter.searchAllStockIdAccordingToCheckPoint(checkPoint);
		// }
		// }
		reporter.searchAllStockIdAccordingToCheckPoint(DailyCombineCheckPoint.HengPan_3_Weeks_MA_RongHe_Break_Platform);
	}
}
