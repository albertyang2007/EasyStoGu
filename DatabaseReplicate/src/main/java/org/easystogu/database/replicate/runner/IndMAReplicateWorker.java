package org.easystogu.database.replicate.runner;

import java.util.List;

import org.easystogu.db.access.table.IndMATableHelper;
import org.easystogu.db.vo.table.MAVO;
import org.easystogu.utils.WeekdayUtil;

public class IndMAReplicateWorker {
	public String fromDate = WeekdayUtil.currentDate();
	public String toDate = WeekdayUtil.currentDate();
	private IndMATableHelper localTable = IndMATableHelper.getInstance();
	private IndMATableHelper georedTable = IndMATableHelper.getGeoredInstance();

	public void run() {
		List<String> dates = WeekdayUtil.getWorkingDatesBetween(fromDate, toDate);
		for (String date : dates) {
			runForDate(date);
		}
	}

	public void runForDate(String date) {

		System.out.println("Checking IndMATable at " + date);

		List<MAVO> localList = localTable.getByDate(date);
		List<MAVO> georedList = georedTable.getByDate(date);
		// sync data from geored database to local if not match
		if (georedList.size() > 0 && localList.size() != georedList.size()) {
			System.out.println(date + " has different data, local size=" + localList.size() + ", geored size="
					+ georedList.size());

			System.out.println("delete local data @" + date + ", and sync from geored");
			localTable.deleteByDate(date);

			for (MAVO vo : georedList) {
				// System.out.println("insert vo:" + vo);
				localTable.insert(vo);
			}
		}
	}

	public static void main(String[] args) {
		IndMAReplicateWorker worker = new IndMAReplicateWorker();
		if (args != null && args.length == 2) {
			worker.fromDate = args[0];
			worker.toDate = args[1];
		}
		worker.run();
	}
}
