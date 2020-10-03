package org.easystogu.runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.db.access.table.CheckPointDailySelectionTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.table.ZhuLiJingLiuRuTableHelper;
import org.easystogu.db.access.table.ZiJinLiuTableHelper;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.db.vo.table.CompanyInfoVO;
import org.easystogu.db.vo.table.ZhuLiJingLiuRuVO;
import org.easystogu.db.vo.table.ZiJinLiuVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.springframework.stereotype.Component;

//recently (10 days) select stock that checkpoint is satisfied
@Component
public class RecentlySelectionRunner implements Runnable {
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
	private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	private ZiJinLiuTableHelper ziJinLiuTableHelper = ZiJinLiuTableHelper.getInstance();
	private ZhuLiJingLiuRuTableHelper zhuLiJingLiuRuTableHelper = ZhuLiJingLiuRuTableHelper.getInstance();
	private CheckPointDailySelectionTableHelper checkPointDailySelectionTable = CheckPointDailySelectionTableHelper
			.getInstance();
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
		List<CheckPointDailySelectionVO> cpList = checkPointDailySelectionTable
				.getRecentDaysCheckPoint(lastNDates.get(lastNDates.size() - 1));
		for (CheckPointDailySelectionVO cpVO : cpList) {
			DailyCombineCheckPoint checkPoint = DailyCombineCheckPoint.getCheckPointByName(cpVO.checkPoint);
			if (checkPoint != null && checkPoint.isSatisfyMinEarnPercent()) {
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

	public String format2f(double d) {
		return String.format("%.2f", d);
	}

	public void run() {
		fetchRecentDaysCheckPointFromDB();
		fetchRecentZiJinLiuFromDB();
		fetchRecentZhuLiJingLiuRuFromDB();
		fetchLiuTongShiZhiFromDB();
	}
}
