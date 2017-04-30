package org.easystogu.portal.flags;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.db.vo.table.BBIVO;
import org.easystogu.db.vo.table.LuZaoVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.VolumeVO;
import org.easystogu.indicator.IND;
import org.easystogu.indicator.MAHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.portal.vo.ShenXianUIVO;
import org.slf4j.Logger;

import com.google.common.primitives.Doubles;

public class FlagsAnalyseHelper {
	private static Logger logger = LogHelper.getLogger(FlagsAnalyseHelper.class);

	public static List<ShenXianUIVO> shenXianBuySellFlagsAnalyse(List<StockPriceVO> spList, List<ShenXianUIVO> sxList,
			List<MacdVO> macdList, List<BBIVO> bbiList, List<LuZaoVO> luzaoList) {

		List<VolumeVO> volumeList = getMAVolumeList(spList);
		//
		for (StockPriceVO spvo : spList) {
			ShenXianUIVO sxvo = getShenXianIndVOByDate(spvo.date, sxList);
			MacdVO macdvo = getMacdIndVOByDate(spvo.date, macdList);
			BBIVO bbivo = getBBIIndVOByDate(spvo.date, bbiList);
			LuZaoVO luzaovo = getLuzaoIndVOByDate(spvo.date, luzaoList);
			VolumeVO volumevo = getVolumeVOByDate(spvo.date, volumeList);

			if (sxvo != null && macdvo != null && bbivo != null && luzaovo != null && volumevo != null) {

				String macdStr = macdvo.dif < 0 ? "零下" : "零上";

				// 金叉
				if (isMacdGordon(spvo.date, macdList) && isShenXianGordon(spvo.date, sxList)) {
					sxvo.setDuoFlagsTitle("G2");
					sxvo.setDuoFlagsText(macdStr + "Macd金叉, 神仙金叉");
				} else if (isMacdGordon(spvo.date, macdList)) {
					sxvo.setDuoFlagsTitle("G");
					sxvo.setDuoFlagsText(macdStr + "Macd金叉");
				} else if (isShenXianGordon(spvo.date, sxList)) {
					sxvo.setDuoFlagsTitle("G");
					sxvo.setDuoFlagsText("神仙金叉");
				}

				// 死叉
				if (isMacdDead(spvo.date, macdList) && isShenXianDead(spvo.date, sxList)) {
					sxvo.setDuoFlagsTitle("D2");
					sxvo.setDuoFlagsText(macdStr + "Macd神仙死叉");
				} else if (isMacdDead(spvo.date, macdList)) {
					sxvo.setDuoFlagsTitle("D");
					sxvo.setDuoFlagsText(macdStr + "Macd死叉");
				} else if (isShenXianDead(spvo.date, sxList)) {
					sxvo.setDuoFlagsTitle("D");
					sxvo.setDuoFlagsText("神仙死叉");
				}

				// 空头
				if (isBBIDead(spvo.date, bbiList)) {
					if (isMacdDead(spvo.date, macdList) && isShenXianDead(spvo.date, sxList)) {
						sxvo.setSellFlagsTitle("死空2");
						sxvo.setSellFlagsText(spvo.close + " " + macdStr + "Macd神仙死叉,清仓2/3");
					} else if (isMacdDead(spvo.date, macdList)) {
						sxvo.setSellFlagsTitle("死空");
						sxvo.setSellFlagsText(spvo.close + " " + macdStr + "Macd死叉,清仓2/3");
					} else if (isShenXianDead(spvo.date, sxList)) {
						sxvo.setSellFlagsTitle("死空");
						sxvo.setSellFlagsText(spvo.close + " 神仙死叉,清仓2/3");
					} else {
						sxvo.setSellFlagsTitle("空");
					}
				}

				// 多头
				if (isBBIGordon(spvo.date, bbiList)) {
					if (isMacdGordon(spvo.date, macdList) && isShenXianGordon(spvo.date, sxList)) {
						sxvo.setDuoFlagsTitle("金多2");
						sxvo.setDuoFlagsText(spvo.low + " " + macdStr + " Macd神仙金叉,试仓1/3");
					} else if (isShenXianGordon(spvo.date, sxList)) {
						sxvo.setDuoFlagsTitle("金多");
						sxvo.setDuoFlagsText(spvo.low + " 神仙金叉,试仓1/3");
					} else if (isMacdGordon(spvo.date, macdList)) {
						sxvo.setDuoFlagsTitle("金多");
						sxvo.setDuoFlagsText(spvo.low + " " + macdStr + " MACD金叉,试仓1/3");
					} else {
						// most of time come here
						sxvo.setDuoFlagsTitle("多");
						// sxvo.setDuoFlagsText("");
					}
				}

				// 多头做T
				if ((sxvo.h1 >= sxvo.h2)) {
					// buy point
					if (spvo.low <= sxvo.hc6) {
						sxvo.setBuyFlagsTitle("B");
						sxvo.setBuyFlagsText(sxvo.hc6 + " HC6支撑,增仓1/3");
					}

					// sell point
					if (spvo.high >= sxvo.hc5) {
						sxvo.setSellFlagsTitle("S");
						sxvo.setSellFlagsText(sxvo.hc5 + " HC5压力, 减仓1/3");
					}
				}

				// 86天缩量
				if (volumevo.maVolume < volumevo.hhvVolume / 10) {
					sxvo.setSuoFlagsTitle("缩");
					sxvo.setSuoFlagsText("成交萎缩");
				}
			}

		}
		return sxList;
	}

