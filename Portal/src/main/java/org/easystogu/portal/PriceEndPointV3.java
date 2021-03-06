package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.portal.init.TrendModeLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

//v3, with forecast data, query qian fuquan stockprice and count in real time

@RestController
@RequestMapping(value = "/portal/pricev3")
public class PriceEndPointV3 {
	@Autowired
	private ConfigurationServiceCache config;
	@Autowired
	protected CompanyInfoFileHelper companyInfoHelper;
	@Autowired
	protected ProcessRequestParmsInPostBody postParmsProcess;
	@Autowired
	protected TrendModeLoader trendModeLoader;

	private Gson gson = new Gson();

	@PostMapping("/{stockId}/{date}")
	@Produces("application/json")
	public String queryDayPriceByIdWithForecastPrice(@PathVariable("stockId") String stockIdParm,
			@PathVariable("date") String dateParm, String postBody, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StockPriceVO> rtnSpList = new ArrayList<StockPriceVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

		for (StockPriceVO vo : spList) {
			if (postParmsProcess.isStockDateSelected(postBody, dateParm, vo.date)) {
				rtnSpList.add(vo);
			}
		}

		return gson.toJson(rtnSpList);
	}
}
