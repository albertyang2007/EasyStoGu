package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.LuZaoVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.QSDDVO;
import org.easystogu.db.table.ShenXianVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.BOLLHelper;
import org.easystogu.indicator.KDJHelper;
import org.easystogu.indicator.LuZaoHelper;
import org.easystogu.indicator.MACDHelper;
import org.easystogu.indicator.QSDDHelper;
import org.easystogu.indicator.ShenXianHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.primitives.Doubles;

//V3, with forecast data, query from qian fuquan stock price and count in real time
public class IndicatorEndPointV3 {
	protected static String HHmmss = "00:00:00";
	protected MACDHelper macdHelper = new MACDHelper();
	protected KDJHelper kdjHelper = new KDJHelper();
	protected ShenXianHelper shenXianHelper = new ShenXianHelper();
	protected QSDDHelper qsddHelper = new QSDDHelper();
	protected BOLLHelper bollHelper = new BOLLHelper();
	protected LuZaoHelper luzaoHelper = new LuZaoHelper();
	@Autowired
	protected ProcessRequestParmsInPostBody postParmsProcess;
	protected String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	protected String fromToRegex = dateRegex + "_" + dateRegex;

	@POST
	@Path("/macd/{stockId}/{date}")
	@Produces("application/json")
	public List<MacdVO> queryMACDById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			String postBody) {
		List<MacdVO> list = new ArrayList<MacdVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

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
		return list;
	}

	@POST
	@Path("/kdj/{stockId}/{date}")
	@Produces("application/json")
	public List<KDJVO> queryKDJById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			String postBody) {
		List<KDJVO> list = new ArrayList<KDJVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

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
		return list;
	}

	@POST
	@Path("/boll/{stockId}/{date}")
	@Produces("application/json")
	public List<BollVO> queryBollById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			String postBody) {
		List<BollVO> list = new ArrayList<BollVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

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

		return list;
	}

	@POST
	@Path("/shenxian/{stockId}/{date}")
	@Produces("application/json")
	public List<ShenXianVO> queryShenXian2ById(@PathParam("stockId") String stockIdParm,
			@PathParam("date") String dateParm, String postBody) {
		List<ShenXianVO> list = new ArrayList<ShenXianVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

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

		return list;
	}

	@POST
	@Path("/luzao/{stockId}/{date}")
	@Produces("application/json")
	public List<LuZaoVO> queryLuZaoById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			String postBody) {
		List<LuZaoVO> list = new ArrayList<LuZaoVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

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

	@POST
	@Path("/qsdd/{stockId}/{date}")
	@Produces("application/json")
	public List<QSDDVO> queryQSDDById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm,
			String postBody) {
		List<QSDDVO> list = new ArrayList<QSDDVO>();
		List<StockPriceVO> spList = postParmsProcess.updateStockPriceAccordingToRequest(stockIdParm, postBody);

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

		return list;
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
