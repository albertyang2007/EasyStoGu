package org.easystogu.portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.cache.CheckPointDailySelectionTableCache;
import org.easystogu.cache.CommonViewCache;
import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cache.StockPriceCache;
import org.easystogu.config.Constants;
import org.easystogu.db.access.table.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.table.FavoritesStockHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.db.vo.view.CommonViewVO;
import org.easystogu.db.vo.view.FavoritesStockVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;

public class FavoritesEndPoint {
	private static Logger logger = LogHelper.getLogger(FavoritesEndPoint.class);
	private ConfigurationServiceCache config = ConfigurationServiceCache.getInstance();
	private String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private CheckPointDailySelectionTableCache checkPointDailySelectionCache = CheckPointDailySelectionTableCache
			.getInstance();
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTableHelper = CheckPointDailySelectionTableHelper
			.getInstance();
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private CommonViewCache commonViewCache = CommonViewCache.getInstance();
	private FavoritesStockHelper favoritesStockHelper = FavoritesStockHelper.getInstance();
	private StockPriceCache stockPriceCache = StockPriceCache.getInstance();

	@GET
	@Path("/")
	@Produces("application/json")
	public List<CommonViewVO> queryFavoritesSelectionCheckPoints(@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		String date = request.getParameter("date");
		String isZiXuanGu = request.getParameter("isZiXuanGu");
		List<CommonViewVO> list = new ArrayList<CommonViewVO>();
		List<CheckPointDailySelectionVO> cps = null;

		// get the latest N date: Latest_50
		if (date != null && date.contains("Latest_")) {
			String limitNumber = date.split("_")[1];
			List<String> latestNDate = stockPriceCache.get(Constants.cacheLatestNStockDate + ":" + limitNumber);
			if (latestNDate != null && latestNDate.size() > 0) {
				date = latestNDate.get(latestNDate.size() - 1);
			}

			// get all checkpoint that in latest N date
			cps = checkPointDailySelectionTableHelper.getRecentDaysCheckPoint(date);

		} else {
			// fetch the checkpoint by specify date
			cps = checkPointDailySelectionCache.getCheckPointByDate(date);
		}

		List<FavoritesStockVO> favoritesStockIds = null;

		Map<String, List<String>> selectedStockIds = new HashMap<String, List<String>>();

		// only select the stockId that is in ZiXuanGu (favorites stockids)
		if ("true".equalsIgnoreCase(isZiXuanGu)) {
			// here hardcode userId to admin since there is no other customer
			favoritesStockIds = favoritesStockHelper.getByUserId("admin");
			cps = filterZiXuanGu(favoritesStockIds, cps);
		}

		for (CheckPointDailySelectionVO cp : cps) {
			if (this.isWeekGordon(cp)) {
				if (!selectedStockIds.containsKey(cp.stockId)) {
					List<String> cpList = new ArrayList<String>();
					cpList.add(cp.checkPoint);
					selectedStockIds.put(cp.stockId, cpList);
				} else {
					List<String> cpList = selectedStockIds.get(cp.stockId);
					cpList.add(cp.checkPoint);

					// more than one gordon occurs in one date, then put it to return list
					if (!this.isStockIdExist(list, cp.stockId)) {
						CommonViewVO cvo = new CommonViewVO();
						cvo.stockId = cp.stockId;
						cvo.name = stockConfig.getStockName(cp.stockId);
						cvo.date = date;

						list.add(cvo);
					}
				}
			}

			// first must meets Week Gordon (macd or kdj)
			// if (isWeekGordon(cp)) {
			// // seconds must has at least one zijinliu top
			// if (isZiJinLiuRu(cps, cp.stockId, date)) {
			// CommonViewVO cvo = new CommonViewVO();
			// cvo.stockId = cp.stockId;
			// cvo.name = stockConfig.getStockName(cp.stockId);
			// cvo.date = date;

			// list.add(cvo);
			// }
			// }
		}

		return list;
	}

	@POST
	@Path("/{userId}/{stockId}")
	@Produces("application/json")
	public List<FavoritesStockVO> addToFavorites(@PathParam("userId") String userIdParm,
			@PathParam("stockId") String stockIdParm, String postBody, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		favoritesStockHelper.insert(new FavoritesStockVO(stockIdParm, userIdParm));
		List<FavoritesStockVO> rtn = favoritesStockHelper.getByUserId(userIdParm);
		for (FavoritesStockVO vo : rtn) {
			vo.setName(stockConfig.getByStockId(vo.stockId).name);
		}
		return rtn;
	}

	@DELETE
	@Path("/{userId}/{stockId}")
	@Produces("application/json")
	public List<FavoritesStockVO> deleteFromFavorites(@PathParam("userId") String userIdParm,
			@PathParam("stockId") String stockIdParm, String postBody, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		favoritesStockHelper.delete(stockIdParm, userIdParm);
		List<FavoritesStockVO> rtn = favoritesStockHelper.getByUserId(userIdParm);
		for (FavoritesStockVO vo : rtn) {
			vo.setName(stockConfig.getByStockId(vo.stockId).name);
		}
		return rtn;
	}

	@GET
	@Path("/{userId}")
	@Produces("application/json")
	public List<FavoritesStockVO> getFavorites(@PathParam("userId") String userIdParm, String postBody,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<FavoritesStockVO> rtn = favoritesStockHelper.getByUserId(userIdParm);
		for (FavoritesStockVO vo : rtn) {
			vo.setName(stockConfig.getByStockId(vo.stockId).name);
		}
		return rtn;
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

	private boolean isInZiXuanGu(List<FavoritesStockVO> favoritesStockIds, String stockId) {
		for (FavoritesStockVO fsvo : favoritesStockIds) {
			if (fsvo.stockId.equals(stockId)) {
				return true;
			}
		}
		return false;
	}

	private List<CheckPointDailySelectionVO> filterZiXuanGu(List<FavoritesStockVO> favoritesStockIds,
			List<CheckPointDailySelectionVO> cps) {
		List<CheckPointDailySelectionVO> rtn = new ArrayList<CheckPointDailySelectionVO>();
		for (CheckPointDailySelectionVO cp : cps) {
			if (this.isInZiXuanGu(favoritesStockIds, cp.stockId)) {
				rtn.add(cp);
			}
		}
		return rtn;
	}

	private boolean isStockIdExist(List<CommonViewVO> list, String stockId) {
		for (CommonViewVO vo : list) {
			if (vo.stockId.equals(stockId)) {
				return true;
			}
		}
		return false;
	}
}
