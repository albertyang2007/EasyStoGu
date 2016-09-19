package org.easystogu.analyse.util;

import java.util.List;

import org.easystogu.db.vo.table.StockSuperVO;

public class VolumeIncreaseHelper {

	// ���㽻��volume��ǰһ�յİٷֱ�
	public static void volumeIncreasePuls(List<StockSuperVO> overList) {
		for (int index = 0; index < overList.size() - 1; index++) {
			StockSuperVO superVO = overList.get(index);
			StockSuperVO superNextVO = overList.get(index + 1);
			superNextVO.volumeIncreasePercent = superNextVO.priceVO.volume
					* 1.0 / superVO.priceVO.volume * 1.0;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
