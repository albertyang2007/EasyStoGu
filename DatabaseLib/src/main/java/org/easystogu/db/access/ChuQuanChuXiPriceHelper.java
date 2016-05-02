package org.easystogu.db.access;

import java.util.List;

import org.easystogu.db.table.ChuQuanChuXiVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.utils.Strings;

public class ChuQuanChuXiPriceHelper {
	private CompanyInfoFileHelper companyInfoHelper = CompanyInfoFileHelper.getInstance();
	private EventChuQuanChuXiTableHelper chuQuanChuXiTable = EventChuQuanChuXiTableHelper.getInstance();
	private FuQuanStockPriceTableHelper fuquanStockPriceTable = FuQuanStockPriceTableHelper.getInstance();

	// priceList is order by date
	// using hou fuquan stockprce to count the qian fuquan stockprice
	// 使用后复权的数据计算前复权的价格数据
	public void updateQianFuQianPriceBasedOnHouFuQuan(String stockId, List<StockPriceVO> spList) {
		if (companyInfoHelper.isStockIdAMajorZhiShu(stockId)) {
			return;
		}

		List<StockPriceVO> fq_spList = fuquanStockPriceTable.getStockPriceById(stockId);
		if (fq_spList.size() != spList.size()) {
			System.out.println("Fatel error for " + stockId + ", fuquan StockPrice length is not same as StockPrice");
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

	public void updatePrice_NotUsedThisMethod(String stockId, List<StockPriceVO> priceList) {
		List<ChuQuanChuXiVO> list = chuQuanChuXiTable.getAllChuQuanChuXiVO(stockId);
		// list is order by date
		for (ChuQuanChuXiVO chuQuanVO : list) {
			for (StockPriceVO priceVO : priceList) {
				if (priceVO.date.compareTo(chuQuanVO.date) < 0) {
					priceVO.close = priceVO.close * chuQuanVO.rate;
					priceVO.open = priceVO.open * chuQuanVO.rate;
					priceVO.high = priceVO.high * chuQuanVO.rate;
					priceVO.low = priceVO.low * chuQuanVO.rate;
				}
			}
		}
	}

	public void updateWeekPrice(String stockId, List<StockPriceVO> priceList, String firstDate, String lastDate) {
		List<ChuQuanChuXiVO> list = chuQuanChuXiTable.getNDateChuQuanChuXiVO(stockId, 1);

		if (list == null || list.size() == 0)
			return;

		ChuQuanChuXiVO chuQuanVO = list.get(0);

		for (StockPriceVO priceVO : priceList) {
			// only limit the price update when chuQuan happens at that week
			if (chuQuanVO.date.compareTo(firstDate) >= 0 && chuQuanVO.date.compareTo(lastDate) <= 0) {
				if (priceVO.date.compareTo(chuQuanVO.date) < 0) {
					priceVO.close = priceVO.close * chuQuanVO.rate;
					priceVO.open = priceVO.open * chuQuanVO.rate;
					priceVO.high = priceVO.high * chuQuanVO.rate;
					priceVO.low = priceVO.low * chuQuanVO.rate;
				}
			}
		}
	}

	public void updateSuperPrice(String stockId, List<StockSuperVO> superList) {
		List<ChuQuanChuXiVO> list = chuQuanChuXiTable.getNDateChuQuanChuXiVO(stockId, 1);

		if (list == null || list.size() == 0)
			return;

		ChuQuanChuXiVO chuQuanVO = list.get(0);

		for (StockSuperVO superVO : superList) {
			if (superVO.priceVO.date.compareTo(chuQuanVO.date) < 0) {
				superVO.priceVO.close = superVO.priceVO.close * chuQuanVO.rate;
				superVO.priceVO.open = superVO.priceVO.open * chuQuanVO.rate;
				superVO.priceVO.high = superVO.priceVO.high * chuQuanVO.rate;
				superVO.priceVO.low = superVO.priceVO.low * chuQuanVO.rate;
			}
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
