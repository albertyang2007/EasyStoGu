package org.easystogu.ai.sklearn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.db.access.table.CheckPointDailyStatisticsTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.CheckPointDailyStatisticsVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.CSVFileHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.StringComparator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//找出股价在19日，43日，86日内的最低最高价格和对应的日期
//related python sctips is: EasyStock_Predict_High.py and EasyStock_Predict_Low.py
@Component
public class StockPriceHighLowFinder {
	private static Logger logger = LogHelper.getLogger(StockPriceHighLowFinder.class);
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	private StockPriceTableHelper qianFuQuanStockPriceTable;
	@Autowired
	private CheckPointDailyStatisticsTableHelper checkPointDailyStatisticsTableHelper;

	public void saveFileHighLowPriceInDays(String fileName, String stockId, int day) {
		List<StockPriceVO> spList = qianFuQuanStockPriceTable.queryByStockId(stockId);
		int[] indexs = findHighPriceIndex(spList, day);

		String[] csvHeader = new String[] { "date", "flags" };
		List<String[]> csvData = new ArrayList<String[]>(spList.size());
		for (int i = 0; i < indexs.length; i++) {
			csvData.add(new String[] { spList.get(i).getDate(), Integer.toString(indexs[i]) });
		}

		CSVFileHelper.write(fileName, csvHeader, csvData);
	}

