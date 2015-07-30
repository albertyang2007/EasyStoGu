package org.easystogu.portal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.easystogu.runner.DailySelectionRunner;
import org.easystogu.runner.DailyUpdateOverAllRunner;
import org.easystogu.sina.runner.RealtimeDisplayStockPriceRunner;

public class HomeEndPoint {
	@GET
	@Path("/hello")
	public Response test() {
		return Response.ok().entity("Welcome easystogu").build();
	}

	@GET
	@Path("/DailyUpdateOverAllRunner")
	public String dailyUpdateOverAllRunner() {
		Thread t = new Thread(new DailyUpdateOverAllRunner());
		t.start();
		return "DailyUpdateOverAllRunner already running, please check folder result.";
	}

	@GET
	@Path("/DailySelectionRunner")
	public String dailySelectionRunner() {
		Thread t = new Thread(new DailySelectionRunner());
		t.start();
		return "DailySelectionRunner already running, please check folder result.";
	}

	@GET
	@Path("/RealtimeDisplayStockPriceRunner")
	public String realtimeDisplayStockPriceRunner() {
		return new RealtimeDisplayStockPriceRunner().printRealTimeOutput();
	}
}
