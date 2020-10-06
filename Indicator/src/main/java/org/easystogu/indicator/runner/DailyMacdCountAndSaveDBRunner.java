package org.easystogu.indicator.runner;

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

//每日根据最新数据计算当天的macd值，每天运行一次
@Component
public class DailyMacdCountAndSaveDBRunner {
	private static Logger logger = LogHelper.getLogger(DailyMacdCountAndSaveDBRunner.class);
	@Autowired
	@Qualifier("macdTable")
	protected IndicatorDBHelperIF macdTable;
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	protected MACDHelper macdHelper;
	@Autowired
	protected CompanyInfoFileHelper stockConfig;

	public void validate() {
		if (this instanceof DailyMacdCountAndSaveDBRunner && stockPriceTable instanceof QianFuQuanStockPriceTableHelper
				&& (macdTable instanceof IndMacdCassTableHelper || macdTable instanceof IndMacdDBTableHelper)) {
			// pass
		} else {
			throw new RuntimeException("SubClass ERROR: This is " + this.getClass().getSimpleName()
					+ ", stockPriceTable is " + stockPriceTable.getClass().getSimpleName() + ", macdTable is "
					+ macdTable.getClass().getSimpleName());
		}
	}

	public void deleteMacd(String stockId, String date) {
		macdTable.delete(stockId, date);
	}

	public void countAndSaved(String stockId) {

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

		index = priceList.size() - 1;

		double dif = macd[0][index];
		double dea = macd[1][index];
		double macdRtn = macd[2][index];

		MacdVO vo = new MacdVO();
		vo.setStockId(stockId);
		vo.setDate(priceList.get(index).date);
		vo.setDif(Strings.convert2ScaleDecimal(dif));
		vo.setDea(Strings.convert2ScaleDecimal(dea));
		vo.setMacd(Strings.convert2ScaleDecimal(macdRtn));

		this.deleteMacd(stockId, vo.date);

		macdTable.insert(vo);
	}

	public void countAndSaved(List<String> stockIds) {
		validate();

		stockIds.parallelStream().forEach(stockId -> {
			this.countAndSaved(stockId);
		});

		// int index = 0;
		// for (String stockId : stockIds) {
		// if (index++ % 500 == 0) {
		// logger.debug("MACD countAndSaved: " + stockId + " " + (index) + "/" +
		// stockIds.size());
		// }
		// this.countAndSaved(stockId);
		// }
	}

	public void setStockPriceTable(StockPriceTableHelper stockPriceTable) {
		this.stockPriceTable = stockPriceTable;
	}

	public void setMacdTable(IndicatorDBHelperIF macdTable) {
		this.macdTable = macdTable;
	}
}
