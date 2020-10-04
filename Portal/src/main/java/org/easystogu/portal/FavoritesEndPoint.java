package org.easystogu.portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.cache.CheckPointDailySelectionTableCache;
import org.easystogu.cache.CommonViewCache;
import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cache.FavoritesCache;
import org.easystogu.cache.StockPriceCache;
import org.easystogu.config.Constants;
import org.easystogu.db.access.table.FavoritesStockHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.db.vo.table.CompanyInfoVO;
import org.easystogu.db.vo.view.CommonViewVO;
import org.easystogu.db.vo.view.FavoritesStockVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@RequestMapping(value = "/favorites")
public class FavoritesEndPoint {
	private static Logger logger = LogHelper.getLogger(FavoritesEndPoint.class);
	@Autowired
	private ConfigurationServiceCache config;
	@Autowired
	private CompanyInfoFileHelper stockConfig;
	@Autowired
	private CheckPointDailySelectionTableCache checkPointDailySelectionCache;
	@Autowired
	@Qualifier("stockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	private CommonViewCache commonViewCache;
	@Autowired
	private FavoritesStockHelper favoritesStockHelper;
	@Autowired
	private FavoritesCache favoritesCache;
	@Autowired
	private StockPriceCache stockPriceCache;

	private Gson gson = new Gson();

	@GetMapping("/")
	@Produces("application/json")
	public String queryFavoritesSelectionCheckPoints(@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		String date = request.getParameter("date");
		String isZiXuanGu = request.getParameter("isZiXuanGu");
		String viewName = request.getParameter("viewName");

		if ("MACD_WK_GORDON_KDJ_DIF_DAY_GORDON".equalsIgnoreCase(viewName)) {
			return gson.toJson(getSelectView1(date, isZiXuanGu));
		}

		if ("MACD_KDJ_WK_GORDON_ZIJIN_LIURU".equalsIgnoreCase(viewName)) {
			return gson.toJson(getSelectView2(date, isZiXuanGu));
		}

		return "{}";
	}

	@GetMapping("/{userId}/{stockId}")
	@Produces("application/json")
	public void addToFavorites(@PathParam("userId") String userIdParm, @PathParam("stockId") String stockIdParm,
			String postBody, @Context HttpServletResponse response) {
		// response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		favoritesStockHelper.insert(new FavoritesStockVO(stockIdParm, userIdParm));
		favoritesCache.refreshAll();
	}

	@DeleteMapping("/{userId}/{stockId}")
	@Produces("application/json")
	public void deleteFromFavorites(@PathParam("userId") String userIdParm, @PathParam("stockId") String stockIdParm,
			String postBody, @Context HttpServletResponse response) {
		// response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		favoritesStockHelper.delete(stockIdParm, userIdParm);
		favoritesCache.refreshAll();
	}

	@GetMapping("/{userId}")
	@Produces("application/json")
	public String getFavorites(@PathParam("userId") String userIdParm, String postBody,
			@Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<FavoritesStockVO> rtn = favoritesCache.get(userIdParm);
		for (FavoritesStockVO vo : rtn) {
			CompanyInfoVO cvo = stockConfig.getByStockId(vo.stockId);
			if (cvo != null) {
				vo.setName(cvo.name);
			}
		}
		return gson.toJson(rtn);
	}

	private List<CommonViewVO> getSelectView1(String date, String isZiXuanGu) {
		List<CommonViewVO> list = new ArrayList<CommonViewVO>();
		List<CheckPointDailySelectionVO> cps = null;
		List<FavoritesStockVO> favoritesStockIds = null;
		Map<String, List<String>> selectedStockIds = new HashMap<String, List<String>>();

		// get the latest N date: Latest_50
		if (date != null && date.contains("Latest_")) {
			String limitNumber = date.split("_")[1];
			List<String> latestNDate = stockPriceCache.get(Constants.cacheLatestNStockDate + ":" + limitNumber);
			if (latestNDate != null && latestNDate.size() > 0) {
				date = latestNDate.get(latestNDate.size() - 1);
			}

			// get all checkpoint that in latest N date
			cps = checkPointDailySelectionCache.getRecentDaysCheckPoint(date);

		} else {
			// fetch the checkpoint by specify date
			cps = checkPointDailySelectionCache.getCheckPointByDate(date);
		}

		// only select the stockId that is in ZiXuanGu (favorites stockids)
		if ("true".equalsIgnoreCase(isZiXuanGu)) {
			// here hardcode userId to admin since there is no other customer
			favoritesStockIds = favoritesCache.get("admin");
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
		}

		return list;
	}

	private List<CommonViewVO> getSelectView2(String date, String isZiXuanGu) {
		List<CommonViewVO> list = new ArrayList<CommonViewVO>();
		List<CheckPointDailySelectionVO> cps = null;
		List<FavoritesStockVO> favoritesStockIds = null;
		Map<String, String> selectedStockIds = new HashMap<String, String>();

		// get the latest N date: Latest_50
		if (date != null && date.contains("Latest_")) {
			String limitNumber = date.split("_")[1];
			List<String> latestNDate = stockPriceCache.get(Constants.cacheLatestNStockDate + ":" + limitNumber);
			if (latestNDate != null && latestNDate.size() > 0) {
				date = latestNDate.get(latestNDate.size() - 1);
			}
			// get all checkpoint that in latest N date
			cps = checkPointDailySelectionCache.getRecentDaysCheckPoint(date);
		} else {
			// fetch the checkpoint by specify date
			cps = checkPointDailySelectionCache.getCheckPointByDate(date);
		}

		// only select the stockId that is in ZiXuanGu (favorites stockids)
		if ("true".equalsIgnoreCase(isZiXuanGu)) {
			// here hardcode userId to admin since there is no other customer
			favoritesStockIds = favoritesCache.get("admin");
			cps = filterZiXuanGu(favoritesStockIds, cps);
		}

		for (CheckPointDailySelectionVO cp : cps) {
			// first must meets Week Gordon (macd or kdj)
			if (isWeekGordon(cp)) {
				// seconds must has at least one zijinliu top
				if (isZiJinLiuRu(cps, cp.stockId, date)) {

					if (!selectedStockIds.containsKey(cp.stockId)) {
						selectedStockIds.put(cp.stockId, cp.stockId);

						CommonViewVO cvo = new CommonViewVO();
						cvo.stockId = cp.stockId;
						cvo.name = stockConfig.getStockName(cp.stockId);
						cvo.date = date;

						list.add(cvo);
					}
				}
			}
		}

		return list;
	}

	private boolean isWeekGordon(CheckPointDailySelectionVO cpvo) {
		if (cpvo.checkPoint.equalsIgnoreCase("LuZao_PhaseII_MACD_WEEK_GORDON_MACD_DAY_DIF_CROSS_0")
				|| cpvo.checkPoint.equalsIgnoreCase("LuZao_PhaseIII_MACD_WEEK_GORDON_MACD_DAY_DIF_CROSS_0")
				|| cpvo.checkPoint.equalsIgnoreCase("LuZao_PhaseII_MACD_WEEK_GORDON_KDJ_WEEK_GORDON")
				|| cpvo.checkPoint.equalsIgnoreCase("LuZao_PhaseIII_MACD_WEEK_GORDON_KDJ_WEEK_GORDON")) {
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