	private static ShenXianUIVO getShenXianIndVOByDate(String date, List<ShenXianUIVO> indList) {
		for (ShenXianUIVO vo : indList) {
			if (vo.date.equals(date))
				return vo;
		}
		return null;
	}

	private static MacdVO getMacdIndVOByDate(String date, List<MacdVO> indList) {
		for (MacdVO vo : indList) {
			if (vo.date.equals(date))
				return vo;
		}
		return null;
	}

	private static LuZaoVO getLuzaoIndVOByDate(String date, List<LuZaoVO> indList) {
		for (LuZaoVO vo : indList) {
			if (vo.date.equals(date))
				return vo;
		}
		return null;
	}

	private static BBIVO getBBIIndVOByDate(String date, List<BBIVO> indList) {
		for (BBIVO vo : indList) {
			if (vo.date.equals(date))
				return vo;
		}
		return null;
	}

	private static boolean isMacdGordon(String date, List<MacdVO> indList) {
		for (int index = 0; index < indList.size(); index++) {
			MacdVO curvo = indList.get(index);
			if (curvo.date.equals(date)) {
				if (index - 1 >= 0) {
					MacdVO prevo = indList.get(index - 1);
					if (curvo.macd >= 0 && prevo.macd < 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean isMacdDead(String date, List<MacdVO> indList) {
		for (int index = 0; index < indList.size(); index++) {
			MacdVO curvo = indList.get(index);
			if (curvo.date.equals(date)) {
				if (index - 1 >= 0) {
					MacdVO prevo = indList.get(index - 1);
					if (curvo.macd < 0 && prevo.macd >= 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean isBBIGordon(String date, List<BBIVO> indList) {
		for (int index = 0; index < indList.size(); index++) {
			BBIVO curvo = indList.get(index);
			if (curvo.date.equals(date)) {
				if (index - 1 >= 0) {
					BBIVO prevo = indList.get(index - 1);
					if (curvo.close > curvo.bbi && prevo.close < prevo.bbi) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean isBBIDead(String date, List<BBIVO> indList) {
		for (int index = 0; index < indList.size(); index++) {
			BBIVO curvo = indList.get(index);
			if (curvo.date.equals(date)) {
				if (index - 1 >= 0) {
					BBIVO prevo = indList.get(index - 1);
					if (curvo.close < curvo.bbi && prevo.close > prevo.bbi) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean isShenXianGordon(String date, List<ShenXianUIVO> indList) {
		for (int index = 0; index < indList.size(); index++) {
			ShenXianUIVO curvo = indList.get(index);
			if (curvo.date.equals(date)) {
				if (index - 1 >= 0) {
					ShenXianUIVO prevo = indList.get(index - 1);
					if (curvo.h1 > curvo.h2 && prevo.h1 < prevo.h2) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean isShenXianDead(String date, List<ShenXianUIVO> indList) {
		for (int index = 0; index < indList.size(); index++) {
			ShenXianUIVO curvo = indList.get(index);
			if (curvo.date.equals(date)) {
				if (index - 1 >= 0) {
					ShenXianUIVO prevo = indList.get(index - 1);
					if (curvo.h1 < curvo.h2 && prevo.h1 > prevo.h2) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static List<VolumeVO> getMAVolumeList(List<StockPriceVO> spList) {
		List<VolumeVO> volumesList = new ArrayList<VolumeVO>();
		List<Double> volumes = new ArrayList<Double>();
		for (StockPriceVO spvo : spList) {
			volumes.add(spvo.volume + 0.0);
		}

		double[] maVolumes = new MAHelper().getMAList(Doubles.toArray(volumes), 3);
		double[] hhvVolumes = new IND().HHV(maVolumes, 86);

		for (int i = 0; i < spList.size(); i++) {
			StockPriceVO spvo = spList.get(i);
			VolumeVO vvo = new VolumeVO();
			vvo.stockId = spvo.stockId;
			vvo.date = spvo.date;
			vvo.volume = spvo.volume;
			vvo.maVolume = (long) maVolumes[i];
			vvo.hhvVolume = (long) hhvVolumes[i];

			volumesList.add(vvo);
		}

		return volumesList;
	}

	private static VolumeVO getVolumeVOByDate(String date, List<VolumeVO> indList) {
		for (VolumeVO vo : indList) {
			if (vo.date.equals(date))
				return vo;
		}
		return null;
	}
}
