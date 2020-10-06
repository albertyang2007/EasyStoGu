package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.ShenXianVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.ShenXianHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Doubles;

@Component
public class DailyShenXianCountAndSaveDBRunner{
	private static Logger logger = LogHelper.getLogger(DailyShenXianCountAndSaveDBRunner.class);
	@Autowired
	@Qualifier("shenXianTable")
	protected IndicatorDBHelperIF shenXianTable;
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected QianFuQuanStockPriceTableHelper qianFuQuanStockPriceTable;

	private ShenXianHelper shenXianHelper = new ShenXianHelper();
	@Autowired
	protected CompanyInfoFileHelper stockConfig;

	public void deleteShenXian(String stockId, String date) {
		shenXianTable.delete(stockId, date);
	}

	public void deleteShenXian(String stockId) {
		shenXianTable.delete(stockId);
	}

	public void deleteShenXian(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			logger.debug("Delete ShenXian for " + stockId + " " + (++index) + "/" + stockIds.size());
			this.deleteShenXian(stockId);
		}
	}

	public void countAndSaved(String stockId) {
		List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

		if (priceList.size() < 1) {
			return;
		}

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);

		double[][] shenXian = shenXianHelper.getShenXianList(Doubles.toArray(close));

		int index = priceList.size() - 1;

		ShenXianVO vo = new ShenXianVO();
		vo.setH1(Strings.convert2ScaleDecimal(shenXian[0][index]));
		vo.setH2(Strings.convert2ScaleDecimal(shenXian[1][index]));
		vo.setH3(Strings.convert2ScaleDecimal(shenXian[2][index]));
		vo.setStockId(stockId);
		vo.setDate(priceList.get(index).date);

		this.deleteShenXian(stockId, vo.date);

		shenXianTable.insert(vo);

	}

	public void countAndSaved(List<String> stockIds) {
	  stockIds.parallelStream().forEach(stockId -> {
        this.countAndSaved(stockId);
      });
	  
//		int index = 0;
//		for (String stockId : stockIds) {
//			if (index++ % 500 == 0) {
//				logger.debug("ShenXian countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
//			}
//			this.countAndSaved(stockId);
//		}
	}
}
