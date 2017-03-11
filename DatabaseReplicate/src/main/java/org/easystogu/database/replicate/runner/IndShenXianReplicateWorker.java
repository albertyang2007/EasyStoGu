package org.easystogu.database.replicate.runner;

import java.util.List;

import org.easystogu.db.access.table.IndShenXianTableHelper;
import org.easystogu.db.vo.table.ShenXianVO;
import org.easystogu.utils.WeekdayUtil;

public class IndShenXianReplicateWorker {
	public String fromDate = WeekdayUtil.currentDate();
	public String toDate = WeekdayUtil.currentDate();
	private IndShenXianTableHelper localTable = IndShenXianTableHelper.getInstance();
	private IndShenXianTableHelper georedTable = IndShenXianTableHelper.getGeoredInstance();

	public void run() {
		List<String> dates = WeekdayUtil.getWorkingDatesBetween(fromDate, toDate);
		for (String date : dates) {
			runForDate(date);
		}
	}

	public void runForDate(String date) {

		System.out.println("Checking IndShenXianTable at " + date);

		List<ShenXianVO> localList = localTable.getByDate(date);
		List<ShenXianVO> georedList = georedTable.getByDate(date);
		// sync data from geored database to local if not match
		if (georedList.size() > 0 && localList.size() != georedList.size()) {
			System.out.println(date + " has different data, local size=" + localList.size() + ", geored size="
					+ georedList.size());

			System.out.println("delete local data @" + date + ", and sync from geored");
			localTable.deleteByDate(date);

			for (ShenXianVO vo : georedList) {
				// System.out.println("insert vo:" + vo);
				localTable.insert(vo);
			}
		}
	}

	public static void main(String[] args) {
		IndShenXianReplicateWorker worker = new IndShenXianReplicateWorker();
		if (args != null && args.length == 2) {
			worker.fromDate = args[0];
			worker.toDate = args[1];
		}
		worker.run();
	}
}
