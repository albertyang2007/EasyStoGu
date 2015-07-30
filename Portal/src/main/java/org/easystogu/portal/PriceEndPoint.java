package org.easystogu.portal;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;

public class PriceEndPoint {
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper
			.getInstance();

	@GET
	@Path("{stockid}")
	@Produces( "application/json" )
	public List<StockPriceVO> queryStockPriceById(@PathParam("stockid") String stockid) {
		return stockPriceTable.getNdateStockPriceById(stockid, 1);
	}
}
