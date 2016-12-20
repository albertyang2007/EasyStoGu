package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.ConfigurationService;
import org.easystogu.config.DBConfigurationService;
import org.easystogu.db.access.table.CheckPointDailyStatisticsTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.portal.vo.StatisticsVO;
import org.easystogu.utils.WeekdayUtil;

public class CheckPointStatisticsEndPoint {
	private ConfigurationService config = DBConfigurationService.getInstance();
	private String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
	private CheckPointDailyStatisticsTableHelper checkPointStatisticsTable = CheckPointDailyStatisticsTableHelper
			.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private String fromToRegex = dateRegex + "_" + dateRegex;

	@GET
	@Path("/luzao/trend/{date}")
	@Produces("application/json")
	public List<StatisticsVO> queryLuZaoTrendStatistics(@PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
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
	@Path("/luzao/gordon/{date}")
	@Produces("application/json")
	public List<StatisticsVO> queryLuZaoGordonStatistics(@PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StatisticsVO> list = new ArrayList<StatisticsVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];

			List<String> dateList = stockPriceTable.getDayListByIdAndBetweenDates("999999", date1, date2);
			for (String date : dateList) {
				StatisticsVO vo = new StatisticsVO();
				vo.date = date;
				vo.count1 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
						DailyCombineCheckPoint.LuZao_GordonO_MA43_DownCross_MA86.name());
				vo.count2 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
						DailyCombineCheckPoint.LuZao_GordonI_MA19_UpCross_MA43.name());
				vo.count3 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
						DailyCombineCheckPoint.LuZao_GordonII_MA19_UpCross_MA86.name());
				vo.count4 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
						DailyCombineCheckPoint.LuZao_DeadI_MA43_UpCross_MA86.name());
				vo.count5 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
						DailyCombineCheckPoint.LuZao_DeadII_MA19_DownCross_MA43.name());
				list.add(vo);
			}
		}

		return list;
	}

	@GET
	@Path("/qsdd/{date}")
	@Produces("application/json")
	public List<StatisticsVO> queryQsddStatistics(@PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
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
	public List<StatisticsVO> queryShenXianStatistics(@PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
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

	@GET
	@Path("/macd/{date}")
	@Produces("application/json")
	public List<StatisticsVO> queryMACDStatistics(@PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
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
							DailyCombineCheckPoint.MACD_Gordon.name());
					vo.count2 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
							DailyCombineCheckPoint.MACD_Dead.name());
					list.add(vo);
				}
			}
		}

		return list;
	}

	@GET
	@Path("/wr/{date}")
	@Produces("application/json")
	public List<StatisticsVO> queryWRStatistics(@PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
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
							DailyCombineCheckPoint.WR_Top_Area.name());
					vo.count2 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
							DailyCombineCheckPoint.WR_Bottom_Area.name());
					vo.count3 = checkPointStatisticsTable.countByDateAndCheckPoint(date,
							DailyCombineCheckPoint.WR_Bottom_Gordon.name());
					list.add(vo);
				}
			}
		}

		return list;
	}
}
