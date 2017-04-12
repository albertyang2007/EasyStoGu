package org.easystogu.database.replicate.runner;

import java.util.List;

import org.easystogu.db.access.table.CheckPointDailySelectionTableHelper;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.utils.WeekdayUtil;

public class DailySelectionReplicateWorker {
	public String fromDate = WeekdayUtil.currentDate();
	public String toDate = WeekdayUtil.currentDate();
	private CheckPointDailySelectionTableHelper localTable = CheckPointDailySelectionTableHelper.getInstance();
	private CheckPointDailySelectionTableHelper georedTable = CheckPointDailySelectionTableHelper.getGeoredInstance();

	public void run() {
		List<String> dates = WeekdayUtil.getWorkingDatesBetween(fromDate, toDate);
		for (String date : dates) {
			runForDate(date);
		}
	}

	public void runForDate(String date) {

		System.out.println("Checking CheckPointDailySelectionTable at " + date);

		List<CheckPointDailySelectionVO> localList = localTable.getCheckPointByDate(date);
		List<CheckPointDailySelectionVO> georedList = georedTable.getCheckPointByDate(date);
		// sync data from geored database to local if not match
		if (georedList.size() > 0 && localList.size() != georedList.size()) {
			System.out.println(date + " has different data, local size=" + localList.size() + ", geored size="
					+ georedList.size());

			System.out.println("delete local data @" + date + ", and sync from geored");
			localTable.deleteByDate(date);

			for (CheckPointDailySelectionVO vo : georedList) {
				// System.out.println("insert vo:" + vo);
				localTable.insert(vo);
			}
		}
	}

	public static void main(String[] args) {
		DailySelectionReplicateWorker worker = new DailySelectionReplicateWorker();
		if (args != null && args.length == 2) {
			worker.fromDate = args[0];
			worker.toDate = args[1];
		}
		worker.run();
	}
}
