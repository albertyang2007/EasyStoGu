package org.easystogu.db.access.table;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.easystogu.cassandra.access.table.IndWeekKDJCassTableHelper;
import org.easystogu.cassandra.access.table.IndWeekMacdCassTableHelper;
import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.StockSuperVO;
import org.easystogu.postgresql.access.table.IndWeekKDJDBTableHelper;
import org.easystogu.postgresql.access.table.IndWeekMacdDBTableHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("weekStockSuperVOHelper")
public class WeekStockSuperVOHelper extends StockSuperVOHelper {
	@Autowired
	@Qualifier("weekStockPriceTable")
	protected StockPriceTableHelper _stockPriceTable;
	
	@Autowired
	protected DBAccessFacdeFactory dBAccessFacdeFactory;

	@PostConstruct
	public void init() {
		stockPriceTable = _stockPriceTable;
		macdTable = dBAccessFacdeFactory.getInstance(Constants.indWeekMacd);
		kdjTable = dBAccessFacdeFactory.getInstance(Constants.indWeekKDJ);
	}

	@Override
	public void validate() {
		if (this instanceof WeekStockSuperVOHelper && stockPriceTable instanceof WeekStockPriceTableHelper
				&& (kdjTable instanceof IndWeekKDJCassTableHelper || kdjTable instanceof IndWeekKDJDBTableHelper)
				&& (macdTable instanceof IndWeekMacdCassTableHelper || macdTable instanceof IndWeekMacdDBTableHelper)) {
			// pass
		} else {
			throw new RuntimeException("SubClass ERROR: This is " + this.getClass().getSimpleName()
					+ ", stockPriceTable is " + stockPriceTable.getClass().getSimpleName() + ", kdjTable is "
					+ kdjTable.getClass().getSimpleName() + ", macdTable is " + macdTable.getClass().getSimpleName());
		}
	}

	@Override
	public List<StockSuperVO> getAllStockSuperVO(String stockId) {
		// get from map
		// ValueWrapper valueObj = allStockSuperVOMap.get(stockId);
		// if (valueObj != null && valueObj.isYangEnough()) {
		// return valueObj.overList;
		// }

		// merge them into one overall VO
		List<StockSuperVO> overList = new ArrayList<StockSuperVO>();

		List<StockPriceVO> spList = stockPriceTable.getStockPriceById(stockId);
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

		// put to map
		// allStockSuperVOMap.put(stockId, new ValueWrapper(overList));

		return overList;
	}
}
