package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.db.access.table.IndMai1Mai2TableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.Mai1Mai2VO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.Mai1Mai2Helper;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class HistoryMai1Mai2CountAndSaveDBRunner {
	private static Logger logger = LogHelper.getLogger(HistoryMai1Mai2CountAndSaveDBRunner.class);
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	private CompanyInfoFileHelper stockConfig;
	@Autowired
	protected IndMai1Mai2TableHelper mai1mai2Table;

	protected Mai1Mai2Helper mai1mai2Helper = new Mai1Mai2Helper();

	public void deleteMai1Mai2(String stockId) {
		mai1mai2Table.delete(stockId);
	}

	public void deleteMai1Mai2(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			logger.debug("Delete Mai1Mai2 for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteMai1Mai2(stockId);
		}
	}

	public void countAndSaved(String stockId) {
        this.deleteMai1Mai2(stockId);
        
		List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

		if (priceList.size() <= 20) {
			logger.debug("StockPrice data is less than 20, skip " + stockId);
			return;
		}

		// list is order by date
		int length = priceList.size();
		double[] var1 = new double[length];
		int index = 0;
		for (StockPriceVO vo : priceList) {
			var1[index++] = (2 * vo.close + vo.open + vo.high + vo.low) / 5;
		}

		double[][] mai1mai2 = mai1mai2Helper.getMai1Mai2List(var1);

		for (int i = 0; i < mai1mai2[0].length; i++) {
			Mai1Mai2VO vo = new Mai1Mai2VO();
			vo.setSd(Strings.convert2ScaleDecimal(mai1mai2[0][i], 3));
			vo.setSk(Strings.convert2ScaleDecimal(mai1mai2[1][i], 3));
			vo.setStockId(stockId);
			vo.setDate(priceList.get(i).date);

			try {
				// if (vo.date.compareTo("2015-06-29") >= 0)
				// if (mai1mai2Table.getMai1Mai2(vo.stockId, vo.date) == null) {
				mai1mai2Table.insert(vo);
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
				logger.debug("Mai1Mai2 countAndSaved: " + stockId + " " + (index) + " of " + stockIds.size());
			this.countAndSaved(stockId);
		}
	}

	// TODO Auto-generated method stub
	// 一次性计算数据库中所有ShenXian数据，入库
	public void mainWork(String[] args) {
		this.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}
}
