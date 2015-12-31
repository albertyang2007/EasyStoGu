package org.easystogu.runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.IndDDXTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.CheckPointDailySelectionVO;
import org.easystogu.db.table.DDXVO;
import org.easystogu.report.comparator.CheckPointEventComparator;

public class DailyDaPanStatusSummary {
	private FileConfigurationService config = FileConfigurationService.getInstance();
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
			.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private IndDDXTableHelper ddxTable = IndDDXTableHelper.getInstance();
	private String latestDate = stockPriceTable.getLatestStockDate();
	private String generalCheckPoints = config.getString("general_CheckPoint", "");
	private Map<String, List<CheckPointDailySelectionVO>> stockEventsMap = new HashMap<String, List<CheckPointDailySelectionVO>>();

	private void analyseDailyCheckPointEvent() {
		List<CheckPointDailySelectionVO> list = checkPointDailySelectionTable.getDailyCheckPointByDate(latestDate);
		for (CheckPointDailySelectionVO checkPointSVO : list) {
			if (generalCheckPoints.contains(checkPointSVO.checkPoint)) {
				this.addToMap(checkPointSVO);
			}
		}
	}

	private void addToMap(CheckPointDailySelectionVO checkPointSVO) {
		List<CheckPointDailySelectionVO> list = this.stockEventsMap.get(checkPointSVO.stockId);
		if (list == null) {
			list = new ArrayList<CheckPointDailySelectionVO>();
			this.stockEventsMap.put(checkPointSVO.stockId, list);
		}
		list.add(checkPointSVO);
	}

	private void sortMap() {
		this.stockEventsMap = CheckPointEventComparator.sortMapByEvent(stockEventsMap);
	}

	private void printResult() {
		Set<String> stockIds = this.stockEventsMap.keySet();
		Iterator<String> its = stockIds.iterator();
		while (its.hasNext()) {
			String stockId = its.next();
			List<CheckPointDailySelectionVO> list = this.stockEventsMap.get(stockId);
			DDXVO ddx = ddxTable.getDDX(stockId, latestDate);
			if (ddx == null)
				continue;
			if (ddx.ddx < 1.0 && list.size() <= 3)
				continue;
			System.out.println(stockId + " " + ddx.toDDXString() + " " + list.size() + " " + list.toString());
		}
	}

	public void run() {
		this.analyseDailyCheckPointEvent();
		this.sortMap();
		this.printResult();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DailyDaPanStatusSummary runner = new DailyDaPanStatusSummary();
		runner.run();
	}
}
