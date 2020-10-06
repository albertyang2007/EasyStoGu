package org.easystogu.indicator.runner.history;

import javax.annotation.PostConstruct;

import org.easystogu.cassandra.access.table.IndWeekKDJCassTableHelper;
import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.postgresql.access.table.IndWeekKDJDBTableHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("weekKdjRunner")
public class HistoryWeeklyKDJCountAndSaveDBRunner extends HistoryKDJCountAndSaveDBRunner {
	private static Logger logger = LogHelper.getLogger(HistoryWeeklyKDJCountAndSaveDBRunner.class);
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
		if (this instanceof HistoryWeeklyKDJCountAndSaveDBRunner && stockPriceTable instanceof WeekStockPriceTableHelper
				&& (kdjTable instanceof IndWeekKDJCassTableHelper || kdjTable instanceof IndWeekKDJDBTableHelper)) {
			// pass
		} else {
			throw new RuntimeException("SubClass ERROR: This is " + this.getClass().getSimpleName()
					+ ", stockPriceTable is " + stockPriceTable.getClass().getSimpleName() + ", kdjTable is "
					+ kdjTable.getClass().getSimpleName());
		}
	}

	public void mainWork(String[] args) {
		// TODO Auto-generated method stub
		this.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}
}
