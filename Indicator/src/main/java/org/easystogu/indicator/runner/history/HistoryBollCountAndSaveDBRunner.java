package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.BollVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.BOLLHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//计算数据库中所有boll值，包括最新和历史的，一次性运行
@Component
public class HistoryBollCountAndSaveDBRunner {
	private static Logger logger = LogHelper.getLogger(HistoryBollCountAndSaveDBRunner.class);
	@Autowired
	@Qualifier("bollTable")
	protected IndicatorDBHelperIF bollTable;
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	private BOLLHelper bollHelper;
	@Autowired
	private CompanyInfoFileHelper stockConfig;

	public void deleteBoll(String stockId) {
		bollTable.delete(stockId);
	}

	public void deleteBoll(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			logger.debug("Delete Boll for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteBoll(stockId);
		}
	}

	public void countAndSaved(String stockId) {
		this.deleteBoll(stockId);

		try {
			List<StockPriceVO> priceList = stockPriceTable.getStockPriceById(stockId);

			int length = priceList.size();

			if (length < 1) {
				return;
			}

			double[] close = new double[length];
			int index = 0;
			for (StockPriceVO vo : priceList) {
				close[index++] = vo.close;
			}

			double[][] boll = bollHelper.getBOLLList(close, 20, 2, 2);

			for (index = priceList.size() - 1; index >= 0; index--) {
				double up = Strings.convert2ScaleDecimal(boll[0][index], 3);
				double mb = Strings.convert2ScaleDecimal(boll[1][index], 3);
				double dn = Strings.convert2ScaleDecimal(boll[2][index], 3);
				// logger.debug("MB=" + mb);
				// logger.debug("UP=" + up);
				// logger.debug("DN=" + dn);

				BollVO bollVO = new BollVO();
				bollVO.setStockId(stockId);
				bollVO.setDate(priceList.get(index).date);
				bollVO.setMb(mb);
				bollVO.setUp(up);
				bollVO.setDn(dn);

				// if (bollVO.date.compareTo("2015-06-29") >= 0)
				// if (bollTable.getBoll(bollVO.stockId, bollVO.date) == null) {
				bollTable.insert(bollVO);
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void countAndSaved(List<String> stockIds) {
		logger.debug("Boll countAndSaved start");
		stockIds.parallelStream().forEach(stockId -> {
			this.countAndSaved(stockId);
		});

		// int index = 0;
		// for (String stockId : stockIds) {
		// if (index++ % 100 == 0)
		// logger.debug("Boll countAndSaved: " + stockId + " " + (index) + "/" +
		// stockIds.size());
		// this.countAndSaved(stockId);
		// }

		logger.debug("Boll countAndSaved stop");
	}

	public void mainWork(String[] args) {
		// TODO Auto-generated method stub
		this.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}

}
