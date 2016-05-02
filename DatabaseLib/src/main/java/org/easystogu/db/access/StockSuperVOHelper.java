package org.easystogu.db.access;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.QSDDVO;
import org.easystogu.db.table.ShenXianVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.table.StockSuperVO;

public class StockSuperVOHelper {

	protected StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
	protected IndMacdTableHelper macdTable = IndMacdTableHelper.getInstance();
	protected IndKDJTableHelper kdjTable = IndKDJTableHelper.getInstance();
	protected IndBollTableHelper bollTable = IndBollTableHelper.getInstance();
	protected IndShenXianTableHelper shenXianTable = IndShenXianTableHelper.getInstance();
	protected IndDDXTableHelper ddxTable = IndDDXTableHelper.getInstance();
	protected IndQSDDTableHelper qsddTable = IndQSDDTableHelper.getInstance();
	protected ChuQuanChuXiPriceHelper chuQuanChuXiHelper = ChuQuanChuXiPriceHelper.getInstance();

	// protected IndYiMengBSTableHelper ymbsTable =
	// IndYiMengBSTableHelper.getInstance();

	public List<StockSuperVO> getAllStockSuperVO(String stockId) {
		// merge them into one overall VO
		List<StockSuperVO> overList = new ArrayList<StockSuperVO>();

		List<StockPriceVO> spList = stockPriceTable.getStockPriceById(stockId);
		List<MacdVO> macdList = macdTable.getAllMacd(stockId);
		List<KDJVO> kdjList = kdjTable.getAllKDJ(stockId);
		List<BollVO> bollList = bollTable.getAllBoll(stockId);
		List<ShenXianVO> shenXianList = shenXianTable.getAllShenXian(stockId);
		List<QSDDVO> qsddList = qsddTable.getAllQSDD(stockId);
		// List<YiMengBSVO> ymbsList = ymbsTable.getAllYiMengBS(stockId);

		chuQuanChuXiHelper.updateQianFuQianPriceBasedOnHouFuQuan(stockId, spList);

		if ((spList.size() != macdList.size()) || (macdList.size() != kdjList.size())
				|| (kdjList.size() != spList.size()) || (bollList.size() != spList.size())
				|| (shenXianList.size() != spList.size()) || (qsddList.size() != spList.size())) {
			return overList;
		}

		if ((spList.size() == 0) || (macdList.size() == 0) || (kdjList.size() == 0) || (bollList.size() == 0)
				|| (shenXianList.size() == 0) || (qsddList.size() == 0)) {
			return overList;
		}

		if (!spList.get(0).date.equals(macdList.get(0).date) || !spList.get(0).date.equals(kdjList.get(0).date)
				|| !spList.get(0).date.equals(bollList.get(0).date)
				|| !spList.get(0).date.equals(shenXianList.get(0).date)
				|| !spList.get(0).date.equals(qsddList.get(0).date)) {
			return overList;
		}

		for (int index = 0; index < spList.size(); index++) {
			StockSuperVO superVO = new StockSuperVO(spList.get(index), macdList.get(index), kdjList.get(index),
					bollList.get(index));
			superVO.setShenXianVO(shenXianList.get(index));
			// superVO.setYiMengBSVO(ymbsList.get(index));
			superVO.setDdxVO(ddxTable.getDDX(superVO.priceVO.stockId, superVO.priceVO.date));
			superVO.setQsddVO(qsddList.get(index));
			overList.add(superVO);
		}

		return overList;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}
