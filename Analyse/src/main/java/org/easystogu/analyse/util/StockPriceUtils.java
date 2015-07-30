package org.easystogu.analyse.util;

import org.easystogu.db.table.StockPriceVO;

public class StockPriceUtils {
	// check the K line of stock: big/small red, or big/small green
	public static boolean isKLineBigRed(StockPriceVO vo) {
		if ((vo.close > vo.lastClose) && (vo.high >= vo.close)
				&& (vo.close > vo.open) && (vo.open >= vo.low)) {
			double r1 = vo.close - vo.open;
			double r2 = vo.high - vo.low;
			if ((r1 / r2) >= 0.7) {
				return true;
			}
		}
		return false;
	}

	public static boolean isKLineRed(StockPriceVO vo) {
		if ((vo.close >= vo.lastClose) && (vo.high >= vo.close)
				&& (vo.close >= vo.open) && (vo.open >= vo.low)) {
			return true;
		}
		return false;
	}

	public static boolean isKLineRed(StockPriceVO vo, double incPerncent) {
		if ((vo.close > vo.lastClose) && (vo.high >= vo.close)
				&& (vo.close >= vo.open) && (vo.open >= vo.low)) {
			if ((((vo.close - vo.lastClose) * 100.0) / vo.lastClose) >= incPerncent) {
				return true;
			}
		}
		return false;
	}

	public static boolean isKLineRed(StockPriceVO vo, double incMinPerncent,
			double incMaxPercent) {
		if ((vo.close > vo.lastClose) && (vo.high >= vo.close)
				&& (vo.close >= vo.open) && (vo.open >= vo.low)) {
			double inc = ((vo.close - vo.lastClose) * 100.0) / vo.lastClose;
			if ((inc >= incMinPerncent) && (inc <= incMaxPercent)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isKLineBigGreen(StockPriceVO vo) {
		if ((vo.close < vo.lastClose) && (vo.high >= vo.open)
				&& (vo.open > vo.close) && (vo.close >= vo.low)) {
			double r1 = vo.open - vo.close;
			double r2 = vo.high - vo.low;
			if ((r1 / r2) >= 0.70) {
				return true;
			}
		}
		return false;
	}

	public static boolean isKLineGreen(StockPriceVO vo) {
		if ((vo.close < vo.lastClose) && (vo.high >= vo.open)
				&& (vo.open >= vo.close) && (vo.close >= vo.low)) {
			return true;
		}
		return false;
	}

	public static boolean isKLineRedCross(StockPriceVO vo) {
		if ((vo.high >= vo.close) && (vo.close >= vo.open)
				&& (vo.open >= vo.low)) {
			double r1 = vo.open - vo.close;
			double r2 = vo.high - vo.low;
			if ((r1 / r2) <= 0.30) {
				return true;
			}
		}
		return false;
	}

	public static boolean isKLineOpenEqualLower(StockPriceVO vo) {
		return (vo.open == vo.low) ? true : false;
	}

	public static boolean isKLineCloseEqualHigh(StockPriceVO vo) {
		return (vo.close == vo.high) ? true : false;
	}
}
