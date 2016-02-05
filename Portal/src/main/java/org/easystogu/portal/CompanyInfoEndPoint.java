package org.easystogu.portal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.db.table.CompanyInfoVO;
import org.easystogu.file.access.CompanyInfoFileHelper;

public class CompanyInfoEndPoint {
    private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();

    @GET
    @Path("/{stockId}")
    @Produces("application/json")
    public CompanyInfoVO getByStockId(@PathParam("stockId")
    String stockId) {
        return stockConfig.getByStockId(stockId);
    }

    @GET
    @Path("/name={name}")
    @Produces("application/json")
    public CompanyInfoVO getByName(@PathParam("name")
    String name) {
        return stockConfig.getByStockName(name);
    }
}
