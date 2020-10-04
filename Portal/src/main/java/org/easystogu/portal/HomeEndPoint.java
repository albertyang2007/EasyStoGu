package org.easystogu.portal;

import java.util.regex.Pattern;

import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.easystogu.config.Constants;
import org.easystogu.config.DBConfigurationService;
import org.easystogu.file.FileReaderAndWriter;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.runner.history.IndicatorHistortOverAllRunner;
import org.easystogu.report.HistoryAnalyseReport;
import org.easystogu.runner.DailyOverAllRunner;
import org.easystogu.runner.DailySelectionRunner;
import org.easystogu.runner.DailyUpdateAllStockRunner;
import org.easystogu.runner.DailyUpdateStockPriceAndIndicatorRunner;
import org.easystogu.runner.DailyViewAnalyseRunner;
import org.easystogu.runner.DataBaseSanityCheck;
import org.easystogu.runner.HistoryDailySelectionRunner;
import org.easystogu.sina.runner.DailyStockPriceDownloadAndStoreDBRunner2;
import org.easystogu.sina.runner.RealtimeDisplayStockPriceRunner;
import org.easystogu.sina.runner.history.StockPriceHistoryOverAllRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/portal/home")
public class HomeEndPoint {
	@Autowired
	private DBConfigurationService config;
	@Autowired
	private StockPriceHistoryOverAllRunner stockPriceHistoryOverAllRunner;
	@Autowired
	private DailyUpdateAllStockRunner dailyUpdateAllStockRunner;
	@Autowired
	private DailySelectionRunner dailySelectionRunner;
	@Autowired
	private DataBaseSanityCheck dataBaseSanityCheck;
	@Autowired
	private DailyOverAllRunner dailyOverAllRunner;
	@Autowired
	private DailyUpdateStockPriceAndIndicatorRunner dailyUpdateStockPriceAndIndicatorRunner;
	@Autowired
	private DailyViewAnalyseRunner dailyViewAnalyseRunner;
	@Autowired
	private DailyStockPriceDownloadAndStoreDBRunner2 dailyStockPriceDownloadAndStoreDBRunner2;
	@Autowired
	private IndicatorHistortOverAllRunner indicatorHistortOverAllRunner;
	@Autowired
	private HistoryDailySelectionRunner historyDailySelectionRunner;
	@Autowired
	private HistoryAnalyseReport historyAnalyseReport;
	
	protected final String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	protected final String fromToRegex = dateRegex + "_" + dateRegex;

	@GetMapping("/")
	public Response mainPage() {
		StringBuffer sb = new StringBuffer();
		sb.append("<a href='/portal/home/DailyUpdateAllStockRunner'>DailyUpdateAllStockRunner</a><br>");
		sb.append("<a href='/portal/home/DailyOverAllRunner'>DailyOverAllRunner</a><br>");
		sb.append(
				"<a href='/portal/home/DailyUpdatePriceAndIndicatorRunner'>DailyUpdatePriceAndIndicatorRunner</a><br>");
		sb.append("<a href='/portal/home/FastDailyOverAllRunner'>FastDailyOverAllRunner</a><br>");
		sb.append("<a href='/portal/home/DailySelectionRunner'>DailySelectionRunner</a><br>");
		sb.append("<a href='/portal/home/RealtimeDisplayStockPriceRunner'>RealtimeDisplayStockPriceRunner</a><br>");
		sb.append("<a href='/portal/home/DailyViewAnalyseRunner'>DailyViewAnalyseRunner</a><br>");
		sb.append("<a href='/portal/home/DailyZiJinLiuXiangRunner'>DailyZiJinLiuXiangRunner</a><br>");
		sb.append("<a href='/portal/home/DataBaseSanityCheck'>DataBaseSanityCheck</a><br>");
		sb.append("<a href='/portal/home/DownloadStockPrice'>DownloadStockPrice</a><br>");
		sb.append("<a href='/portal/home/UpdateCompanyFromFileToDB'>UpdateCompanyFromFileToDB</a><br>");
		sb.append(
				"<a href='/portal/home/updateStockPriceHistoryOverAllRunner/2016-10-17_2016-11-23'>updateStockPriceHistoryOverAllRunner</a><br>");
		sb.append("<a href='/portal/home/IndicatorHistortOverAllRunner'>IndicatorHistortOverAllRunner</a><br>");
		sb.append("<a href='/portal/home/HistoryAnalyseReport'>HistoryAnalyseReport Count All Check Point</a><br>");
		sb.append(
				"<a href='/portal/home/HistoryDailySelectionRunner'>HistoryDailySelectionRunner Count All Daily Check Point Statistics</a><br>");
		sb.append("<a href='/portal/home/Serverlog'>Serverlog</a><br>");

		sb.append("<br><a href='/eweb/index.htm'>eweb index</a><br>");

		return Response.ok().entity(sb.toString()).build();
	}

