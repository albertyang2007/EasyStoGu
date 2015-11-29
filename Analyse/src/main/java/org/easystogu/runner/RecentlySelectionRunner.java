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
import org.easystogu.db.access.ZhuLiJingLiuRuTableHelper;
import org.easystogu.db.access.ZiJinLiuTableHelper;
import org.easystogu.db.table.CheckPointDailySelectionVO;
import org.easystogu.db.table.CompanyInfoVO;
import org.easystogu.db.table.ZhuLiJingLiuRuVO;
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
	private ZhuLiJingLiuRuTableHelper zhuLiJingLiuRuTableHelper = ZhuLiJingLiuRuTableHelper.getInstance();
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
			.getInstance();
	private String latestDate = stockPriceTable.getLatestStockDate();
	private List<String> lastNDates = stockPriceTable.getAllLastNDate(stockConfig.getSZZSStockIdForDB(), 10);
	// <stockId, checkPoints>
	private Map<String, List<CheckPointDailySelectionVO>> checkPointStocks = new HashMap<String, List<CheckPointDailySelectionVO>>();
	// <stockId, ziJinLius>>
	private Map<String, List<ZiJinLiuVO>> ziJinLius = new HashMap<String, List<ZiJinLiuVO>>();
	// <stockId, zhuLiJingLiuRu>>
	private Map<String, List<ZhuLiJingLiuRuVO>> zhuLiJingLiuRus = new HashMap<String, List<ZhuLiJingLiuRuVO>>();
	// <stockId, liuTongShiZhi>>
	private Map<String, Integer> liuTongShiZhi = new HashMap<String, Integer>();

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

				List<ZiJinLiuVO> zjlList = null;
				if (!this.ziJinLius.containsKey(stockId)) {
					zjlList = new ArrayList<ZiJinLiuVO>();
					this.ziJinLius.put(stockId, zjlList);
				} else {
					zjlList = this.ziJinLius.get(stockId);
				}
				//
				if (_1dayVO != null) {
					_1dayVO._DayType = ZiJinLiuVO._1Day;
					zjlList.add(_1dayVO);
				}
			}
		}
	}

	private void fetchRecentZhuLiJingLiuRuFromDB() {
		Set<String> stockIds = this.checkPointStocks.keySet();
		Iterator<String> its = stockIds.iterator();
		while (its.hasNext()) {
			String stockId = its.next();

			for (String date : lastNDates) {
				ZhuLiJingLiuRuVO vo = zhuLiJingLiuRuTableHelper.getZhuLiJingLiuRu(stockId, date);

				List<ZhuLiJingLiuRuVO> zljlrList = null;
				if (!this.zhuLiJingLiuRus.containsKey(stockId)) {
					zljlrList = new ArrayList<ZhuLiJingLiuRuVO>();
					this.zhuLiJingLiuRus.put(stockId, zljlrList);
				} else {
					zljlrList = this.zhuLiJingLiuRus.get(stockId);
				}
				//
				if (vo != null) {
					zljlrList.add(vo);
				}
			}
		}
	}

	private void fetchLiuTongShiZhiFromDB() {
		Set<String> stockIds = this.checkPointStocks.keySet();
		Iterator<String> its = stockIds.iterator();
		while (its.hasNext()) {
			String stockId = its.next();
			int liuTongShiZhi = this.getLiuTongShiZhi(stockId);
			this.liuTongShiZhi.put(stockId, new Integer(liuTongShiZhi));
		}
	}

	private void searchAllStockIdsWithMany1DayZiJinLiuFromDB() {
		List<String> stockIds = stockConfig.getAllStockId();
		for (String stockId : stockIds) {
			List<ZiJinLiuVO> zjlList = new ArrayList<ZiJinLiuVO>();
			for (String date : lastNDates) {
				ZiJinLiuVO _1dayVO = ziJinLiuTableHelper.getZiJinLiu(stockId, date);
				if (_1dayVO != null) {
					_1dayVO._DayType = ZiJinLiuVO._1Day;
					zjlList.add(_1dayVO);
				}
			}
			// in 10 days, there more than _1Day ZiJinLiVO, means big money buy
			// this stock in recent days
			if (zjlList.size() >= 5) {
				if (!this.ziJinLius.containsKey(stockId)) {
					this.ziJinLius.put(stockId, zjlList);
				}
				//
				CheckPointDailySelectionVO mockCPVO = new CheckPointDailySelectionVO();
				mockCPVO.setCheckPoint(DailyCombineCheckPoint.Continue_1Day_ZiJinLiu.toString());
				mockCPVO.setStockId(stockId);
				mockCPVO.setDate(zjlList.get(0).date);
				this.addCheckPointStockToMap(mockCPVO);
			}
		}
	}

	private void printRecentCheckPointToHtml() {

		// before report, sort
		this.checkPointStocks = CheckPointEventAndZiJinLiuComparator.sortMapByValue(lastNDates, checkPointStocks, ziJinLius,
				liuTongShiZhi, zhuLiJingLiuRus);

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
				fout.write(this.getCompanyBaseInfo(stockId));
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

	private String getCompanyBaseInfo(String stockId) {
		StringBuffer sb = new StringBuffer();
		sb.append(stockId + "&nbsp;" + this.stockConfig.getStockName(stockId) + "&nbsp;");
		sb.append(this.liuTongShiZhi.get(stockId) + "亿<br>");
		return sb.toString();
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
		List<ZhuLiJingLiuRuVO> zljlrList = this.zhuLiJingLiuRus.get(stockId);

		String cpRtn = this.getCheckPointOnDate(date, cpList);
		String zjlRtn = this.getZiJinLiuOnDate(date, zjlList);
		String zljlrRtn = this.getZhuLiJingLiuRuOnDate(date, zljlrList);

		String rtn = cpRtn + zjlRtn + zljlrRtn;
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
				sb.append(zjl.toNetPerString() + "<br>");
			}
		}
		return sb.toString();
	}

	private String getZhuLiJingLiuRuOnDate(String date, List<ZhuLiJingLiuRuVO> zljlrList) {
		StringBuffer sb = new StringBuffer();
		for (ZhuLiJingLiuRuVO zjl : zljlrList) {
			if (date.equals(zjl.date)) {
				sb.append(zjl.toNetInString() + "<br>");
			}
		}
		return sb.toString();
	}

	// 盘子大小 (流通市值)
	private int getLiuTongShiZhi(String stockId) {
		CompanyInfoVO companyVO = stockConfig.getByStockId(stockId);
		double liuTongShiZhi = 0.0;
		if (companyVO != null) {
			double close = stockPriceTable.getAvgClosePrice(stockId, 1);
			liuTongShiZhi = companyVO.countLiuTongShiZhi(close);
		}
		return (int) liuTongShiZhi;
	}

	public void run() {
		fetchRecentDaysCheckPointFromDB();
		fetchRecentZiJinLiuFromDB();
		fetchRecentZhuLiJingLiuRuFromDB();
		fetchLiuTongShiZhiFromDB();
		printRecentCheckPointToHtml();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RecentlySelectionRunner runner = new RecentlySelectionRunner();
		runner.fetchRecentDaysCheckPointFromDB();
		runner.fetchRecentZiJinLiuFromDB();
		runner.fetchRecentZhuLiJingLiuRuFromDB();
		runner.fetchLiuTongShiZhiFromDB();
		runner.printRecentCheckPointToHtml();
	}
}
