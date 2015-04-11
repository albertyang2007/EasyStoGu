package org.easystogu.analyse.util;

import java.util.List;

import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.ShenXianVO;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.utils.CrossType;

public class IndCrossCheckingHelper {

	// 输入的数组必须按照时间顺序
	public static void macdCross(List<StockSuperVO> overList) {
		for (int index = 0; index < (overList.size() - 1); index++) {
			StockSuperVO superVO = overList.get(index);
			StockSuperVO superNextVO = overList.get(index + 1);
			MacdVO vo = superVO.macdVO;
			MacdVO nextvo = superNextVO.macdVO;
			// check cross
			if ((vo.dif <= vo.dea) && (nextvo.dif > nextvo.dea)) {
				superNextVO.macdCorssType = CrossType.GORDON;
				continue;
			}

			if ((vo.dif >= vo.dea) && (nextvo.dif < nextvo.dea)) {
				superNextVO.macdCorssType = CrossType.DEAD;
				continue;
			}
		}
	}

	// 输入的数组必须按照时间顺序
	public static void kdjCross(List<StockSuperVO> overList) {

		for (int index = 0; index < (overList.size() - 1); index++) {
			StockSuperVO superVO = overList.get(index);
			StockSuperVO superNextVO = overList.get(index + 1);
			KDJVO vo = superVO.kdjVO;
			KDJVO nextvo = superNextVO.kdjVO;
			// check cross
			if ((vo.k <= vo.d) && (nextvo.k > nextvo.d)) {
				superNextVO.kdjCorssType = CrossType.GORDON;
				continue;
			}

			if ((vo.k >= vo.d) && (nextvo.k < nextvo.d)) {
				superNextVO.kdjCorssType = CrossType.DEAD;
				continue;
			}

			if ((vo.k / vo.d) <= 0.925) {
				if ((nextvo.k <= nextvo.d) && ((nextvo.k / nextvo.d) >= 0.975)) {
					superNextVO.kdjCorssType = CrossType.NEAR_GORDON;
					continue;
				}
			}
		}
	}

	// 输入的数组必须按照时间顺序
	public static void rsvCross(List<StockSuperVO> overList) {

		for (int index = 0; index < (overList.size() - 1); index++) {
			StockSuperVO superVO = overList.get(index);
			StockSuperVO superNextVO = overList.get(index + 1);
			KDJVO vo = superVO.kdjVO;
			KDJVO nextvo = superNextVO.kdjVO;
			// check cross
			if ((vo.rsv <= vo.k) && (nextvo.rsv > nextvo.k)) {
				superNextVO.rsvCorssType = CrossType.GORDON;
				continue;
			}

			if ((vo.rsv >= vo.k) && (nextvo.rsv < nextvo.k)) {
				superNextVO.rsvCorssType = CrossType.DEAD;
				continue;
			}
		}
	}

	public static void shenXianCross(List<StockSuperVO> overList) {

		for (int index = 0; index < (overList.size() - 1); index++) {
			StockSuperVO superVO = overList.get(index);
			StockSuperVO superNextVO = overList.get(index + 1);
			ShenXianVO vo = superVO.shenXianVO;
			ShenXianVO nextvo = superNextVO.shenXianVO;
			// check cross
			if ((vo.h1 <= vo.h2) && (nextvo.h1 > nextvo.h2)) {
				superNextVO.shenXianCorssType = CrossType.GORDON;
				// System.out.println("crossType shanXian Gordon:" + vo.stockId
				// + " " + vo.date);
				continue;
			}

			if ((vo.h1 >= vo.h2) && (nextvo.h1 < nextvo.h2)) {
				superNextVO.shenXianCorssType = CrossType.DEAD;
				// System.out.println("crossType shanXian Dead:" + vo.stockId +
				// " " + vo.date);
				continue;
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
