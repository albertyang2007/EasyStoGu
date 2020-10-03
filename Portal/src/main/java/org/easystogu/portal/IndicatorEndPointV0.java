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
import org.easystogu.db.access.table.IndDDXTableHelper;
import org.easystogu.db.vo.table.BollVO;
import org.easystogu.db.vo.table.DDXVO;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.db.vo.table.LuZaoVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.db.vo.table.QSDDVO;
import org.easystogu.db.vo.table.ShenXianVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.WRVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.BOLLHelper;
import org.easystogu.indicator.KDJHelper;
import org.easystogu.indicator.LuZaoHelper;
import org.easystogu.indicator.MACDHelper;
import org.easystogu.indicator.QSDDHelper;
import org.easystogu.indicator.ShenXianHelper;
import org.easystogu.indicator.WRHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.portal.vo.ShenXianUIVO;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.primitives.Doubles;
import com.google.gson.Gson;

//V0, query stockprice (no chuquan) and count in real time

@RestController
@RequestMapping(value = "/indv0")
public class IndicatorEndPointV0 {
	@Autowired
	private ConfigurationServiceCache config;
	protected String accessControlAllowOrgin = "*";//config.getString("Access-Control-Allow-Origin", "");
	protected static String HHmmss = "00:00:00";
	@Autowired
	protected CompanyInfoFileHelper companyInfoHelper;
	@Autowired
	protected StockIndicatorCache indicatorCache;
	@Autowired
	protected IndDDXTableHelper ddxTable;

	@Autowired
	protected MACDHelper macdHelper;
	@Autowired
	protected KDJHelper kdjHelper;
	@Autowired
	protected ShenXianHelper shenXianHelper;
	@Autowired
	protected QSDDHelper qsddHelper;
	@Autowired
	protected BOLLHelper bollHelper;
	@Autowired
	protected LuZaoHelper luzaoHelper;
	@Autowired
	protected WRHelper wrHelper;

	protected String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	protected String fromToRegex = dateRegex + "_" + dateRegex;

	private Gson gson = new Gson();

	@GetMapping("/macd/{stockId}/{date}")
	@Produces("application/json")
	public String queryMACDById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<MacdVO> list = new ArrayList<MacdVO>();
		List<StockPriceVO> spList = this.fetchAllPrices(stockIdParm);
		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		double[][] macd = macdHelper.getMACDList(Doubles.toArray(close));
		for (int i = 0; i < macd[0].length; i++) {
			if (this.isStockDateSelected(dateParm, spList.get(i).date)) {
				MacdVO vo = new MacdVO();
				vo.setDif(Strings.convert2ScaleDecimal(macd[0][i]));
				vo.setDea(Strings.convert2ScaleDecimal(macd[1][i]));
				vo.setMacd(Strings.convert2ScaleDecimal(macd[2][i]));
				vo.setStockId(stockIdParm);
				vo.setDate(spList.get(i).date);
				list.add(vo);
			}
		}
		return gson.toJson(list);
	}

	@GetMapping("/kdj/{stockId}/{date}")
	@Produces("application/json")
	public String queryKDJById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<KDJVO> list = new ArrayList<KDJVO>();
		List<StockPriceVO> spList = this.fetchAllPrices(stockIdParm);
		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		List<Double> low = StockPriceFetcher.getLowPrice(spList);
		List<Double> high = StockPriceFetcher.getHighPrice(spList);
		double[][] kdj = kdjHelper.getKDJList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));
		for (int i = 0; i < kdj[0].length; i++) {
			if (this.isStockDateSelected(dateParm, spList.get(i).date)) {
				KDJVO vo = new KDJVO();
				vo.setK(Strings.convert2ScaleDecimal(kdj[0][i]));
				vo.setD(Strings.convert2ScaleDecimal(kdj[1][i]));
				vo.setJ(Strings.convert2ScaleDecimal(kdj[2][i]));
				vo.setRsv(Strings.convert2ScaleDecimal(kdj[3][i]));
				vo.setStockId(stockIdParm);
				vo.setDate(spList.get(i).date);
				list.add(vo);
			}
		}
		return gson.toJson(list);
	}

	@GetMapping("/boll/{stockId}/{date}")
	@Produces("application/json")
	public String queryBollById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<BollVO> list = new ArrayList<BollVO>();
		List<StockPriceVO> spList = this.fetchAllPrices(stockIdParm);
		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		double[][] boll = bollHelper.getBOLLList(Doubles.toArray(close), 20, 2.0, 2.0);
		for (int i = 0; i < boll[0].length; i++) {
			if (this.isStockDateSelected(dateParm, spList.get(i).date)) {
				BollVO vo = new BollVO();
				vo.setUp(Strings.convert2ScaleDecimal(boll[0][i]));
				vo.setMb(Strings.convert2ScaleDecimal(boll[1][i]));
				vo.setDn(Strings.convert2ScaleDecimal(boll[2][i]));
				vo.setStockId(stockIdParm);
				vo.setDate(spList.get(i).date);
				list.add(vo);
			}
		}

		return gson.toJson(list);
	}

	@GetMapping("/shenxian/{stockId}/{date}")
	@Produces("application/json")
	public String queryShenXianById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<ShenXianVO> list = new ArrayList<ShenXianVO>();
		List<StockPriceVO> spList = this.fetchAllPrices(stockIdParm);
		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		double[][] shenXian = shenXianHelper.getShenXianList(Doubles.toArray(close));
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

		return gson.toJson(list);
	}

	// h3 is replaced by HC5
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
		List<StockPriceVO> spList = this.fetchAllPrices(stockIdParm);
		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		List<Double> low = StockPriceFetcher.getLowPrice(spList);
		List<Double> high = StockPriceFetcher.getHighPrice(spList);
		double[][] qsdd = qsddHelper.getQSDDList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));
		for (int i = 0; i < qsdd[0].length; i++) {
			if (this.isStockDateSelected(dateParm, spList.get(i).date)) {
				QSDDVO vo = new QSDDVO();
				vo.setLonTerm(Strings.convert2ScaleDecimal(qsdd[0][i]));
				vo.setShoTerm(Strings.convert2ScaleDecimal(qsdd[1][i]));
				vo.setMidTerm(Strings.convert2ScaleDecimal(qsdd[2][i]));
				vo.setStockId(stockIdParm);
				vo.setDate(spList.get(i).date);
				list.add(vo);
			}
		}

		return gson.toJson(list);
	}

	@GetMapping("/wr/{stockId}/{date}")
	@Produces("application/json")
	public String queryWRById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<WRVO> list = new ArrayList<WRVO>();
		List<StockPriceVO> spList = this.fetchAllPrices(stockIdParm);
		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		List<Double> low = StockPriceFetcher.getLowPrice(spList);
		List<Double> high = StockPriceFetcher.getHighPrice(spList);
		double[][] wr = wrHelper.getWRList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high), 19, 43,
				86);
		for (int i = 0; i < wr[0].length; i++) {
			if (this.isStockDateSelected(dateParm, spList.get(i).date)) {
				WRVO vo = new WRVO();
				vo.setLonTerm(Strings.convert2ScaleDecimal(wr[0][i]));
				vo.setShoTerm(Strings.convert2ScaleDecimal(wr[1][i]));
				vo.setMidTerm(Strings.convert2ScaleDecimal(wr[2][i]));
				vo.setStockId(stockIdParm);
				vo.setDate(spList.get(i).date);
				list.add(vo);
			}
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
		List<StockPriceVO> cacheSpList = indicatorCache.queryByStockId(Constants.cacheStockPrice + ":" + stockid);
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
