package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.QSDDVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.QSDDHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Doubles;

@Component
public class DailyQSDDCountAndSaveDBRunner{
	private static Logger logger = LogHelper.getLogger(DailyQSDDCountAndSaveDBRunner.class);
	@Autowired
	@Qualifier("qsddTable")
	protected IndicatorDBHelperIF qsddTable;

	private QSDDHelper qsddHelper = new QSDDHelper();
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected QianFuQuanStockPriceTableHelper qianFuQuanStockPriceTable;
	@Autowired
	protected CompanyInfoFileHelper stockConfig;

	public void deleteQSDD(String stockId, String date) {
		qsddTable.delete(stockId, date);
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

		if (priceList.size() < 1) {
			return;
		}

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);
		List<Double> low = StockPriceFetcher.getLowPrice(priceList);
		List<Double> high = StockPriceFetcher.getHighPrice(priceList);

		double[][] qsdd = qsddHelper.getQSDDList(Doubles.toArray(close), Doubles.toArray(low), Doubles.toArray(high));

		int index = priceList.size() - 1;

		QSDDVO vo = new QSDDVO();
		vo.setLonTerm(Strings.convert2ScaleDecimal(qsdd[0][index]));
		vo.setShoTerm(Strings.convert2ScaleDecimal(qsdd[1][index]));
		vo.setMidTerm(Strings.convert2ScaleDecimal(qsdd[2][index]));
		vo.setStockId(stockId);
		vo.setDate(priceList.get(index).date);

		this.deleteQSDD(stockId, vo.date);

		qsddTable.insert(vo);

	}

	public void countAndSaved(List<String> stockIds) {
	  stockIds.parallelStream().forEach(stockId -> {
        this.countAndSaved(stockId);
      });
	  
//		int index = 0;
//		for (String stockId : stockIds) {
//			if (index++ % 500 == 0) {
//				logger.debug("QSDD countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
//			}
//			this.countAndSaved(stockId);
//		}
	}
}
