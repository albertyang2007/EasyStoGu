package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.db.access.CheckPointDailySelectionTableHelper;
import org.easystogu.portal.vo.LuZaoStatisticsVO;
import org.easystogu.utils.WeekdayUtil;

public class StatisticsEndPoint {
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
			.getInstance();
	private String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private String fromToRegex = dateRegex + "_" + dateRegex;

	@GET
	@Path("/luzao_trend_phase/{date}")
	@Produces("application/json")
	public List<LuZaoStatisticsVO> queryLuZaoTrendPhaseStatistics(@PathParam("date") String dateParm) {
		List<LuZaoStatisticsVO> list = new ArrayList<LuZaoStatisticsVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];

			List<String> dateList = WeekdayUtil.getWorkingDatesBetween(date1, date2);
			for (String date : dateList) {
				LuZaoStatisticsVO vo = new LuZaoStatisticsVO();
				vo.date = date;
				vo.count_I_GuanCha = checkPointDailySelectionTable.countByDateAndCheckPoint(date,
						DailyCombineCheckPoint.Trend_PhaseI_GuanCha.name());
				vo.count_II_JianCang = checkPointDailySelectionTable.countByDateAndCheckPoint(date,
						DailyCombineCheckPoint.Trend_PhaseII_JianCang.name());
				vo.count_III_ChiGu = checkPointDailySelectionTable.countByDateAndCheckPoint(date,
						DailyCombineCheckPoint.Trend_PhaseIII_ChiGu.name());
				vo.count_VI_JianCang = checkPointDailySelectionTable.countByDateAndCheckPoint(date,
						DailyCombineCheckPoint.Trend_PhaseVI_JianCang.name());
				list.add(vo);
			}
		}

		return list;
	}
}
