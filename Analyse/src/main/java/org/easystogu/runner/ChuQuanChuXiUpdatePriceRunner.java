package org.easystogu.runner;

import java.util.List;

import org.easystogu.config.StockListConfigurationService;
import org.easystogu.db.access.EventChuQuanChuXiTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.ChuQuanChuXiVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.utils.Strings;

//if table event_gaosongzhuan has update, please run this runner 
//to update all the gaoSongZhuan price data
//manually to update gaoSongZhuan table, pls refer to 
//http://www.cninfo.com.cn/search/memo.jsp?datePara=2015-05-13

public class ChuQuanChuXiUpdatePriceRunner {
	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected EventChuQuanChuXiTableHelper gaoSongZhuanTable = EventChuQuanChuXiTableHelper.getInstance();

	private void checkIfGaoSongZhuanExist(String stockId) {
		// get latest two day vo
		List<StockPriceVO> list = stockPriceTable.getNdateStockPriceById(stockId, 2);
		if (list != null && list.size() == 2) {
			StockPriceVO cur = list.get(0);
			StockPriceVO pre = list.get(1);
			// System.out.println(cur);
			// System.out.println(pre);
			if (cur.lastClose != 0 && cur.lastClose != pre.close) {
				// chuQuan happen!
				ChuQuanChuXiVO vo = gaoSongZhuanTable.getChuQuanChuXIVO(stockId, cur.date);
				if (vo == null) {
					vo = new ChuQuanChuXiVO();
					vo.setStockId(cur.stockId);
					vo.setDate(cur.date);
					vo.setRate(cur.lastClose / pre.close);
					vo.setAlreadyUpdatePrice(false);

					System.out.println("ChuQuan happen for " + vo);
					gaoSongZhuanTable.insert(vo);
				}
			}
		}

	}

	private void checkIfGaoSongZhuanExist(List<String> stockIds) {
		for (String stockId : stockIds) {
			this.checkIfGaoSongZhuanExist(stockId);
		}
	}

	private void updatePriceBasedOnChuQuanChuXi(List<String> stockIds) {
		for (String stockId : stockIds) {
			this.updatePriceBasedOnChuQuanChuXi(stockId);
		}
	}

	private void updatePriceBasedOnChuQuanChuXi(String stockId) {
		List<ChuQuanChuXiVO> list = gaoSongZhuanTable.getAllGaoSongZhuanVO(stockId);
		// list is order by date
		for (ChuQuanChuXiVO chuQuanVO : list) {
			if (!chuQuanVO.isAlreadyUpdatePrice()) {
				// update price before the date
				System.out.println("Update price for " + stockId + " before " + chuQuanVO.date + ", rate=" + chuQuanVO.rate);
				// stockPriceTable.updateChuQuanBatchPrice(vo);

				List<StockPriceVO> stockPriceVOs = stockPriceTable.getStockPriceByIdLessThanDate(stockId, chuQuanVO.date);
				for (StockPriceVO spVO : stockPriceVOs) {
					spVO.open = Strings.getDecimalWith2Scale(spVO.open * chuQuanVO.rate);
					spVO.high = Strings.getDecimalWith2Scale(spVO.high * chuQuanVO.rate);
					spVO.low = Strings.getDecimalWith2Scale(spVO.low * chuQuanVO.rate);
					spVO.close = Strings.getDecimalWith2Scale(spVO.close * chuQuanVO.rate);

					// update to database
					stockPriceTable.updateChuQuanPrice(spVO);
				}

				// then update the gaoSongZhuanVO to true
				chuQuanVO.setAlreadyUpdatePrice(true);
				gaoSongZhuanTable.updateGaoSongZhuanVO(chuQuanVO);
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockListConfigurationService stockConfig = StockListConfigurationService.getInstance();

		List<String> stockIds = stockConfig.getAllStockId();
		ChuQuanChuXiUpdatePriceRunner runner = new ChuQuanChuXiUpdatePriceRunner();

		runner.checkIfGaoSongZhuanExist(stockIds);
		//runner.updatePriceBasedOnChuQuanChuXi(stockIds);
	}

}
