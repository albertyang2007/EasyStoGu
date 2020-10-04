package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.cache.CheckPointStatisticsCache;
import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cache.StockPriceCache;
import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.Constants;
import org.easystogu.db.vo.table.CheckPointDailyStatisticsVO;
import org.easystogu.portal.vo.StatisticsVO;
import org.easystogu.utils.WeekdayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@RequestMapping(value = "/statistics")
public class CheckPointStatisticsEndPoint {
	@Autowired
	private ConfigurationServiceCache config;
	@Autowired
	private CheckPointStatisticsCache checkPointStatisticsCache;
	@Autowired
	private StockPriceCache stockPriceCache;
	
	private String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private String fromToRegex = dateRegex + "_" + dateRegex;

	private Gson gson = new Gson();

	@GetMapping("/luzao/trend/{date}")
	@Produces("application/json")
	public String queryLuZaoTrendStatistics(@PathParam("date") String dateParm, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StatisticsVO> list = new ArrayList<StatisticsVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];

			// List<String> dateList =
			// stockPriceTable.getSZZSDayListByIdAndBetweenDates(date1, date2);
			List<String> dateList = this.stockPriceCache
					.get(Constants.cacheSZZSDayListByIdAndBetweenDates + ":" + date1 + ":" + date2);

			List<CheckPointDailyStatisticsVO> statisticsList = checkPointStatisticsCache.get(date1 + ":" + date2);

