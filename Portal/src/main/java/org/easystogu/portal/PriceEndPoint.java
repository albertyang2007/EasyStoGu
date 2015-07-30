package org.easystogu.portal;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;

public class PriceEndPoint {
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private StockPriceTableHelper weekStockPriceTable = WeekStockPriceTableHelper.getInstance();

	@GET
	@Path("/day/{date}/{stockid}")
	@Produces("application/json")
	public StockPriceVO queryDayPriceById(@PathParam("date") String date, @PathParam("stockid") String stockid) {
		return stockPriceTable.getStockPriceByIdAndDate(stockid, date);
	}

	@GET
	@Path("/day/{date1}_{date2}/{stockid}")
	@Produces("application/json")
	public List<StockPriceVO> queryDayListPriceById(@PathParam("date1") String date1, @PathParam("date2") String date2,
			@PathParam("stockid") String stockid) {
		return stockPriceTable.getStockPriceByIdBetweenDate(stockid, date1, date2);
	}

	// will have problem on query
	@GET
	@Path("/week/{date}/{stockid}")
	@Produces("application/json")
	public StockPriceVO queryWeekPriceById(@PathParam("date") String date, @PathParam("stockid") String stockid) {
		return weekStockPriceTable.getStockPriceByIdAndDate(stockid, date);
	}

	// will have problem on query
	@GET
	@Path("/week/{date1}_{date2}/{stockid}")
	@Produces("application/json")
	public List<StockPriceVO> queryWeekListPriceById(@PathParam("date1") String date1,
			@PathParam("date2") String date2, @PathParam("stockid") String stockid) {
		return weekStockPriceTable.getStockPriceByIdBetweenDate(stockid, date1, date2);
	}

}
