package org.easystogu.portal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cache.StockPriceCache;
import org.easystogu.config.Constants;
import org.easystogu.db.access.view.FavoritesStockCheckpointViewHelper;
import org.easystogu.db.vo.view.FavoritesStockCheckpointVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@RequestMapping(value = "/report")
public class ReportEndPoint {
	@Autowired
	private ConfigurationServiceCache config;
	@Autowired
	private FavoritesStockCheckpointViewHelper favoritesStockCheckpointViewHelper;
	@Autowired
	private StockPriceCache stockPriceCache;
	protected String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
	private Gson gson = new Gson();

	@GetMapping("/{DateOffset}")
	@Produces("text/html; charset=UTF-8")
	// @Produces("application/json")
	// DateOffset is like 0,1,2,3 means today, yesterday, ...
	public String queryReportByDate(@PathParam("DateOffset") String dateOffset, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<String> latestDates = this.stockPriceCache.get(Constants.cacheLatestNStockDate + ":10");
		String date = latestDates.get(Math.abs(Integer.parseInt(dateOffset)));
		List<FavoritesStockCheckpointVO> list = favoritesStockCheckpointViewHelper.getByDateAndUserId(date, "admin");
		StringBuffer rtn = new StringBuffer("====" + date + "====<br>");
		Map<String, FavoritesStockCheckpointVO> info = new HashMap<String, FavoritesStockCheckpointVO>();
		for (FavoritesStockCheckpointVO vo : list) {
			if (!info.containsKey(vo.stockId)) {
				info.put(vo.stockId, vo);
				rtn.append(vo.name + ":<br>");
				rtn.append(vo.checkPoint + "<br>");
			} else {
				rtn.append(vo.checkPoint + "<br>");
			}
		}
		return gson.toJson(rtn.toString());
	}
}
