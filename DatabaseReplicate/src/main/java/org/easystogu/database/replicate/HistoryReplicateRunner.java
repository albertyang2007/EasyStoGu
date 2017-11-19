package org.easystogu.database.replicate;

import org.easystogu.database.replicate.runner.CompanyInfoReplicateWorker;
import org.easystogu.database.replicate.runner.DailySelectionReplicateWorker;
import org.easystogu.database.replicate.runner.DailyStatisticsReplicateWorker;
import org.easystogu.database.replicate.runner.IndDDXReplicateWorker;
import org.easystogu.database.replicate.runner.QianFuQuanStockPriceReplicateWorker;
import org.easystogu.database.replicate.runner.StockPriceReplicateWorker;
import org.easystogu.database.replicate.runner.WeekStockPriceReplicateWorker;
import org.easystogu.database.replicate.runner.ZiJinLiuReplicateWorker;

public class HistoryReplicateRunner {
	public static void main(String[] args) {
		String[] myArgs = { "2017-05-17", "2017-05-18" };
		
		CompanyInfoReplicateWorker.main(myArgs);
		StockPriceReplicateWorker.main(myArgs);
		QianFuQuanStockPriceReplicateWorker.main(myArgs);

		// daily ind

		// week
		WeekStockPriceReplicateWorker.main(myArgs);

		// zijinliu & ddx
		ZiJinLiuReplicateWorker.main(myArgs);
		IndDDXReplicateWorker.main(myArgs);

		DailySelectionReplicateWorker.main(myArgs);
		DailyStatisticsReplicateWorker.main(myArgs);
		
		// should do santity to verify the data
	}
}
