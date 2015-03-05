package org.easystogu.runner;

import java.util.List;

import org.easystogu.analyse.util.IndCrossCheckingHelper;
import org.easystogu.analyse.util.VolumeCheckingHelper;
import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.StockSuperVOHelper;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.utils.CrossType;

public class HistoryAnalyseRunner {

	private StockSuperVOHelper stockOverAllHelper = new StockSuperVOHelper();

	public void analyseHistoryMackKDJVol(String stockId) {
		List<StockSuperVO> overList = stockOverAllHelper
				.getAllStockSuperVO(stockId);

		// count and update the macd/kdj corss
		IndCrossCheckingHelper.macdCross(overList);
		IndCrossCheckingHelper.kdjCross(overList);
		VolumeCheckingHelper.volumeIncreasePuls(overList);

		for (int index = 0; index < overList.size() - 1; index++) {
			StockSuperVO spVO = overList.get(index);
			StockSuperVO spNextVO = overList.get(index + 1);
			int gordonCrossNumberInTwoDays = 0;
			if (spVO.volumeIncreasePercent >= 2.0) {
				if (spVO.macdCorssType == CrossType.GORDON
						|| spVO.kdjCorssType == CrossType.GORDON) {
					gordonCrossNumberInTwoDays++;
					// System.out.println(spVO);
				}
			}
			if (spNextVO.volumeIncreasePercent >= 2.0) {
				if (spNextVO.macdCorssType == CrossType.GORDON
						|| spNextVO.kdjCorssType == CrossType.GORDON) {
					gordonCrossNumberInTwoDays++;
					// System.out.println(spNextVO);
				}
			}

			if (gordonCrossNumberInTwoDays == 2) {
				StockSuperVO spNextNextVO = overList.get(index + 2);
				System.out
						.println(stockId + " two Gordon! \n" + spVO.priceVO
								+ "\n" + spNextVO.priceVO + "\n"
								+ spNextNextVO.priceVO);
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockListConfigurationService stockConfig = StockListConfigurationService
				.getInstance();
		HistoryAnalyseRunner runner = new HistoryAnalyseRunner();

		List<String> stockIds = stockConfig.getAllStockId();

		for (String stockId : stockIds) {
			runner.analyseHistoryMackKDJVol(stockId);
		}
	}

}
