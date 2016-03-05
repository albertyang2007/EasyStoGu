package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.db.table.StockPriceVO;
import org.easystogu.trendmode.loader.ModeLoader;
import org.easystogu.trendmode.vo.SimplePriceVO;
import org.easystogu.trendmode.vo.TrendModeVO;
import org.easystogu.utils.Strings;
import org.easystogu.utils.WeekdayUtil;

public class TrendModeEndPoint {
    private ModeLoader modeLoader = ModeLoader.getInstance();

    @GET
    @Path("/query/{name}")
    @Produces("application/json")
    public List<StockPriceVO> queryTrendModeByName(@PathParam("name")
    String name) {
        List<StockPriceVO> spList = new ArrayList<StockPriceVO>();
        TrendModeVO tmo = modeLoader.loadTrendMode(name);
        List<String> nextWorkingDateList = WeekdayUtil
                .nextWorkingDateList(WeekdayUtil.currentDate(), tmo.prices.size());
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
    public List<String> queryAllTrendModeNames() {
        return modeLoader.getAllNames();
    }
}
