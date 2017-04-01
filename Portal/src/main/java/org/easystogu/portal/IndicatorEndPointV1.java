package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.config.ConfigurationService;
import org.easystogu.config.Constants;
import org.easystogu.cache.StockIndicatorCache;
import org.easystogu.config.DBConfigurationService;
import org.easystogu.db.access.table.IndBollTableHelper;
import org.easystogu.db.access.table.IndDDXTableHelper;
import org.easystogu.db.access.table.IndKDJTableHelper;
import org.easystogu.db.access.table.IndMacdTableHelper;
import org.easystogu.db.access.table.IndQSDDTableHelper;
import org.easystogu.db.access.table.IndShenXianTableHelper;
import org.easystogu.db.access.table.IndWRTableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
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
import org.easystogu.utils.Strings;

import com.google.common.primitives.Doubles;

//V1, query indicator from DB, qian FuQuan (suggest to use this v1)
public class IndicatorEndPointV1 {
	protected static String HHmmss = "00:00:00";
	private ConfigurationService config = DBConfigurationService.getInstance();
	private String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
	protected QianFuQuanStockPriceTableHelper qianfuquanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
	protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
	protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
	protected IndQSDDTableHelper qsddTable = IndQSDDTableHelper.getInstance();
	protected IndWRTableHelper wrTable = IndWRTableHelper.getInstance();
	protected IndDDXTableHelper ddxTable = IndDDXTableHelper.getInstance();
	protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();
	protected LuZaoHelper luzaoHelper = new LuZaoHelper();
	protected ShenXianHelper shenXianHelper = new ShenXianHelper();

	protected StockIndicatorCache indicatorCache = StockIndicatorCache.getInstance();

	protected String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	protected String fromToRegex = dateRegex + "_" + dateRegex;

