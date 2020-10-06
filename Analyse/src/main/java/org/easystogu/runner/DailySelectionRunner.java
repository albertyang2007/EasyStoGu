package org.easystogu.runner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easystogu.analyse.CombineAnalyseHelper;
import org.easystogu.analyse.util.IndProcessHelper;
import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.DBConfigurationService;
import org.easystogu.db.access.table.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.table.CheckPointDailyStatisticsTableHelper;
import org.easystogu.db.access.table.FavoritesStockHelper;
import org.easystogu.db.access.table.ScheduleActionTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.table.StockSuperVOHelper;
import org.easystogu.db.access.table.WeekStockSuperVOHelper;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.db.vo.table.CheckPointDailyStatisticsVO;
import org.easystogu.db.vo.table.ScheduleActionVO;
import org.easystogu.db.vo.table.StockSuperVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.easystogu.utils.WeekdayUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

// daily select stock that checkpoint is satisfied
@Component
public class DailySelectionRunner {
	private static Logger logger = LogHelper.getLogger(DailySelectionRunner.class);
	@Autowired
	private DBConfigurationService config;
	@Autowired
	protected FavoritesStockHelper favoritesStockHelper;
	@Autowired
	protected CompanyInfoFileHelper stockConfig;
	@Autowired
	@Qualifier("stockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	@Qualifier("stockSuperVOHelper")
	private StockSuperVOHelper stockOverAllHelper;
	@Autowired
	@Qualifier("weekStockSuperVOHelper")
	private WeekStockSuperVOHelper weekStockOverAllHelper;
	@Autowired
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable;
	@Autowired
	private CheckPointDailyStatisticsTableHelper checkPointDailyStatisticsTable;
	@Autowired
	private ScheduleActionTableHelper scheduleActionTableHelper;
	@Autowired
	private CombineAnalyseHelper combineAnalyserHelper;

	public void doAnalyse(String stockId, String latestDate, boolean addToScheduleActionTable,
			boolean checkDayPriceEqualWeekPrice, Map<DailyCombineCheckPoint, List<String>> generalCheckPointGordonMap) {
		try {
			List<StockSuperVO> overDayList = stockOverAllHelper.getAllStockSuperVO(stockId);
			List<StockSuperVO> overWeekList = weekStockOverAllHelper.getAllStockSuperVO(stockId);

			if (addToScheduleActionTable && overDayList.size() == 0) {
				logger.debug("No stockprice data for " + stockId + ", add to Schedule Action.");
				// next action should be fetch all the data from web, it must be
				// a new board id
				ScheduleActionVO vo = new ScheduleActionVO();
				vo.setActionDo(ScheduleActionVO.ActionDo.refresh_history_stockprice.name());
				vo.setStockId(stockId);
				vo.setCreateDate(latestDate);
				vo.setRunDate(WeekdayUtil.nextNDateString(latestDate, 20));
				this.scheduleActionTableHelper.deleteIfExistAndThenInsert(vo);
				return;
			}

			if (addToScheduleActionTable && overWeekList.size() == 0) {
				logger.debug("No stockprice data for " + stockId + ", add to Schedule Action.");
				// next action should be fetch all the data from web, it must be
				// a new board id
				ScheduleActionVO vo = new ScheduleActionVO();
				vo.setActionDo(ScheduleActionVO.ActionDo.refresh_history_stockprice.name());
				vo.setStockId(stockId);
				vo.setCreateDate(latestDate);
				vo.setRunDate(WeekdayUtil.nextNDateString(latestDate, 20));
				this.scheduleActionTableHelper.deleteIfExistAndThenInsert(vo);
				return;
			}

			int dayListLen = this.getDateIndex(latestDate, overDayList) + 1;
			if (dayListLen >= 120)
				overDayList = overDayList.subList(dayListLen - 120, dayListLen);

			int weekListLen = overWeekList.size();
			if (weekListLen >= 24)
				overWeekList = overWeekList.subList(weekListLen - 24, weekListLen);

			if (overWeekList.size() == 0 || overDayList.size() == 0) {
				return;
			}

			// so must reverse in date order
			// Collections.reverse(overDayList);
			// Collections.reverse(overWeekList);

			IndProcessHelper.processDayList(overDayList);
			IndProcessHelper.processWeekList(overWeekList);

			StockSuperVO superVO = overDayList.get(overDayList.size() - 1);
			StockSuperVO weekSuperVO = overWeekList.get(overWeekList.size() - 1);

			if (checkDayPriceEqualWeekPrice && !superVO.priceVO.date.equals(weekSuperVO.priceVO.date)) {
				logger.debug(stockId + " DayPrice VO date (" + superVO.priceVO.date
						+ ") is not equal WeekPrice VO date (" + weekSuperVO.priceVO.date + ")");
				return;
			}

			// exclude ting pai
			if (!superVO.priceVO.date.equals(latestDate)) {
				logger.debug(stockId + " priveVO date (" + superVO.priceVO.date + " ) is not equal latestDate ("
						+ latestDate + ")");
				return;
			}

			// check all combine check point
			for (DailyCombineCheckPoint checkPoint : DailyCombineCheckPoint.values()) {
				// logger.debug("checkpoint=" + checkPoint);
				if (this.isSelectedCheckPoint(checkPoint)) {
					if (combineAnalyserHelper.isConditionSatisfy(checkPoint, overDayList, overWeekList)) {
						this.saveToCheckPointSelectionDB(superVO, checkPoint);
						// this.addToConditionMapForReportDisplay(superVO, checkPoint);
					}
				} else if (this.isDependCheckPoint(checkPoint)) {
					if (combineAnalyserHelper.isConditionSatisfy(checkPoint, overDayList, overWeekList)) {
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
								// this.addToConditionMapForReportDisplay(superVO, checkPoint);
							}
						}
					}
				}
				if (this.isGeneralCheckPoint(checkPoint)) {
					if (combineAnalyserHelper.isConditionSatisfy(checkPoint, overDayList, overWeekList)) {
						// only save QSDD Bottom to daily selection table and
						// display
						if (checkPoint.compareTo(DailyCombineCheckPoint.QSDD_Bottom_Area) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.QSDD_Bottom_Gordon) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.QSDD_Top_Area) == 0// add to DB? more top
																									// area
								|| checkPoint.compareTo(DailyCombineCheckPoint.WR_Bottom_Area) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.WR_Bottom_Gordon) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.WR_Top_Area) == 0// add to DB? more top
																								// area
								|| checkPoint.compareTo(DailyCombineCheckPoint.LuZao_GordonO_MA43_DownCross_MA86) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.LuZao_GordonI_MA19_UpCross_MA43) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.LuZao_GordonII_MA19_UpCross_MA86) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.LuZao_DeadI_MA43_UpCross_MA86) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.LuZao_DeadII_MA19_DownCross_MA43) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.LuZao_DeadIII_MA43_DownCross_MA86) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.ShenXian_Gordon) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.ShenXian_Dead) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.MACD_Gordon) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.MACD_Dead) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.WR_DI_BeiLi) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.MAGIC_NIGHT_DAYS_SHANG_ZHANG) == 0
								|| checkPoint.compareTo(DailyCombineCheckPoint.MAGIC_NIGHT_DAYS_XIA_DIE) == 0) {
							this.saveToCheckPointSelectionDB(superVO, checkPoint);
							// this.addToConditionMapForReportDisplay(superVO, checkPoint);
						}
						this.addToGeneralCheckPointGordonMap(checkPoint, stockId, generalCheckPointGordonMap);
					}
				}
			}
		} catch (Exception e) {
			logger.debug("Exception for " + stockId);
			e.printStackTrace();
		}
	}

	private void saveToCheckPointSelectionDB(StockSuperVO superVO, DailyCombineCheckPoint checkPoint) {
		CheckPointDailySelectionVO vo = new CheckPointDailySelectionVO();
		vo.setStockId(superVO.priceVO.stockId);
		vo.setDate(superVO.priceVO.date);
		vo.setCheckPoint(checkPoint.toString());
		this.checkPointDailySelectionTable.insertIfNotExist(vo);
	}

	private void addToGeneralCheckPointGordonMap(DailyCombineCheckPoint checkPoint, String stockId,
			Map<DailyCombineCheckPoint, List<String>> generalCheckPointGordonMap) {
		List<String> stockIds = generalCheckPointGordonMap.get(checkPoint);
		if (stockIds == null) {
			stockIds = new ArrayList<String>();
			generalCheckPointGordonMap.put(checkPoint, stockIds);
		}
		stockIds.add(stockId);
	}

	private boolean isSelectedCheckPoint(DailyCombineCheckPoint checkPoint) {
		String[] specifySelectCheckPoints = config.getString("specify_Select_CheckPoint", "").split(";");
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
		String[] specifyDependCheckPoints = config.getString("specify_Depend_CheckPoint", "").split(";");
		for (String cp : specifyDependCheckPoints) {
			if (cp.equals(checkPoint.toString())) {
				return true;
			}
		}
		return false;
	}

	private boolean isGeneralCheckPoint(DailyCombineCheckPoint checkPoint) {
		String[] generalCheckPoints = config.getString("general_CheckPoint", "").split(";");
		for (String cp : generalCheckPoints) {
			if (cp.equals(checkPoint.toString())) {
				// logger.debug(checkPoint + " is meet");
				return true;
			}
		}
		return false;
	}

	private void addGeneralCheckPointStatisticsResultToDB(String latestDate,
			Map<DailyCombineCheckPoint, List<String>> generalCheckPointGordonMap) {
		logger.debug("==================General CheckPoint Statistics=====================");
		Set<DailyCombineCheckPoint> keys = generalCheckPointGordonMap.keySet();
		Iterator<DailyCombineCheckPoint> keysIt = keys.iterator();
		while (keysIt.hasNext()) {
			DailyCombineCheckPoint checkPoint = keysIt.next();
			List<String> stockIds = generalCheckPointGordonMap.get(checkPoint);

			CheckPointDailyStatisticsVO cpdsvo = new CheckPointDailyStatisticsVO();
			cpdsvo.date = latestDate;
			cpdsvo.checkPoint = checkPoint.name();
			cpdsvo.count = stockIds.size();
			// count the stock company has deal at that date
			int totalCompanyDeal = this.stockPriceTable.countByDate(cpdsvo.date);
			cpdsvo.rate = Strings.convert2ScaleDecimal(cpdsvo.count * 1.0 / totalCompanyDeal, 4);

			// update
			String previousDate = this.stockPriceTable.getPreviousStockDate(latestDate);
			CheckPointDailyStatisticsVO lastVO = checkPointDailyStatisticsTable.getByCheckPointAndDate(previousDate,
					cpdsvo.checkPoint);
			checkPointDailyStatisticsTable.delete(cpdsvo.date, cpdsvo.checkPoint);
			checkPointDailyStatisticsTable.insert(cpdsvo);

			String diff = "N/A";
			if (lastVO != null) {
				diff = Integer.toString(cpdsvo.count - lastVO.count);
			}
			logger.debug(cpdsvo.checkPoint + " = " + cpdsvo.count + " (Diff: " + diff + ")");
		}
	}

	// get the index which is equal date, if date == LatestDate, it should
	// return size - 1, overList is ORDER BY DATE
	private int getDateIndex(String date, List<StockSuperVO> overList) {

		for (int index = 0; index < overList.size(); index++) {
			StockSuperVO vo = overList.get(index);
			if (vo.priceVO.date.equals(date)) {
				return index;
			}
		}
		return overList.size() - 1;
	}

	public void runForStockIds(List<String> stockIds, String latestDate, boolean addToScheduleActionTable,
			boolean checkDayPriceEqualWeekPrice) {
		logger.debug("DailySelection runForStockIds start for date " + latestDate);

		Map<DailyCombineCheckPoint, List<String>> generalCheckPointGordonMap = new java.util.concurrent.ConcurrentHashMap<DailyCombineCheckPoint, List<String>>();

		stockIds.parallelStream().forEach(stockId -> {
			this.doAnalyse(stockId, latestDate, addToScheduleActionTable, checkDayPriceEqualWeekPrice,
					generalCheckPointGordonMap);
		});

		// int index = 0;
		// for (String stockId : stockIds) {
		// // if (!stockId.equals("300300"))
		// // continue;
		// if (index++ % 500 == 0) {
		// logger.debug("Analyse of " + index + "/" + stockIds.size());
		// }
		// doAnalyse(stockId);
		// }

		addGeneralCheckPointStatisticsResultToDB(latestDate, generalCheckPointGordonMap);

		logger.debug("DailySelection runForStockIds stop for date " + latestDate);
	}

	public void run() {
		String latestDate = stockPriceTable.getLatestStockDate();
		boolean addToScheduleActionTable = true;
		boolean checkDayPriceEqualWeekPrice = true;
		List<String> stockIds = stockConfig.getAllStockId();
		this.runForStockIds(stockIds, latestDate, addToScheduleActionTable, checkDayPriceEqualWeekPrice);
	}

	// run for CHECKPOINT_DAILY_STATISTICS when all zero statistics for date
	public void runForDate(String date, List<String> stockIds) {
		String latestDate = date;
		boolean addToScheduleActionTable = false;
		boolean checkDayPriceEqualWeekPrice = false;
		this.runForStockIds(stockIds, latestDate, addToScheduleActionTable, checkDayPriceEqualWeekPrice);
	}

	// run for CHECKPOINT_DAILY_STATISTICS when all zero statistics for date
	public void runForDate(String date) {
		String latestDate = date;
		boolean addToScheduleActionTable = false;
		boolean checkDayPriceEqualWeekPrice = false;
		this.runForStockIds(stockConfig.getAllStockId(), latestDate, addToScheduleActionTable,
				checkDayPriceEqualWeekPrice);
	}
}
