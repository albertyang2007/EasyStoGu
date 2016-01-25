package org.easystogu.portal;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.db.access.IndBollTableHelper;
import org.easystogu.db.access.IndKDJTableHelper;
import org.easystogu.db.access.IndMacdTableHelper;
import org.easystogu.db.access.IndMai1Mai2TableHelper;
import org.easystogu.db.access.IndShenXianTableHelper;
import org.easystogu.db.access.IndXueShi2TableHelper;
import org.easystogu.db.access.IndZhuliJinChuTableHelper;
import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.ShenXianVO;

public class IndicatorEndPoint {
    protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
    protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
    protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
    protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();
    protected IndXueShi2TableHelper xueShi2Table = IndXueShi2TableHelper.getInstance();
    protected IndMai1Mai2TableHelper mai1mai2Table = IndMai1Mai2TableHelper.getInstance();
    protected IndZhuliJinChuTableHelper zhulijinchuTable = IndZhuliJinChuTableHelper.getInstance();

    @GET
    @Path("/macd/{stockid}")
    @Produces("application/json")
    public List<MacdVO> queryMACDById(@PathParam("stockid")
    String stockid) {
        return macdTable.getNDateMacd(stockid, 1);
    }

    @GET
    @Path("/kdj/{stockid}")
    @Produces("application/json")
    public List<KDJVO> queryKDJById(@PathParam("stockid")
    String stockid) {
        return kdjTable.getNDateKDJ(stockid, 1);
    }

    @GET
    @Path("/boll/{stockid}")
    @Produces("application/json")
    public List<BollVO> queryBollById(@PathParam("stockid")
    String stockid) {
        return bollTable.getNDateBoll(stockid, 1);
    }

    @GET
    @Path("/shenxian/{stockid}")
    @Produces("application/json")
    public List<ShenXianVO> queryShenXianById(@PathParam("stockid")
    String stockid) {
        return shenXianTable.getNDateShenXian(stockid, 1);
    }

}
