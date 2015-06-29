package org.easystogu.analyse.util;

import java.util.List;

import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.Mai1Mai2VO;
import org.easystogu.db.table.ShenXianVO;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.db.table.XueShi2VO;
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

			if ((vo.k / vo.d) <= 0.925) {
				if ((nextvo.k <= nextvo.d) && ((nextvo.k / nextvo.d) >= 0.975)) {
					superNextVO.kdjCorssType = CrossType.NEAR_GORDON;
					continue;
				}
			}

			if ((vo.k >= vo.d) && (nextvo.k < nextvo.d)) {
				superNextVO.kdjCorssType = CrossType.DEAD;
				continue;
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

	public static void bollXueShi2DnCross(List<StockSuperVO> overList) {

		for (int index = 0; index < (overList.size() - 1); index++) {
			StockSuperVO superVO = overList.get(index);
			StockSuperVO superNextVO = overList.get(index + 1);
			BollVO bullVo = superVO.bollVO;
			BollVO nextBullVo = superNextVO.bollVO;

			XueShi2VO xueShi2Vo = superVO.xueShi2VO;
			XueShi2VO nextXueShi2Vo = superNextVO.xueShi2VO;
			// check cross
			if ((bullVo.dn > xueShi2Vo.dn) && (nextBullVo.dn < nextXueShi2Vo.dn)) {
				superNextVO.bullXueShi2DnCrossType = CrossType.GORDON;
				continue;
			}

			if ((xueShi2Vo.dn / bullVo.dn) < 0.98) {
				if ((nextXueShi2Vo.dn / nextBullVo.dn) > 0.98) {
					// boll is lower and lower, xueShi2 is bigger and bigger
					if (bullVo.dn > nextBullVo.dn && xueShi2Vo.dn < nextXueShi2Vo.dn) {
						superNextVO.bullXueShi2DnCrossType = CrossType.NEAR_GORDON;
						continue;
					}
				}
			}

			if ((bullVo.dn < xueShi2Vo.dn) && (nextBullVo.dn > nextXueShi2Vo.dn)) {
				superNextVO.bullXueShi2DnCrossType = CrossType.DEAD;
				continue;
			}
		}
	}

	public static void shenXianCross12(List<StockSuperVO> overList) {

		for (int index = 0; index < (overList.size() - 1); index++) {
			StockSuperVO superVO = overList.get(index);
			StockSuperVO superNextVO = overList.get(index + 1);
			ShenXianVO vo = superVO.shenXianVO;
			ShenXianVO nextvo = superNextVO.shenXianVO;
			// check cross
			if ((vo.h1 <= vo.h2) && (nextvo.h1 > nextvo.h2)) {
				superNextVO.shenXianCorssType12 = CrossType.GORDON;
				continue;
			}

			if ((vo.h1 >= vo.h2) && (nextvo.h1 < nextvo.h2)) {
				superNextVO.shenXianCorssType12 = CrossType.DEAD;
				continue;
			}
		}
	}

	// mai1 gordon cross
	public static void mai1Mai2Cross(List<StockSuperVO> overList) {
		for (int index = 0; index < (overList.size() - 1); index++) {
			StockSuperVO superVO = overList.get(index);
			StockSuperVO superNextVO = overList.get(index + 1);
			Mai1Mai2VO vo = superVO.mai1mai2VO;
			Mai1Mai2VO nextvo = superNextVO.mai1mai2VO;

			// check cross
			if ((vo.sk <= vo.sd) && (nextvo.sk > nextvo.sd) && vo.sk < 0) {
				superNextVO.mai1mai2CrossTypeMai1 = CrossType.GORDON;
				continue;
			}

			if ((vo.sk <= 0) && (nextvo.sk > 0)) {
				superNextVO.mai1mai2CrossTypeMai2 = CrossType.GORDON;
				continue;
			}

			if ((vo.sk <= vo.sd) && (nextvo.sk > nextvo.sd) && vo.sk > 0) {
				superNextVO.mai1mai2CrossTypeMai2 = CrossType.GORDON;
				continue;
			}

			if ((vo.sd <= vo.sk) && (nextvo.sd > nextvo.sk)) {
				superNextVO.mai1mai2CrossTypeMai1 = CrossType.DEAD;
				superNextVO.mai1mai2CrossTypeMai2 = CrossType.DEAD;
				continue;
			}
		}
	}

	public static void shenXianCross13(List<StockSuperVO> overList) {

		for (int index = 0; index < (overList.size() - 1); index++) {
			StockSuperVO superVO = overList.get(index);
			StockSuperVO superNextVO = overList.get(index + 1);
			ShenXianVO vo = superVO.shenXianVO;
			ShenXianVO nextvo = superNextVO.shenXianVO;
			// check cross
			if ((vo.h1 <= vo.h3) && (nextvo.h1 > nextvo.h3)) {
				superNextVO.shenXianCorssType13 = CrossType.GORDON;
				continue;
			}

			if ((vo.h1 >= vo.h3) && (nextvo.h1 < nextvo.h3)) {
				superNextVO.shenXianCorssType13 = CrossType.DEAD;
				continue;
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
