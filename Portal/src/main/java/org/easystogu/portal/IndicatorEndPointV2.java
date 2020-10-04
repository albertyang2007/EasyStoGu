package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.db.vo.table.StockPriceVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//V2, query price and count in real time, qian FuQuan

@RestController
@RequestMapping(value = "/portal/indv2")
public class IndicatorEndPointV2 extends IndicatorEndPointV0{
	@Override
	protected List<StockPriceVO> fetchAllPrices(String stockid) {
		List<StockPriceVO> spList = new ArrayList<StockPriceVO>();
		List<StockPriceVO> cacheSpList = indicatorCache.queryByStockId(Constants.cacheQianFuQuanStockPrice + ":" +stockid);
		for (Object obj : cacheSpList) {
			spList.add((StockPriceVO)obj);
		}
		return spList;
	}
}
