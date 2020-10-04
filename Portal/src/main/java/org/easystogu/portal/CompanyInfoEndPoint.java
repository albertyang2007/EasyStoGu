package org.easystogu.portal;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cache.StockPriceCache;
import org.easystogu.config.Constants;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;


@RestController
@RequestMapping(value = "/portal/company")
public class CompanyInfoEndPoint {
	@Autowired
	private ConfigurationServiceCache config ;
	@Autowired
	private CompanyInfoFileHelper stockConfig ;
	@Autowired
	private StockPriceCache stockPriceCache;
	
	private Gson gson = new Gson();

	@GetMapping("/{stockId}")
	@Produces("application/json")
	public String getByStockId(@PathVariable("stockId") String stockId, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		return gson.toJson(stockConfig.getByStockId(stockId));
	}

	@GetMapping("/name={name}")
	@Produces("application/json")
	public String getByName(@PathVariable("name") String name, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		return gson.toJson(stockConfig.getByStockName(name));
	}

	@GetMapping("/latestndate/{limit}")
	@Produces("application/json")
	public String getLatestDate(@PathVariable("limit") int limit, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		return gson.toJson(this.stockPriceCache.get(Constants.cacheLatestNStockDate + ":" + limit));
	}
}
