package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.db.access.table.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.view.CommonViewHelper;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.db.vo.view.CommonViewVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

public class ViewEndPoint {
	private static Logger logger = LogHelper.getLogger(ViewEndPoint.class);
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
			.getInstance();
	private CommonViewHelper commonViewHelper = CommonViewHelper.getInstance();

	@GET
	@Path("/{viewname}")
	@Produces("application/json")
	public List<CommonViewVO> queryDayPriceByIdFromAnalyseViewAtRealTime(@PathParam("viewname") String viewname,
			@Context HttpServletRequest request, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String date = request.getParameter("date");
		logger.debug("viewName=" + viewname + ",date=" + date);

		if ("luzao_phaseII_zijinliu_top300".equals(viewname) || "luzao_phaseIII_zijinliu_top300".equals(viewname)) {
			// get result from view directory, since they are fast
			String searchViewName = viewname + "_Details";
			return this.commonViewHelper.queryByDateForViewDirectlySearch(searchViewName, date);
		}

		// else get result for checkpoint data, since they are analyse daily and
		// save to daily table
		List<CommonViewVO> list = new ArrayList<CommonViewVO>();
		String checkpoint = viewname;
		List<CheckPointDailySelectionVO> cps = checkPointDailySelectionTable.queryByDateAndCheckPoint(date, checkpoint);
		for (CheckPointDailySelectionVO cp : cps) {
			CommonViewVO cvo = new CommonViewVO();
			cvo.stockId = cp.stockId;
			cvo.name = stockConfig.getStockName(cp.stockId);
			cvo.date = date;

			list.add(cvo);
		}

		return list;
	}
}
