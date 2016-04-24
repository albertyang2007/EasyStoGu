package org.easystogu.db.util;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.utils.WeekdayUtil;

//merge days price vo into week price vo
public class MergeNDaysPriceUtil {
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();

	public List<StockPriceVO> generateAllWeekPriceVO(String stockId, List<StockPriceVO> spList) {
		List<StockPriceVO> spWeekList = new ArrayList<StockPriceVO>();
		for (int year = 1997; year <= WeekdayUtil.currentYear(); year++) {
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

						// update price based on chuQuanChuXi event
						chuQuanChuXiPriceHelper.updateWeekPrice(stockId, spSubList, firstDate, lastDate);

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

		//System.out.println("sub startDay=" + spDayList.get(0).date + ", size=" + spDayList.size() + ", startIndex="
		//		+ startIndex);

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
}
