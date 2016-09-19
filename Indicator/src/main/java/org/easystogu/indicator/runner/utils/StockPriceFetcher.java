package org.easystogu.indicator.runner.utils;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.vo.table.StockPriceVO;

public class StockPriceFetcher {
	public static List<Double> getOpenPrice(List<StockPriceVO> priceList) {
		List<Double> prices = new ArrayList<Double>(priceList.size());
		for (StockPriceVO spVO : priceList) {
			prices.add(new Double(spVO.open));
		}
		return prices;
	}

	public static List<Double> getLowPrice(List<StockPriceVO> priceList) {
		List<Double> prices = new ArrayList<Double>(priceList.size());
		for (StockPriceVO spVO : priceList) {
			prices.add(new Double(spVO.low));
		}
		return prices;
	}

	public static List<Double> getHighPrice(List<StockPriceVO> priceList) {
		List<Double> prices = new ArrayList<Double>(priceList.size());
		for (StockPriceVO spVO : priceList) {
			prices.add(new Double(spVO.high));
		}
		return prices;
	}

	public static List<Double> getClosePrice(List<StockPriceVO> priceList) {
		List<Double> prices = new ArrayList<Double>(priceList.size());
		for (StockPriceVO spVO : priceList) {
			prices.add(new Double(spVO.close));
		}
		return prices;
	}
}
