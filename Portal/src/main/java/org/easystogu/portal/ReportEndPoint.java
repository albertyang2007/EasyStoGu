package org.easystogu.portal;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.db.access.view.FavoritesStockCheckpointViewHelper;
import org.easystogu.db.vo.view.FavoritesStockCheckpointVO;
import org.easystogu.utils.WeekdayUtil;

public class ReportEndPoint {
    private ConfigurationServiceCache config = ConfigurationServiceCache.getInstance();
    private FavoritesStockCheckpointViewHelper favoritesStockCheckpointViewHelper = FavoritesStockCheckpointViewHelper
            .getInstance();
    protected String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");

    @GET
    @Path("/{DateOffset}")
    //@Produces("application/json")
    //DateOffset is like 0,1,2,3 means today, yesterday, ...
    public String queryReportByDate(@PathParam("DateOffset") String dateOffset, @Context HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
        String date = WeekdayUtil.nextNDate(WeekdayUtil.currentDate(), Integer.parseInt(dateOffset));
        List<FavoritesStockCheckpointVO> list = favoritesStockCheckpointViewHelper.getByDateAndUserId(date, "admin");
        StringBuffer rtn = new StringBuffer();
        for (FavoritesStockCheckpointVO vo : list) {
            rtn.append(vo.toString() + "<br>");
        }
        return rtn.toString();
    }
}
