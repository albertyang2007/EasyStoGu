package org.easystogu.runner;

import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.CheckPointDailyStatisticsTableHelper;
import org.easystogu.db.access.table.IndMATableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.BollVO;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.db.vo.table.MAVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.db.vo.table.QSDDVO;
import org.easystogu.db.vo.table.ShenXianVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.WRVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.runner.history.HistoryBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryMACountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryQSDDCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryShenXianCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryWRCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryWeeklyKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryWeeklyMacdCountAndSaveDBRunner;
import org.easystogu.sina.runner.history.HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DataBaseSanityCheck {
	@Autowired
	private CompanyInfoFileHelper stockConfig;
	@Autowired
	@Qualifier("stockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected QianFuQuanStockPriceTableHelper qianfuquanStockPriceTable;
	@Autowired
	private DBAccessFacdeFactory dBAccessFacdeFactory;
	protected IndicatorDBHelperIF macdTable = dBAccessFacdeFactory.getInstance(Constants.indMacd);
	protected IndicatorDBHelperIF kdjTable = dBAccessFacdeFactory.getInstance(Constants.indKDJ);
	protected IndicatorDBHelperIF bollTable = dBAccessFacdeFactory.getInstance(Constants.indBoll);
	protected IndicatorDBHelperIF shenXianTable = dBAccessFacdeFactory.getInstance(Constants.indShenXian);
	protected IndicatorDBHelperIF qsddTable = dBAccessFacdeFactory.getInstance(Constants.indQSDD);
	protected IndicatorDBHelperIF wrTable = dBAccessFacdeFactory.getInstance(Constants.indWR);
	@Autowired
	protected IndMATableHelper maTable;
	@Autowired
	protected HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner historyQianFuQuanRunner;
	@Autowired
	protected CheckPointDailyStatisticsTableHelper checkPointDailyStatisticsTable;

	@Autowired
	@Qualifier("weekStockPriceTable")
	protected WeekStockPriceTableHelper weekStockPriceTable;
	protected IndicatorDBHelperIF weekMacdTable = dBAccessFacdeFactory.getInstance(Constants.indWeekMacd);
	protected IndicatorDBHelperIF weekKdjTable = dBAccessFacdeFactory.getInstance(Constants.indWeekKDJ);
	@Autowired
	protected HistoryMacdCountAndSaveDBRunner macdRunner = new HistoryMacdCountAndSaveDBRunner();
	@Autowired
	protected HistoryKDJCountAndSaveDBRunner kdjRunner;
	@Autowired
	protected HistoryBollCountAndSaveDBRunner boolRunner;
	@Autowired
	protected HistoryShenXianCountAndSaveDBRunner shenxianRunner;
	@Autowired
	protected HistoryQSDDCountAndSaveDBRunner qsddRunner;
	@Autowired
	protected HistoryMACountAndSaveDBRunner maRunner;
	@Autowired
	protected HistoryWRCountAndSaveDBRunner wrRunner;

	@Autowired
	protected HistoryWeeklyMacdCountAndSaveDBRunner weekMacdRunner;
	@Autowired
	protected HistoryWeeklyKDJCountAndSaveDBRunner weekKdjRunner;

	@Autowired
	protected DailySelectionRunner dailySelectionRunner;

	public void sanityDailyCheck(List<String> stockIds) {
		System.out.println("sanityDailyCheck start.");
		stockIds.parallelStream().forEach(stockId -> {
			this.sanityDailyCheck(stockId);
		});

		// int index = 0;
		// for (String stockId : stockIds) {
		// if (index++ % 100 == 0) {
		// System.out.println("Processing " + index + "/" + stockIds.size());
		// }
		// this.sanityDailyCheck(stockId);
		// }
		System.out.println("sanityDailyCheck completed.");
	}

	public void sanityDailyCheck(String stockId) {

		List<StockPriceVO> spList = stockPriceTable.getStockPriceById(stockId);
		List<StockPriceVO> qianfuquan_spList = qianfuquanStockPriceTable.getStockPriceById(stockId);
		List<MacdVO> macdList = macdTable.getAll(stockId);
		List<KDJVO> kdjList = kdjTable.getAll(stockId);
		List<BollVO> bollList = bollTable.getAll(stockId);
		List<ShenXianVO> shenXianList = shenXianTable.getAll(stockId);
		List<QSDDVO> qsddList = qsddTable.getAll(stockId);
		List<MAVO> maList = maTable.getAllMA(stockId);
		List<WRVO> wrList = wrTable.getAll(stockId);

		for (StockPriceVO vo : spList) {
			if (vo.close == 0 || vo.open == 0 || vo.high == 0 || vo.low == 0 || Strings.isEmpty(vo.date)) {
				System.out.println("Sanity Delete StockPrice " + vo);
				this.stockPriceTable.delete(vo.stockId, vo.date);
			}
		}

		if (spList.size() != qianfuquan_spList.size()) {
			System.out.println(stockId + " StockPrice Length is not equal to Qian FuQuan StockPrice");
			this.historyQianFuQuanRunner.countAndSave(stockId);
		}

		if ((spList.size() != macdList.size())) {
			System.out.println(stockId + " size of macd is not equal:" + spList.size() + "!=" + macdList.size());

			// figureOutDifferenceDate(spList, macdList);

			macdTable.delete(stockId);
			macdRunner.countAndSaved(stockId);
		}
		if ((spList.size() != kdjList.size())) {
			System.out.println(stockId + " size of kdj is not equal:" + spList.size() + "!=" + kdjList.size());
			kdjTable.delete(stockId);
			kdjRunner.countAndSaved(stockId);
		}
		if ((spList.size() != bollList.size())) {
			System.out.println(stockId + " size of boll is not equal:" + spList.size() + "!=" + bollList.size());
			bollTable.delete(stockId);
			boolRunner.countAndSaved(stockId);
		}
		if ((spList.size() != shenXianList.size())) {
			System.out
					.println(stockId + " size of shenXian is not equal:" + spList.size() + "!=" + shenXianList.size());
			shenXianTable.delete(stockId);
			shenxianRunner.countAndSaved(stockId);
		}
		if ((spList.size() != qsddList.size())) {
			System.out.println(stockId + " size of QSDD is not equal:" + spList.size() + "!=" + qsddList.size());
			qsddTable.delete(stockId);
			qsddRunner.countAndSaved(stockId);
		}
		if ((spList.size() != maList.size())) {
			System.out.println(stockId + " size of MA is not equal:" + spList.size() + "!=" + maList.size());
			maTable.delete(stockId);
			maRunner.countAndSaved(stockId);
		}
		if ((spList.size() != wrList.size())) {
			System.out.println(stockId + " size of WR is not equal:" + spList.size() + "!=" + wrList.size());
			wrTable.delete(stockId);
			wrRunner.countAndSaved(stockId);
		}

	}

	public void sanityWeekCheck(List<String> stockIds) {
		System.out.println("sanityWeekCheck completed.");

		stockIds.parallelStream().forEach(stockId -> {
			this.sanityWeekCheck(stockId);
		});

		// int index = 0;
		// for (String stockId : stockIds) {
		// if (index++ % 100 == 0) {
		// System.out.println("Processing week " + index + "/" + stockIds.size());
		// }
		// this.sanityWeekCheck(stockId);
		// }
		System.out.println("sanityWeekCheck completed.");
	}

	public void sanityWeekCheck(String stockId) {
		List<StockPriceVO> spList = weekStockPriceTable.getStockPriceById(stockId);
		List<MacdVO> macdList = weekMacdTable.getAll(stockId);
		List<KDJVO> kdjList = weekKdjTable.getAll(stockId);

		boolean refresh = false;
		for (StockPriceVO vo : spList) {
			if (vo.close == 0 || vo.open == 0 || vo.high == 0 || vo.low == 0 || Strings.isEmpty(vo.date)) {
				System.out.println("Sanity Delete WeekStockPrice " + vo);
				this.weekStockPriceTable.delete(vo.stockId, vo.date);
				refresh = true;
			}
		}
		// refresh if above delete vo
		if (refresh)
			spList = weekStockPriceTable.getStockPriceById(stockId);

		if ((spList.size() != macdList.size())) {
			System.out.println(stockId + " size of week macd is not equal:" + spList.size() + "!=" + macdList.size());

			figureOutDifferenceDate(spList, macdList);

			weekMacdTable.delete(stockId);
			weekMacdRunner.countAndSaved(stockId);
		}
		if ((spList.size() != kdjList.size())) {
			System.out.println(stockId + " size of week kdj is not equal:" + spList.size() + "!=" + kdjList.size());
			weekKdjTable.delete(stockId);
			weekKdjRunner.countAndSaved(stockId);
		}

	}

	public void sanityDailyStatisticsCheck(List<String> stockIds) {
		System.out.println("sanityDailyStatisticsCheck start.");
		List<String> dates = stockPriceTable.getAllDealDate("999999");
		for (String date : dates) {
			// do not care the count date before 2000 year
			if (date.compareTo("2000-01-01") >= 0) {
				int rtn = checkPointDailyStatisticsTable.countByDate(date);
				if (rtn == 0) {
					System.out.println("Daily Statistics is all zero for date " + date + ", try to re-count it.");
					dailySelectionRunner.runForDate(date, stockIds);
				}
			}
		}
		System.out.println("sanityDailyStatisticsCheck completed.");
	}

	public void figureOutDifferenceDate(List<StockPriceVO> spList, List<MacdVO> macdList) {
		int minLen = Math.min(spList.size(), macdList.size());
		int index = 0;
		for (; index < minLen; index++) {
			StockPriceVO spvo = spList.get(index);
			MacdVO macdvo = macdList.get(index);
			if (!spvo.date.equals(macdvo.date)) {
				System.out.println("spList date != macdList @" + spvo.date);
			}
		}
		if (index == spList.size()) {
			System.out.println("spList has, but macdList do not have @" + macdList.get(index).date);
		}

		if (index == macdList.size()) {
			System.out.println("macdList has, but spList do not have @" + spList.get(index).date);
		}
	}

	public void run() {
		// TODO Auto-generated method stub
		this.sanityDailyCheck(stockConfig.getAllStockId());
		this.sanityWeekCheck(stockConfig.getAllStockId());
		this.sanityDailyStatisticsCheck(stockConfig.getAllStockId());

		// this.sanityDailyCheck("002797");
		// this.sanityWeekCheck("002797");
	}
}
