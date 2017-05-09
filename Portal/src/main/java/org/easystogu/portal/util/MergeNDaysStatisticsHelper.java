package org.easystogu.portal.util;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.portal.vo.StatisticsVO;

public class MergeNDaysStatisticsHelper {
	// input: date price, order by date
	// output: month price, order by month
	public static List<StatisticsVO> mergeToMonthBased(List<StatisticsVO> spList) {
		List<StatisticsVO> monthList = new ArrayList<StatisticsVO>();
		for (StatisticsVO spvo : spList) {
			String year = spvo.date.split("_")[0].split("-")[0];
			String month = spvo.date.split("_")[0].split("-")[1];
			StatisticsVO vo = getStockPriceVOByYearMonth(year, month, monthList);
			if (vo == null) {
				vo = new StatisticsVO();
				vo.date = spvo.date;
				monthList.add(vo);
			} else {
				vo.date = spvo.date;
				if (vo.count1 < spvo.count1) {
					vo.count1 = spvo.count1;
				}
				if (vo.count2 < spvo.count2) {
					vo.count2 = spvo.count2;
				}
				if (vo.count3 < spvo.count3) {
					vo.count3 = spvo.count3;
				}
				if (vo.count4 < spvo.count4) {
					vo.count4 = spvo.count4;
				}
				if (vo.count5 < spvo.count5) {
					vo.count5 = spvo.count5;
				}
			}
		}
		return monthList;
	}

	private static StatisticsVO getStockPriceVOByYearMonth(String _year, String _month, List<StatisticsVO> monthList) {
		for (StatisticsVO vo : monthList) {
			String year = vo.date.split("_")[0].split("-")[0];
			String month = vo.date.split("_")[0].split("-")[1];
			if (year.equals(_year) && month.equals(_month)) {
				return vo;
			}
		}
		return null;
	}
}
