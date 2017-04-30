package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.indicator.runner.utils.StockPriceFetcher;

import com.google.common.primitives.Doubles;

public class BBIHelper extends IND {
	public double[][] getBBIList(double[] CLOSE) {
		double[][] bbi = new double[2][CLOSE.length];

		bbi[0] = DIV((ADD(MA(CLOSE, 3), MA(CLOSE, 6), MA(CLOSE, 12), MA(CLOSE, 24))), 4);
		bbi[1] = CLOSE;
		return bbi;
	}

	public static void main(String[] args) {
		BBIHelper helper = new BBIHelper();
		StockPriceTableHelper stockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
		List<StockPriceVO> spList = stockPriceTable.getStockPriceById("601336");
		List<Double> close = StockPriceFetcher.getClosePrice(spList);
		double bbi[][] = helper.getBBIList(Doubles.toArray(close));

		System.out.println(bbi[0][close.size() - 6]);
		System.out.println(bbi[1][close.size() - 6]);
	}
}
