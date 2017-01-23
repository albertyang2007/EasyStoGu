package org.easystogu.database.replicate;

import java.util.List;

import org.easystogu.db.access.table.CompanyInfoTableHelper;
import org.easystogu.db.vo.table.CompanyInfoVO;

//Replicate the two database
//One is Active and Other is Standby
//Data from Active will overwrite the Standby
public class CompanyInfoReplicateWorker implements Runnable {
	private CompanyInfoTableHelper localTable = CompanyInfoTableHelper.getInstance();
	private CompanyInfoTableHelper georedTable = CompanyInfoTableHelper.getGeoredInstance();

	public void run() {

		System.out.println("Checking CompanyInfoTable.");

		List<CompanyInfoVO> localList = localTable.getAllCompanyInfo();
		List<CompanyInfoVO> georedList = georedTable.getAllCompanyInfo();
		// sync data from geored database to local if not match
		if (georedList.size() > 0 && localList.size() != georedList.size()) {
			System.out.println(
					"Has different data, local size=" + localList.size() + ", geored size=" + georedList.size());

			System.out.println("delete local data, and sync from geored");

			for (CompanyInfoVO vo : georedList) {
				localTable.delete(vo.stockId);
				// System.out.println("insert vo:" + vo);
				localTable.insert(vo);
			}
		}
	}

	public static void main(String[] args) {
		CompanyInfoReplicateWorker worker = new CompanyInfoReplicateWorker();
		worker.run();
	}
}
