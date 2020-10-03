package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cache.StockIndicatorCache;
import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.IndDDXTableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.BollVO;
import org.easystogu.db.vo.table.DDXVO;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.db.vo.table.LuZaoVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.db.vo.table.QSDDVO;
import org.easystogu.db.vo.table.ShenXianVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.WRVO;
import org.easystogu.indicator.LuZaoHelper;
import org.easystogu.indicator.ShenXianHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.portal.vo.ShenXianUIVO;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.primitives.Doubles;
import com.google.gson.Gson;

//V1, query indicator from DB, qian FuQuan (suggest to use this v1)

@RestController
@RequestMapping(value = "/indv1")
public class IndicatorEndPointV1 {
	protected static String HHmmss = "00:00:00";
	private ConfigurationServiceCache config = ConfigurationServiceCache.getInstance();
	protected String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
	protected QianFuQuanStockPriceTableHelper qianfuquanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	@Autowired
	private DBAccessFacdeFactory dBAccessFacdeFactory;
	protected IndicatorDBHelperIF kdjTable = dBAccessFacdeFactory.getInstance(Constants.indKDJ);
	protected IndicatorDBHelperIF macdTable = dBAccessFacdeFactory.getInstance(Constants.indMacd);
	protected IndicatorDBHelperIF bollTable = dBAccessFacdeFactory.getInstance(Constants.indBoll);
	protected IndicatorDBHelperIF qsddTable = dBAccessFacdeFactory.getInstance(Constants.indQSDD);
	protected IndicatorDBHelperIF wrTable = dBAccessFacdeFactory.getInstance(Constants.indWR);
	protected IndicatorDBHelperIF shenXianTable = dBAccessFacdeFactory.getInstance(Constants.indShenXian);
	@Autowired
	protected IndDDXTableHelper ddxTable;
	protected StockIndicatorCache indicatorCache = StockIndicatorCache.getInstance();

	@Autowired
	protected LuZaoHelper luzaoHelper;
	@Autowired
	protected ShenXianHelper shenXianHelper;

	protected String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	protected String fromToRegex = dateRegex + "_" + dateRegex;

	private Gson gson = new Gson();

