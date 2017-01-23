package org.easystogu.database.replicate;

import java.util.List;

import org.easystogu.db.access.table.CheckPointDailyStatisticsTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.CheckPointDailyStatisticsVO;
import org.easystogu.utils.WeekdayUtil;
import javax.sql.DataSource;

public class DailyStatisticsReplicateWorker {
	public String fromDate;
	public String toDate;
	private StockPriceTableHelper localStockPriceTable = StockPriceTableHelper.getInstance();
	private StockPriceTableHelper georedStockPriceTable = StockPriceTableHelper.getGeoredInstance();
	private CheckPointDailyStatisticsTableHelper localTable = CheckPointDailyStatisticsTableHelper.getInstance();
	private CheckPointDailyStatisticsTableHelper georedTable = CheckPointDailyStatisticsTableHelper.getGeoredInstance();

	public void run() {
		List<String> dates = WeekdayUtil.getWorkingDatesBetween(fromDate, toDate);
		for (String date : dates) {
			runForDate(date);
		}
	}

	public void runForDate(String date) {
		List<CheckPointDailyStatisticsVO> localList = localTable.getByDate(date);
		List<CheckPointDailyStatisticsVO> georedList = georedTable.getByDate(date);
		System.out.println("LocalSize=" + localList.size() + "\t@ " + date);
		System.out.println("GeoreSize=" + georedList.size() + "\t@ " + date);
	}

	public static void main(String[] args) {
		DailyStatisticsReplicateWorker worker = new DailyStatisticsReplicateWorker();
		worker.fromDate = "2016-11-20";
		worker.toDate = WeekdayUtil.currentDate();
		worker.run();
	}
}
