package org.easystogu.db.access;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.db.table.StockSuperVO;

public class WeekStockSuperVOHelper extends StockSuperVOHelper {
	public WeekStockSuperVOHelper() {
		stockPriceTable = WeekStockPriceTableHelper.getInstance();
		macdTable = IndWeekMacdTableHelper.getInstance();
		kdjTable = IndWeekKDJTableHelper.getInstance();
		// bollTable = IndWeekBollTableHelper.getInstance();
		// shenXianTable = IndWeekShenXianTableHelper.getInstance();
		// xueShi2Table = IndWeekXueShi2TableHelper.getInstance();
		// mai1mai2Table = IndWeekMai1Mai2TableHelper.getInstance();
		// yiMengBSTable = IndWeekYiMengBSTableHelper.getInstance();
	}

	@Override
	public List<StockSuperVO> getAllStockSuperVO(String stockId) {
		// merge them into one overall VO
		List<StockSuperVO> overList = new ArrayList<StockSuperVO>();

		List<StockPriceVO> spList = stockPriceTable.getStockPriceById(stockId);
		List<MacdVO> macdList = macdTable.getAllMacd(stockId);
		List<KDJVO> kdjList = kdjTable.getAllKDJ(stockId);
		// List<BollVO> bollList = bollTable.getAllBoll(stockId);
		// List<ShenXianVO> shenXianList =
		// shenXianTable.getAllShenXian(stockId);

		if ((spList.size() != macdList.size()) || (spList.size() != kdjList.size())) {
			return overList;
		}

		if ((spList.size() == 0) || (macdList.size() == 0) || (kdjList.size() == 0)) {
			return overList;
		}

		if (!spList.get(0).date.equals(macdList.get(0).date) || !spList.get(0).date.equals(kdjList.get(0).date)) {
			return overList;
		}

		for (int index = 0; index < spList.size(); index++) {
			StockSuperVO superVO = new StockSuperVO(spList.get(index), macdList.get(index), kdjList.get(index), null);
			// superVO.setShenXianVO(shenXianList.get(index));
			overList.add(superVO);
		}

		return overList;
	}

	@Override
	public List<StockSuperVO> getLatestNStockSuperVO(String stockId, int day) {
		// merge them into one overall VO
		List<StockSuperVO> overList = new ArrayList<StockSuperVO>();

		List<StockPriceVO> spList = stockPriceTable.getNdateStockPriceById(stockId, day);
		List<MacdVO> macdList = macdTable.getNDateMacd(stockId, day);
		List<KDJVO> kdjList = kdjTable.getNDateKDJ(stockId, day);

		if ((spList.size() != day) || (macdList.size() != day) || (kdjList.size() != day)) {
			return overList;
		}

		if (!spList.get(0).date.equals(macdList.get(0).date) || !spList.get(0).date.equals(kdjList.get(0).date)) {
			return overList;
		}

		if (!spList.get(day - 1).date.equals(macdList.get(day - 1).date)
				|| !spList.get(day - 1).date.equals(kdjList.get(day - 1).date)) {
			return overList;
		}

		for (int index = 0; index < spList.size(); index++) {
			StockSuperVO superVO = new StockSuperVO(spList.get(index), macdList.get(index), kdjList.get(index), null);
			// superVO.setShenXianVO(shenXianList.get(index));
			overList.add(superVO);
		}

		return overList;
	}
}
