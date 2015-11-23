package org.easystogu.runner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.db.access.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.ZiJinLiu3DayTableHelper;
import org.easystogu.db.access.ZiJinLiu5DayTableHelper;
import org.easystogu.db.access.ZiJinLiuTableHelper;
import org.easystogu.db.table.CheckPointDailySelectionVO;
import org.easystogu.db.table.CompanyInfoVO;
import org.easystogu.db.table.ZiJinLiuVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.report.ReportTemplate;
import org.easystogu.report.comparator.CheckPointEventAndZiJinLiuComparator;

//recently (10 days) select stock that checkpoint is satisfied
public class RecentlySelectionRunner implements Runnable {
	private FileConfigurationService config = FileConfigurationService.getInstance();
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private ZiJinLiuTableHelper ziJinLiuTableHelper = ZiJinLiuTableHelper.getInstance();
	private ZiJinLiu3DayTableHelper ziJinLiu3DayTableHelper = ZiJinLiu3DayTableHelper.getInstance();
	private ZiJinLiu5DayTableHelper ziJinLiu5DayTableHelper = ZiJinLiu5DayTableHelper.getInstance();
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
			.getInstance();
	private String latestDate = stockPriceTable.getLatestStockDate();
	private List<String> lastNDates = stockPriceTable.getAllLastNDate(stockConfig.getSZZSStockIdForDB(), 10);
	// <stockId, checkPoints>
	private Map<String, List<CheckPointDailySelectionVO>> checkPointStocks = new HashMap<String, List<CheckPointDailySelectionVO>>();
	// <stockId, ziJinLius>>
	private Map<String, List<ZiJinLiuVO>> ziJinLius = new HashMap<String, List<ZiJinLiuVO>>();

	private void fetchRecentDaysCheckPointFromDB() {
		// TODO Auto-generated method stub
		List<CheckPointDailySelectionVO> cpList = checkPointDailySelectionTable.getRecentDaysCheckPoint(lastNDates
				.get(lastNDates.size() - 1));
		for (CheckPointDailySelectionVO cpVO : cpList) {
			DailyCombineCheckPoint checkPoint = DailyCombineCheckPoint.getCheckPointByName(cpVO.checkPoint);
			if (checkPoint.isSatisfyMinEarnPercent()) {
				this.addCheckPointStockToMap(cpVO);
			}
		}
	}

	private void addCheckPointStockToMap(CheckPointDailySelectionVO cpVO) {
		if (!this.checkPointStocks.containsKey(cpVO.stockId)) {
			List<CheckPointDailySelectionVO> cps = new ArrayList<CheckPointDailySelectionVO>();
			cps.add(cpVO);
			this.checkPointStocks.put(cpVO.stockId, cps);
		} else {
			List<CheckPointDailySelectionVO> cps = this.checkPointStocks.get(cpVO.stockId);
			cps.add(cpVO);
		}
	}

	private void fetchRecentZiJinLiuFromDB() {
		Set<String> stockIds = this.checkPointStocks.keySet();
		Iterator<String> its = stockIds.iterator();
		while (its.hasNext()) {
			String stockId = its.next();

			for (String date : lastNDates) {
				ZiJinLiuVO _1dayVO = ziJinLiuTableHelper.getZiJinLiu(stockId, date);
				ZiJinLiuVO _3dayVO = ziJinLiu3DayTableHelper.getZiJinLiu(stockId, date);
				ZiJinLiuVO _5dayVO = ziJinLiu5DayTableHelper.getZiJinLiu(stockId, date);

				List<ZiJinLiuVO> list = null;
				if (!this.ziJinLius.containsKey(stockId)) {
					list = new ArrayList<ZiJinLiuVO>();
					this.ziJinLius.put(stockId, list);
				} else {
					list = this.ziJinLius.get(stockId);
				}
				if (_1dayVO != null) {
					_1dayVO._DayType = ZiJinLiuVO._1Day;
					list.add(_1dayVO);
				}
				if (_3dayVO != null) {
					_3dayVO._DayType = ZiJinLiuVO._3Day;
					list.add(_3dayVO);
				}
				if (_5dayVO != null) {
					_5dayVO._DayType = ZiJinLiuVO._5Day;
					list.add(_5dayVO);
				}
			}
		}
	}

