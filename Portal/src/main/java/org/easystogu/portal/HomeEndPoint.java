package org.easystogu.portal;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.easystogu.cassandra.access.table.IndMacdCassTableHelper;
import org.easystogu.config.Constants;
import org.easystogu.config.DBConfigurationService;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.IndicatorVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.runner.history.IndicatorHistortOverAllRunner;
import org.easystogu.log.LogHelper;
import org.easystogu.postgresql.access.table.IndMacdDBTableHelper;
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
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/portal/home")
public class HomeEndPoint {
	private static Logger logger = LogHelper.getLogger(HomeEndPoint.class);
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
	@Autowired
	private CompanyInfoFileHelper companyInfoFileHelper;
	
	//just for test
	@Autowired
	DBAccessFacdeFactory dBAccessFacdeFactory;
	@Autowired
	IndMacdDBTableHelper indMacdDBTableHelper;
	@Autowired
	IndMacdCassTableHelper indMacdCassTableHelper;

	protected final String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	protected final String fromToRegex = dateRegex + "_" + dateRegex;

	@GetMapping("/")
	@Produces("text/html")
	public Response mainPage() {
		StringBuffer sb = new StringBuffer("<!DOCTYPE html>\r\n" + "<html>\r\n" + "<body>\r\n");
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
		sb.append("<a href='/portal/home/TestDB'>TestDB</a><br>");

		sb.append("<br><a href='/eweb/index.htm'>eweb index</a><br>");
		
		sb.append("<br>HostName: "+ getHostName() +"</br>");

		sb.append("</body>\r\n" + "</html>");

		return Response.ok().entity(sb.toString()).build();
	}

	@GetMapping("/DailyUpdateAllStockRunner")
	public String dailyUpdateOverAllRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dailyUpdateAllStockRunner.run()).start();
			return getHostName() + " DailyUpdateAllStockRunner already running, please check folder result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/DailySelectionRunner")
	public String dailySelectionRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dailySelectionRunner.run()).start();
			return getHostName() + " DailySelectionRunner already running, please check folder result.";
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
			return getHostName() + " DataBaseSanityCheck already running, please check DB result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/DailyOverAllRunner")
	public String dailyOverAllRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dailyOverAllRunner.run()).start();
			return getHostName() + " DailyOverAllRunner already running, please check DB result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/FastDailyOverAllRunner")
	public String fastDailyOverAllRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dailyOverAllRunner.run()).start();
			return getHostName() + " FastDailyOverAllRunner (without zijinliu) already running, please check DB result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/DailyUpdatePriceAndIndicatorRunner")
	public String dailyUpdatePriceAndIndicatorRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dailyUpdateStockPriceAndIndicatorRunner.run()).start();
			return getHostName() + " DailyUpdateStockPriceAndIndicatorRunner already running, please check DB result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/DailyViewAnalyseRunner")
	public String dailyViewAnalyseRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dailyViewAnalyseRunner.run()).start();
			return getHostName() + " DailyViewAnalyseRunner already running, please check folder result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/DownloadStockPrice")
	public String downloadStockPrice() {
		String zone = config.getString("zone", "");
		// day (download all stockIds price)
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> dailyStockPriceDownloadAndStoreDBRunner2.run()).start();
			return getHostName() + " DailyStockPriceDownloadAndStoreDBRunner2 already running, please check folder result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/UpdateCompanyFromFileToDB")
	public String updateCompanyFromFileToDB() {
		String zone = config.getString("zone", "");
		// update the total GuBen and LiuTong GuBen
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> companyInfoFileHelper.updateCompanyFromFileToDB()).start();
			return getHostName() + " UpdateCompanyFromFileToDB already running, please check folder result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/updateStockPriceHistoryOverAllRunner/{date}")
	public String updateStockPriceHistoryOverAllRunner(@PathVariable("date") String dateParm) {
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

			return getHostName() + " StockPriceHistoryOverAllRunner already running, startDate=" + startDate + ", endDate=" + endDate;
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/IndicatorHistortOverAllRunner")
	public String indicatorHistortOverAllRunner() {
		String zone = config.getString("zone", "");
		if (Constants.ZONE_OFFICE.equals(zone)) {
			new Thread(() -> indicatorHistortOverAllRunner.run()).start();
			return getHostName() + " IndicatorHistortOverAllRunner already running, please check folder result.";
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

			return getHostName() + " OneTimeTempRunner already running, please check folder result.";
		}
		return zone + " not allow to run this method.";
	}

	@GetMapping("/HistoryDailySelectionRunner")
	public String test() {
		new Thread(() -> historyDailySelectionRunner.run()).start();

		return getHostName() + " start CheckPointStatisticsPrepareData";
	}

	@GetMapping("/TestDB")
	public String serverlog() {
		StringBuffer sb = new StringBuffer();
		try {
			MacdVO vo = new MacdVO();
			vo.setDate("2020-10-03");
			vo.setDea(1.0);
			vo.setDif(1.0);
			vo.setMacd(1.0);
			
			//facde insert
			vo.setStockId("900001");
			IndicatorDBHelperIF macdTable = dBAccessFacdeFactory.getInstance(Constants.indMacd);
			macdTable.insert(vo);
			IndicatorVO rtn1 = macdTable.getSingle(vo.getStockId(), vo.getDate());
			sb.append("facde get rtn1: "+rtn1.getClass().getSimpleName() + ", " + rtn1.toString());
			
			//cassandra insert
			vo.setStockId("900002");
			indMacdCassTableHelper.insert(vo);
			IndicatorVO rtn2 = indMacdCassTableHelper.getSingle(vo.getStockId(), vo.getDate());
			sb.append("cass get rtn2: "+rtn2.getClass().getSimpleName() + ", " + rtn2.toString());
			
			//postgres insert
			vo.setStockId("900003");
			indMacdDBTableHelper.insert(vo);
			IndicatorVO rtn3 = indMacdDBTableHelper.getSingle(vo.getStockId(), vo.getDate());
			sb.append("postgres get rtn3: "+rtn3.getClass().getSimpleName() + ", " + rtn3.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			return "exception: " + e.getMessage();
		}
		return getHostName() + " TestCassandra result: \n" + sb.toString();
	}
	
	private String getHostName() {
	  InetAddress ip;
      String hostname;
      try {
          ip = InetAddress.getLocalHost();
          hostname = ip.getHostName();
          return hostname;

      } catch (UnknownHostException e) {
          e.printStackTrace();
      }
      return "Unknown Hostname";
	}
}
