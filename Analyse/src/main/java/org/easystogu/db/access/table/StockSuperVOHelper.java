package org.easystogu.db.access.table;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.cassandra.access.table.IndKDJCassTableHelper;
import org.easystogu.cassandra.access.table.IndMacdCassTableHelper;
import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.easystogu.db.vo.table.BollVO;
import org.easystogu.db.vo.table.KDJVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.db.vo.table.QSDDVO;
import org.easystogu.db.vo.table.ShenXianVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.StockSuperVO;
import org.easystogu.db.vo.table.WRVO;
import org.easystogu.postgresql.access.table.IndKDJDBTableHelper;
import org.easystogu.postgresql.access.table.IndMacdDBTableHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("stockSuperVOHelper")
public class StockSuperVOHelper {
	@Autowired
	@Qualifier("qianFuQuanStockPriceTable")
	protected StockPriceTableHelper stockPriceTable;
	@Autowired
	@Qualifier("macdTable")
	protected IndicatorDBHelperIF macdTable;
	@Autowired
	@Qualifier("kdjTable")
	protected IndicatorDBHelperIF kdjTable;
	@Autowired
	@Qualifier("bollTable")
	protected IndicatorDBHelperIF bollTable;
	@Autowired
	@Qualifier("shenXianTable")
	protected IndicatorDBHelperIF shenXianTable;
	@Autowired
	@Qualifier("qsddTable")
	protected IndicatorDBHelperIF qsddTable;
	@Autowired
	@Qualifier("wrTable")
	protected IndicatorDBHelperIF wrTable;
	@Autowired
	protected IndDDXTableHelper ddxTable;

	public void validate() {
		if (this instanceof StockSuperVOHelper && stockPriceTable instanceof QianFuQuanStockPriceTableHelper
				&& (kdjTable instanceof IndKDJCassTableHelper || kdjTable instanceof IndKDJDBTableHelper)
				&& (macdTable instanceof IndMacdCassTableHelper || macdTable instanceof IndMacdDBTableHelper)) {
			// pass
		} else {
			throw new RuntimeException("SubClass ERROR: This is " + this.getClass().getSimpleName()
					+ ", stockPriceTable is " + stockPriceTable.getClass().getSimpleName() + ", kdjTable is "
					+ kdjTable.getClass().getSimpleName() + ", macdTable is " + macdTable.getClass().getSimpleName());
		}
	}

	public List<StockSuperVO> getAllStockSuperVO(String stockId) {
		validate();

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
		List<BollVO> bollList = bollTable.getAll(stockId);
		List<ShenXianVO> shenXianList = shenXianTable.getAll(stockId);
		List<QSDDVO> qsddList = qsddTable.getAll(stockId);
		List<WRVO> wrList = wrTable.getAll(stockId);

		if ((spList.size() != macdList.size()) || (macdList.size() != kdjList.size())
				|| (kdjList.size() != spList.size()) || (bollList.size() != spList.size())
				|| (shenXianList.size() != spList.size()) || (qsddList.size() != spList.size())
				|| (wrList.size() != spList.size())) {
			// System.out.println("rtn 1");
			return overList;
		}

		if ((spList.size() == 0) || (macdList.size() == 0) || (kdjList.size() == 0) || (bollList.size() == 0)
				|| (shenXianList.size() == 0) || (qsddList.size() == 0) || (wrList.size() == 0)) {
			// System.out.println("rtn 2");
			return overList;
		}

		if (!spList.get(0).date.equals(macdList.get(0).date) || !spList.get(0).date.equals(kdjList.get(0).date)
				|| !spList.get(0).date.equals(bollList.get(0).date)
				|| !spList.get(0).date.equals(shenXianList.get(0).date)
				|| !spList.get(0).date.equals(qsddList.get(0).date) || !spList.get(0).date.equals(wrList.get(0).date)) {
			// System.out.println("rtn 3");
			return overList;
		}

		for (int index = 0; index < spList.size(); index++) {
			StockSuperVO superVO = new StockSuperVO(spList.get(index), macdList.get(index), kdjList.get(index),
					bollList.get(index));
			superVO.setShenXianVO(shenXianList.get(index));
			superVO.setWRVO(wrList.get(index));
			superVO.setDdxVO(ddxTable.getDDX(superVO.priceVO.stockId, superVO.priceVO.date));
			superVO.setQsddVO(qsddList.get(index));
			overList.add(superVO);
		}

		// put to map
		// allStockSuperVOMap.put(stockId, new ValueWrapper(overList));

		return overList;
	}
}

class ValueWrapper {
	long putTimeStamp;
	List<StockSuperVO> overList;

	public ValueWrapper(List<StockSuperVO> overList) {
		this.putTimeStamp = System.currentTimeMillis();
		this.overList = new ArrayList<StockSuperVO>(overList);
	}

	public boolean isYangEnough() {
		// if the time is still within 1 hours
		if ((System.currentTimeMillis() - this.putTimeStamp) <= (1000 * 3600)) {
			return true;
		}
		return false;
	}
}
