package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.db.access.table.StockPriceTableHelper;
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
public class HistoryShenXianCountAndSaveDBRunner {
	private static Logger logger = LogHelper.getLogger(HistoryShenXianCountAndSaveDBRunner.class);
	@Autowired
	@Qualifier("shenXianTable")
	protected IndicatorDBHelperIF shenXianTable;
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	private CompanyInfoFileHelper stockConfig;

	protected ShenXianHelper shenXianHelper = new ShenXianHelper();

	public void deleteShenXian(String stockId) {
		shenXianTable.delete(stockId);
	}

	public void deleteShenXian(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			logger.debug("Delete ShenXian for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteShenXian(stockId);
		}
	}

	public void countAndSaved(String stockId) {
        this.deleteShenXian(stockId);
        
		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

		if (priceList.size() < 1) {
			return;
		}

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);

		double[][] shenXian = shenXianHelper.getShenXianList(Doubles.toArray(close));

		for (int i = 0; i < shenXian[0].length; i++) {
			ShenXianVO vo = new ShenXianVO();
			vo.setH1(Strings.convert2ScaleDecimal(shenXian[0][i], 3));
			vo.setH2(Strings.convert2ScaleDecimal(shenXian[1][i], 3));
			vo.setH3(Strings.convert2ScaleDecimal(shenXian[2][i], 3));
			vo.setStockId(stockId);
			vo.setDate(priceList.get(i).date);

			try {
				// if (shenXianTable.getShenXian(vo.stockId, vo.date) == null) {
				shenXianTable.insert(vo);
				// }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void countAndSaved(List<String> stockIds) {
      logger.debug("Shenxian countAndSaved start");
      stockIds.parallelStream().forEach(stockId -> {
        this.countAndSaved(stockId);
      });
      
//      int index = 0;
//      for (String stockId : stockIds) {
//          if (index++ % 100 == 0)
//              logger.debug("Shenxian countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
//          this.countAndSaved(stockId);
//      }
      
      logger.debug("Shenxian countAndSaved stop");
    }

	// TODO Auto-generated method stub
	// 一次性计算数据库中所有ShenXian数据，入库
	public void mainWork(String[] args) {
		this.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}

}
