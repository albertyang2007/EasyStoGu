package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.config.ConfigurationService;
import org.easystogu.config.DBConfigurationService;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.portal.init.TrendModeLoader;
import org.easystogu.trendmode.vo.SimplePriceVO;
import org.easystogu.trendmode.vo.TrendModeVO;
import org.easystogu.utils.Strings;
import org.easystogu.utils.WeekdayUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class TrendModeEndPoint {
	private ConfigurationService config = DBConfigurationService.getInstance();
	private String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
	@Autowired
	private TrendModeLoader modeLoader;

	@GET
	@Path("/query/{name}")
	@Produces("application/json")
	public List<StockPriceVO> queryTrendModeByName(@PathParam("name") String name,
			@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StockPriceVO> spList = new ArrayList<StockPriceVO>();
		TrendModeVO tmo = modeLoader.loadTrendMode(name);
		if (tmo == null)
			return spList;
		List<String> nextWorkingDateList = WeekdayUtil.nextWorkingDateList(WeekdayUtil.currentDate(),
				tmo.prices.size());
		StockPriceVO curSPVO = StockPriceVO.createDefaulyVO();
		for (int i = 0; i < tmo.prices.size(); i++) {
			SimplePriceVO svo = tmo.prices.get(i);
			StockPriceVO spvo = new StockPriceVO();
			spvo.setDate(nextWorkingDateList.get(i));
			spvo.setStockId(name);
			spvo.setLastClose(curSPVO.close);
			spvo.setOpen(Strings.convert2ScaleDecimal(spvo.lastClose * (1.0 + svo.getOpen() / 100.0)));
			spvo.setClose(Strings.convert2ScaleDecimal(spvo.lastClose * (1.0 + svo.getClose() / 100.0)));
			spvo.setLow(Strings.convert2ScaleDecimal(spvo.lastClose * (1.0 + svo.getLow() / 100.0)));
			spvo.setHigh(Strings.convert2ScaleDecimal(spvo.lastClose * (1.0 + svo.getHigh() / 100.0)));
			spvo.setVolume((long) (curSPVO.volume * svo.getVolume()));

			spList.add(spvo);
			curSPVO = spvo;
		}
		return spList;
	}

	@GET
	@Path("/listnames")
	@Produces("application/json")
	public List<String> queryAllTrendModeNames(@Context HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		return modeLoader.getAllNames();
	}
}
