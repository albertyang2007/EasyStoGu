package org.easystogu.portal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.db.access.view.CommonViewHelper;
import org.easystogu.db.vo.view.CommonViewVO;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

public class ViewEndPoint {
	private static Logger logger = LogHelper.getLogger(ViewEndPoint.class);
	private CommonViewHelper commonViewHelper = CommonViewHelper.getInstance();

	@GET
	@Path("/{viewname}")
	@Produces("application/json")
	public List<CommonViewVO> queryDayPriceById(@PathParam("viewname") String viewname,
			@Context HttpServletRequest request, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		logger.debug("viewName=" + viewname + ",date=" + request.getParameter("date"));
		return this.commonViewHelper.queryByDate(viewname, request.getParameter("date"));
	}
}
