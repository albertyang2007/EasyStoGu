package org.easystogu.database.replicate.runner;

import java.util.List;

import org.easystogu.db.access.table.IndWeekMacdTableHelper;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.utils.WeekdayUtil;

public class IndWeekMacdReplicateWorker {
	public String fromDate = WeekdayUtil.currentDate();
	public String toDate = WeekdayUtil.currentDate();
	private IndWeekMacdTableHelper localTable = IndWeekMacdTableHelper.getInstance();
	private IndWeekMacdTableHelper georedTable = IndWeekMacdTableHelper.getGeoredInstance();

	public void run() {
		List<String> dates = WeekdayUtil.getWorkingDatesBetween(fromDate, toDate);
		for (String date : dates) {
			runForDate(date);
		}
	}

	public void runForDate(String date) {

		System.out.println("Checking IndWeekMacdTable at " + date);

		List<MacdVO> localList = localTable.getByDate(date);
		List<MacdVO> georedList = georedTable.getByDate(date);
		// sync data from geored database to local if not match
		if (georedList.size() > 0 && localList.size() != georedList.size()) {
			System.out.println(date + " has different data, local size=" + localList.size() + ", geored size="
					+ georedList.size());

			System.out.println("delete local data @" + date + ", and sync from geored");
			localTable.deleteByDate(date);

			for (MacdVO vo : georedList) {
				// System.out.println("insert vo:" + vo);
				localTable.insert(vo);
			}
		}
	}

	public static void main(String[] args) {
		IndWeekMacdReplicateWorker worker = new IndWeekMacdReplicateWorker();
		if (args != null && args.length == 2) {
			worker.fromDate = args[0];
			worker.toDate = args[1];
		}
		worker.run();
	}
}