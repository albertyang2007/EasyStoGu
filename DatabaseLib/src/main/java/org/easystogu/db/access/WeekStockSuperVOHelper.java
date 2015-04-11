package org.easystogu.db.access;

public class WeekStockSuperVOHelper extends StockSuperVOHelper {
	public WeekStockSuperVOHelper() {
		stockPriceTable = WeekStockPriceTableHelper.getInstance();
		macdTable = IndWeekMacdTableHelper.getInstance();
		kdjTable = IndWeekKDJTableHelper.getInstance();
		bollTable = IndWeekBollTableHelper.getInstance();
		shenXianTable = IndWeekShenXianTableHelper.getInstance();
	}
}
