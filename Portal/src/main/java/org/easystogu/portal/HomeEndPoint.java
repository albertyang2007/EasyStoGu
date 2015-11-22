package org.easystogu.portal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.easystogu.easymoney.runner.DailyZiJinLiuRunner;
import org.easystogu.runner.DailyOverAllRunner;
import org.easystogu.runner.DailySelectionRunner;
import org.easystogu.runner.DailyUpdateAllStockRunner;
import org.easystogu.runner.DailyUpdateEstimateStockRunner;
import org.easystogu.runner.DataBaseSanityCheck;
import org.easystogu.runner.PreEstimateStockPriceRunner;
import org.easystogu.runner.RecentlySelectionRunner;
import org.easystogu.sina.runner.RealtimeDisplayStockPriceRunner;

public class HomeEndPoint {
	@GET
	@Path("/")
	public Response test() {
		StringBuffer sb = new StringBuffer();
		sb.append("<a href='DailyUpdateAllStockRunner'>DailyUpdateAllStockRunner</a><br>");
		sb.append("<a href='DailyUpdateEstimateStockRunner'>DailyUpdateEstimateStockRunner</a><br>");
		sb.append("<a href='PreEstimateStockPriceRunner'>PreEstimateStockPriceRunner</a><br>");
		sb.append("<a href='DailyOverAllRunner'>DailyOverAllRunner</a><br>");
		sb.append("<a href='FastDailyOverAllRunner'>FastDailyOverAllRunner</a><br>");
		sb.append("<a href='DailySelectionRunner'>DailySelectionRunner</a><br>");
		sb.append("<a href='RealtimeDisplayStockPriceRunner'>RealtimeDisplayStockPriceRunner</a><br>");
		sb.append("<a href='DailyZiJinLiuXiangRunner'>DailyZiJinLiuXiangRunner</a><br>");
		sb.append("<a href='DataBaseSanityCheck'>DataBaseSanityCheck</a><br>");
		sb.append("<a href='RecentlySelectionRunner'>RecentlySelectionRunner</a><br>");
		return Response.ok().entity(sb.toString()).build();
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
		boolean isGetZiJinLiu = true;
		Thread t = new Thread(new DailyUpdateAllStockRunner(isGetZiJinLiu));
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

	@GET
	@Path("/DataBaseSanityCheck")
	public String dataBaseSanityCheck() {
		Thread t = new Thread(new DataBaseSanityCheck());
		t.start();
		return "DataBaseSanityCheck already running, please check DB result.";
	}

	@GET
	@Path("/DailyOverAllRunner")
	public String dailyOverAllRunner() {
		boolean isGetZiJinLiu = true;
		Thread t = new Thread(new DailyOverAllRunner(isGetZiJinLiu));
		t.start();
		return "DailyOverAllRunner already running, please check DB result.";
	}

	@GET
	@Path("/FastDailyOverAllRunner")
	public String fastDailyOverAllRunner() {
		boolean isGetZiJinLiu = false;
		Thread t = new Thread(new DailyOverAllRunner(isGetZiJinLiu));
		t.start();
		return "FastDailyOverAllRunner already running, please check DB result.";
	}

	@GET
	@Path("/RecentlySelectionRunner")
	public String recentlySelectionRunner() {
		Thread t = new Thread(new RecentlySelectionRunner());
		t.start();
		return "RecentlySelectionRunner already running, please check DB result.";
	}
}
