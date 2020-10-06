package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.table.IndMATableHelper;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.vo.table.MAVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.MAHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DailyMACountAndSaveDBRunner {
	private static Logger logger = LogHelper.getLogger(DailyMACountAndSaveDBRunner.class);
	@Autowired
	protected IndMATableHelper maTable;
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected QianFuQuanStockPriceTableHelper qianFuQuanStockPriceTable;
	@Autowired
	protected MAHelper maHelper;
	@Autowired
	protected CompanyInfoFileHelper stockConfig;

	public void deleteMa(String stockId, String date) {
		maTable.delete(stockId, date);
	}

	public void countAndSaved(String stockId) {

		List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);
		// List<StockPriceVO> list =
		// stockPriceTable.getNdateStockPriceById(stockId, minLength);
		// Collections.reverse(list);

		int length = priceList.size();

		if (length < 1) {
			return;
		}

		double[] close = new double[length];
		int index = 0;
		for (StockPriceVO vo : priceList) {
			close[index++] = vo.close;
		}

		double[] cls = maHelper.getMAList(close, 1);
		double[] ma5 = maHelper.getMAList(close, 5);
		double[] ma10 = maHelper.getMAList(close, 10);
		double[] ma19 = maHelper.getMAList(close, 19);
		double[] ma20 = maHelper.getMAList(close, 20);
		double[] ma30 = maHelper.getMAList(close, 30);
		double[] ma43 = maHelper.getMAList(close, 43);
		double[] ma60 = maHelper.getMAList(close, 60);
		double[] ma86 = maHelper.getMAList(close, 86);
		double[] ma120 = maHelper.getMAList(close, 120);
		double[] ma250 = maHelper.getMAList(close, 250);

		index = priceList.size() - 1;

		MAVO vo = new MAVO();
		vo.setStockId(stockId);
		vo.setDate(priceList.get(index).date);
		vo.setMa5(Strings.convert2ScaleDecimal(ma5[index], 2));
		vo.setMa10(Strings.convert2ScaleDecimal(ma10[index], 2));
		vo.setMa19(Strings.convert2ScaleDecimal(ma19[index], 2));
		vo.setMa20(Strings.convert2ScaleDecimal(ma20[index], 2));
		vo.setMa30(Strings.convert2ScaleDecimal(ma30[index], 2));
		vo.setMa43(Strings.convert2ScaleDecimal(ma43[index], 2));
		vo.setMa60(Strings.convert2ScaleDecimal(ma60[index], 2));
		vo.setMa86(Strings.convert2ScaleDecimal(ma86[index], 2));
		vo.setMa120(Strings.convert2ScaleDecimal(ma120[index], 2));
		vo.setMa250(Strings.convert2ScaleDecimal(ma250[index], 2));
		vo.setClose(Strings.convert2ScaleDecimal(cls[index], 2));

		// logger.debug(vo);
		this.deleteMa(stockId, vo.date);
		maTable.insert(vo);

	}

	public void countAndSaved(List<String> stockIds) {
	  stockIds.parallelStream().forEach(stockId -> {
        this.countAndSaved(stockId);
      });
	  
//		int index = 0;
//		for (String stockId : stockIds) {
//			if (index++ % 500 == 0) {
//				logger.debug("MA countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
//			}
//			this.countAndSaved(stockId);
//		}
	}
}
