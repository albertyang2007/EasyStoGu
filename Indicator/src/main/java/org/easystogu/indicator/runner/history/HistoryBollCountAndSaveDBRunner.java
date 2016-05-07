package org.easystogu.indicator.runner.history;

import java.util.List;

import org.easystogu.db.access.ChuQuanChuXiPriceHelper;
import org.easystogu.db.access.IndBollTableHelper;
import org.easystogu.db.access.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.indicator.BOLLHelper;
import org.easystogu.utils.Strings;

//计算数据库中所有boll值，包括最新和历史的，一次性运行
public class HistoryBollCountAndSaveDBRunner {

	protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
	protected StockPriceTableHelper qianFuQuanStockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
	private BOLLHelper bollHelper = new BOLLHelper();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiPriceHelper = new ChuQuanChuXiPriceHelper();

	public void deleteBoll(String stockId) {
		bollTable.delete(stockId);
	}

	public void deleteBoll(List<String> stockIds) {
		int index = 0;
		for (String stockId : stockIds) {
			System.out.println("Delete Boll for " + stockId + " " + (++index) + " of " + stockIds.size());
			this.deleteBoll(stockId);
		}
	}

	public void countAndSaved(String stockId) {
        this.deleteBoll(stockId);
        
		try {
			List<StockPriceVO> priceList = qianFuQuanStockPriceTable.getStockPriceById(stockId);

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
				// System.out.println("MB=" + mb);
				// System.out.println("UP=" + up);
				// System.out.println("DN=" + dn);

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
		int index = 0;
		for (String stockId : stockIds) {
			if (index++ % 100 == 0)
				System.out.println("Boll countAndSaved: " + stockId + " " + (index) + "/" + stockIds.size());
			this.countAndSaved(stockId);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();
		HistoryBollCountAndSaveDBRunner runner = new HistoryBollCountAndSaveDBRunner();
		runner.countAndSaved(stockConfig.getAllStockId());
		// runner.countAndSaved("600750");
	}

}
