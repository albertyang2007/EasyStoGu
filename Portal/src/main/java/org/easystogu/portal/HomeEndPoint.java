package org.easystogu.portal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.easystogu.easymoney.runner.DailyZiJinLiuXiangRunner;
import org.easystogu.runner.DailySelectionRunner;
import org.easystogu.runner.DailyUpdateOverAllRunner;
import org.easystogu.runner.PreEstimateStockPriceRunner;
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
	@Path("/PreEstimateStockPriceRunner")
	public String preEstimateStockPriceRunner() {
		Thread t = new Thread(new PreEstimateStockPriceRunner());
		t.start();
		return "PreEstimateStockPriceRunner already running, please check folder result.";
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
	
	@GET
    @Path("/DailyZiJinLiuXiangRunner")
    public String dailyZiJinLiuXiangRunner() {
	    Thread t = new Thread(new DailyZiJinLiuXiangRunner());
        t.start();
        return "DailyZiJinLiuXiangRunner already running, please check DB result.";
    }
}
