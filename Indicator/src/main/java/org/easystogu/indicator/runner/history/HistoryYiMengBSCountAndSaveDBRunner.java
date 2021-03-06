package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.db.access.table.IndYiMengBSTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.YiMengBSVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.YiMengBSHelper;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Doubles;

@Component
public class HistoryYiMengBSCountAndSaveDBRunner {
	private static Logger logger = LogHelper.getLogger(HistoryYiMengBSCountAndSaveDBRunner.class);
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	private CompanyInfoFileHelper stockConfig;
	@Autowired
	protected IndYiMengBSTableHelper yiMengBSTable;

	protected YiMengBSHelper yiMengBSHelper = new YiMengBSHelper();

	public void deleteYiMengBS(String stockId) {
		yiMengBSTable.delete(stockId);
	}

	public void deleteYiMengBS(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			logger.debug("Delete YiMengBS for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteYiMengBS(stockId);
		}
	}

	public void countAndSaved(String stockId) {
		deleteYiMengBS(stockId);

		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

		if (priceList.size() <= 108) {
			logger.debug("StockPrice data is less than 108, skip " + stockId);
			return;
		}

		List<Double> close = StockPriceFetcher.getClosePrice(priceList);
		List<Double> low = StockPriceFetcher.getLowPrice(priceList);
		List<Double> high = StockPriceFetcher.getHighPrice(priceList);

		double[][] yiMeng = yiMengBSHelper.getYiMengBSList(Doubles.toArray(close), Doubles.toArray(low),
				Doubles.toArray(high));

		for (int i = 0; i < yiMeng[0].length; i++) {
			YiMengBSVO vo = new YiMengBSVO();
			vo.setX2(Strings.convert2ScaleDecimal(yiMeng[0][i]));
			vo.setX3(Strings.convert2ScaleDecimal(yiMeng[1][i]));
			vo.setStockId(stockId);
			vo.setDate(priceList.get(i).date);

			try {
				// if (yiMengBSTable.getYiMengBS(vo.stockId, vo.date) == null) {
				yiMengBSTable.insert(vo);
				// }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void countAndSaved(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			if (index++ % 100 == 0)
				logger.debug("YiMengBS countAndSaved: " + stockId + " " + (index) + " of " + stockIds.size());
			this.countAndSaved(stockId);
		}
	}

	// TODO Auto-generated method stub
	// 一次性计算数据库中所有ShenXian数据，入库
	public void mainWork(String[] args) {
		this.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("002194");
	}

}
