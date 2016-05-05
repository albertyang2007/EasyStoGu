package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.db.access.HouFuQuanStockPriceTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;

//v2, hou FuQuan stockprice 
public class PriceEndPointV2 {
	protected static String HHmmss = "00:00:00";
	protected CompanyInfoFileHelper companyInfoHelper = CompanyInfoFileHelper.getInstance();
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected HouFuQuanStockPriceTableHelper houfuquanStockPriceTable = HouFuQuanStockPriceTableHelper.getInstance();
	@Autowired
	protected ProcessRequestParmsInPostBody postParmsProcess;
	private String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private String fromToRegex = dateRegex + "_" + dateRegex;

	@GET
	@Path("/{stockId}/{date}")
	@Produces("application/json")
	public List<StockPriceVO> queryDayPriceById(@PathParam("stockId") String stockIdParm,
			@PathParam("date") String dateParm) {

		List<StockPriceVO> spList = new ArrayList<StockPriceVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			if (companyInfoHelper.isStockIdAMajorZhiShu(stockIdParm)) {
				spList = stockPriceTable.getStockPriceByIdAndBetweenDate(stockIdParm, date1, date2);
			} else {
				spList = houfuquanStockPriceTable.getStockPriceByIdAndBetweenDate(stockIdParm, date1, date2);
			}
		}
		if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			if (companyInfoHelper.isStockIdAMajorZhiShu(stockIdParm)) {
				spList.add(stockPriceTable.getStockPriceByIdAndDate(stockIdParm, dateParm));
			} else {
				spList.add(houfuquanStockPriceTable.getStockPriceByIdAndDate(stockIdParm, dateParm));
			}
		}

		return spList;
	}
}
