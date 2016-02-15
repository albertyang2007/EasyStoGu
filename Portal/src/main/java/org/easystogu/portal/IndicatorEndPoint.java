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
import org.easystogu.db.access.IndQSDDTableHelper;
import org.easystogu.db.access.IndShenXianTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.LuZaoVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.QSDDVO;
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
	protected IndQSDDTableHelper qsddTable = IndQSDDTableHelper.getInstance();
	protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();
	protected ShenXianHelper shenXianHelper = new ShenXianHelper();

	private String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	private String fromToRegex = dateRegex + "_" + dateRegex;

	@GET
	@Path("/macd/{stockId}/{date}")
	@Produces("application/json")
	public List<MacdVO> queryMACDById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm) {
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			return macdTable.getByIdAndBetweenDate(stockIdParm, date1, date2);
		}
		if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			List<MacdVO> list = new ArrayList<MacdVO>();
			list.add(macdTable.getMacd(stockIdParm, dateParm));
			return list;
		}
		return new ArrayList<MacdVO>();
	}

	@GET
	@Path("/kdj/{stockId}/{date}")
	@Produces("application/json")
	public List<KDJVO> queryKDJById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm) {
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			return kdjTable.getByIdAndBetweenDate(stockIdParm, date1, date2);
		}
		if (Pattern.matches(fromToRegex, dateParm) || Strings.isEmpty(dateParm)) {
			List<KDJVO> list = new ArrayList<KDJVO>();
			list.add(kdjTable.getKDJ(stockIdParm, dateParm));
			return list;
		}
		return new ArrayList<KDJVO>();
	}

	@GET
	@Path("/boll/{stockId}/{date}")
	@Produces("application/json")
	public List<BollVO> queryBollById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm) {
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			return bollTable.getByIdAndBetweenDate(stockIdParm, date1, date2);
		}
		if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			List<BollVO> list = new ArrayList<BollVO>();
			list.add(bollTable.getBoll(stockIdParm, dateParm));
			return list;
		}
		return new ArrayList<BollVO>();
	}

	// fetch ind from db directly
	@GET
	@Path("/shenxian/{stockId}/{date}")
	@Produces("application/json")
	public List<ShenXianVO> queryShenXianById(@PathParam("stockId") String stockIdParm,
			@PathParam("date") String dateParm) {
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			return shenXianTable.getByIdAndBetweenDate(stockIdParm, date1, date2);
		}
		if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			List<ShenXianVO> list = new ArrayList<ShenXianVO>();
			list.add(shenXianTable.getShenXian(stockIdParm, dateParm));
			return list;
		}
		return new ArrayList<ShenXianVO>();
	}

	// fetch price from db and count ind on real time
	@GET
	@Path("/shenxian2/{stockId}/{date}")
	@Produces("application/json")
	public List<ShenXianVO> queryShenXian2ById(@PathParam("stockId") String stockIdParm,
			@PathParam("date") String dateParm) {
		List<ShenXianVO> list = new ArrayList<ShenXianVO>();
		List<StockPriceVO> spList = this.fetchAllPrices(stockIdParm);
		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		double[][] shenXian = shenXianHelper.getShenXianList(close.toArray(new Double[0]));
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
	public List<LuZaoVO> queryLuZaoById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm) {
		List<StockPriceVO> spList = this.fetchAllPrices(stockIdParm);
		List<LuZaoVO> list = LuZaoHelper.countAvgMA(spList);
		List<LuZaoVO> subList = new ArrayList<LuZaoVO>();
		for (LuZaoVO vo : list) {
			if (this.isStockDateSelected(dateParm, vo.date)) {
				subList.add(vo);
			}
		}
		Collections.reverse(subList);
		return subList;
	}

	@GET
	@Path("/qsdd/{stockId}/{date}")
	@Produces("application/json")
	public List<QSDDVO> queryQSDDById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm) {
		if (Pattern.matches(fromToRegex, dateParm)) {
			String date1 = dateParm.split("_")[0];
			String date2 = dateParm.split("_")[1];
			return qsddTable.getByIdAndBetweenDate(stockIdParm, date1, date2);
		}
		if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
			List<QSDDVO> list = new ArrayList<QSDDVO>();
			list.add(qsddTable.getQSDD(stockIdParm, dateParm));
			return list;
		}
		return new ArrayList<QSDDVO>();
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
