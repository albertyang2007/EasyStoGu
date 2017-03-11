package org.easystogu.database.replicate.runner;

import java.util.List;

import org.easystogu.db.access.table.IndWeekKDJTableHelper;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.utils.WeekdayUtil;

public class IndWeekKDJReplicateWorker {
	public String fromDate = WeekdayUtil.currentDate();
	public String toDate = WeekdayUtil.currentDate();
	private IndWeekKDJTableHelper localTable = IndWeekKDJTableHelper.getInstance();
	private IndWeekKDJTableHelper georedTable = IndWeekKDJTableHelper.getGeoredInstance();

	public void run() {
		List<String> dates = WeekdayUtil.getWorkingDatesBetween(fromDate, toDate);
		for (String date : dates) {
			runForDate(date);
		}
	}

	public void runForDate(String date) {

		System.out.println("Checking IndWeekKDJTable at " + date);

		List<KDJVO> localList = localTable.getByDate(date);
		List<KDJVO> georedList = georedTable.getByDate(date);
		// sync data from geored database to local if not match
		if (georedList.size() > 0 && localList.size() != georedList.size()) {
			System.out.println(date + " has different data, local size=" + localList.size() + ", geored size="
					+ georedList.size());

			System.out.println("delete local data @" + date + ", and sync from geored");
			localTable.deleteByDate(date);

			for (KDJVO vo : georedList) {
				// System.out.println("insert vo:" + vo);
				localTable.insert(vo);
			}
		}
	}

	public static void main(String[] args) {
		IndWeekKDJReplicateWorker worker = new IndWeekKDJReplicateWorker();
		if (args != null && args.length == 2) {
			worker.fromDate = args[0];
			worker.toDate = args[1];
		}
		worker.run();
	}

}
