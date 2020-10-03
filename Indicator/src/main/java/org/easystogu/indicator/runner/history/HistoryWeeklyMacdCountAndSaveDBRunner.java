package org.easystogu.indicator.runner.history;

import javax.annotation.PostConstruct;

import org.easystogu.cassandra.access.table.IndWeekMacdCassTableHelper;
import org.easystogu.config.Constants;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.postgresql.access.table.IndWeekMacdDBTableHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class HistoryWeeklyMacdCountAndSaveDBRunner extends HistoryMacdCountAndSaveDBRunner {
	@Autowired
	@Qualifier("weekStockPriceTable")
	private StockPriceTableHelper _stockPriceTable;
	@Autowired
	protected IndicatorDBHelperIF _macdTable = dBAccessFacdeFactory.getInstance(Constants.indWeekMacd);
	
	@PostConstruct
	public void init() {
		super.setMacdTable(_macdTable);
		super.setStockPriceTable(_stockPriceTable);
	}
	
	@Override
	public void validate() {
		if (this instanceof HistoryWeeklyMacdCountAndSaveDBRunner && stockPriceTable instanceof WeekStockPriceTableHelper
				&& (macdTable instanceof IndWeekMacdCassTableHelper || macdTable instanceof IndWeekMacdDBTableHelper)) {
			// pass
		} else {
			throw new RuntimeException("SubClass ERROR: This is " + this.getClass().getSimpleName()
					+ ", stockPriceTable is " + stockPriceTable.getClass().getSimpleName() + ", macdTable is "
					+ macdTable.getClass().getSimpleName());
		}
	}

	public void mainWork(String[] args) {
		// TODO Auto-generated method stub
		this.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}
}
