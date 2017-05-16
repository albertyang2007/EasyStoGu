package org.easystogu.database.replicate;

import org.easystogu.database.replicate.runner.CompanyInfoReplicateWorker;
import org.easystogu.database.replicate.runner.DailySelectionReplicateWorker;
import org.easystogu.database.replicate.runner.DailyStatisticsReplicateWorker;
import org.easystogu.database.replicate.runner.IndBollReplicateWorker;
import org.easystogu.database.replicate.runner.IndDDXReplicateWorker;
import org.easystogu.database.replicate.runner.IndKDJReplicateWorker;
import org.easystogu.database.replicate.runner.IndMAReplicateWorker;
import org.easystogu.database.replicate.runner.IndMacdReplicateWorker;
import org.easystogu.database.replicate.runner.IndQSDDReplicateWorker;
import org.easystogu.database.replicate.runner.IndShenXianReplicateWorker;
import org.easystogu.database.replicate.runner.IndWRReplicateWorker;
import org.easystogu.database.replicate.runner.IndWeekKDJReplicateWorker;
import org.easystogu.database.replicate.runner.IndWeekMacdReplicateWorker;
import org.easystogu.database.replicate.runner.QianFuQuanStockPriceReplicateWorker;
import org.easystogu.database.replicate.runner.StockPriceReplicateWorker;
import org.easystogu.database.replicate.runner.WeekStockPriceReplicateWorker;
import org.easystogu.database.replicate.runner.ZiJinLiuReplicateWorker;

//sync data from aliyun to office
public class DailyReplicateRunner implements Runnable {

	public void run() {
		String[] args = null;
		// TODO Auto-generated method stub
		CompanyInfoReplicateWorker.main(args);
		StockPriceReplicateWorker.main(args);
		QianFuQuanStockPriceReplicateWorker.main(args);

		// daily ind
		IndMacdReplicateWorker.main(args);
		IndKDJReplicateWorker.main(args);
		IndShenXianReplicateWorker.main(args);
		IndWRReplicateWorker.main(args);
		IndMAReplicateWorker.main(args);
		IndQSDDReplicateWorker.main(args);
		IndBollReplicateWorker.main(args);

		// week
		WeekStockPriceReplicateWorker.main(args);
		IndWeekMacdReplicateWorker.main(args);
		IndWeekKDJReplicateWorker.main(args);

		// zijinliu & ddx
		ZiJinLiuReplicateWorker.main(args);
		IndDDXReplicateWorker.main(args);

		DailySelectionReplicateWorker.main(args);
		DailyStatisticsReplicateWorker.main(args);

		// should do santity to verify the data
	}

	public static void main(String[] args) {
		DailyReplicateRunner runner = new DailyReplicateRunner();
		runner.run();
	}
}
