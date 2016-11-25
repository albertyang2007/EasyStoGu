package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.db.access.table.WSFConfigTableHelper;

public class AllDailyIndCountAndSaveDBRunner {
	private WSFConfigTableHelper wsfConfig = WSFConfigTableHelper.getInstance();
	private String zone = wsfConfig.getValue("zone", Constants.ZONE_OFFICE);

	public void runDailyIndForStockIds(List<String> stockIds) {
		// day ind
		if (Constants.ZONE_OFFICE.equalsIgnoreCase(zone)) {
			new DailyMacdCountAndSaveDBRunner().countAndSaved(stockIds);
			new DailyKDJCountAndSaveDBRunner().countAndSaved(stockIds);
			new DailyBollCountAndSaveDBRunner().countAndSaved(stockIds);
			new DailyShenXianCountAndSaveDBRunner().countAndSaved(stockIds);
			new DailyQSDDCountAndSaveDBRunner().countAndSaved(stockIds);
			new DailyWRCountAndSaveDBRunner().countAndSaved(stockIds);
			new DailyMACountAndSaveDBRunner().countAndSaved(stockIds);
			// new DailyYiMengBSCountAndSaveDBRunner().countAndSaved(stockIds);
			// new DailyMai1Mai2CountAndSaveDBRunner().countAndSaved(stockIds);
			// new DailyXueShi2CountAndSaveDBRunner().countAndSaved(stockIds);
			// new
			// DailyZhuliJinChuCountAndSaveDBRunner().countAndSaved(stockIds);
			// new DailyYiMengBSCountAndSaveDBRunner().countAndSaved(stockIds);
		} else if (Constants.ZONE_ALIYUN.equalsIgnoreCase(zone)) {
			new DailyShenXianCountAndSaveDBRunner().countAndSaved(stockIds);
			new DailyWRCountAndSaveDBRunner().countAndSaved(stockIds);
			new DailyMACountAndSaveDBRunner().countAndSaved(stockIds);
		}
	}

	public void runDailyWeekIndForStockIds(List<String> stockIds) {
		// week ind
		if (Constants.ZONE_OFFICE.equalsIgnoreCase(zone)) {
			new DailyWeekMacdCountAndSaveDBRunner().countAndSaved(stockIds);
			new DailyWeekKDJCountAndSaveDBRunner().countAndSaved(stockIds);
			// new DailyWeekBollCountAndSaveDBRunner().countAndSaved(stockIds);
			// new
			// DailyWeekMai1Mai2CountAndSaveDBRunner().countAndSaved(stockIds);
			// new
			// DailyWeekShenXianCountAndSaveDBRunner().countAndSaved(stockIds);
			// new
			// DailyWeekYiMengBSCountAndSaveDBRunner().countAndSaved(stockIds);
			// new DailyWeekQSDDCountAndSaveDBRunner().countAndSaved(stockIds);
		} else if (Constants.ZONE_ALIYUN.equalsIgnoreCase(zone)){

		}
	}
}
