package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.config.Constants;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

//v2, qian FuQuan stockprice (v2 same as v1, can be delete?)

@RestController
@RequestMapping(value = "/pricev2")
public class PriceEndPointV2 extends PriceEndPointV0{
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	private Gson gson = new Gson();
	
	@Override
	@GetMapping("/{stockId}/{date}")
	@Produces("application/json")
	public String queryDayPriceById(@PathParam("stockId") String stockIdParm,
			@PathParam("date") String dateParm, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StockPriceVO> spList = new ArrayList<StockPriceVO>();
		
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			List<StockPriceVO> cacheSpList = indicatorCache.queryByStockId(Constants.cacheQianFuQuanStockPrice + ":" +stockIdParm);
			for (Object obj : cacheSpList) {
				StockPriceVO spvo = (StockPriceVO)obj;
				if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " + HHmmss, spvo.date + " " + HHmmss)) {
					spList.add(spvo);
				}
			}
		} else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			spList.add(stockPriceTable.getStockPriceByIdAndDate(stockIdParm, dateParm));
		}

		return gson.toJson(spList);
	}
}
