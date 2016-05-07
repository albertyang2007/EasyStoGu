package org.easystogu.db.access;

import java.util.List;

import org.easystogu.db.table.StockPriceVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.Strings;

public class ChuQuanChuXiPriceHelper {
	private CompanyInfoFileHelper companyInfoHelper = CompanyInfoFileHelper.getInstance();
	private EventChuQuanChuXiTableHelper chuQuanChuXiTable = EventChuQuanChuXiTableHelper.getInstance();
	private HouFuQuanStockPriceTableHelper houfuquanStockPriceTable = HouFuQuanStockPriceTableHelper.getInstance();
	private static ChuQuanChuXiPriceHelper instance = null;

	public static ChuQuanChuXiPriceHelper getInstance() {
		if (instance == null) {
			instance = new ChuQuanChuXiPriceHelper();
		}
		return instance;
	}

	// priceList is order by date
	// using hou fuquan stockprce to count the qian fuquan stockprice
	// 使用后复权的数据计算前复权的价格数据
	private void updateQianFuQianPriceBasedOnHouFuQuan(String stockId, List<StockPriceVO> spList) {
		if (companyInfoHelper.isStockIdAMajorZhiShu(stockId)) {
			return;
		}

		List<StockPriceVO> fq_spList = houfuquanStockPriceTable.getStockPriceById(stockId);
		if (fq_spList.size() != spList.size()) {
			System.out.println("Fatel error for " + stockId + ", fuquan StockPrice length (" + fq_spList.size()
					+ ") is not same as StockPrice (" + spList.size() + ")");
			return;
		}

		StockPriceVO spvo = spList.get(spList.size() - 1);
		StockPriceVO fq_spvo = fq_spList.get(fq_spList.size() - 1);

		if (!fq_spvo.date.equals(spvo.date)) {
			System.out.println("Fatel error for " + stockId
					+ ", fuquan StockPrice latest date is not same as StockPrice");
			return;
		}

		double rate = spvo.close / fq_spvo.close;
		// System.out.println(stockId + " at " + spvo.date + " fuquan rate= " +
		// rate);
		// count the qian fuquan stockprice for stockid from the latest chuquan
		// event
		int chuquan_index = spList.size() - 1;
		for (int index = spList.size() - 1; index >= 1; index--) {
			StockPriceVO vo = spList.get(index);
			StockPriceVO prevo = spList.get(index - 1);
			if (vo.lastClose != 0 && prevo.close != 0 && vo.lastClose != prevo.close) {
				chuquan_index = index - 1;
				// System.out.println("chuquan index= " + chuquan_index + " at "
				// + prevo.date);
				break;
			}
		}

		// need to update price from this chuquan_index
		for (int index = chuquan_index; index >= 0; index--) {
			StockPriceVO vo = spList.get(index);
			StockPriceVO fq_prevo = fq_spList.get(index);
			vo.open = Strings.convert2ScaleDecimal(fq_prevo.open * rate);
			vo.close = Strings.convert2ScaleDecimal(fq_prevo.close * rate);
			vo.low = Strings.convert2ScaleDecimal(fq_prevo.low * rate);
			vo.high = Strings.convert2ScaleDecimal(fq_prevo.high * rate);
		}
	}

	public static void main(String[] args) {
		String stockId = "002356";// "000038"
		StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
		List<StockPriceVO> spList = stockPriceTable.getStockPriceById(stockId);
		ChuQuanChuXiPriceHelper helper = new ChuQuanChuXiPriceHelper();
		helper.updateQianFuQianPriceBasedOnHouFuQuan(stockId, spList);
		for (StockPriceVO vo : spList) {
			System.out.println(vo);
		}
	}
}
