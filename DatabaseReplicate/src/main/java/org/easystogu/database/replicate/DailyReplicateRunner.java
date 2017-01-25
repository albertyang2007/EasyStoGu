package org.easystogu.database.replicate;

import org.easystogu.database.replicate.runner.DailySelectionReplicateWorker;
import org.easystogu.database.replicate.runner.DailyStatisticsReplicateWorker;
import org.easystogu.database.replicate.runner.IndDDXReplicateWorker;
import org.easystogu.database.replicate.runner.QianFuQuanStockPriceReplicateWorker;
import org.easystogu.database.replicate.runner.StockPriceReplicateWorker;
import org.easystogu.database.replicate.runner.ZiJinLiuReplicateWorker;

public class DailyReplicateRunner {
	public static void main(String[] args) {
		StockPriceReplicateWorker.main(args);
		QianFuQuanStockPriceReplicateWorker.main(args);
		ZiJinLiuReplicateWorker.main(args);
		IndDDXReplicateWorker.main(args);
		DailySelectionReplicateWorker.main(args);
		DailyStatisticsReplicateWorker.main(args);
	}
}
