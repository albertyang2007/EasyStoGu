package org.easystogu.indicator.runner;

import java.util.List;

import javax.annotation.PostConstruct;

import org.easystogu.cassandra.access.table.IndWeekMacdCassTableHelper;
import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.postgresql.access.table.IndWeekMacdDBTableHelper;
import org.easystogu.utils.WeekdayUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DailyWeekMacdCountAndSaveDBRunner extends DailyMacdCountAndSaveDBRunner {
	private static Logger logger = LogHelper.getLogger(DailyWeekMacdCountAndSaveDBRunner.class);
	@Autowired
	@Qualifier("weekStockPriceTable")
	private StockPriceTableHelper _stockPriceTable;
	@Autowired
	protected DBAccessFacdeFactory dBAccessFacdeFactory;
	
	@PostConstruct
	public void init() {
		super.setMacdTable(dBAccessFacdeFactory.getInstance(Constants.indWeekMacd));
		super.setStockPriceTable(_stockPriceTable);
	}
	
	@Override
	public void validate() {
		if (this instanceof DailyWeekMacdCountAndSaveDBRunner && stockPriceTable instanceof WeekStockPriceTableHelper
				&& (macdTable instanceof IndWeekMacdCassTableHelper || macdTable instanceof IndWeekMacdDBTableHelper)) {
			// pass
		} else {
			throw new RuntimeException("SubClass ERROR: This is " + this.getClass().getSimpleName()
					+ ", stockPriceTable is " + stockPriceTable.getClass().getSimpleName() + ", macdTable is "
					+ macdTable.getClass().getSimpleName());
		}
	}

	@Override
	public void deleteMacd(String stockId, String date) {
		List<String> dates = WeekdayUtil.getWeekWorkingDates(date);
		// first clean one tuple in week_stockprice table
		// loop all this week's date, in fact, only one tuple match and del
		for (String d : dates) {
			macdTable.delete(stockId, d);
		}
	}

	public void mainWork(String[] args) {
		// TODO Auto-generated method stub
		this.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002327");
	}

}
