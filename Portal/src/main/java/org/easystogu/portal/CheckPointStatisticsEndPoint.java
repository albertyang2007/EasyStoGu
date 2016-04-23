package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.db.access.CheckPointDailyStatisticsTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.portal.vo.StatisticsVO;
import org.easystogu.utils.WeekdayUtil;

public class CheckPointStatisticsEndPoint {
	private CheckPointDailyStatisticsTableHelper checkPointStatisticsTable = CheckPointDailyStatisticsTableHelper
			.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private String fromToRegex = dateRegex + "_" + dateRegex;

	@GET
	@Path("/luzao/{date}")
	@Produces("application/json")
	public List<StatisticsVO> queryLuZaoStatistics(@PathParam("date") String dateParm) {
		List<StatisticsVO> list = new ArrayList<StatisticsVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];

			List<String> dateList = stockPriceTable.getDayListByIdAndBetweenDates("999999", date1, date2);
			for (String date : dateList) {
				StatisticsVO vo = new StatisticsVO();
				vo.date = date;
				vo.count1 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
						DailyCombineCheckPoint.Trend_PhaseI_GuanCha.name());
				vo.count2 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
						DailyCombineCheckPoint.Trend_PhaseII_JianCang.name());
				vo.count3 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
						DailyCombineCheckPoint.Trend_PhaseIII_ChiGu.name());
				vo.count4 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
						DailyCombineCheckPoint.Trend_PhaseVI_JianCang.name());
				list.add(vo);
			}
		}

		return list;
	}

	@GET
	@Path("/qsdd/{date}")
	@Produces("application/json")
	public List<StatisticsVO> queryQsddStatistics(@PathParam("date") String dateParm) {
		List<StatisticsVO> list = new ArrayList<StatisticsVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];

			List<String> dateList = WeekdayUtil.getWorkingDatesBetween(date1, date2);
			for (String date : dateList) {
				if (stockPriceTable.isDateInDealDate(date)) {
					StatisticsVO vo = new StatisticsVO();
					vo.date = date;
					vo.count1 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
							DailyCombineCheckPoint.QSDD_Top_Area.name());
					vo.count2 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
							DailyCombineCheckPoint.QSDD_Bottom_Area.name());
					vo.count3 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
							DailyCombineCheckPoint.QSDD_Bottom_Gordon.name());
					list.add(vo);
				}
			}
		}

		return list;
	}

	@GET
	@Path("/shenxian/{date}")
	@Produces("application/json")
	public List<StatisticsVO> queryShenXianStatistics(@PathParam("date") String dateParm) {
		List<StatisticsVO> list = new ArrayList<StatisticsVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];

			List<String> dateList = WeekdayUtil.getWorkingDatesBetween(date1, date2);
			for (String date : dateList) {
				if (stockPriceTable.isDateInDealDate(date)) {
					StatisticsVO vo = new StatisticsVO();
					vo.date = date;
					vo.count1 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
							DailyCombineCheckPoint.ShenXian_Gordon.name());
					vo.count2 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
							DailyCombineCheckPoint.ShenXian_Dead.name());
					list.add(vo);
				}
			}
		}

		return list;
	}
}
