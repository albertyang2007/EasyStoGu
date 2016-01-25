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
import org.easystogu.db.access.IndShenXianTableHelper;
import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.ShenXianVO;
import org.easystogu.utils.Strings;

public class IndicatorEndPoint {
    protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
    protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
    protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
    protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();

    private String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
    private String fromToRegex = dateRegex + "_" + dateRegex;

    @GET
    @Path("/macd/{stockid}/{date}")
    @Produces("application/json")
    public List<MacdVO> queryMACDById(@PathParam("stockid")
    String stockid, @PathParam("date")
    String date) {
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
        return null;
    }

    @GET
    @Path("/kdj/{stockid}/{date}")
    @Produces("application/json")
    public List<KDJVO> queryKDJById(@PathParam("stockid")
    String stockid, @PathParam("date")
    String date) {
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
        return null;
    }

    @GET
    @Path("/boll/{stockid}/{date}")
    @Produces("application/json")
    public List<BollVO> queryBollById(@PathParam("stockid")
    String stockid, @PathParam("date")
    String date) {
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
        return null;
    }

    @GET
    @Path("/shenxian/{stockid}/{date}")
    @Produces("application/json")
    public List<ShenXianVO> queryShenXianById(@PathParam("stockid")
    String stockid, @PathParam("date")
    String date) {
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
        return null;
    }
}
