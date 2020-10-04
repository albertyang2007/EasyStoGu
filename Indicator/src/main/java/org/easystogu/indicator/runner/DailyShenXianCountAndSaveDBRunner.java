package org.easystogu.indicator.runner;

import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.ShenXianVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.ShenXianHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Doubles;

@Component
public class DailyShenXianCountAndSaveDBRunner{
	@Autowired
	@Qualifier("shenXianTable")
	protected IndicatorDBHelperIF shenXianTable;
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected QianFuQuanStockPriceTableHelper qianFuQuanStockPriceTable;
	@Autowired
	private ShenXianHelper shenXianHelper;
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
			System.out.println("Delete ShenXian for " + stockId + " " + (++index) + "/" + stockIds.size());
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
//				System.out.println("ShenXian countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
//			}
//			this.countAndSaved(stockId);
//		}
	}
}
