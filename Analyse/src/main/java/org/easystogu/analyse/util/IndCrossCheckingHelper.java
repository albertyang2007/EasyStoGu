package org.easystogu.analyse.util;

import java.util.List;

import org.easystogu.db.table.BollVO;
import org.easystogu.db.table.KDJVO;
import org.easystogu.db.table.MacdVO;
import org.easystogu.db.table.Mai1Mai2VO;
import org.easystogu.db.table.QSDDVO;
import org.easystogu.db.table.ShenXianVO;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.db.table.XueShi2VO;
import org.easystogu.db.table.YiMengBSVO;
import org.easystogu.db.table.ZhuliJinChuVO;
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

	public static void yiMengBSCross(List<StockSuperVO> overList) {

		for (int index = 0; index < (overList.size() - 1); index++) {
			StockSuperVO superVO = overList.get(index);
			StockSuperVO superNextVO = overList.get(index + 1);
			YiMengBSVO vo = superVO.yiMengBSVO;
			YiMengBSVO nextvo = superNextVO.yiMengBSVO;
			// check cross
			if ((vo.x2 <= vo.x3) && (nextvo.x2 > nextvo.x3)) {
				superNextVO.yiMengBSCrossType = CrossType.GORDON;
				continue;
			}

			if ((vo.x2 >= vo.x3) && (nextvo.x2 < nextvo.x3)) {
				superNextVO.yiMengBSCrossType = CrossType.DEAD;
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

	public static void zhuliJinChuCross(List<StockSuperVO> overList) {
		for (int index = 0; index < (overList.size() - 1); index++) {
			StockSuperVO superVO = overList.get(index);
			StockSuperVO superNextVO = overList.get(index + 1);
			ZhuliJinChuVO vo = superVO.zhuliJinChuVO;
			ZhuliJinChuVO nextvo = superNextVO.zhuliJinChuVO;

			// check cross
			if ((vo.duofang <= vo.kongfang) && (nextvo.duofang > nextvo.kongfang)) {
				superNextVO.zhuliJinChuCrossType = CrossType.GORDON;
				continue;
			}

			if ((vo.duofang >= vo.kongfang) && (nextvo.duofang < nextvo.kongfang)) {
				superNextVO.zhuliJinChuCrossType = CrossType.DEAD;
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

	public static void qsddCross(List<StockSuperVO> overList) {

		for (int index = 1; index < (overList.size() - 1); index++) {
			StockSuperVO superPre1VO = overList.get(index - 1);
			StockSuperVO superVO = overList.get(index);
			StockSuperVO superNextVO = overList.get(index + 1);
			QSDDVO prevo = superPre1VO.qsddVO;
			QSDDVO vo = superVO.qsddVO;
			QSDDVO nextvo = superNextVO.qsddVO;
			boolean isTop = false;
			boolean topArea = false;
			boolean bottomArea = false;
			// check TOP
			if ((vo.midTerm > 85.0) && (vo.shoTerm > 85.0) && (vo.lonTerm > 65.0)) {
				// cross(longTerm, shoTerm);
				if ((vo.lonTerm <= vo.shoTerm) && (nextvo.lonTerm > nextvo.shoTerm)) {
					isTop = true;
				}
			}

			// check TOP Area
			if ((nextvo.midTerm < vo.midTerm) && (vo.midTerm > 80.0) && ((vo.shoTerm > 95.0) || (prevo.shoTerm > 95.0))
					&& (nextvo.lonTerm > 60.0) && (nextvo.shoTerm < 83.5) && (nextvo.shoTerm < nextvo.midTerm)
					&& (nextvo.shoTerm < nextvo.lonTerm + 4.0)) {
				topArea = true;
			}

			// how to FILTER(顶部区域,4);???

			if (isTop || topArea) {
				superNextVO.qsddTopArea = true;
				continue;
			}

			// check Bottom
			boolean b1 = false;
			boolean b2 = false;
			boolean b3 = false;
			if ((nextvo.lonTerm < 12.0) && (nextvo.midTerm < 8.0) && ((nextvo.shoTerm < 7.2) || (vo.shoTerm < 5.0))
					&& ((nextvo.midTerm > vo.midTerm) || (nextvo.shoTerm > vo.shoTerm))) {
				b1 = true;
			}
			if ((nextvo.lonTerm < 8.0) && (nextvo.midTerm < 7.0) && (nextvo.shoTerm < 15.0)
					&& (nextvo.shoTerm > vo.shoTerm)) {
				b2 = true;
			}
			if ((nextvo.lonTerm < 10.0) && (nextvo.midTerm < 7.0) && (nextvo.shoTerm < 1.0)) {
				b3 = true;
			}
			if (b1 || b2 || b3) {
				bottomArea = true;
				superNextVO.qsddBottomArea = true;
			}

			// check bottom Cross
			if ((nextvo.lonTerm < 15.0) && (vo.lonTerm < 15.0) && (nextvo.midTerm < 18.0)
					&& (nextvo.shoTerm > vo.shoTerm)) {
				// cross(shoTerm, LonTerm)
				if ((vo.shoTerm <= vo.lonTerm) && (nextvo.shoTerm > nextvo.lonTerm)) {
					if ((nextvo.shoTerm > nextvo.midTerm) && ((vo.shoTerm < 5.0) || (prevo.shoTerm < 5.0))
							&& ((nextvo.midTerm >= nextvo.lonTerm) || (vo.shoTerm < 1.0))) {
						superNextVO.qsddBottomCrossType = CrossType.GORDON;
					}
				}
			}
		}
	}
	
	public static void wrCross(List<StockSuperVO> overList) {
		
	}

	// Line1 Cross Line2 ? true: false;
	private static boolean cross(double preLine1, double preLine2, double line1, double line2) {
		if ((preLine1 <= preLine2) && (line1 > line2)) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