	@GetMapping("/DailyUpdateAllStockRunner")
	public String dailyUpdateOverAllRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dailyUpdateAllStockRunner.run()).start();
			return "DailyUpdateAllStockRunner already running, please check folder result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/DailySelectionRunner")
	public String dailySelectionRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dailySelectionRunner.run()).start();
			return "DailySelectionRunner already running, please check folder result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/RealtimeDisplayStockPriceRunner")
	@Produces("text/html; charset=UTF-8")
	public String realtimeDisplayStockPriceRunner() {
		return new RealtimeDisplayStockPriceRunner().printRealTimeOutput();
	}

	@GetMapping("/DataBaseSanityCheck")
	public String dataBaseSanityCheck() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dataBaseSanityCheck.run()).start();
			return "DataBaseSanityCheck already running, please check DB result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/DailyOverAllRunner")
	public String dailyOverAllRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dailyOverAllRunner.run()).start();
			return "DailyOverAllRunner already running, please check DB result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/FastDailyOverAllRunner")
	public String fastDailyOverAllRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dailyOverAllRunner.run()).start();
			return "FastDailyOverAllRunner (withour zijinliu) already running, please check DB result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/DailyUpdatePriceAndIndicatorRunner")
	public String dailyUpdatePriceAndIndicatorRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dailyUpdateStockPriceAndIndicatorRunner.run()).start();
			return "DailyUpdateStockPriceAndIndicatorRunner already running, please check DB result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/DailyViewAnalyseRunner")
	public String dailyViewAnalyseRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dailyViewAnalyseRunner.run()).start();
			return "DailyViewAnalyseRunner already running, please check folder result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/DownloadStockPrice")
	public String downloadStockPrice() {
		String zone = config.getString("zone", "");
		// day (download all stockIds price)
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dailyStockPriceDownloadAndStoreDBRunner2.run()).start();
			return "DailyStockPriceDownloadAndStoreDBRunner2 already running, please check folder result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/UpdateCompanyFromFileToDB")
	public String updateCompanyFromFileToDB() {
		String zone = config.getString("zone", "");
		// update the total GuBen and LiuTong GuBen
		if (Constants.ZONE_OFFICE.equals(zone)) {
			CompanyInfoFileHelper ins = new CompanyInfoFileHelper();
			ins.updateCompanyFromFileToDB();
			return "UpdateCompanyFromFileToDB already running, please check folder result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/updateStockPriceHistoryOverAllRunner/{date}")
	public String updateStockPriceHistoryOverAllRunner(@PathParam("date") String dateParm) {
		String zone = config.getString("zone", "");
		// update the total GuBen and LiuTong GuBen
		if (Constants.ZONE_OFFICE.equals(zone)) {
			String startDate = null, endDate = null;
			if (Pattern.matches(fromToRegex, dateParm)) {
				startDate = dateParm.split("_")[0];
				endDate = dateParm.split("_")[1];
			}
			
			final String _startDate = new String(startDate);
			final String _endDate = new String(endDate);
			
			new Thread(() -> stockPriceHistoryOverAllRunner.run(_startDate, _endDate)).start();
			
			return "StockPriceHistoryOverAllRunner already running, startDate=" + startDate + ", endDate=" + endDate;
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/IndicatorHistortOverAllRunner")
	public String indicatorHistortOverAllRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> indicatorHistortOverAllRunner.run()).start();
			return "IndicatorHistortOverAllRunner already running, please check folder result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/HistoryAnalyseReport")
	public String oneTimeTempRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					historyAnalyseReport.countAllStockIdAnalyseHistoryBuySellCheckPoint();
					historyAnalyseReport.countAllStockIdStatisticsCheckPoint();
				}
			});
			t.start();

			return "OneTimeTempRunner already running, please check folder result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/Serverlog")
	public String serverlog() {
		return FileReaderAndWriter.tailFile("/home/eyaweiw/software/jboss-eap-6.4/standalone/log/server.log", 20);
	}

	@GetMapping("/HistoryDailySelectionRunner")
	public String test() {
		new Thread(() -> historyDailySelectionRunner.run()).start();

		return "start CheckPointStatisticsPrepareData";
	}
}
