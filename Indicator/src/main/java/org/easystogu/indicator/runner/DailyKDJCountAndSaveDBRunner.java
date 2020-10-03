package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.cassandra.access.table.IndKDJCassTableHelper;
import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.KDJHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.postgresql.access.table.IndKDJDBTableHelper;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Doubles;

@Component
public class DailyKDJCountAndSaveDBRunner {
	@Autowired
	protected DBAccessFacdeFactory dBAccessFacdeFactory;
	protected IndicatorDBHelperIF kdjTable = dBAccessFacdeFactory.getInstance(Constants.indKDJ);
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	protected KDJHelper kdjHelper;
	@Autowired
	protected CompanyInfoFileHelper stockConfig;

	public void validate() {
		if (this instanceof DailyKDJCountAndSaveDBRunner && stockPriceTable instanceof QianFuQuanStockPriceTableHelper
				&& (kdjTable instanceof IndKDJCassTableHelper || kdjTable instanceof IndKDJDBTableHelper)) {
			// pass
		} else {
			throw new RuntimeException("SubClass ERROR: This is " + this.getClass().getSimpleName()
					+ ", stockPriceTable is " + stockPriceTable.getClass().getSimpleName() + ", kdjTable is "
					+ kdjTable.getClass().getSimpleName());
		}
	}

	public void deleteKDJ(String stockId, String date) {
		kdjTable.delete(stockId, date);
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

		if (priceList.size() <= 9) {
			// System.out.println("StockPrice data is less than 9, skip " +
			// stockId);
			return;
		}

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);
		List<Double> low = StockPriceFetcher.getLowPrice(priceList);
		List<Double> high = StockPriceFetcher.getHighPrice(priceList);

		double[][] KDJ = kdjHelper.getKDJList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));

		int index = priceList.size() - 1;

		KDJVO vo = new KDJVO();
		vo.setK(Strings.convert2ScaleDecimal(KDJ[0][index]));
		vo.setD(Strings.convert2ScaleDecimal(KDJ[1][index]));
		vo.setJ(Strings.convert2ScaleDecimal(KDJ[2][index]));
		vo.setRsv(Strings.convert2ScaleDecimal(KDJ[3][index]));
		vo.setStockId(stockId);
		vo.setDate(priceList.get(index).date);

		this.deleteKDJ(stockId, vo.date);

		kdjTable.insert(vo);
	}

	public void countAndSaved(List<String> stockIds) {
		validate();
		
		stockIds.parallelStream().forEach(stockId -> {
			this.countAndSaved(stockId);
		});

		// int index = 0;
		// for (String stockId : stockIds) {
		// if (index++ % 500 == 0) {
		// System.out.println("KDJ countAndSaved: " + stockId + " " + (index) + "/" +
		// stockIds.size());
		// }
		// this.countAndSaved(stockId);
		// }
	}

	public void setKdjTable(IndicatorDBHelperIF kdjTable) {
		this.kdjTable = kdjTable;
	}

	public void setStockPriceTable(StockPriceTableHelper stockPriceTable) {
		this.stockPriceTable = stockPriceTable;
	}
}
