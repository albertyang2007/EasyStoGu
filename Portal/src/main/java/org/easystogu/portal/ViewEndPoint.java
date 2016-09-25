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
	@Path("/view/{viewname}")
	@Produces("application/json")
	public List<CommonViewVO> queryDayPriceByIdFromAnalyseViewAtRealTime(@PathParam("viewname") String viewname,
			@Context HttpServletRequest request, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String date = request.getParameter("date");
		logger.debug("viewName=" + viewname + ",date=" + date);
		if (this.commonViewHelper.isViewExist(viewname)) {
			return this.commonViewHelper.queryByDateForViewDirectlySearch(viewname, date);
		}
		logger.debug("viewName=" + viewname + " not exist");
		return new ArrayList<CommonViewVO>();
	}

	@GET
	@Path("/checkpoint/{checkpoint}")
	@Produces("application/json")
	public List<CommonViewVO> queryDayPriceByIdFromCheckPointTable(@PathParam("checkpoint") String checkpoint,
			@Context HttpServletRequest request, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");

		List<CommonViewVO> list = new ArrayList<CommonViewVO>();
		String date = request.getParameter("date");
		logger.debug("checkpoint=" + checkpoint + ",date=" + request.getParameter("date"));
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
