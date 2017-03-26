package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.config.ConfigurationService;
import org.easystogu.config.DBConfigurationService;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.portal.init.TrendModeLoader;
import org.easystogu.trendmode.vo.TrendModeVO;
import org.easystogu.utils.Strings;
import org.easystogu.utils.WeekdayUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

//v3, with forecast data, query qian fuquan stockprice and count in real time
public class PriceEndPointV3 {
	private ConfigurationService config = DBConfigurationService.getInstance();
	private String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
	protected static String HHmmss = "00:00:00";
	protected CompanyInfoFileHelper companyInfoHelper = CompanyInfoFileHelper.getInstance();
	@Autowired
	protected ProcessRequestParmsInPostBody postParmsProcess;
	@Autowired
	protected TrendModeLoader trendModeLoader;
	private String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private String fromToRegex = dateRegex + "_" + dateRegex;

	@POST
	@Path("/{stockId}/{date}")
	@Produces("application/json")
	public List<StockPriceVO> queryDayPriceByIdWithForecastPrice(@PathParam("stockId") String stockIdParm,
			@PathParam("date") String dateParm, String postBody, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StockPriceVO> rtnSpList = new ArrayList<StockPriceVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

		for (StockPriceVO vo : spList) {
			if (this.isStockDateSelected(postBody, dateParm, vo.date)) {
				rtnSpList.add(vo);
			}
		}

		return rtnSpList;
	}
	
	protected String appendTrendModeDateToDateRange(String postBody, String date) {
		String fromDate = WeekdayUtil.currentDate();
		String endDate = WeekdayUtil.currentDate();

		if (Pattern.matches(fromToRegex, date)) {
			fromDate = date.split("_")[0];
			endDate = date.split("_")[1];

			// if postBody contains the trendMode, then get the dateLengh from
			// it
			// and append the last date to dateRange
			if (Strings.isNotEmpty(postBody)) {
				try {
					JSONObject jsonParm = new JSONObject(postBody);
					String trendModeName = jsonParm.getString("trendModeName");
					if (Strings.isNotEmpty(trendModeName)) {
						TrendModeVO tmo = trendModeLoader.loadTrendMode(trendModeName);
						String newEndDate = WeekdayUtil.nextNWorkingDate(endDate, tmo.length);
						return fromDate + "_" + newEndDate;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		// return the default data range
		return date;
	}

	protected boolean isStockDateSelected(String postBody, String date, String aDate) {

		String newDate = this.appendTrendModeDateToDateRange(postBody, date);

		if (Pattern.matches(fromToRegex, newDate)) {
			String date1 = newDate.split("_")[0];
			String date2 = newDate.split("_")[1];
			return Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " + HHmmss, aDate + " " + HHmmss);
		}
		if (Pattern.matches(dateRegex, newDate) || Strings.isEmpty(newDate)) {
			return aDate.equals(newDate);
		}
		return false;
	}
}
