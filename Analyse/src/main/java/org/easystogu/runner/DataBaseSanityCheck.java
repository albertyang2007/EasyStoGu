package org.easystogu.runner;

import java.util.List;

import org.easystogu.db.access.IndBollTableHelper;
import org.easystogu.db.access.IndKDJTableHelper;
import org.easystogu.db.access.IndMacdTableHelper;
import org.easystogu.db.access.IndQSDDTableHelper;
import org.easystogu.db.access.IndShenXianTableHelper;
import org.easystogu.db.access.IndWeekKDJTableHelper;
import org.easystogu.db.access.IndWeekMacdTableHelper;
import org.easystogu.db.access.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.QSDDVO;
import org.easystogu.db.table.ShenXianVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.runner.history.HistoryBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryQSDDCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryShenXianCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryWeeklyKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryWeeklyMacdCountAndSaveDBRunner;
import org.easystogu.sina.runner.history.HistoryHouFuQuanStockPriceDownloadAndStoreDBRunner;
import org.easystogu.sina.runner.history.HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner;
import org.easystogu.utils.Strings;

public class DataBaseSanityCheck implements Runnable {
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected QianFuQuanStockPriceTableHelper qianfuquanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
	protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
	protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
	protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();
	protected IndQSDDTableHelper qsddTable = IndQSDDTableHelper.getInstance();
	protected HistoryHouFuQuanStockPriceDownloadAndStoreDBRunner historyHouFuQuanRunner = new HistoryHouFuQuanStockPriceDownloadAndStoreDBRunner();
	protected HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner historyQianFuQuanRunner = new HistoryQianFuQuanStockPriceDownloadAndStoreDBRunner();
	// protected IndYiMengBSTableHelper ymbsTable =
	// IndYiMengBSTableHelper.getInstance();

	protected WeekStockPriceTableHelper weekStockPriceTable = WeekStockPriceTableHelper.getInstance();
	protected IndWeekMacdTableHelper macdWeekTable = IndWeekMacdTableHelper.getInstance();
	protected IndWeekKDJTableHelper kdjWeekTable = IndWeekKDJTableHelper.getInstance();

	// protected IndWeekBollTableHelper bollWeekTable =
	// IndWeekBollTableHelper.getInstance();
	// protected IndWeekMai1Mai2TableHelper mai1mai2WeekTable =
	// IndWeekMai1Mai2TableHelper.getInstance();
	// protected IndWeekShenXianTableHelper shenXianWeekTable =
	// IndWeekShenXianTableHelper.getInstance();
	// protected IndWeekYiMengBSTableHelper yiMengBSWeekTable =
	// IndWeekYiMengBSTableHelper.getInstance();
	// protected IndWeekQSDDTableHelper qsddWeekTable =
	// IndWeekQSDDTableHelper.getInstance();

