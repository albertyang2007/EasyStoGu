package org.easystogu.portal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.easystogu.runner.DailySelectionRunner;
import org.easystogu.runner.DailyUpdateOverAllRunner;
import org.easystogu.sina.runner.RealtimeDisplayStockPriceRunner;

@Path("/home")
public class HomeEndPoint {
	@GET
	@Path("/hello")
	public Response test() {
		return Response.ok().build();
	}

	@GET
	@Path("/DailyUpdateOverAllRunner")
	public String dailyUpdateOverAllRunner() {
		DailyUpdateOverAllRunner.main(null);
		return "DailyUpdateOverAllRunner already running, please check folder result.";
	}
	
	@GET
	@Path("/DailySelectionRunner")
	public String dailySelectionRunner() {
		DailySelectionRunner.main(null);
		return "DailySelectionRunner already running, please check folder result.";
	}
	
	@GET
	@Path("/RealtimeDisplayStockPriceRunner")
	public String realtimeDisplayStockPriceRunner() {
		RealtimeDisplayStockPriceRunner.main(null);
		return "RealtimeDisplayStockPriceRunner already running, please check console result.";
	}
}
