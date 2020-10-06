package org.easystogu.db.util;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.WeekdayUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

//merge days price vo into week price vo
@Component
public class MergeNDaysPriceUtil {
	private static Logger logger = LogHelper.getLogger(MergeNDaysPriceUtil.class);
	public List<StockPriceVO> generateAllWeekPriceVO(String stockId, List<StockPriceVO> spList) {
		List<StockPriceVO> spWeekList = new ArrayList<StockPriceVO>();
		for (int year = 2000; year <= WeekdayUtil.currentYear(); year++) {
			for (int week = 1; week <= 54; week++) {
				List<String> dates = WeekdayUtil.getWorkingDaysOfWeek(year, week);
				if ((dates != null) && (dates.size() >= 1)) {
					String firstDate = dates.get(0);
					String lastDate = dates.get(dates.size() - 1);
					// List<StockPriceVO> spSubList =
					// stockPriceTable.getStockPriceByIdAndBetweenDate(stockId,
					// firstDate,
					// lastDate);
					List<StockPriceVO> spSubList = this.getSubList(spList, firstDate, lastDate);
					if ((spSubList != null) && (spSubList.size() >= 1)) {

						int last = spSubList.size() - 1;
						// first day
						StockPriceVO mergeVO = spSubList.get(0).copy();
						// last day
						mergeVO.close = spSubList.get(last).close;
						mergeVO.date = spSubList.get(last).date;

						if (spSubList.size() > 1) {
							for (int j = 1; j < spSubList.size(); j++) {
								StockPriceVO vo = spSubList.get(j);
								mergeVO.volume += vo.volume;
								if (mergeVO.high < vo.high) {
									mergeVO.high = vo.high;
								}
								if (mergeVO.low > vo.low) {
									mergeVO.low = vo.low;
								}
							}
						}
						spWeekList.add(mergeVO);
					}
				}
			}
		}
		return spWeekList;
	}

	// 后对其模式
	// such as merge 5 days price into a real week price, it will discard some
	// vo from the start index, let all the remain can group into a week vo
	public List<StockPriceVO> generateNDaysPriceVOInDescOrder(int nDays, List<StockPriceVO> spDayList) {
		if (nDays <= 1)
			return spDayList;

		List<StockPriceVO> newWeekSpList = new ArrayList<StockPriceVO>();

		int startIndex = spDayList.size() % nDays;

		// logger.debug("sub startDay=" + spDayList.get(0).date + ",
		// size=" + spDayList.size() + ", startIndex="
		// + startIndex);

		for (int i = startIndex; i < spDayList.size(); i += nDays) {

			List<StockPriceVO> subSpList = spDayList.subList(i, i + nDays);

			int last = subSpList.size() - 1;
			// first day
			StockPriceVO mergeVO = subSpList.get(0).copy();
			// last day
			mergeVO.close = subSpList.get(last).close;
			mergeVO.date = subSpList.get(last).date;

			if (subSpList.size() > 1) {
				for (int j = 1; j < subSpList.size(); j++) {
					StockPriceVO vo = subSpList.get(j);
					mergeVO.volume += vo.volume;
					if (mergeVO.high < vo.high) {
						mergeVO.high = vo.high;
					}
					if (mergeVO.low > vo.low) {
						mergeVO.low = vo.low;
					}
				}
			}

			newWeekSpList.add(mergeVO);
		}
		return newWeekSpList;
	}

	private List<StockPriceVO> getSubList(List<StockPriceVO> spList, String startDay, String endDay) {
		List<StockPriceVO> subList = new ArrayList<StockPriceVO>();
		for (StockPriceVO vo : spList) {
			if (vo.date.compareTo(startDay) >= 0 && vo.date.compareTo(endDay) <= 0) {
				subList.add(vo);
			}
		}
		return subList;
	}

	// input: date price, order by date
	// output: month price, order by month
	public List<StockPriceVO> mergeToMonthBased(List<StockPriceVO> spList) {
		List<StockPriceVO> monthList = new ArrayList<StockPriceVO>();
		for (StockPriceVO spvo : spList) {
			String year = spvo.date.split("_")[0].split("-")[0];
			String month = spvo.date.split("_")[0].split("-")[1];
			StockPriceVO vo = this.getStockPriceVOByYearMonth(year, month, monthList);
			if (vo == null) {
				vo = new StockPriceVO();
				vo.name = spvo.name;
				vo.open = spvo.open;
				vo.close = spvo.close;
				vo.high = spvo.high;
				vo.low = spvo.low;
				vo.date = spvo.date;
				vo.stockId = spvo.stockId;
				vo.volume = spvo.volume;

				monthList.add(vo);
			} else {
				vo.date = spvo.date;
				vo.close = spvo.close;
				vo.volume += spvo.volume;

				if (vo.high < spvo.high) {
					vo.high = spvo.high;
				}
				if (vo.low > spvo.low) {
					vo.low = spvo.low;
				}
			}
		}
		return monthList;
	}

	private StockPriceVO getStockPriceVOByYearMonth(String _year, String _month, List<StockPriceVO> monthList) {
		for (StockPriceVO vo : monthList) {
			String year = vo.date.split("_")[0].split("-")[0];
			String month = vo.date.split("_")[0].split("-")[1];
			if (year.equals(_year) && month.equals(_month)) {
				return vo;
			}
		}
		return null;
	}
}
