package org.easystogu.db.access.table;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.StockSuperVO;

public class WeekStockSuperVOHelper extends StockSuperVOHelper {
	public WeekStockSuperVOHelper() {
		qianFuQuanStockPriceTable = WeekStockPriceTableHelper.getInstance();
		macdTable = DBAccessFacdeFactory.getInstance(Constants.indWeekMacd);
		kdjTable = DBAccessFacdeFactory.getInstance(Constants.indWeekKDJ);
	}

	@Override
	public List<StockSuperVO> getAllStockSuperVO(String stockId) {
		// merge them into one overall VO
		List<StockSuperVO> overList = new ArrayList<StockSuperVO>();

		List<StockPriceVO> spList = qianFuQuanStockPriceTable.getStockPriceById(stockId);
		List<MacdVO> macdList = macdTable.getAll(stockId);
		List<KDJVO> kdjList = kdjTable.getAll(stockId);

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

		List<StockPriceVO> spList = qianFuQuanStockPriceTable.getNdateStockPriceById(stockId, day);
		List<MacdVO> macdList = macdTable.getByIdAndLatestNDate(stockId, day);
		List<KDJVO> kdjList = kdjTable.getByIdAndLatestNDate(stockId, day);

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
