package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.access.table.StockPriceTableHelper;
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
public class HistoryShenXianCountAndSaveDBRunner {
	@Autowired
	@Qualifier("shenXianTable")
	protected IndicatorDBHelperIF shenXianTable;
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	private CompanyInfoFileHelper stockConfig;
	@Autowired
	protected ShenXianHelper shenXianHelper;

	public void deleteShenXian(String stockId) {
		shenXianTable.delete(stockId);
	}

	public void deleteShenXian(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Delete ShenXian for " + stockId + " " + (++index) + " of " + stockIds.size());
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
      System.out.println("Shenxian countAndSaved start");
      stockIds.parallelStream().forEach(stockId -> {
        this.countAndSaved(stockId);
      });
      
//      int index = 0;
//      for (String stockId : stockIds) {
//          if (index++ % 100 == 0)
//              System.out.println("Shenxian countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
//          this.countAndSaved(stockId);
//      }
      
      System.out.println("Shenxian countAndSaved stop");
    }

	// TODO Auto-generated method stub
	// 一次性计算数据库中所有ShenXian数据，入库
	public void mainWork(String[] args) {
		this.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}

}
