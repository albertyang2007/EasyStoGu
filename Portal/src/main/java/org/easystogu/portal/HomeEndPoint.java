package org.easystogu.portal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.easystogu.easymoney.runner.DailyZiJinLiuRunner;
import org.easystogu.runner.DailySelectionRunner;
import org.easystogu.runner.DailyUpdateAllStockRunner;
import org.easystogu.runner.DailyOverAllRunner;
import org.easystogu.runner.DailyUpdateEstimateStockRunner;
import org.easystogu.runner.PreEstimateStockPriceRunner;
import org.easystogu.sina.runner.RealtimeDisplayStockPriceRunner;

public class HomeEndPoint {
	@GET
	@Path("/hello")
	public Response test() {
		return Response.ok().entity("Welcome easystogu").build();
	}
	
	@GET
	@Path("/DailyUpdateEstimateStockRunner")
	public String dailyUpdateEstimateStockRunner() {
		Thread t = new Thread(new DailyUpdateEstimateStockRunner());
		t.start();
		return "DailyUpdateEstimateStockRunner already running, please check DB result.";
	}

	@GET
	@Path("/DailyUpdateAllStockRunner")
	public String dailyUpdateOverAllRunner() {
		Thread t = new Thread(new DailyUpdateAllStockRunner());
		t.start();
		return "DailyUpdateAllStockRunner already running, please check folder result.";
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
	@Path("/DailyOverAllRunner")
	public String dailyOverAllRunner() {
		Thread t = new Thread(new DailyOverAllRunner());
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
		Thread t = new Thread(new DailyZiJinLiuRunner());
		t.start();
		return "DailyZiJinLiuXiangRunner already running, please check DB result.";
	}
}
