package org.easystogu.portal;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import net.sf.json.JSONArray;

import org.easystogu.trendmode.loader.ModeLoader;
import org.easystogu.trendmode.vo.TrendModeVO;

public class TrendModeEndPoint {
    private ModeLoader modeLoader = ModeLoader.getInstance();

    @GET
    @Path("/query/{name}")
    @Produces("application/json")
    public String queryTrendModeByName(@PathParam("name")
    String name) {
        TrendModeVO tmVO = modeLoader.loadTrendMode(name);
        return JSONArray.fromObject(tmVO.prices).toString();
    }

    @GET
    @Path("/listnames")
    @Produces("application/json")
    public List<String> queryAllTrendModeNames() {
        return modeLoader.getAllNames();
    }
}