	public void printRecentCheckPointToHtml() {

		// before report, sort
		this.checkPointStocks = CheckPointEventAndZiJinLiuComparator.sortMapByValue(checkPointStocks, ziJinLius);

		String file = config.getString("report.recent.analyse.html.file").replaceAll("currentDate", latestDate);
		System.out.println("\nSaving report to " + file);
		try {
			BufferedWriter fout = new BufferedWriter(new FileWriter(file));
			fout.write(ReportTemplate.htmlStart);
			fout.newLine();
			fout.write(ReportTemplate.tableStart);
			fout.newLine();

			Set<String> stockIds = this.checkPointStocks.keySet();
			Iterator<String> its = stockIds.iterator();
			while (its.hasNext()) {
				String stockId = its.next();

				fout.write(ReportTemplate.tableTrStart);
				fout.newLine();

				fout.write(ReportTemplate.tableTdStart);
				fout.write(stockId + "&nbsp;" + this.getLiuTongShiZhi(stockId) + "<br>");
				fout.write("<img src='http://image.sinajs.cn/newchart/daily/n/" + withPrefixStockId(stockId)
						+ ".gif'/>");
				fout.write(ReportTemplate.tableTdEnd);
				fout.newLine();

				fout.write(ReportTemplate.tableTdStart);
				fout.write(ReportTemplate.tableStart);
				fout.newLine();

				for (String date : lastNDates) {
					fout.write(ReportTemplate.tableTrStart);
					fout.newLine();

					fout.write(ReportTemplate.tableTdStart);
					fout.write(date);
					fout.write(ReportTemplate.tableTdEnd);
					fout.newLine();

					fout.write(ReportTemplate.tableTdStart);
					fout.write(this.getCheckPointAndZiJinLiuOnDate(stockId, date));
					fout.write(ReportTemplate.tableTdEnd);
					fout.newLine();

					fout.write(ReportTemplate.tableTrEnd);
					fout.newLine();
				}

				fout.write(ReportTemplate.tableEnd);
				fout.newLine();

				fout.write(ReportTemplate.tableTdEnd);
				fout.newLine();

				fout.write(ReportTemplate.tableTrEnd);
				fout.newLine();
			}

			fout.write(ReportTemplate.tableEnd);
			fout.newLine();
			fout.write(ReportTemplate.htmlEnd);
			fout.newLine();

			fout.flush();
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String withPrefixStockId(String stockId) {
		if (stockId.startsWith("0") || stockId.startsWith("3")) {
			return "sz" + stockId;
		} else if (stockId.startsWith("6")) {
			return "sh" + stockId;
		}
		return stockId;
	}

	private String getCheckPointAndZiJinLiuOnDate(String stockId, String date) {
		List<CheckPointDailySelectionVO> cpList = this.checkPointStocks.get(stockId);
		List<ZiJinLiuVO> zjlList = this.ziJinLius.get(stockId);

		String cpRtn = this.getCheckPointOnDate(date, cpList);
		String zjlRtn = this.getZiJinLiuOnDate(date, zjlList);

		String rtn = cpRtn + zjlRtn;
		if (rtn.length() == 0)
			rtn = "&nbsp;";
		return rtn;
	}

	private String getCheckPointOnDate(String date, List<CheckPointDailySelectionVO> cpList) {
		StringBuffer sb = new StringBuffer();
		for (CheckPointDailySelectionVO cp : cpList) {
			if (date.equals(cp.date)) {
				sb.append(cp.checkPoint + "<br>");
			}
		}
		return sb.toString();
	}

	private String getZiJinLiuOnDate(String date, List<ZiJinLiuVO> zjlList) {
		StringBuffer sb = new StringBuffer();
		for (ZiJinLiuVO zjl : zjlList) {
			if (date.equals(zjl.date)) {
				sb.append(zjl._DayType + zjl.toNetPerString() + "<br>");
			}
		}
		return sb.toString();
	}

	// 盘子大小 (流通市值)
	private String getLiuTongShiZhi(String stockId) {
		CompanyInfoVO companyVO = stockConfig.getByStockId(stockId);
		double liuTongShiZhi = 0.0;
		if (companyVO != null) {
			double close = stockPriceTable.getAvgClosePrice(stockId, 1);
			liuTongShiZhi = companyVO.liuTongAGu * close;
		}
		return (int) liuTongShiZhi + "亿";
	}

	public void run() {
		fetchRecentDaysCheckPointFromDB();
		fetchRecentZiJinLiuFromDB();
		printRecentCheckPointToHtml();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RecentlySelectionRunner runner = new RecentlySelectionRunner();
		runner.fetchRecentDaysCheckPointFromDB();
		runner.fetchRecentZiJinLiuFromDB();
		runner.printRecentCheckPointToHtml();
	}
}
