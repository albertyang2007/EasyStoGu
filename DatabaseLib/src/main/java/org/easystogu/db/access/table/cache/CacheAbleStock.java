package org.easystogu.db.access.table.cache;

import java.util.List;

public interface CacheAbleStock {
	public List<Object> queryByStockId(String stockId);
}
