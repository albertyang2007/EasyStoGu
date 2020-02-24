package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.config.ConfigurationService;
import org.easystogu.config.DBConfigurationService;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.util.MergeNDaysPriceUtil;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.portal.util.MergeNDaysStatisticsHelper;
import org.easystogu.utils.Strings;
import org.easystogu.cache.StockIndicatorCache;
import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.config.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.gson.Gson;

//v1, qian FuQuan stockprice data (suggest to use this v1)
public class PriceEndPointV1 {
	private ConfigurationServiceCache config = ConfigurationServiceCache.getInstance();
	private String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
	protected static String HHmmss = "00:00:00";
	protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	protected StockIndicatorCache indicatorCache = StockIndicatorCache.getInstance();
	private MergeNDaysPriceUtil nDaysPriceMergeUtil = new MergeNDaysPriceUtil();

	@Autowired
	protected ProcessRequestParmsInPostBody postParmsProcess;
	private String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private String fromToRegex = dateRegex + "_" + dateRegex;
	
	private Gson gson = new Gson();

	@GET
	@Path("/{stockId}/{date}")
	@Produces("application/json")
	public String queryDayPriceById(@PathParam("stockId") String stockIdParm,
			@PathParam("date") String dateParm, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StockPriceVO> spList = new ArrayList<StockPriceVO>();

		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			List<StockPriceVO> cacheSpList = indicatorCache
					.queryByStockId(Constants.cacheQianFuQuanStockPrice + ":" + stockIdParm);
			for (Object obj : cacheSpList) {
				StockPriceVO spvo = (StockPriceVO) obj;
				if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " + HHmmss, spvo.date + " " + HHmmss)) {
					spList.add(spvo);
				}
			}
		} else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			spList.add(qianFuQuanStockPriceTable.getStockPriceByIdAndDate(stockIdParm, dateParm));
		}

		return gson.toJson(spList);
	}

	@GET
	@Path("/month/{stockId}/{date}")
	@Produces("application/json")
	public String queryDayPriceByIdMonthBased(@PathParam("stockId") String stockIdParm,
			@PathParam("date") String dateParm, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StockPriceVO> spList = this.queryDayPriceById2(stockIdParm, dateParm, response);
		// merge to month based
		return gson.toJson(nDaysPriceMergeUtil.mergeToMonthBased(spList));
	}
	
	   private List<StockPriceVO> queryDayPriceById2(String stockIdParm,
           String dateParm, HttpServletResponse response) {
       response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
       List<StockPriceVO> spList = new ArrayList<StockPriceVO>();

       if (Pattern.matches(fromToRegex, dateParm)) {
           String date1 = dateParm.split("_")[0];
           String date2 = dateParm.split("_")[1];
           List<StockPriceVO> cacheSpList = indicatorCache
                   .queryByStockId(Constants.cacheQianFuQuanStockPrice + ":" + stockIdParm);
           for (Object obj : cacheSpList) {
               StockPriceVO spvo = (StockPriceVO) obj;
               if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " + HHmmss, spvo.date + " " + HHmmss)) {
                   spList.add(spvo);
               }
           }
       } else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
           spList.add(qianFuQuanStockPriceTable.getStockPriceByIdAndDate(stockIdParm, dateParm));
       }

       return spList;
   }
}
