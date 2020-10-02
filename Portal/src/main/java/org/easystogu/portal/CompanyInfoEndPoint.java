package org.easystogu.portal;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cache.StockPriceCache;
import org.easystogu.config.Constants;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;


@RestController
@RequestMapping(value = "/company")
public class CompanyInfoEndPoint {
	private ConfigurationServiceCache config = ConfigurationServiceCache.getInstance();
	private String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private StockPriceCache stockPriceCache = StockPriceCache.getInstance();
	
	private Gson gson = new Gson();

	@GetMapping("/{stockId}")
	@Produces("application/json")
	public String getByStockId(@PathParam("stockId") String stockId, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		return gson.toJson(stockConfig.getByStockId(stockId));
	}

	@GetMapping("/name={name}")
	@Produces("application/json")
	public String getByName(@PathParam("name") String name, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		return gson.toJson(stockConfig.getByStockName(name));
	}

	@GetMapping("/latestndate/{limit}")
	@Produces("application/json")
	public String getLatestDate(@PathParam("limit") int limit, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		return gson.toJson(this.stockPriceCache.get(Constants.cacheLatestNStockDate + ":" + limit));
	}
}