	@GET
	@Path("/macd/{stockId}/{date}")
	@Produces("application/json")
	public List<MacdVO> queryMACDById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<MacdVO> list = new ArrayList<MacdVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			// return macdTable.getByIdAndBetweenDate(stockIdParm, date1,
			// date2);
			List<Object> cacheSpList = indicatorCache.queryByStockId(Constants.cacheQianFuQuanStockPrice + ":" + stockIdParm);
			for (Object obj : cacheSpList) {
				MacdVO spvo = (MacdVO) obj;
				if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " + HHmmss, spvo.date + " " + HHmmss)) {
					list.add(spvo);
				}
			}
		} else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			list.add(macdTable.getMacd(stockIdParm, dateParm));
		}
		return list;
	}

	@GET
	@Path("/kdj/{stockId}/{date}")
	@Produces("application/json")
	public List<KDJVO> queryKDJById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<KDJVO> list = new ArrayList<KDJVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			// return kdjTable.getByIdAndBetweenDate(stockIdParm, date1, date2);
			List<Object> cacheSpList = indicatorCache.queryByStockId(Constants.cacheIndKDJ + ":" + stockIdParm);
			for (Object obj : cacheSpList) {
				KDJVO spvo = (KDJVO) obj;
				if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " + HHmmss, spvo.date + " " + HHmmss)) {
					list.add(spvo);
				}
			}
		} else if (Pattern.matches(fromToRegex, dateParm) || Strings.isEmpty(dateParm)) {
			list.add(kdjTable.getKDJ(stockIdParm, dateParm));
		}
		return list;
	}

	@GET
	@Path("/boll/{stockId}/{date}")
	@Produces("application/json")
	public List<BollVO> queryBollById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<BollVO> list = new ArrayList<BollVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			// return bollTable.getByIdAndBetweenDate(stockIdParm, date1,
			// date2);
			List<Object> cacheSpList = indicatorCache.queryByStockId(Constants.cacheIndBoll + ":" +stockIdParm);
			for (Object obj : cacheSpList) {
				BollVO spvo = (BollVO) obj;
				if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " + HHmmss, spvo.date + " " + HHmmss)) {
					list.add(spvo);
				}
			}
		} else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			list.add(bollTable.getBoll(stockIdParm, dateParm));
		}
		return list;
	}

	// fetch ind from db directly
	@GET
	@Path("/shenxian/{stockId}/{date}")
	@Produces("application/json")
	public List<ShenXianVO> queryShenXianById(@PathParam("stockId") String stockIdParm,
			@PathParam("date") String dateParm, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<ShenXianVO> list = new ArrayList<ShenXianVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			// return shenXianTable.getByIdAndBetweenDate(stockIdParm, date1,
			// date2);
			List<Object> cacheSpList = indicatorCache.queryByStockId(Constants.cacheIndShenXian + ":" +stockIdParm);
			for (Object obj : cacheSpList) {
				ShenXianVO spvo = (ShenXianVO) obj;
				if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " + HHmmss, spvo.date + " " + HHmmss)) {
					list.add(spvo);
				}
			}
		} else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			list.add(shenXianTable.getShenXian(stockIdParm, dateParm));
		}
		return list;
	}

	// since there is no table to store the HC5, just
	// return empty result.
	@GET
	@Path("/shenxianSell/{stockId}/{date}")
	@Produces("application/json")
	public List<ShenXianVO> queryShenXianSellById(@PathParam("stockId") String stockIdParm,
			@PathParam("date") String dateParm, @Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);

		List<ShenXianVO> list = new ArrayList<ShenXianVO>();
		List<StockPriceVO> spList = this.fetchAllPrices(stockIdParm);
		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		List<Double> high = StockPriceFetcher.getHighPrice(spList);
		double[][] shenXian = shenXianHelper.getShenXianSellPointList(Doubles.toArray(close), Doubles.toArray(high));
		for (int i = 0; i < shenXian[0].length; i++) {
			if (this.isStockDateSelected(dateParm, spList.get(i).date)) {
				ShenXianVO vo = new ShenXianVO();
				vo.setH1(Strings.convert2ScaleDecimal(shenXian[0][i]));
				vo.setH2(Strings.convert2ScaleDecimal(shenXian[1][i]));
				vo.setH3(Strings.convert2ScaleDecimal(shenXian[2][i]));
				vo.setStockId(stockIdParm);
				vo.setDate(spList.get(i).date);
				list.add(vo);
			}
		}

		return list;
	}

	@GET
	@Path("/luzao/{stockId}/{date}")
	@Produces("application/json")
	public List<LuZaoVO> queryLuZaoById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
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

		return list;
	}

	@GET
	@Path("/qsdd/{stockId}/{date}")
	@Produces("application/json")
	public List<QSDDVO> queryQSDDById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<QSDDVO> list = new ArrayList<QSDDVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			// return qsddTable.getByIdAndBetweenDate(stockIdParm, date1,
			// date2);
			List<Object> cacheSpList = indicatorCache.queryByStockId(Constants.cacheIndQSDD + ":" +stockIdParm);
			for (Object obj : cacheSpList) {
				QSDDVO spvo = (QSDDVO) obj;
				if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " + HHmmss, spvo.date + " " + HHmmss)) {
					list.add(spvo);
				}
			}
		} else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			list.add(qsddTable.getQSDD(stockIdParm, dateParm));

		}
		return list;
	}

	@GET
	@Path("/wr/{stockId}/{date}")
	@Produces("application/json")
	public List<WRVO> queryWRById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<WRVO> list = new ArrayList<WRVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			// return wrTable.getByIdAndBetweenDate(stockIdParm, date1, date2);
			List<Object> cacheSpList = indicatorCache.queryByStockId(Constants.cacheIndWR + ":" +stockIdParm);
			for (Object obj : cacheSpList) {
				WRVO spvo = (WRVO) obj;
				if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " + HHmmss, spvo.date + " " + HHmmss)) {
					list.add(spvo);
				}
			}
		} else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			list.add(wrTable.getWR(stockIdParm, dateParm));
		}
		return list;
	}

	@GET
	@Path("/ddx/{stockId}/{date}")
	@Produces("application/json")
	public List<DDXVO> queryDDXById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<DDXVO> list = new ArrayList<DDXVO>();
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			// return ddxTable.getByIdAndBetweenDate(stockIdParm, date1, date2);
			List<Object> cacheSpList = indicatorCache.queryByStockId(Constants.cacheIndDDX + ":" +stockIdParm);
			for (Object obj : cacheSpList) {
				DDXVO spvo = (DDXVO) obj;
				if (Strings.isDateSelected(date1 + " " + HHmmss, date2 + " " + HHmmss, spvo.date + " " + HHmmss)) {
					list.add(spvo);
				}
			}
		} else if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			list.add(ddxTable.getDDX(stockIdParm, dateParm));
		}
		return list;
	}
	
	// common function to fetch price from stockPrice table
	protected List<StockPriceVO> fetchAllPrices(String stockid) {
		List<StockPriceVO> spList = new ArrayList<StockPriceVO>();
		List<Object> cacheSpList = indicatorCache.queryByStockId(Constants.cacheQianFuQuanStockPrice + ":" +stockid);
		for (Object obj : cacheSpList) {
			spList.add((StockPriceVO)obj);
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
