package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.cache.CheckPointDailySelectionTableCache;
import org.easystogu.cache.CommonViewCache;
import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.db.vo.view.CommonViewVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@RequestMapping(value = "/view")
public class ViewEndPoint {
	private static Logger logger = LogHelper.getLogger(ViewEndPoint.class);
	@Autowired
	private ConfigurationServiceCache config;
	private String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
	@Autowired
	private CompanyInfoFileHelper stockConfig;
	@Autowired
	private CheckPointDailySelectionTableCache checkPointDailySelectionCache;
	@Autowired
	@Qualifier("stockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	private CommonViewCache commonViewCache;

	private Gson gson = new Gson();

	@GetMapping("/{viewname}")
	@Produces("application/json")
	public String queryDayPriceByIdFromAnalyseViewAtRealTime(@PathParam("viewname") String viewname,
			@Context HttpServletRequest request, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		String date = request.getParameter("date");
		String cixin = request.getParameter("cixin");
		logger.debug("viewName=" + viewname + ",date=" + date + ",cixin=" + cixin);

		if ("luzao_phaseII_zijinliu_top300".equals(viewname) || "luzao_phaseIII_zijinliu_top300".equals(viewname)
				|| "luzao_phaseII_ddx_bigger_05".equals(viewname) || "luzao_phaseIII_ddx_bigger_05".equals(viewname)) {
			// get result from view directory, since they are fast
			String searchViewName = viewname + "_Details";
			List<CommonViewVO> list = this.commonViewCache.queryByDateForViewDirectlySearch(date, searchViewName);

			return gson.toJson(this.fliterCiXinGu(cixin, list));
		}

		// else get result for checkpoint data, since they are analyse daily and
		// save to daily table
		List<CommonViewVO> list = new ArrayList<CommonViewVO>();
		String checkpoint = viewname;
		List<CheckPointDailySelectionVO> cps = checkPointDailySelectionCache.queryByDateAndCheckPoint(date, checkpoint);
		for (CheckPointDailySelectionVO cp : cps) {
			CommonViewVO cvo = new CommonViewVO();
			cvo.stockId = cp.stockId;
			cvo.name = stockConfig.getStockName(cp.stockId);
			cvo.date = date;

			list.add(cvo);
		}

		return gson.toJson(this.fliterCiXinGu(cixin, list));
	}

	// fliter cixin
	private List<CommonViewVO> fliterCiXinGu(String cixin, List<CommonViewVO> originList) {
		if ("True".equalsIgnoreCase(cixin) && originList.size() > 0) {
			List<CommonViewVO> cixinList = new ArrayList<CommonViewVO>();
			int cixinStockLen = config.getInt("cixin_Stock_Length", 86 * 2);

			for (CommonViewVO cvo : originList) {
				int spLength = stockPriceTable.countByStockId(cvo.stockId);
				if (spLength <= cixinStockLen) {
					cixinList.add(cvo);
				}
			}
			return cixinList;
		}

		return originList;
	}
}
