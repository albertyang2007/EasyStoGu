package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.db.access.FuQuanStockPriceTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;

//v3, with forecast data, query stockprice and count in real time
public class PriceEndPointV3 {
	protected static String HHmmss = "00:00:00";
	protected CompanyInfoFileHelper companyInfoHelper = CompanyInfoFileHelper.getInstance();
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected FuQuanStockPriceTableHelper fuquanStockPriceTable = FuQuanStockPriceTableHelper.getInstance();
	@Autowired
	protected ProcessRequestParmsInPostBody postParmsProcess;
	private String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private String fromToRegex = dateRegex + "_" + dateRegex;

	@POST
	@Path("/{stockId}/{date}")
	@Produces("application/json")
	public List<StockPriceVO> queryDayPriceByIdWithForecastPrice(@PathParam("stockId") String stockIdParm,
			@PathParam("date") String dateParm, String postBody) {
		List<StockPriceVO> rtnSpList = new ArrayList<StockPriceVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

		for (StockPriceVO vo : spList) {
			if (this.isStockDateSelected(dateParm, vo.date)) {
				rtnSpList.add(vo);
			}
		}

		return rtnSpList;
	}

	protected boolean isStockDateSelected(String date, String aDate) {
		if (Pattern.matches(fromToRegex, date)) {
			String date1 = date.split("_")[0];
			String date2 = date.split("_")[1];
			return Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " + HHmmss, aDate + " " + HHmmss);
		}
		if (Pattern.matches(dateRegex, date) || Strings.isEmpty(date)) {
			return aDate.equals(date);
		}
		return false;
	}
}
