package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.IndMacdTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.IndMacdVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.indicator.TALIBWraper;

//每日根据最新数据计算当天的macd值，每天运行一次
public class MacdDailyCountAndSaveDBRunner {
	private IndMacdTableHelper macdTable = new IndMacdTableHelper();
	private StockPriceTableHelper stockPriceTable = new StockPriceTableHelper();
	private TALIBWraper talib = new TALIBWraper();

	public void countAndSaved(String stockId) {
		try {
			List<StockPriceVO> list = stockPriceTable
					.getStockPriceById(stockId);

			int length = list.size();
			double[] close = new double[length];
			int index = 0;
			for (StockPriceVO vo : list) {
				close[index++] = vo.close;
			}

			double[][] macd = talib.getMacdExt(close, 12, 26, 9);

			index = list.size() - 1;
			double dif = macd[0][index];
			double dea = macd[1][index];
			double macdRtn = (dif - dea) * 2;
			// System.out.println("DIF=" + dif);
			// System.out.println("DEA=" + dea);
			// System.out.println("MACD=" + macdRtn);

			IndMacdVO macdVo = new IndMacdVO();
			macdVo.setStockId(stockId);
			macdVo.setDate(list.get(index).date);
			macdVo.setDif(dif);
			macdVo.setDea(dea);
			macdVo.setMacd(macdRtn);

			macdTable.insert(macdVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void countAndSaved(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("MACD countAndSaved: " + stockId + " "
					+ (++index) + "/" + stockIds.size());
			this.countAndSaved(stockId);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockListConfigurationService stockConfig = StockListConfigurationService
				.getInstance();
		MacdDailyCountAndSaveDBRunner runner = new MacdDailyCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
	}

}
