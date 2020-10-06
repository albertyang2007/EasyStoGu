package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.db.vo.table.BBIVO;
import org.easystogu.db.vo.table.BollVO;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.db.vo.table.LuZaoVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.db.vo.table.QSDDVO;
import org.easystogu.db.vo.table.ShenXianVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.WRVO;
import org.easystogu.indicator.BBIHelper;
import org.easystogu.indicator.BOLLHelper;
import org.easystogu.indicator.KDJHelper;
import org.easystogu.indicator.LuZaoHelper;
import org.easystogu.indicator.MACDHelper;
import org.easystogu.indicator.QSDDHelper;
import org.easystogu.indicator.ShenXianHelper;
import org.easystogu.indicator.WRHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.portal.init.TrendModeLoader;
import org.easystogu.portal.vo.ShenXianUIVO;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.primitives.Doubles;
import com.google.gson.Gson;

//V3, with forecast data, query from qian fuquan stock price and count in real time

@RestController
@RequestMapping(value = "/portal/indv3")
public class IndicatorEndPointV3 {
	protected static String HHmmss = "00:00:00";
	@Autowired
	private ConfigurationServiceCache config ;
	@Autowired
	protected ProcessRequestParmsInPostBody postParmsProcess;
	@Autowired
	protected TrendModeLoader trendModeLoader;
	@Autowired
	protected FlagsAnalyseHelper flagsAnalyseHelper;
	
	protected MACDHelper macdHelper = new MACDHelper();
	protected KDJHelper kdjHelper = new KDJHelper();
	protected ShenXianHelper shenXianHelper = new ShenXianHelper();
	protected QSDDHelper qsddHelper = new QSDDHelper();
	protected WRHelper wrHelper = new WRHelper();
	protected BOLLHelper bollHelper = new BOLLHelper();
	protected BBIHelper bbiHelper = new BBIHelper();
	protected LuZaoHelper luzaoHelper = new LuZaoHelper();

	private Gson gson = new Gson();

