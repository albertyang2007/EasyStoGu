package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.WRVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.WRHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Doubles;

@Component
public class DailyWRCountAndSaveDBRunner {
	private static Logger logger = LogHelper.getLogger(DailyWRCountAndSaveDBRunner.class);
	@Autowired
	@Qualifier("wrTable")
	protected IndicatorDBHelperIF wrTable;
	
	private WRHelper wrHelper = new WRHelper();
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	protected CompanyInfoFileHelper stockConfig;

	public void deleteWR(String stockId, String date) {
		wrTable.delete(stockId, date);
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

		if (priceList.size() < 1) {
			return;
		}

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);
		List<Double> low = StockPriceFetcher.getLowPrice(priceList);
		List<Double> high = StockPriceFetcher.getHighPrice(priceList);

		double[][] wr = wrHelper.getWRList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high), 19, 43,
				86);

		int index = priceList.size() - 1;

		WRVO vo = new WRVO();
		vo.setShoTerm(Strings.convert2ScaleDecimal(wr[0][index]));
		vo.setMidTerm(Strings.convert2ScaleDecimal(wr[1][index]));
		vo.setLonTerm(Strings.convert2ScaleDecimal(wr[2][index]));
		vo.setStockId(stockId);
		vo.setDate(priceList.get(index).date);

		this.deleteWR(stockId, vo.date);

		wrTable.insert(vo);

	}

	public void countAndSaved(List<String> stockIds) {
		stockIds.parallelStream().forEach(stockId -> {
			this.countAndSaved(stockId);
		});

		// int index = 0;
		// for (String stockId : stockIds) {
		// if (index++ % 500 == 0) {
		// logger.debug("WR countAndSaved: " + stockId + " " + (index) + "/" +
		// stockIds.size());
		// }
		// this.countAndSaved(stockId);
		// }
	}

	public void setWrTable(IndicatorDBHelperIF wrTable) {
		this.wrTable = wrTable;
	}

	public void setStockPriceTable(StockPriceTableHelper stockPriceTable) {
		this.stockPriceTable = stockPriceTable;
	}
}
