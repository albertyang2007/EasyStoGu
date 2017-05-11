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

import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.db.vo.view.CommonViewVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cache.CheckPointDailySelectionTableCache;
import org.easystogu.cache.CommonViewCache;

public class FavoritesEndPoint {
	private static Logger logger = LogHelper.getLogger(FavoritesEndPoint.class);
	private ConfigurationServiceCache config = ConfigurationServiceCache.getInstance();
	private String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private CheckPointDailySelectionTableCache checkPointDailySelectionCache = CheckPointDailySelectionTableCache
			.getInstance();
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private CommonViewCache commonViewCache = CommonViewCache.getInstance();

	@GET
	@Path("/")
	@Produces("application/json")
	public List<CommonViewVO> queryFavoritesSelectionCheckPoints(@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		String date = request.getParameter("date");
		List<CommonViewVO> list = new ArrayList<CommonViewVO>();
		List<CheckPointDailySelectionVO> cps = checkPointDailySelectionCache.getCheckPointByDate(date);
		for (CheckPointDailySelectionVO cp : cps) {

			// first must meets Week Gordon (macd or kdj)
			if (isWeekGordon(cp)) {
				// seconds must has at least one zijinliu top
				if (isZiJinLiuRu(cps, cp.stockId, date)) {
					CommonViewVO cvo = new CommonViewVO();
					cvo.stockId = cp.stockId;
					cvo.name = stockConfig.getStockName(cp.stockId);
					cvo.date = date;

					list.add(cvo);
				}
			}
		}

		return list;
	}

	private boolean isWeekGordon(CheckPointDailySelectionVO cpvo) {
		if (cpvo.checkPoint.equals("LuZao_PhaseII_MACD_WEEK_GORDON_MACD_DAY_DIF_CROSS_0")
				|| cpvo.checkPoint.equals("LuZao_PhaseIII_MACD_WEEK_GORDON_MACD_DAY_DIF_CROSS_0")
				|| cpvo.checkPoint.equals("LuZao_PhaseII_MACD_WEEK_GORDON_KDJ_WEEK_GORDON")
				|| cpvo.checkPoint.equals("LuZao_PhaseIII_MACD_WEEK_GORDON_KDJ_WEEK_GORDON")) {
			return true;
		}
		return false;
	}

	private boolean isZiJinLiuRu(List<CheckPointDailySelectionVO> cps, String stockId, String date) {
		// first check daily view
		if (this.isZiJinLiuRuAtDate("luzao_phaseII_zijinliu_top300", stockId, date)
				|| this.isZiJinLiuRuAtDate("luzao_phaseIII_zijinliu_top300", stockId, date)
				|| this.isZiJinLiuRuAtDate("luzao_phaseII_ddx_bigger_05", stockId, date)
				|| this.isZiJinLiuRuAtDate("luzao_phaseIII_ddx_bigger_05", stockId, date)) {
			return true;
		}
		// second check recent view
		for (CheckPointDailySelectionVO cp : cps) {
			if (cp.stockId.equals(stockId)) {
				if (cp.checkPoint.equals("luzao_phaseII_ddx_2_of_5_days_bigger_05")
						|| cp.checkPoint.equals("luzao_phaseII_zijinliu_3_days_top300")
						|| cp.checkPoint.equals("luzao_phaseII_zijinliu_3_of_5_days_top300")
						|| cp.checkPoint.equals("luzao_phaseIII_ddx_2_of_5_days_bigger_05")
						|| cp.checkPoint.equals("luzao_phaseIII_zijinliu_3_days_top300")
						|| cp.checkPoint.equals("luzao_phaseIII_zijinliu_3_of_5_days_top300")
						|| cp.checkPoint.equals("zijinliu_3_days_top300")
						|| cp.checkPoint.equals("zijinliu_3_of_5_days_top300")) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isZiJinLiuRuAtDate(String viewName, String stockId, String date) {
		// get result from view directory, since they are fast
		String searchViewName = viewName + "_Details";
		List<CommonViewVO> list = this.commonViewCache.queryByDateForViewDirectlySearch(date, searchViewName);
		for (CommonViewVO cvo : list) {
			if (cvo.date.equals(date) && cvo.stockId.equals(stockId)) {
				return true;
			}
		}
		return false;
	}
}