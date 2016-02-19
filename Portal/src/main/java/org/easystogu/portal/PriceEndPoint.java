package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.utils.Strings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PriceEndPoint {
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();
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
		List<StockPriceVO> spList = this.fetchAllPrices(stockIdParm);
		try {
			// parse the forecast body and add back to spList
			if (Strings.isNotEmpty(postBody)) {
				//System.out.println("postBody=\n" + postBody);
				JSONArray myJsonArray = new JSONArray(postBody);
				for (int i = 0; i < myJsonArray.length(); i++) {
					JSONObject jobj = myJsonArray.getJSONObject(i);
					StockPriceVO vo = new StockPriceVO();
					vo.setStockId(jobj.getString("stockId"));
					vo.setDate(jobj.getString("date"));
					vo.setOpen(Double.parseDouble(jobj.getString("open")));
					vo.setClose(Double.parseDouble(jobj.getString("close")));
					vo.setLow(Double.parseDouble(jobj.getString("low")));
					vo.setHigh(Double.parseDouble(jobj.getString("high")));
					vo.setVolume(Long.parseLong(jobj.getString("volume")));

					spList.add(vo);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return spList;
	}

	// common function to fetch price from stockPrice table
	protected List<StockPriceVO> fetchAllPrices(String stockid) {
		List<StockPriceVO> spList = null;
		spList = stockPriceTable.getStockPriceById(stockid);
		return spList;
	}
}
