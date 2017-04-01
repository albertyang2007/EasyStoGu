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
import org.easystogu.utils.WeekdayUtil;

public class HistoryReplicateRunner {
	public static void main(String[] args) {
		String[] myArgs = { "2017-03-01", WeekdayUtil.currentDate() };
		CompanyInfoReplicateWorker.main(myArgs);
		StockPriceReplicateWorker.main(myArgs);
		QianFuQuanStockPriceReplicateWorker.main(myArgs);

		// daily ind
		//IndMacdReplicateWorker.main(myArgs);
		//IndKDJReplicateWorker.main(myArgs);
		//IndShenXianReplicateWorker.main(myArgs);
		//IndWRReplicateWorker.main(myArgs);
		//IndMAReplicateWorker.main(myArgs);
		//IndQSDDReplicateWorker.main(myArgs);
		//IndBollReplicateWorker.main(myArgs);

		// week
		//WeekStockPriceReplicateWorker.main(myArgs);
		//IndWeekMacdReplicateWorker.main(myArgs);
		//IndWeekKDJReplicateWorker.main(myArgs);

		// zijinliu & ddx
		ZiJinLiuReplicateWorker.main(myArgs);
		IndDDXReplicateWorker.main(myArgs);

		DailySelectionReplicateWorker.main(myArgs);
		DailyStatisticsReplicateWorker.main(myArgs);
	}
}
