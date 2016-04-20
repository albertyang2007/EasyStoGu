package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;

public class PriceEndPoint {
	protected static String HHmmss = "00:00:00";
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	@Autowired
	protected ProcessRequestParmsInPostBody postParmsProcess;
	private String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private String fromToRegex = dateRegex + "_" + dateRegex;

	@GET
	@Path("/{stockId}/{date}")
	@Produces("application/json")
	public List<StockPriceVO> queryDayPriceById(@PathParam("stockId") String stockIdParm,
			@PathParam("date") String dateParm) {
		List<StockPriceVO> spList = new ArrayList<StockPriceVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			spList = stockPriceTable.getStockPriceByIdAndBetweenDate(stockIdParm, date1, date2);
		}
		if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			spList.add(stockPriceTable.getStockPriceByIdAndDate(stockIdParm, dateParm));
		}

		return spList;
	}

	@POST
	@Path("/forecast/{stockId}/{date}")
	@Produces("application/json")
	public List<StockPriceVO> queryDayPriceByIdWithForecastPrice(@PathParam("stockId") String stockIdParm,
			@PathParam("date") String dateParm, String postBody) {
		List<StockPriceVO> rtnSpList = new ArrayList<StockPriceVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

		for (StockPriceVO vo : spList) {
			if (this.isStockDateSelected(dateParm, vo.date)) {
				rtnSpList.add(vo);
			}
		}

		return rtnSpList;
	}

	protected boolean isStockDateSelected(String date, String aDate) {
		if (Pattern.matches(fromToRegex, date)) {
			String date1 = date.split("_")[0];
			String date2 = date.split("_")[1];
			return Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " + HHmmss, aDate + " " + HHmmss);
		}
		if (Pattern.matches(dateRegex, date) || Strings.isEmpty(date)) {
			return aDate.equals(date);
		}
		return false;
	}
}
