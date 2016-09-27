package org.easystogu.portal;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.CompanyInfoVO;
import org.easystogu.file.access.CompanyInfoFileHelper;

public class CompanyInfoEndPoint {
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();

	@GET
	@Path("/{stockId}")
	@Produces("application/json")
	public CompanyInfoVO getByStockId(@PathParam("stockId") String stockId, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return stockConfig.getByStockId(stockId);
	}

	@GET
	@Path("/name={name}")
	@Produces("application/json")
	public CompanyInfoVO getByName(@PathParam("name") String name, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return stockConfig.getByStockName(name);
	}

	@GET
	@Path("/latestndate/{limit}")
	@Produces("application/json")
	public List<String> getLatestDate(@PathParam("limit") int limit, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return stockPriceTable.getLatestNStockDate(limit);
	}
}
