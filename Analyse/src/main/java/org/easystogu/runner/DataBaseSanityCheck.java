package org.easystogu.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndBollTableHelper;
import org.easystogu.db.access.IndKDJTableHelper;
import org.easystogu.db.access.IndMacdTableHelper;
import org.easystogu.db.access.IndMai1Mai2TableHelper;
import org.easystogu.db.access.IndShenXianTableHelper;
import org.easystogu.db.access.IndWeekBollTableHelper;
import org.easystogu.db.access.IndWeekKDJTableHelper;
import org.easystogu.db.access.IndWeekMacdTableHelper;
import org.easystogu.db.access.IndWeekMai1Mai2TableHelper;
import org.easystogu.db.access.IndWeekShenXianTableHelper;
import org.easystogu.db.access.IndWeekYiMengBSTableHelper;
import org.easystogu.db.access.IndXueShi2TableHelper;
import org.easystogu.db.access.IndYiMengBSTableHelper;
import org.easystogu.db.access.IndZhuliJinChuTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.access.WeekStockPriceTableHelper;
import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.Mai1Mai2VO;
import org.easystogu.db.table.ShenXianVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.table.XueShi2VO;
import org.easystogu.db.table.YiMengBSVO;
import org.easystogu.db.table.ZhuliJinChuVO;
import org.easystogu.indicator.runner.history.HistoryBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryMai1Mai2CountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryShenXianCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryWeeklyBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryWeeklyKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryWeeklyMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryWeeklyMai1Mai2CountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryWeeklyShenXianCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryWeeklyYiMengBSCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryXueShi2CountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryYiMengBSCountAndSaveDBRunner;
import org.easystogu.indicator.runner.history.HistoryZhuliJinChuCountAndSaveDBRunner;

public class DataBaseSanityCheck implements Runnable {
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
	protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
	protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
	protected IndMai1Mai2TableHelper mai1mai2Table = IndMai1Mai2TableHelper.getInstance();
	protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();
	protected IndXueShi2TableHelper xueShi2Table = IndXueShi2TableHelper.getInstance();
	protected IndYiMengBSTableHelper yiMengBSTable = IndYiMengBSTableHelper.getInstance();
	protected IndZhuliJinChuTableHelper zhuliJinChuTable = IndZhuliJinChuTableHelper.getInstance();

	protected WeekStockPriceTableHelper weekStockPriceTable = WeekStockPriceTableHelper.getInstance();
	protected IndWeekMacdTableHelper macdWeekTable = IndWeekMacdTableHelper.getInstance();
	protected IndWeekKDJTableHelper kdjWeekTable = IndWeekKDJTableHelper.getInstance();
	protected IndWeekBollTableHelper bollWeekTable = IndWeekBollTableHelper.getInstance();
	protected IndWeekMai1Mai2TableHelper mai1mai2WeekTable = IndWeekMai1Mai2TableHelper.getInstance();
	protected IndWeekShenXianTableHelper shenXianWeekTable = IndWeekShenXianTableHelper.getInstance();
	protected IndWeekYiMengBSTableHelper yiMengBSWeekTable = IndWeekYiMengBSTableHelper.getInstance();

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
		List<MacdVO> macdList = macdTable.getAllMacd(stockId);
		List<KDJVO> kdjList = kdjTable.getAllKDJ(stockId);
		List<BollVO> bollList = bollTable.getAllBoll(stockId);
		List<ShenXianVO> shenXianList = shenXianTable.getAllShenXian(stockId);
		List<XueShi2VO> xueShie2List = xueShi2Table.getAllXueShi2(stockId);
		List<Mai1Mai2VO> mai1mai2List = mai1mai2Table.getAllMai1Mai2(stockId);
		List<ZhuliJinChuVO> zhuliJinChuList = zhuliJinChuTable.getAllZhuliJinChu(stockId);
		List<YiMengBSVO> yiMengBSList = yiMengBSTable.getAllYiMengBS(stockId);

		if (spList.size() <= 108)
			return;