			for (String date : dateList) {
				StatisticsVO vo = new StatisticsVO();
				vo.date = date;
				vo.count1 = this.getRate(statisticsList, date, DailyCombineCheckPoint.Trend_PhaseI_GuanCha.name());
				vo.count2 = this.getRate(statisticsList, date, DailyCombineCheckPoint.Trend_PhaseII_JianCang.name());
				vo.count3 = this.getRate(statisticsList, date, DailyCombineCheckPoint.Trend_PhaseIII_ChiGu.name());
				vo.count4 = this.getRate(statisticsList, date, DailyCombineCheckPoint.Trend_PhaseVI_JianCang.name());
				list.add(vo);
			}
		}

		return gson.toJson(list);
	}

	@GetMapping("/luzao/gordon/{date}")
	@Produces("application/json")
	public String queryLuZaoGordonStatistics(@PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StatisticsVO> list = new ArrayList<StatisticsVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];

			// List<String> dateList =
			// stockPriceTable.getSZZSDayListByIdAndBetweenDates(date1, date2);
			List<String> dateList = this.stockPriceCache
					.get(Constants.cacheSZZSDayListByIdAndBetweenDates + ":" + date1 + ":" + date2);
			List<CheckPointDailyStatisticsVO> statisticsList = checkPointStatisticsCache.get(date1 + ":" + date2);

			for (String date : dateList) {
				StatisticsVO vo = new StatisticsVO();
				vo.date = date;

				vo.count1 = this.getRate(statisticsList, date,
						DailyCombineCheckPoint.LuZao_GordonO_MA43_DownCross_MA86.name());
				vo.count2 = this.getRate(statisticsList, date,
						DailyCombineCheckPoint.LuZao_GordonI_MA19_UpCross_MA43.name());
				vo.count3 = this.getRate(statisticsList, date,
						DailyCombineCheckPoint.LuZao_GordonII_MA19_UpCross_MA86.name());
				vo.count4 = this.getRate(statisticsList, date,
						DailyCombineCheckPoint.LuZao_DeadI_MA43_UpCross_MA86.name());
				vo.count5 = this.getRate(statisticsList, date,
						DailyCombineCheckPoint.LuZao_DeadII_MA19_DownCross_MA43.name());

				list.add(vo);
			}
		}

		return gson.toJson(list);
	}

	@GetMapping("/qsdd/{date}")
	@Produces("application/json")
	public String queryQsddStatistics(@PathParam("date") String dateParm, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StatisticsVO> list = new ArrayList<StatisticsVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];

			List<String> allDealDateList = this.stockPriceCache.get(Constants.cacheAllDealDate + ":999999");
			List<String> dateList = WeekdayUtil.getWorkingDatesBetween(date1, date2);
			List<CheckPointDailyStatisticsVO> statisticsList = checkPointStatisticsCache.get(date1 + ":" + date2);

			for (String date : dateList) {
				if (this.isDateInDealDate(allDealDateList, date)) {
					StatisticsVO vo = new StatisticsVO();
					vo.date = date;

					vo.count1 = this.getRate(statisticsList, date, DailyCombineCheckPoint.QSDD_Top_Area.name());
					vo.count2 = this.getRate(statisticsList, date, DailyCombineCheckPoint.QSDD_Bottom_Area.name());
					vo.count3 = this.getRate(statisticsList, date, DailyCombineCheckPoint.QSDD_Bottom_Gordon.name());

					list.add(vo);
				}
			}
		}

		return gson.toJson(list);
	}

	@GetMapping("/shenxian/{date}")
	@Produces("application/json")
	public String queryShenXianStatistics(@PathParam("date") String dateParm, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StatisticsVO> list = new ArrayList<StatisticsVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];

			List<String> allDealDateList = this.stockPriceCache.get(Constants.cacheAllDealDate + ":999999");
			List<String> dateList = WeekdayUtil.getWorkingDatesBetween(date1, date2);
			List<CheckPointDailyStatisticsVO> statisticsList = checkPointStatisticsCache.get(date1 + ":" + date2);

			for (String date : dateList) {
				if (this.isDateInDealDate(allDealDateList, date)) {
					StatisticsVO vo = new StatisticsVO();
					vo.date = date;

					vo.count1 = this.getRate(statisticsList, date, DailyCombineCheckPoint.ShenXian_Gordon.name());
					vo.count2 = this.getRate(statisticsList, date, DailyCombineCheckPoint.ShenXian_Dead.name());

					list.add(vo);
				}
			}
		}

		return gson.toJson(list);
	}

	@GetMapping("/macd/{date}")
	@Produces("application/json")
	public String queryMACDStatistics(@PathParam("date") String dateParm, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StatisticsVO> list = new ArrayList<StatisticsVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];

			List<String> allDealDateList = this.stockPriceCache.get(Constants.cacheAllDealDate + ":999999");
			List<String> dateList = WeekdayUtil.getWorkingDatesBetween(date1, date2);
			List<CheckPointDailyStatisticsVO> statisticsList = checkPointStatisticsCache.get(date1 + ":" + date2);

			for (String date : dateList) {
				if (this.isDateInDealDate(allDealDateList, date)) {
					StatisticsVO vo = new StatisticsVO();
					vo.date = date;

					vo.count1 = this.getRate(statisticsList, date, DailyCombineCheckPoint.MACD_Gordon.name());
					vo.count2 = this.getRate(statisticsList, date, DailyCombineCheckPoint.MACD_Dead.name());

					list.add(vo);
				}
			}
		}

		return gson.toJson(list);
	}

	@GetMapping("/magic9day/{date}")
	@Produces("application/json")
	public String queryMagic9DayStatistics(@PathParam("date") String dateParm, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StatisticsVO> list = new ArrayList<StatisticsVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];

			List<String> allDealDateList = this.stockPriceCache.get(Constants.cacheAllDealDate + ":999999");
			List<String> dateList = WeekdayUtil.getWorkingDatesBetween(date1, date2);
			List<CheckPointDailyStatisticsVO> statisticsList = checkPointStatisticsCache.get(date1 + ":" + date2);

			for (String date : dateList) {
				if (this.isDateInDealDate(allDealDateList, date)) {
					StatisticsVO vo = new StatisticsVO();
					vo.date = date;

					vo.count1 = this.getRate(statisticsList, date,
							DailyCombineCheckPoint.MAGIC_NIGHT_DAYS_SHANG_ZHANG.name());
					vo.count2 = this.getRate(statisticsList, date,
							DailyCombineCheckPoint.MAGIC_NIGHT_DAYS_XIA_DIE.name());

					list.add(vo);
				}
			}
		}

		return gson.toJson(list);
	}

	@GetMapping("/wr/{date}")
	@Produces("application/json")
	public String queryWRStatistics(@PathParam("date") String dateParm, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StatisticsVO> list = new ArrayList<StatisticsVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];

			List<String> allDealDateList = this.stockPriceCache.get(Constants.cacheAllDealDate + ":999999");
			List<String> dateList = WeekdayUtil.getWorkingDatesBetween(date1, date2);
			List<CheckPointDailyStatisticsVO> statisticsList = checkPointStatisticsCache.get(date1 + ":" + date2);

			for (String date : dateList) {
				if (this.isDateInDealDate(allDealDateList, date)) {
					StatisticsVO vo = new StatisticsVO();
					vo.date = date;

					vo.count1 = this.getRate(statisticsList, date, DailyCombineCheckPoint.WR_Top_Area.name());
					vo.count2 = this.getRate(statisticsList, date, DailyCombineCheckPoint.WR_Bottom_Area.name());
					vo.count3 = this.getRate(statisticsList, date, DailyCombineCheckPoint.WR_Bottom_Gordon.name());

					list.add(vo);
				}
			}
		}

		return gson.toJson(list);
	}

	@GetMapping("/sameDigitsInHighPrice/{date}")
	@Produces("application/json")
	public String querySameDigitsInHighPriceStatistics(@PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StatisticsVO> list = new ArrayList<StatisticsVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];

			List<String> allDealDateList = this.stockPriceCache.get(Constants.cacheAllDealDate + ":999999");
			List<String> dateList = WeekdayUtil.getWorkingDatesBetween(date1, date2);
			List<CheckPointDailyStatisticsVO> statisticsList = checkPointStatisticsCache.get(date1 + ":" + date2);

			for (String date : dateList) {
				if (this.isDateInDealDate(allDealDateList, date)) {
					StatisticsVO vo = new StatisticsVO();
					vo.date = date;

					vo.count1 = this.getRate(statisticsList, date,
							DailyCombineCheckPoint.High_Price_Digit_In_Order.name());
					vo.count2 = this.getRate(statisticsList, date,
							DailyCombineCheckPoint.Low_Price_Digit_In_Order.name());

					list.add(vo);
				}
			}
		}

		return gson.toJson(list);
	}

	private boolean isDateInDealDate(List<String> allDealDateList, String adate) {
		for (String date : allDealDateList) {
			if (date.equals(adate)) {
				return true;
			}
		}
		return false;
	}

	private double getRate(List<CheckPointDailyStatisticsVO> statisticsList, String date, String checkPoint) {
		for (CheckPointDailyStatisticsVO cpvo : statisticsList) {
			if (cpvo.checkPoint.equalsIgnoreCase(checkPoint) && cpvo.date.equalsIgnoreCase(date)) {
				return cpvo.rate;
			}
		}
		return 0.0;
	}
}