	public void sanityDailyCheck(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			if (index++ % 100 == 0) {
				System.out.println("Processing " + index + "/" + stockIds.size());
			}
			this.sanityDailyCheck(stockId);
		}
		System.out.println("sanityDailyCheck completed.");
	}

	public void sanityDailyCheck(String stockId) {

		List<StockPriceVO> spList = stockPriceTable.getStockPriceById(stockId);
		//List<StockPriceVO> houfuquan_spList = houfuquanStockPriceTable.getStockPriceById(stockId);
		List<StockPriceVO> qianfuquan_spList = qianfuquanStockPriceTable.getStockPriceById(stockId);
		List<MacdVO> macdList = macdTable.getAllMacd(stockId);
		List<KDJVO> kdjList = kdjTable.getAllKDJ(stockId);
		List<BollVO> bollList = bollTable.getAllBoll(stockId);
		List<ShenXianVO> shenXianList = shenXianTable.getAllShenXian(stockId);
		List<QSDDVO> qsddList = qsddTable.getAllQSDD(stockId);
		// List<YiMengBSVO> ymbsList = ymbsTable.getAllYiMengBS(stockId);
		// List<XueShi2VO> xueShie2List = xueShi2Table.getAllXueShi2(stockId);
		// List<Mai1Mai2VO> mai1mai2List =
		// mai1mai2Table.getAllMai1Mai2(stockId);
		// List<ZhuliJinChuVO> zhuliJinChuList =
		// zhuliJinChuTable.getAllZhuliJinChu(stockId);

		// if (spList.size() <= 108)
		// return;

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

			//figureOutDifferenceDate(spList, macdList);

			macdTable.delete(stockId);
			HistoryMacdCountAndSaveDBRunner runner = new HistoryMacdCountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		if ((spList.size() != kdjList.size())) {
			System.out.println(stockId + " size of kdj is not equal:" + spList.size() + "!=" + kdjList.size());
			kdjTable.delete(stockId);
			HistoryKDJCountAndSaveDBRunner runner = new HistoryKDJCountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		if ((spList.size() != bollList.size())) {
			System.out.println(stockId + " size of boll is not equal:" + spList.size() + "!=" + bollList.size());
			bollTable.delete(stockId);
			HistoryBollCountAndSaveDBRunner runner = new HistoryBollCountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		if ((spList.size() != shenXianList.size())) {
			System.out
					.println(stockId + " size of shenXian is not equal:" + spList.size() + "!=" + shenXianList.size());
			shenXianTable.delete(stockId);
			HistoryShenXianCountAndSaveDBRunner runner = new HistoryShenXianCountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		if ((spList.size() != qsddList.size())) {
			System.out.println(stockId + " size of QSDD is not equal:" + spList.size() + "!=" + qsddList.size());
			qsddTable.delete(stockId);
			HistoryQSDDCountAndSaveDBRunner runner = new HistoryQSDDCountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		// if ((spList.size() != ymbsList.size())) {
		// System.out.println(stockId + " size of YiMeng is not equal:" +
		// spList.size() + "!=" + ymbsList.size());
		// ymbsTable.delete(stockId);
		// HistoryYiMengBSCountAndSaveDBRunner runner = new
		// HistoryYiMengBSCountAndSaveDBRunner();
		// runner.countAndSaved(stockId);
		// }
	}

	public void sanityWeekCheck(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			if (index++ % 100 == 0) {
				System.out.println("Processing week " + index + "/" + stockIds.size());
			}
			this.sanityWeekCheck(stockId);
		}
		System.out.println("sanityWeekCheck completed.");
	}

	public void sanityWeekCheck(String stockId) {
		List<StockPriceVO> spList = weekStockPriceTable.getStockPriceById(stockId);
		List<MacdVO> macdList = macdWeekTable.getAllMacd(stockId);
		List<KDJVO> kdjList = kdjWeekTable.getAllKDJ(stockId);
		// List<ShenXianVO> shenXianList =
		// shenXianWeekTable.getAllShenXian(stockId);
		// List<QSDDVO> qsddList = qsddWeekTable.getAllQSDD(stockId);
		// List<BollVO> bollList = bollWeekTable.getAllBoll(stockId);
		// List<Mai1Mai2VO> mai1mai2List =
		// mai1mai2WeekTable.getAllMai1Mai2(stockId);
		// List<YiMengBSVO> yiMengBSList =
		// yiMengBSWeekTable.getAllYiMengBS(stockId);

		// if (spList.size() <= 108)
		// return;
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

			macdWeekTable.delete(stockId);
			HistoryWeeklyMacdCountAndSaveDBRunner runner = new HistoryWeeklyMacdCountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		if ((spList.size() != kdjList.size())) {
			System.out.println(stockId + " size of week kdj is not equal:" + spList.size() + "!=" + kdjList.size());
			kdjWeekTable.delete(stockId);
			HistoryWeeklyKDJCountAndSaveDBRunner runner = new HistoryWeeklyKDJCountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		// if ((spList.size() != shenXianList.size())) {
		// System.out.println(stockId + " size of week shenXian is not equal:" +
		// spList.size() + "!="
		// + shenXianList.size());
		// shenXianWeekTable.delete(stockId);
		// HistoryWeeklyShenXianCountAndSaveDBRunner runner = new
		// HistoryWeeklyShenXianCountAndSaveDBRunner();
		// runner.countAndSaved(stockId);
		// }
		// if ((spList.size() != qsddList.size())) {
		// System.out.println(stockId + " size of week QSDD is not equal:" +
		// spList.size() + "!=" + qsddList.size());
		// qsddWeekTable.delete(stockId);
		// HistoryWeeklyQSDDCountAndSaveDBRunner runner = new
		// HistoryWeeklyQSDDCountAndSaveDBRunner();
		// runner.countAndSaved(stockId);
		// }
		// if ((spList.size() != bollList.size())) {
		// System.out.println(stockId + " size of week boll is not equal:" +
		// spList.size() + "!=" + bollList.size());
		// bollWeekTable.delete(stockId);
		// HistoryWeeklyBollCountAndSaveDBRunner runner = new
		// HistoryWeeklyBollCountAndSaveDBRunner();
		// runner.countAndSaved(stockId);
		// }
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
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		DataBaseSanityCheck check = new DataBaseSanityCheck();
		check.sanityDailyCheck(stockConfig.getAllStockId());
		check.sanityWeekCheck(stockConfig.getAllStockId());
		// check.sanityDailyCheck("999999");
		// check.sanityDailyCheck("399001");
		// check.sanityDailyCheck("399006");
		// check.sanityWeekCheck("999999");
		// check.sanityWeekCheck("399001");
		// check.sanityWeekCheck("399006");
	}

	public static void main(String[] args) {
		new DataBaseSanityCheck().run();
	}
}