	@GetMapping("/macd/{stockId}/{date}")
	@Produces("application/json")
	public String queryMACDById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<MacdVO> list = new ArrayList<MacdVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			return gson.toJson(macdTable.getByIdAndBetweenDate(stockIdParm, date1, date2));
			// List<Object> cacheSpList =
			// indicatorCache.queryByStockId(Constants.cacheIndMacd + ":" +
			// stockIdParm);
			// for (Object obj : cacheSpList) {
			// MacdVO spvo = (MacdVO) obj;
			// if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " +
			// HHmmss, spvo.date + " " + HHmmss)) {
			// list.add(spvo);
			// }
			// }
		} else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			list.add((MacdVO) macdTable.getSingle(stockIdParm, dateParm));
		}
		return gson.toJson(list);
	}

	@GetMapping("/kdj/{stockId}/{date}")
	@Produces("application/json")
	public String queryKDJById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<KDJVO> list = new ArrayList<KDJVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			return gson.toJson(kdjTable.getByIdAndBetweenDate(stockIdParm, date1, date2));
			// List<Object> cacheSpList =
			// indicatorCache.queryByStockId(Constants.cacheIndKDJ + ":" +
			// stockIdParm);
			// for (Object obj : cacheSpList) {
			// KDJVO spvo = (KDJVO) obj;
			// if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " +
			// HHmmss, spvo.date + " " + HHmmss)) {
			// list.add(spvo);
			// }
			// }
		} else if (Pattern.matches(fromToRegex, dateParm) || Strings.isEmpty(dateParm)) {
			list.add((KDJVO) kdjTable.getSingle(stockIdParm, dateParm));
		}
		return gson.toJson(list);
	}

	@GetMapping("/boll/{stockId}/{date}")
	@Produces("application/json")
	public String queryBollById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<BollVO> list = new ArrayList<BollVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			return gson.toJson(bollTable.getByIdAndBetweenDate(stockIdParm, date1, date2));
			// List<Object> cacheSpList =
			// indicatorCache.queryByStockId(Constants.cacheIndBoll + ":" +
			// stockIdParm);
			// for (Object obj : cacheSpList) {
			// BollVO spvo = (BollVO) obj;
			// if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " +
			// HHmmss, spvo.date + " " + HHmmss)) {
			// list.add(spvo);
			// }
			// }
		} else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			list.add((BollVO) bollTable.getSingle(stockIdParm, dateParm));
		}
		return gson.toJson(list);
	}

	// fetch ind from db directly
	@GetMapping("/shenxian/{stockId}/{date}")
	@Produces("application/json")
	public String queryShenXianById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<ShenXianVO> list = new ArrayList<ShenXianVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			return gson.toJson(shenXianTable.getByIdAndBetweenDate(stockIdParm, date1, date2));
			// List<Object> cacheSpList =
			// indicatorCache.queryByStockId(Constants.cacheIndShenXian + ":" +
			// stockIdParm);
			// for (Object obj : cacheSpList) {
			// ShenXianVO spvo = (ShenXianVO) obj;
			// if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " +
			// HHmmss, spvo.date + " " + HHmmss)) {
			// list.add(spvo);
			// }
			// }
		} else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			list.add((ShenXianVO) shenXianTable.getSingle(stockIdParm, dateParm));
		}
		return gson.toJson(list);
	}

	// since there is no table to store the HC5, just
	// return empty result.
	@GetMapping("/shenxianSell/{stockId}/{date}")
	@Produces("application/json")
	public String queryShenXianSellById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);

		List<ShenXianUIVO> list = new ArrayList<ShenXianUIVO>();
		List<StockPriceVO> spList = this.fetchAllPrices(stockIdParm);
		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		List<Double> high = StockPriceFetcher.getHighPrice(spList);
		List<Double> low = StockPriceFetcher.getLowPrice(spList);
		double[][] shenXian = shenXianHelper.getShenXianSellPointList(Doubles.toArray(close), Doubles.toArray(high),
				Doubles.toArray(low));
		for (int i = 0; i < shenXian[0].length; i++) {
			if (this.isStockDateSelected(dateParm, spList.get(i).date)) {
				ShenXianUIVO vo = new ShenXianUIVO();
				vo.setH1(Strings.convert2ScaleDecimal(shenXian[0][i]));
				vo.setH2(Strings.convert2ScaleDecimal(shenXian[1][i]));
				vo.setHc5(Strings.convert2ScaleDecimal(shenXian[2][i]));
				vo.setHc6(Strings.convert2ScaleDecimal(shenXian[3][i]));
				vo.setStockId(stockIdParm);
				vo.setDate(spList.get(i).date);
				list.add(vo);
			}
		}

		return gson.toJson(list);
	}

	@GetMapping("/luzao/{stockId}/{date}")
	@Produces("application/json")
	public String queryLuZaoById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<LuZaoVO> list = new ArrayList<LuZaoVO>();
		List<StockPriceVO> spList = this.fetchAllPrices(stockIdParm);
		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		double[][] lz = luzaoHelper.getLuZaoList(Doubles.toArray(close));
		for (int i = 0; i < lz[0].length; i++) {
			if (this.isStockDateSelected(dateParm, spList.get(i).date)) {
				LuZaoVO vo = new LuZaoVO();
				vo.setMa19(Strings.convert2ScaleDecimal(lz[0][i]));
				vo.setMa43(Strings.convert2ScaleDecimal(lz[1][i]));
				vo.setMa86(Strings.convert2ScaleDecimal(lz[2][i]));
				vo.setStockId(stockIdParm);
				vo.setDate(spList.get(i).date);
				list.add(vo);
			}
		}

		return gson.toJson(list);
	}

	@GetMapping("/qsdd/{stockId}/{date}")
	@Produces("application/json")
	public String queryQSDDById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<QSDDVO> list = new ArrayList<QSDDVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			return gson.toJson(qsddTable.getByIdAndBetweenDate(stockIdParm, date1, date2));
			// List<Object> cacheSpList =
			// indicatorCache.queryByStockId(Constants.cacheIndQSDD + ":" +
			// stockIdParm);
			// for (Object obj : cacheSpList) {
			// QSDDVO spvo = (QSDDVO) obj;
			// if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " +
			// HHmmss, spvo.date + " " + HHmmss)) {
			// list.add(spvo);
			// }
			// }
		} else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			list.add((QSDDVO) qsddTable.getSingle(stockIdParm, dateParm));

		}
		return gson.toJson(list);
	}

	@GetMapping("/wr/{stockId}/{date}")
	@Produces("application/json")
	public String queryWRById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<WRVO> list = new ArrayList<WRVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			return gson.toJson(wrTable.getByIdAndBetweenDate(stockIdParm, date1, date2));
			// List<Object> cacheSpList =
			// indicatorCache.queryByStockId(Constants.cacheIndWR + ":" +
			// stockIdParm);
			// for (Object obj : cacheSpList) {
			// WRVO spvo = (WRVO) obj;
			// if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " +
			// HHmmss, spvo.date + " " + HHmmss)) {
			// list.add(spvo);
			// }
			// }
		} else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			list.add((WRVO) wrTable.getSingle(stockIdParm, dateParm));
		}
		return gson.toJson(list);
	}

	@GetMapping("/ddx/{stockId}/{date}")
	@Produces("application/json")
	public String queryDDXById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<DDXVO> list = new ArrayList<DDXVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			return gson.toJson(ddxTable.getByIdAndBetweenDate(stockIdParm, date1, date2));
			// List<Object> cacheSpList =
			// indicatorCache.queryByStockId(Constants.cacheIndDDX + ":" +
			// stockIdParm);
			// for (Object obj : cacheSpList) {
			// DDXVO spvo = (DDXVO) obj;
			// if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " +
			// HHmmss, spvo.date + " " + HHmmss)) {
			// list.add(spvo);
			// }
			// }
		} else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			list.add(ddxTable.getDDX(stockIdParm, dateParm));
		}
		return gson.toJson(list);
	}

	// common function to fetch price from stockPrice table
	protected List<StockPriceVO> fetchAllPrices(String stockid) {
		List<StockPriceVO> spList = new ArrayList<StockPriceVO>();
		List<StockPriceVO> cacheSpList = indicatorCache
				.queryByStockId(Constants.cacheQianFuQuanStockPrice + ":" + stockid);
		for (Object obj : cacheSpList) {
			spList.add((StockPriceVO) obj);
		}
		return spList;
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
