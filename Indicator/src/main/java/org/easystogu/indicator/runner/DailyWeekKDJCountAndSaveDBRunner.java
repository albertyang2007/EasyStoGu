package org.easystogu.indicator.runner;

import java.util.List;

import javax.annotation.PostConstruct;

import org.easystogu.cassandra.access.table.IndWeekKDJCassTableHelper;
import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.postgresql.access.table.IndWeekKDJDBTableHelper;
import org.easystogu.utils.WeekdayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DailyWeekKDJCountAndSaveDBRunner extends DailyKDJCountAndSaveDBRunner {
	@Autowired
	@Qualifier("weekStockPriceTable")
	private StockPriceTableHelper _stockPriceTable;
	@Autowired
	protected DBAccessFacdeFactory dBAccessFacdeFactory;
	
	@PostConstruct
	public void init() {
		super.setKdjTable(dBAccessFacdeFactory.getInstance(Constants.indWeekKDJ));
		super.setStockPriceTable(_stockPriceTable);
	}
	
	@Override
	public void validate() {
		if (this instanceof DailyWeekKDJCountAndSaveDBRunner && stockPriceTable instanceof WeekStockPriceTableHelper
				&& (kdjTable instanceof IndWeekKDJCassTableHelper || kdjTable instanceof IndWeekKDJDBTableHelper)) {
			// pass
		} else {
			throw new RuntimeException("SubClass ERROR: This is " + this.getClass().getSimpleName()
					+ ", stockPriceTable is " + stockPriceTable.getClass().getSimpleName() + ", kdjTable is "
					+ kdjTable.getClass().getSimpleName());
		}
	}

	@Override
	public void deleteKDJ(String stockId, String date) {
		List<String> dates = WeekdayUtil.getWeekWorkingDates(date);
		// first clean one tuple in week_stockprice table
		// loop all this week's date, in fact, only one tuple match and del
		for (String d : dates) {
			kdjTable.delete(stockId, d);
		}
	}
}
