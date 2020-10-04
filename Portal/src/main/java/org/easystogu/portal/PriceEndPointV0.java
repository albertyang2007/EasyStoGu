
package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cache.StockIndicatorCache;
import org.easystogu.config.Constants;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

//v0, stockprice (no chuquan)

@RestController
@RequestMapping(value = "/portal/pricev0")
public class PriceEndPointV0 {
	@Autowired
	protected ConfigurationServiceCache config;
	protected static String HHmmss = "00:00:00";
	@Autowired
	protected CompanyInfoFileHelper companyInfoHelper;
	@Autowired
	@Qualifier("stockPriceTable")
	private StockPriceTableHelper stockPriceTable;
	@Autowired
	protected StockIndicatorCache indicatorCache;
	@Autowired
	protected ProcessRequestParmsInPostBody postParmsProcess;
	protected String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	protected String fromToRegex = dateRegex + "_" + dateRegex;

	private Gson gson = new Gson();

	@GetMapping("/{stockId}/{date}")
	@Produces("application/json")
	public String queryDayPriceById(@PathVariable("stockId") String stockIdParm, @PathVariable("date") String dateParm,
			@Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);

		List<StockPriceVO> spList = new ArrayList<StockPriceVO>();

		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			List<StockPriceVO> cacheSpList = indicatorCache
					.queryByStockId(Constants.cacheStockPrice + ":" + stockIdParm);
			for (Object obj : cacheSpList) {
				StockPriceVO spvo = (StockPriceVO) obj;
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
