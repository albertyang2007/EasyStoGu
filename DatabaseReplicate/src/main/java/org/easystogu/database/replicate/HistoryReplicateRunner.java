package org.easystogu.database.replicate;

import org.easystogu.database.replicate.runner.DailySelectionReplicateWorker;
import org.easystogu.database.replicate.runner.DailyStatisticsReplicateWorker;
import org.easystogu.database.replicate.runner.IndDDXReplicateWorker;
import org.easystogu.database.replicate.runner.QianFuQuanStockPriceReplicateWorker;
import org.easystogu.database.replicate.runner.StockPriceReplicateWorker;
import org.easystogu.database.replicate.runner.ZiJinLiuReplicateWorker;
import org.easystogu.utils.WeekdayUtil;

public class HistoryReplicateRunner {
	public static void main(String[] args) {
		String[] myArgs = { "2016-11-20", WeekdayUtil.currentDate() };
		StockPriceReplicateWorker.main(myArgs);
		QianFuQuanStockPriceReplicateWorker.main(myArgs);
		ZiJinLiuReplicateWorker.main(myArgs);
		IndDDXReplicateWorker.main(myArgs);
		DailySelectionReplicateWorker.main(myArgs);
		DailyStatisticsReplicateWorker.main(myArgs);
	}
}