		if ((spList.size() != macdList.size())) {
			System.out.println(stockId + " size of macd is not equal:" + spList.size() + "!=" + macdList.size());
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
		if ((spList.size() != xueShie2List.size())) {
			System.out.println(stockId + " size of xueShi2 is not equal:" + spList.size() + "!=" + xueShie2List.size());
			xueShi2Table.delete(stockId);
			HistoryXueShi2CountAndSaveDBRunner runner = new HistoryXueShi2CountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		if ((spList.size() != mai1mai2List.size())) {
			System.out
					.println(stockId + " size of mai1mai2 is not equal:" + spList.size() + "!=" + mai1mai2List.size());
			mai1mai2Table.delete(stockId);
			HistoryMai1Mai2CountAndSaveDBRunner runner = new HistoryMai1Mai2CountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		if ((spList.size() != zhuliJinChuList.size())) {
			System.out.println(stockId + " size of ziJiJinChu is not equal:" + spList.size() + "!="
					+ zhuliJinChuList.size());
			zhuliJinChuTable.delete(stockId);
			HistoryZhuliJinChuCountAndSaveDBRunner runner = new HistoryZhuliJinChuCountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		if ((spList.size() != yiMengBSList.size())) {
			System.out
					.println(stockId + " size of yiMengBS is not equal:" + spList.size() + "!=" + yiMengBSList.size());
			yiMengBSTable.delete(stockId);
			HistoryYiMengBSCountAndSaveDBRunner runner = new HistoryYiMengBSCountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
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
		List<BollVO> bollList = bollWeekTable.getAllBoll(stockId);
		List<ShenXianVO> shenXianList = shenXianWeekTable.getAllShenXian(stockId);
		List<Mai1Mai2VO> mai1mai2List = mai1mai2WeekTable.getAllMai1Mai2(stockId);
		List<YiMengBSVO> yiMengBSList = yiMengBSWeekTable.getAllYiMengBS(stockId);

		if (spList.size() <= 108)
			return;

		if ((spList.size() != macdList.size())) {
			System.out.println(stockId + " size of macd is not equal:" + spList.size() + "!=" + macdList.size());
			macdWeekTable.delete(stockId);
			HistoryWeeklyMacdCountAndSaveDBRunner runner = new HistoryWeeklyMacdCountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		if ((spList.size() != kdjList.size())) {
			System.out.println(stockId + " size of kdj is not equal:" + spList.size() + "!=" + kdjList.size());
			kdjWeekTable.delete(stockId);
			HistoryWeeklyKDJCountAndSaveDBRunner runner = new HistoryWeeklyKDJCountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		if ((spList.size() != bollList.size())) {
			System.out.println(stockId + " size of boll is not equal:" + spList.size() + "!=" + bollList.size());
			bollWeekTable.delete(stockId);
			HistoryWeeklyBollCountAndSaveDBRunner runner = new HistoryWeeklyBollCountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		if ((spList.size() != shenXianList.size())) {
			System.out
					.println(stockId + " size of shenXian is not equal:" + spList.size() + "!=" + shenXianList.size());
			shenXianWeekTable.delete(stockId);
			HistoryWeeklyShenXianCountAndSaveDBRunner runner = new HistoryWeeklyShenXianCountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		if ((spList.size() != mai1mai2List.size())) {
			System.out
					.println(stockId + " size of mai1mai2 is not equal:" + spList.size() + "!=" + mai1mai2List.size());
			mai1mai2WeekTable.delete(stockId);
			HistoryWeeklyMai1Mai2CountAndSaveDBRunner runner = new HistoryWeeklyMai1Mai2CountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
		if ((spList.size() != yiMengBSList.size())) {
			System.out
					.println(stockId + " size of yiMengBS is not equal:" + spList.size() + "!=" + yiMengBSList.size());
			yiMengBSWeekTable.delete(stockId);
			HistoryWeeklyYiMengBSCountAndSaveDBRunner runner = new HistoryWeeklyYiMengBSCountAndSaveDBRunner();
			runner.countAndSaved(stockId);
		}
	}

	public void run() {
		// TODO Auto-generated method stub
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();
		DataBaseSanityCheck check = new DataBaseSanityCheck();
		check.sanityDailyCheck(stockConfig.getAllStockId());
		check.sanityWeekCheck(stockConfig.getAllStockId());
		// check.sanityDailyCheck("300039");
		// check.sanityWeekCheck("300268");
	}

	public static void main(String[] args) {
		new DataBaseSanityCheck().run();
	}
}