	@PostMapping("/macd/{stockId}/{date}")
	@Produces("application/json")
	public String queryMACDById(@PathVariable("stockId") String stockIdParm, @PathVariable("date") String dateParm,
			String postBody, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<MacdVO> list = new ArrayList<MacdVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		double[][] macd = macdHelper.getMACDList(Doubles.toArray(close));
		for (int i = 0; i < macd[0].length; i++) {
			if (postParmsProcess.isStockDateSelected(postBody, dateParm, spList.get(i).date)) {
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

	@PostMapping("/kdj/{stockId}/{date}")
	@Produces("application/json")
	public String queryKDJById(@PathVariable("stockId") String stockIdParm, @PathVariable("date") String dateParm,
			String postBody, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<KDJVO> list = new ArrayList<KDJVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		List<Double> low = StockPriceFetcher.getLowPrice(spList);
		List<Double> high = StockPriceFetcher.getHighPrice(spList);
		double[][] kdj = kdjHelper.getKDJList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));
		for (int i = 0; i < kdj[0].length; i++) {
			if (postParmsProcess.isStockDateSelected(postBody, dateParm, spList.get(i).date)) {
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

	@PostMapping("/boll/{stockId}/{date}")
	@Produces("application/json")
	public String queryBollById(@PathVariable("stockId") String stockIdParm, @PathVariable("date") String dateParm,
			String postBody, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<BollVO> list = new ArrayList<BollVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		double[][] boll = bollHelper.getBOLLList(Doubles.toArray(close), 20, 2.0, 2.0);
		for (int i = 0; i < boll[0].length; i++) {
			if (postParmsProcess.isStockDateSelected(postBody, dateParm, spList.get(i).date)) {
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

	@PostMapping("/shenxian/{stockId}/{date}")
	@Produces("application/json")
	public String queryShenXianById(@PathVariable("stockId") String stockIdParm, @PathVariable("date") String dateParm,
			String postBody, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<ShenXianVO> list = new ArrayList<ShenXianVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		double[][] shenXian = shenXianHelper.getShenXianList(Doubles.toArray(close));
		for (int i = 0; i < shenXian[0].length; i++) {
			if (postParmsProcess.isStockDateSelected(postBody, dateParm, spList.get(i).date)) {
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

	@PostMapping("/shenxianSell/{stockId}/{date}")
	@Produces("application/json")
	public String queryShenXianSellById(@PathVariable("stockId") String stockIdParm, @PathVariable("date") String dateParm,
			String postBody, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);

		List<ShenXianUIVO> sxList = new ArrayList<ShenXianUIVO>();
		List<MacdVO> macdList = new ArrayList<MacdVO>();
		List<BBIVO> bbiList = new ArrayList<BBIVO>();
		List<LuZaoVO> luzaoList = new ArrayList<LuZaoVO>();

		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);
		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		List<Double> high = StockPriceFetcher.getHighPrice(spList);
		List<Double> low = StockPriceFetcher.getLowPrice(spList);

		// shenxian
		double[][] shenXian = shenXianHelper.getShenXianSellPointList(Doubles.toArray(close), Doubles.toArray(high),
				Doubles.toArray(low));
		for (int i = 0; i < shenXian[0].length; i++) {
			if (postParmsProcess.isStockDateSelected(postBody, dateParm, spList.get(i).date)) {
				ShenXianUIVO vo = new ShenXianUIVO();
				vo.setH1(Strings.convert2ScaleDecimal(shenXian[0][i]));
				vo.setH2(Strings.convert2ScaleDecimal(shenXian[1][i]));
				vo.setHc5(Strings.convert2ScaleDecimal(shenXian[2][i]));
				vo.setHc6(Strings.convert2ScaleDecimal(shenXian[3][i]));
				vo.setStockId(stockIdParm);
				vo.setDate(spList.get(i).date);
				sxList.add(vo);
			}
		}

		// macd
		double[][] macd = macdHelper.getMACDList(Doubles.toArray(close));
		for (int i = 0; i < macd[0].length; i++) {
			if (postParmsProcess.isStockDateSelected(postBody, dateParm, spList.get(i).date)) {
				MacdVO vo = new MacdVO();
				vo.setDif(macd[0][i]);
				vo.setDea(macd[1][i]);
				vo.setMacd(macd[2][i]);
				vo.setStockId(stockIdParm);
				vo.setDate(spList.get(i).date);
				macdList.add(vo);
			}
		}

		// bbi
		double[][] bbi = bbiHelper.getBBIList(Doubles.toArray(close));
		for (int i = 0; i < bbi[0].length; i++) {
			if (postParmsProcess.isStockDateSelected(postBody, dateParm, spList.get(i).date)) {
				BBIVO vo = new BBIVO();
				vo.setBbi(bbi[0][i]);
				vo.setClose(bbi[1][i]);
				vo.setStockId(stockIdParm);
				vo.setDate(spList.get(i).date);
				bbiList.add(vo);
			}
		}

		// luzhao
		double[][] lz = luzaoHelper.getLuZaoList(Doubles.toArray(close));
		for (int i = 0; i < lz[0].length; i++) {
			if (postParmsProcess.isStockDateSelected(postBody, dateParm, spList.get(i).date)) {
				LuZaoVO vo = new LuZaoVO();
				vo.setMa19(lz[0][i]);
				vo.setMa43(lz[1][i]);
				vo.setMa86(lz[2][i]);
				vo.setStockId(stockIdParm);
				vo.setDate(spList.get(i).date);
				luzaoList.add(vo);
			}
		}

		return gson
				.toJson(flagsAnalyseHelper.shenXianBuySellFlagsAnalyse(spList, sxList, macdList, bbiList, luzaoList));
	}

	@PostMapping("/luzao/{stockId}/{date}")
	@Produces("application/json")
	public String queryLuZaoById(@PathVariable("stockId") String stockIdParm, @PathVariable("date") String dateParm,
			String postBody, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<LuZaoVO> list = new ArrayList<LuZaoVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		double[][] lz = luzaoHelper.getLuZaoList(Doubles.toArray(close));
		for (int i = 0; i < lz[0].length; i++) {
			if (postParmsProcess.isStockDateSelected(postBody, dateParm, spList.get(i).date)) {
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

	@PostMapping("/qsdd/{stockId}/{date}")
	@Produces("application/json")
	public String queryQSDDById(@PathVariable("stockId") String stockIdParm, @PathVariable("date") String dateParm,
			String postBody, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<QSDDVO> list = new ArrayList<QSDDVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		List<Double> low = StockPriceFetcher.getLowPrice(spList);
		List<Double> high = StockPriceFetcher.getHighPrice(spList);
		double[][] qsdd = qsddHelper.getQSDDList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));
		for (int i = 0; i < qsdd[0].length; i++) {
			if (postParmsProcess.isStockDateSelected(postBody, dateParm, spList.get(i).date)) {
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

	@PostMapping("/wr/{stockId}/{date}")
	@Produces("application/json")
	public String queryWRById(@PathVariable("stockId") String stockIdParm, @PathVariable("date") String dateParm,
			String postBody, @Context HttpServletResponse response) {
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<WRVO> list = new ArrayList<WRVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		List<Double> low = StockPriceFetcher.getLowPrice(spList);
		List<Double> high = StockPriceFetcher.getHighPrice(spList);
		double[][] wr = wrHelper.getWRList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high), 19, 43,
				86);
		for (int i = 0; i < wr[0].length; i++) {
			if (postParmsProcess.isStockDateSelected(postBody, dateParm, spList.get(i).date)) {
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
}
