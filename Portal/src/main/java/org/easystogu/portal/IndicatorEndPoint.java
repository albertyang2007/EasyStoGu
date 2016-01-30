package org.easystogu.portal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndBollTableHelper;
import org.easystogu.db.access.IndKDJTableHelper;
import org.easystogu.db.access.IndMacdTableHelper;
import org.easystogu.db.access.IndShenXianTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.LuZaoVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.ShenXianVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.LuZaoHelper;
import org.easystogu.indicator.ShenXianHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.utils.Strings;

public class IndicatorEndPoint {
	private static String HHmmss = "00:00:00";
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
	protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
	protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
	protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();
	protected ShenXianHelper shenXianHelper = new ShenXianHelper();

	private String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private String fromToRegex = dateRegex + "_" + dateRegex;

	@GET
	@Path("/macd/{stockid}/{date}")
	@Produces("application/json")
	public List<MacdVO> queryMACDById(@PathParam("stockid") String stockid, @PathParam("date") String date) {
		if (Pattern.matches(fromToRegex, date)) {
			String date1 = date.split("_")[0];
			String date2 = date.split("_")[1];
			return macdTable.getByIdAndBetweenDate(stockid, date1, date2);
		}
		if (Pattern.matches(dateRegex, date) || Strings.isEmpty(date)) {
			List<MacdVO> list = new ArrayList<MacdVO>();
			list.add(macdTable.getMacd(stockid, date));
			return list;
		}
		return new ArrayList<MacdVO>();
	}

	@GET
	@Path("/kdj/{stockid}/{date}")
	@Produces("application/json")
	public List<KDJVO> queryKDJById(@PathParam("stockid") String stockid, @PathParam("date") String date) {
		if (Pattern.matches(fromToRegex, date)) {
			String date1 = date.split("_")[0];
			String date2 = date.split("_")[1];
			return kdjTable.getByIdAndBetweenDate(stockid, date1, date2);
		}
		if (Pattern.matches(fromToRegex, date) || Strings.isEmpty(date)) {
			List<KDJVO> list = new ArrayList<KDJVO>();
			list.add(kdjTable.getKDJ(stockid, date));
			return list;
		}
		return new ArrayList<KDJVO>();
	}

	@GET
	@Path("/boll/{stockid}/{date}")
	@Produces("application/json")
	public List<BollVO> queryBollById(@PathParam("stockid") String stockid, @PathParam("date") String date) {
		if (Pattern.matches(fromToRegex, date)) {
			String date1 = date.split("_")[0];
			String date2 = date.split("_")[1];
			return bollTable.getByIdAndBetweenDate(stockid, date1, date2);
		}
		if (Pattern.matches(dateRegex, date) || Strings.isEmpty(date)) {
			List<BollVO> list = new ArrayList<BollVO>();
			list.add(bollTable.getBoll(stockid, date));
			return list;
		}
		return new ArrayList<BollVO>();
	}

	// fetch ind from db directly
	@GET
	@Path("/shenxian/{stockid}/{date}")
	@Produces("application/json")
	public List<ShenXianVO> queryShenXianById(@PathParam("stockid") String stockid, @PathParam("date") String date) {
		if (Pattern.matches(fromToRegex, date)) {
			String date1 = date.split("_")[0];
			String date2 = date.split("_")[1];
			return shenXianTable.getByIdAndBetweenDate(stockid, date1, date2);
		}
		if (Pattern.matches(dateRegex, date) || Strings.isEmpty(date)) {
			List<ShenXianVO> list = new ArrayList<ShenXianVO>();
			list.add(shenXianTable.getShenXian(stockid, date));
			return list;
		}
		return new ArrayList<ShenXianVO>();
	}

	// fetch price from db and count ind on real time
	@GET
	@Path("/shenxian2/{stockid}/{date}")
	@Produces("application/json")
	public List<ShenXianVO> queryShenXian2ById(@PathParam("stockid") String stockid, @PathParam("date") String date) {
		List<ShenXianVO> list = new ArrayList<ShenXianVO>();
		List<StockPriceVO> spList = this.fetchAllPrices(stockid);
		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		double[][] shenXian = shenXianHelper.getShenXianList(close.toArray(new Double[0]));
		for (int i = 0; i < shenXian[0].length; i++) {
			if (this.isStockDateSelected(date, spList.get(i).date)) {
				ShenXianVO vo = new ShenXianVO();
				vo.setH1(Strings.convert2ScaleDecimal(shenXian[0][i]));
				vo.setH2(Strings.convert2ScaleDecimal(shenXian[1][i]));
				vo.setH3(Strings.convert2ScaleDecimal(shenXian[2][i]));
				vo.setStockId(stockid);
				vo.setDate(spList.get(i).date);
				list.add(vo);
			}
		}

		return list;
	}

	@GET
	@Path("/luzao/{stockid}/{date}")
	@Produces("application/json")
	public List<LuZaoVO> queryLuZaoById(@PathParam("stockid") String stockid, @PathParam("date") String date) {
		List<StockPriceVO> spList = this.fetchAllPrices(stockid);
		List<LuZaoVO> list = LuZaoHelper.countAvgMA(spList);
		List<LuZaoVO> subList = new ArrayList<LuZaoVO>();
		for (LuZaoVO vo : list) {
			if (this.isStockDateSelected(date, vo.date)) {
				subList.add(vo);
			}
		}
		Collections.reverse(subList);
		return subList;
	}

	// common function to fetch price from stockPrice table
	// date: xxxx-xx-xx or xxxx-xx-xx_xxxx-xx-xx
	private List<StockPriceVO> fetchAllPrices(String stockid) {
		List<StockPriceVO> spList = null;
		spList = stockPriceTable.getStockPriceById(stockid);
		// update price based on chuQuanChuXi event
		chuQuanChuXiPriceHelper.updatePrice(stockid, spList);
		return spList;
	}

	private boolean isStockDateSelected(String date, String aDate) {
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
