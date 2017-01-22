package org.easystogu.database.replicate;

import java.util.List;

import org.easystogu.db.access.table.CompanyInfoTableHelper;
import org.easystogu.db.ds.PostgreSqlDataSourceFactory;
import org.easystogu.db.vo.table.CompanyInfoVO;

//Replicate the two database
//One is Active and Other is Standby
//Data from Active will overwrite the Standby
public class ReplicateWorker implements Runnable {

	private CompanyInfoTableHelper companyInfoTable = CompanyInfoTableHelper.getInstance();
	private CompanyInfoTableHelper companyInfoTableGeored = CompanyInfoTableHelper
			.getConfigInstance(PostgreSqlDataSourceFactory.createGeoredDataSource());

	public void run() {
		List<CompanyInfoVO> List = companyInfoTable.getAllCompanyInfo();
		List<CompanyInfoVO> georedList = companyInfoTableGeored.getAllCompanyInfo();

		for (CompanyInfoVO vo : List) {
			boolean find = false;
			for (CompanyInfoVO geovo : georedList) {
				if (vo.stockId.equals(geovo.stockId)) {
					find = true;
					break;
				}
			}
			if (!find) {
				System.out.println(vo.stockId + " not found in geored");
			}
		}

		//
		for (CompanyInfoVO geovo : georedList) {
			boolean find = false;
			for (CompanyInfoVO vo : List) {
				if (vo.stockId.equals(geovo.stockId)) {
					find = true;
					break;
				}
			}
			if (!find) {
				System.out.println(geovo.stockId + " not found in local");
			}
		}
	}

	public static void main(String[] args) {
		ReplicateWorker worker = new ReplicateWorker();
		worker.run();
	}

}
