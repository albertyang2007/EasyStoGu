package org.easystogu.analyse;

import java.util.List;

import org.easystogu.checkpoint.CheckPoint;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.utils.CrossType;

public class AnalyserHelper {

	private double KDJMin = 15.0;
	private double KDJMax = 85.0;

	public boolean isConditionSatisfy(CheckPoint checkPoint, List<StockSuperVO> overList) {

		int index = overList.size() - 1;
		StockSuperVO superVO = overList.get(index);

		switch (checkPoint) {
		case MACD_Gordon:
			return superVO.macdCorssType == CrossType.GORDON ? true : false;
		case KDJ_Gordon:
			return superVO.kdjCorssType == CrossType.GORDON ? true : false;

		case KDJ_Near_Gordon:
			return superVO.kdjCorssType == CrossType.NEAR_GORDON ? true : false;
		case Now_3_Days_Red: {
			StockSuperVO pre1VO = overList.get(index - 1);
			StockSuperVO pre2VO = overList.get(index - 2);
			if (superVO.priceVO.isKLineRed() && pre1VO.priceVO.isKLineRed() && pre2VO.priceVO.isKLineRed()) {
				return true;
			}
			return false;
		}
		case Pre_3_Days_Green: {
			StockSuperVO pre1VO = overList.get(index - 1);
			StockSuperVO pre2VO = overList.get(index - 2);
			StockSuperVO pre3VO = overList.get(index - 3);
			if (pre1VO.priceVO.isKLineGreen() && pre2VO.priceVO.isKLineGreen() && pre3VO.priceVO.isKLineGreen()) {
				return true;
			}
			return false;
		}
		case KDJ_Green_Gordon:
			if ((superVO.kdjCorssType == CrossType.GORDON) && superVO.priceVO.isKLineGreen()) {
				return true;
			}
			return false;
		case High_MA5_MA10:
			if (superVO.priceVO.close >= superVO.avgMA5 && superVO.priceVO.close >= superVO.avgMA10) {
				return true;
			}
			return false;
		case Pre_Low_MA5_MA10:
			StockSuperVO pre1VO = overList.get(index - 1);
			if (pre1VO.priceVO.close < pre1VO.avgMA5 && pre1VO.priceVO.close < pre1VO.avgMA10) {
				return true;
			}
			return false;
		case BOLL_Between_MB_UP:
			if (superVO.bollVO.up > superVO.priceVO.close && superVO.priceVO.close > superVO.bollVO.mb) {
				return true;
			}
			return false;
		case Volume_Big_1:
			return superVO.volumeIncreasePercent >= 1.0 ? true : false;
		case Volume_Big_2:
			return superVO.volumeIncreasePercent >= 2.0 ? true : false;
		case Volume_Big_3:
			return superVO.volumeIncreasePercent >= 3.0 ? true : false;
		case Volume_Big_5:
			return superVO.volumeIncreasePercent >= 5.0 ? true : false;
		default:
			return false;
		}
	}

	public void setKDJRange(double min, double max) {
		this.KDJMin = min;
		this.KDJMax = max;
	}
}
