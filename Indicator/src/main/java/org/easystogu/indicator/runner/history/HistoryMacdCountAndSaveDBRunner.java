package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.cassandra.access.table.IndMacdCassTableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.MACDHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.postgresql.access.table.IndMacdDBTableHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//计算数据库中所有macd值，包括最新和历史的，一次性运行
@Component("macdRunner")
public class HistoryMacdCountAndSaveDBRunner {
	private static Logger logger = LogHelper.getLogger(HistoryMacdCountAndSaveDBRunner.class);
	@Autowired
	@Qualifier("macdTable")
	protected IndicatorDBHelperIF macdTable;
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	protected CompanyInfoFileHelper stockConfig;
	@Autowired
	protected MACDHelper macdHelper;
	
	public void validate() {
		if (this instanceof HistoryMacdCountAndSaveDBRunner && stockPriceTable instanceof QianFuQuanStockPriceTableHelper
				&& (macdTable instanceof IndMacdCassTableHelper || macdTable instanceof IndMacdDBTableHelper)) {
			// pass
		} else {
			throw new RuntimeException("SubClass ERROR: This is " + this.getClass().getSimpleName()
					+ ", stockPriceTable is " + stockPriceTable.getClass().getSimpleName() + ", macdTable is "
					+ macdTable.getClass().getSimpleName());
		}
	}

	public void deleteMacd(String stockId) {
		macdTable.delete(stockId);
	}

	public void deleteMacd(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			logger.debug("Delete MACD for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteMacd(stockId);
		}
	}

	public void countAndSaved(String stockId) {
		this.deleteMacd(stockId);

		try {
			List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

			int length = priceList.size();

			if (length < 1) {
				return;
			}

			double[] close = new double[length];
			int index = 0;
			for (StockPriceVO vo : priceList) {
				close[index++] = vo.close;
			}

			double[][] macd = macdHelper.getMACDList(close);

			for (index = priceList.size() - 1; index >= 0; index--) {
				double dif = macd[0][index];
				double dea = macd[1][index];
				double macdRtn = macd[2][index];
				// logger.debug("DIF=" + dif);
				// logger.debug("DEA=" + dea);
				// logger.debug("MACD=" + macdRtn);

				MacdVO macdVo = new MacdVO();
				macdVo.setStockId(stockId);
				macdVo.setDate(priceList.get(index).date);
				macdVo.setDif(Strings.convert2ScaleDecimal(dif, 3));
				macdVo.setDea(Strings.convert2ScaleDecimal(dea, 3));
				macdVo.setMacd(Strings.convert2ScaleDecimal(macdRtn, 3));

				// if (macdVo.date.compareTo("2015-06-29") >= 0)
				// if (macdTable.getMacd(macdVo.stockId, macdVo.date) == null) {
				macdTable.insert(macdVo);
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void countAndSaved(List<String> stockIds) {
	  logger.debug("MACD countAndSaved start");
	  validate();
	  
	  stockIds.parallelStream().forEach(stockId -> {
	    this.countAndSaved(stockId);
	  });
	  
//		int index = 0;
//		for (String stockId : stockIds) {
//			if (index++ % 100 == 0)
//				logger.debug("MACD countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
//			this.countAndSaved(stockId);
//		}
	  
	  logger.debug("MACD countAndSaved stop");
	}

	public void mainWork(String[] args) {
		// TODO Auto-generated method stub
		this.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}
	
	public void setStockPriceTable(StockPriceTableHelper stockPriceTable) {
		this.stockPriceTable = stockPriceTable;
	}

	public void setMacdTable(IndicatorDBHelperIF macdTable) {
		this.macdTable = macdTable;
	}
}
