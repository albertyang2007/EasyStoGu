package org.easystogu.db.access.table.cache;

import java.util.List;

import org.easystogu.db.vo.table.StockPriceVO;

public interface CacheAbleStock {
	public List<StockPriceVO> queryByStockId(String stockId);
}