	private int[] findHighPriceIndex(List<StockPriceVO> spList, int day) {
		int[] indexs = new int[spList.size()];
		try {
			for (int index = 0; index < spList.size() - day; index++) {
				List<StockPriceVO> subSpList = spList.subList(index, index + day);
				int highIndex = getHighestPriceIndex(subSpList);
				int lowIndex = getLowestPriceIndex(subSpList);
				// mark current day, previously day and next day as top / buttom area
				indexs[index + highIndex] = 1;
				indexs[index + highIndex - 1] = 1;
				indexs[index + highIndex + 1] = 1;

				indexs[index + lowIndex] = -1;
				indexs[index + lowIndex - 1] = -1;
				indexs[index + lowIndex + 1] = -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return indexs;
	}

	private int getHighestPriceIndex(List<StockPriceVO> subSpList) {
		double highest = subSpList.get(0).high;
		int index = 0;
		for (int i = 1; i < subSpList.size(); i++) {
			StockPriceVO vo = subSpList.get(i);
			if (highest < vo.high) {
				highest = vo.high;
				index = i;
			}
		}
		return index;
	}

	private int getLowestPriceIndex(List<StockPriceVO> subSpList) {
		double lowest = subSpList.get(0).low;
		int index = 0;
		for (int i = 1; i < subSpList.size(); i++) {
			StockPriceVO vo = subSpList.get(i);
			if (lowest > vo.low) {
				lowest = vo.low;
				index = i;
			}
		}
		return index;
	}

	public void saveFileAllCheckPointDailyStatistics(String basePath) {
		// QSDD
		String[] cps = new String[] { DailyCombineCheckPoint.QSDD_Top_Area.name(),
				DailyCombineCheckPoint.QSDD_Bottom_Gordon.name(), DailyCombineCheckPoint.QSDD_Bottom_Area.name() };
		saveFileCheckPointDailyStatistics(basePath, "QSDD", cps);

		// LuZao Cross
		cps = new String[] { DailyCombineCheckPoint.LuZao_GordonO_MA43_DownCross_MA86.name(),
				DailyCombineCheckPoint.LuZao_GordonI_MA19_UpCross_MA43.name(),
				DailyCombineCheckPoint.LuZao_GordonII_MA19_UpCross_MA86.name(),
				DailyCombineCheckPoint.LuZao_DeadI_MA43_UpCross_MA86.name(),
				DailyCombineCheckPoint.LuZao_DeadII_MA19_DownCross_MA43.name() };
		saveFileCheckPointDailyStatistics(basePath, "LuZaoCross", cps);

		// LuZao Trend
		cps = new String[] { DailyCombineCheckPoint.Trend_PhaseI_GuanCha.name(),
				DailyCombineCheckPoint.Trend_PhaseII_JianCang.name(),
				DailyCombineCheckPoint.Trend_PhaseIII_ChiGu.name(),
				DailyCombineCheckPoint.Trend_PhaseVI_JianCang.name() };
		saveFileCheckPointDailyStatistics(basePath, "LuZaoTrend", cps);

		// WR
		cps = new String[] { DailyCombineCheckPoint.WR_Top_Area.name(), DailyCombineCheckPoint.WR_Bottom_Gordon.name(),
				DailyCombineCheckPoint.WR_Bottom_Area.name() };
		saveFileCheckPointDailyStatistics(basePath, "WR", cps);

		// ShenXian
		cps = new String[] { DailyCombineCheckPoint.ShenXian_Gordon.name(),
				DailyCombineCheckPoint.ShenXian_Dead.name() };
		saveFileCheckPointDailyStatistics(basePath, "ShenXian", cps);

		// MACD
		cps = new String[] { DailyCombineCheckPoint.MACD_Gordon.name(), DailyCombineCheckPoint.MACD_Dead.name() };
		saveFileCheckPointDailyStatistics(basePath, "MACD", cps);
	}

	public void saveFileCheckPointDailyStatistics(String basePath, String cpShortName, String[] checkPoints) {
		List<StockPriceVO> spSZZSList = qianFuQuanStockPriceTable.queryByStockId("999999");

		String fileName = basePath + "/" + cpShortName + ".csv";

		// header
		List<String> header = new ArrayList<String>();
		header.add("date");
		for (int c = 0; c < checkPoints.length; c++) {
			header.add(checkPoints[c]);
		}
		String[] csvHeader = header.toArray(new String[0]);

		// Map: date, count
		Map<String, List<String>> map = new TreeMap<String, List<String>>();
		// initial map based on stockPrice data
		for (StockPriceVO avo : spSZZSList) {
			List<String> count = new ArrayList<String>();
			// must zero the list
			for (int cs = 0; cs < checkPoints.length; cs++) {
				count.add("0");
			}
			// must zero all count based on the checkPoint column
			map.put(avo.getDate(), count);
		}

		//
		for (int cpIndex = 0; cpIndex < checkPoints.length; cpIndex++) {
			String checkpoint = checkPoints[cpIndex];
			List<CheckPointDailyStatisticsVO> cpList = checkPointDailyStatisticsTableHelper
					.getByCheckPointOrderByDate(checkpoint);
			for (CheckPointDailyStatisticsVO vo : cpList) {
				if (map.containsKey(vo.getDate())) {
					List<String> count = map.get(vo.getDate());
					// must update it
					count.set(cpIndex, vo.getCount() + "");
				} else {
					logger.error("Fatel Error, map does not contains date:" + vo.getDate());
				}
			}
		}

		// data rows
		List<String[]> csvData = new ArrayList<String[]>();
		Map<String, List<String>> orderMap = sortMapByKey(map);// sort by date
		for (Entry<String, List<String>> row : orderMap.entrySet()) {
			List<String> rowTmp = new ArrayList<String>();
			rowTmp.add(row.getKey());// date
			rowTmp.addAll(row.getValue());// counters
			csvData.add(rowTmp.toArray(new String[0]));
		}

		// save file
		CSVFileHelper.write(fileName, csvHeader, csvData);
	}

	private Map<String, List<String>> sortMapByKey(Map<String, List<String>> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, List<String>> sortMap = new TreeMap<String, List<String>>(new StringComparator());
		sortMap.putAll(map);
		return sortMap;
	}

	public static void main(String[] args) {
		StockPriceHighLowFinder ins = new StockPriceHighLowFinder();
		String stockId = "999999";
		int dayPeriod = 86;
		String resultfileName = "C:/Users/eyaweiw/github/EasyStoGu/AI/mytest/AI/" + stockId + "_high_low.csv";
		ins.saveFileHighLowPriceInDays(resultfileName, stockId, dayPeriod);

		// String cpStatisticsBasePath =
		// "C:/Users/eyaweiw/github/EasyStoGu/AI/mytest/AI/";
		// ins.saveFileAllCheckPointDailyStatistics(cpStatisticsBasePath);
	}

}
