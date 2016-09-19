package org.easystogu.analyse.util;

import java.util.List;

import org.easystogu.db.vo.table.StockSuperVO;

public class VolumeHelper {

	// ���㽻��volume��ǰһ�յİٷֱ�
	public static void volumeIncreasePuls(List<StockSuperVO> overList) {
		for (int index = 0; index < overList.size() - 1; index++) {
			StockSuperVO superVO = overList.get(index);
			StockSuperVO superNextVO = overList.get(index + 1);
			superNextVO.volumeIncreasePercent = superNextVO.priceVO.volume
					* 1.0 / superVO.priceVO.volume * 1.0;
		}
	}

	// 计算5天平均成交量
	public static void avgVolume5(List<StockSuperVO> overList) {
		for (int index = 4; index < overList.size() - 1; index++) {
			StockSuperVO superVO = overList.get(index);
			StockSuperVO pre1VO = overList.get(index - 1);
			StockSuperVO pre2VO = overList.get(index - 2);
			StockSuperVO pre3VO = overList.get(index - 3);
			StockSuperVO pre4VO = overList.get(index - 4);

			superVO.avgVol5 = (superVO.priceVO.volume + pre1VO.priceVO.volume
					+ pre2VO.priceVO.volume + pre3VO.priceVO.volume + pre4VO.priceVO.volume) / 5;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
