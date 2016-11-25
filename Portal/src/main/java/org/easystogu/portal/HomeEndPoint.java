package org.easystogu.portal;

import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.easystogu.easymoney.runner.DailyDDXRunner;
import org.easystogu.easymoney.runner.DailyZhuLiJingLiuRuRunner;
import org.easystogu.easymoney.runner.DailyZiJinLiuRunner;
import org.easystogu.easymoney.runner.OverAllZiJinLiuAndDDXRunner;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.runner.DailyOverAllRunner;
import org.easystogu.runner.DailySelectionRunner;
import org.easystogu.runner.DailyUpdateAllStockRunner;
import org.easystogu.runner.DailyUpdateStockPriceAndIndicatorRunner;
import org.easystogu.runner.DailyViewAnalyseRunner;
import org.easystogu.runner.DataBaseSanityCheck;
import org.easystogu.runner.RecentlySelectionRunner;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner2;
import org.easystogu.sina.runner.RealtimeDisplayStockPriceRunner;
import org.easystogu.sina.runner.history.StockPriceHistoryOverAllRunner;

public class HomeEndPoint {
	protected String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	protected String fromToRegex = dateRegex + "_" + dateRegex;

	@GET
	@Path("/")
	public Response test() {
		StringBuffer sb = new StringBuffer();
		sb.append("<a href='/portal/home/DailyUpdateAllStockRunner'>DailyUpdateAllStockRunner</a><br>");
		sb.append("<a href='/portal/home/DailyOverAllRunner'>DailyOverAllRunner</a><br>");
		sb.append(
				"<a href='/portal/home/DailyUpdatePriceAndIndicatorRunner'>DailyUpdatePriceAndIndicatorRunner</a><br>");
		sb.append("<a href='/portal/home/FastDailyOverAllRunner'>FastDailyOverAllRunner</a><br>");
		sb.append("<a href='/portal/home/DailySelectionRunner'>DailySelectionRunner</a><br>");
		sb.append("<a href='/portal/home/RealtimeDisplayStockPriceRunner'>RealtimeDisplayStockPriceRunner</a><br>");
		sb.append("<a href='/portal/home/DailyZiJinLiuRunner'>DailyZiJinLiuRunner</a><br>");
		sb.append("<a href='/portal/home/DailyZiJinLiuRunnerForAllStockId'>DailyZiJinLiuRunnerForAllStock</a><br>");
		sb.append("<a href='/portal/home/OverAllZiJinLiuAndDDXRunner'>OverAllZiJinLiuAndDDXRunner</a><br>");
		sb.append(
				"<a href='/portal/home/OverAllZiJinLiuAndDDXRunnerForAllStockId'>OverAllZiJinLiuAndDDXRunnerForAllStockId</a><br>");
		sb.append("<a href='/portal/home/DailyDDXRunner'>DailyDDXRunner</a><br>");
		sb.append("<a href='/portal/home/DailyViewAnalyseRunner'>DailyViewAnalyseRunner</a><br>");
		sb.append("<a href='/portal/home/DailyZiJinLiuXiangRunner'>DailyZiJinLiuXiangRunner</a><br>");
		sb.append("<a href='/portal/home/DataBaseSanityCheck'>DataBaseSanityCheck</a><br>");
		sb.append("<a href='/portal/home/RecentlySelectionRunner'>RecentlySelectionRunner</a><br>");
		sb.append("<a href='/portal/home/DownloadStockPrice'>DownloadStockPrice</a><br>");
		sb.append("<a href='/portal/home/UpdateCompanyFromFileToDB'>UpdateCompanyFromFileToDB</a><br>");
		sb.append(
				"<a href='/portal/home/updateStockPriceHistoryOverAllRunner/2016-10-17_2016-11-23'>updateStockPriceHistoryOverAllRunnerFromStartDate</a><br>");
		return Response.ok().entity(sb.toString()).build();
	}

