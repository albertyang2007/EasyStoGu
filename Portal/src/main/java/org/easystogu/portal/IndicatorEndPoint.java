package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

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
import org.easystogu.indicator.LuZaoHelper;
import org.easystogu.utils.Strings;

//V1, query indicator from DB
public class IndicatorEndPoint {
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
	protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
	protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
	protected IndQSDDTableHelper qsddTable = IndQSDDTableHelper.getInstance();
	protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();
	protected LuZaoHelper luzaoHelper = new LuZaoHelper();

	protected String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	protected String fromToRegex = dateRegex + "_" + dateRegex;

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

	@GET
	@Path("/luzao/{stockId}/{date}")
	@Produces("application/json")
	public List<LuZaoVO> queryLuZaoById(@PathParam("stockId") String stockIdParm, @PathParam("date") String dateParm) {
		List<LuZaoVO> list = new ArrayList<LuZaoVO>();
		return list;
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
}
