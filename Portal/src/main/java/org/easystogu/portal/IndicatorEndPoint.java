package org.easystogu.portal;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.db.access.IndKDJTableHelper;
import org.easystogu.db.table.KDJVO;

public class IndicatorEndPoint {
	protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();

	@GET
	@Path("/kdj/{stockid}")
	@Produces( "application/json" )
	public List<KDJVO> queryIndicatorById(@PathParam("stockid") String stockid)
			throws IOException {
		return kdjTable.getNDateKDJ(stockid, 1);
	}
}