	@GET
	@Path("/DailyUpdateAllStockRunner")
	public String dailyUpdateOverAllRunner() {
		boolean isGetZiJinLiu = false;
		Thread t = new Thread(new DailyUpdateAllStockRunner(isGetZiJinLiu));
		t.start();
		return "DailyUpdateAllStockRunner already running, please check folder result.";
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
	@Produces("text/html; charset=UTF-8")
	public String realtimeDisplayStockPriceRunner() {
		return new RealtimeDisplayStockPriceRunner().printRealTimeOutput();
	}

	@GET
	@Path("/DailyZiJinLiuRunner")
	public String dailyZiJinLiuRunner() {
		Thread t = new Thread(new DailyZiJinLiuRunner());
		t.start();
		return "DailyZiJinLiuRunner already running, please check DB result.";
	}

	@GET
	@Path("/OverAllZiJinLiuAndDDXRunner")
	public String overAllZiJinLiuAndDDXRunner() {
		Thread t = new Thread(new OverAllZiJinLiuAndDDXRunner());
		t.start();
		return "OverAllZiJinLiuAndDDXRunner already running, please check DB result.";
	}

	@GET
	@Path("/OverAllZiJinLiuAndDDXRunnerForAllStockId")
	public String overAllZiJinLiuAndDDXRunnerForAllStockId() {
		OverAllZiJinLiuAndDDXRunner runner = new OverAllZiJinLiuAndDDXRunner();
		runner.resetToAllPage();
		Thread t = new Thread(runner);
		t.start();
		return "OverAllZiJinLiuAndDDXRunnerForAllStockId already running, please check DB result.";
	}

	@GET
	@Path("/DailyZiJinLiuRunnerForAllStockId")
	public String dailyZiJinLiuRunnerForAllStockId() {
		DailyZiJinLiuRunner runner = new DailyZiJinLiuRunner();
		runner.resetToAllPage();
		Thread t = new Thread(runner);
		t.start();
		return "DailyZiJinLiuRunner already running, please check DB result.";
	}

	@GET
	@Path("/DailyZhuLiJingLiuRuRunner")
	public String dailyZhuLiJingLiuRuRunner() {
		Thread t = new Thread(new DailyZhuLiJingLiuRuRunner());
		t.start();
		return "DailyZhuLiJingLiuRuRunner already running, please check DB result.";
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

	@GET
	@Path("/DailyUpdatePriceAndIndicatorRunner")
	public String dailyUpdatePriceAndIndicatorRunner() {
		Thread t = new Thread(new DailyUpdateStockPriceAndIndicatorRunner());
		t.start();
		return "DailyUpdatePriceAndIndicatorRunner already running, please check DB result.";
	}

	@GET
	@Path("/DailyDDXRunner")
	public String dailyDDXRunner() {
		Thread t = new Thread(new DailyDDXRunner());
		t.start();
		return "DailyDDXRunner already running, please check DB result.";
	}

	@GET
	@Path("/DailyViewAnalyseRunner")
	public String dailyViewAnalyseRunner() {
		Thread t = new Thread(new DailyViewAnalyseRunner());
		t.start();
		return "DailyViewAnalyseRunner already running, please check folder result.";
	}

	@GET
	@Path("/DownloadStockPrice")
	public String downloadStockPrice() {
		// day (download all stockIds price)
		DailyStockPriceDownloadAndStoreDBRunner2 runner = new DailyStockPriceDownloadAndStoreDBRunner2();
		Thread t = new Thread(runner);
		t.start();
		return "DailyStockPriceDownloadAndStoreDBRunner2 already running, please check folder result.";
	}

	@GET
	@Path("/UpdateCompanyFromFileToDB")
	public String updateCompanyFromFileToDB() {
		// update the total GuBen and LiuTong GuBen
		CompanyInfoFileHelper ins = new CompanyInfoFileHelper();
		ins.updateCompanyFromFileToDB();
		return "UpdateCompanyFromFileToDB already running, please check folder result.";
	}

	@GET
	@Path("/updateStockPriceHistoryOverAllRunner/{date}")
	public String updateStockPriceHistoryOverAllRunner(@PathParam("date") String dateParm) {
		// update the total GuBen and LiuTong GuBen
		String startDate = null, endDate = null;
		if (Pattern.matches(fromToRegex, dateParm)) {
			startDate = dateParm.split("_")[0];
			endDate = dateParm.split("_")[1];
		}
		StockPriceHistoryOverAllRunner runner = new StockPriceHistoryOverAllRunner(startDate, endDate);
		Thread t = new Thread(runner);
		t.start();
		return "StockPriceHistoryOverAllRunner already running, startDate=" + startDate + ", endDate=" + endDate;
	}
}
