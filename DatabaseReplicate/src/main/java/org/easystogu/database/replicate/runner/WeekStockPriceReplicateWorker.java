package org.easystogu.database.replicate.runner;

import java.util.List;

import org.easystogu.db.access.table.WeekStockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.utils.WeekdayUtil;

public class WeekStockPriceReplicateWorker {
	public String fromDate = WeekdayUtil.currentDate();
	public String toDate = WeekdayUtil.currentDate();
	private WeekStockPriceTableHelper localTable = WeekStockPriceTableHelper.getInstance();
	private WeekStockPriceTableHelper georedTable = WeekStockPriceTableHelper.getGeoredInstance();

	public void run() {
		List<String> dates = WeekdayUtil.getWorkingDatesBetween(fromDate, toDate);
		for (String date : dates) {
			runForDate(date);
		}
	}

	public void runForDate(String date) {

		System.out.println("Checking WeekStockPriceTable at " + date);

		List<StockPriceVO> localList = localTable.getAllStockPriceByDate(date);
		List<StockPriceVO> georedList = georedTable.getAllStockPriceByDate(date);
		// sync data from geored database to local if not match
		if (georedList.size() > 0 && localList.size() != georedList.size()) {
			System.out.println(date + " has different data, local size=" + localList.size() + ", geored size="
					+ georedList.size());

			System.out.println("delete local data @" + date + ", and sync from geored");
			localTable.deleteByDate(date);

			for (StockPriceVO vo : georedList) {
				// System.out.println("insert vo:" + vo);
				localTable.insert(vo);
			}
		}
	}

	public static void main(String[] args) {
		WeekStockPriceReplicateWorker worker = new WeekStockPriceReplicateWorker();
		if (args != null && args.length == 2) {
			worker.fromDate = args[0];
			worker.toDate = args[1];
		}
		worker.run();
	}
}
