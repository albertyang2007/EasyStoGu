package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;

import org.easystogu.cache.CheckPointDailySelectionTableCache;
import org.easystogu.db.vo.table.BBIVO;
import org.easystogu.db.vo.table.CheckPointDailySelectionVO;
import org.easystogu.db.vo.table.LuZaoVO;
import org.easystogu.db.vo.table.MacdVO;
import org.easystogu.db.vo.table.StockPriceVO;
import org.easystogu.db.vo.table.VolumeVO;
import org.easystogu.indicator.IND;
import org.easystogu.indicator.MAHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.portal.vo.CheckPointFlagsVO;
import org.easystogu.portal.vo.ShenXianUIVO;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.google.common.primitives.Doubles;

@Component
public class FlagsAnalyseHelper {
	private static Logger logger = LogHelper.getLogger(FlagsAnalyseHelper.class);
	private CheckPointDailySelectionTableCache checkPointDailySelectionCache = CheckPointDailySelectionTableCache
			.getInstance();
	private static String[] zijinliuViewnames = { "luzao_phaseII_zijinliu_top300", "luzao_phaseIII_zijinliu_top300",
			"luzao_phaseII_ddx_bigger_05", "luzao_phaseIII_ddx_bigger_05", "luzao_phaseII_zijinliu_3_days_top300",
			"luzao_phaseII_zijinliu_3_of_5_days_top300", "luzao_phaseII_ddx_2_of_5_days_bigger_05",
			"luzao_phaseIII_zijinliu_3_days_top300", "luzao_phaseIII_zijinliu_3_of_5_days_top300",
			"luzao_phaseIII_ddx_2_of_5_days_bigger_05" };

	public List<ShenXianUIVO> shenXianBuySellFlagsAnalyse(List<StockPriceVO> spList, List<ShenXianUIVO> sxList,
			List<MacdVO> macdList, List<BBIVO> bbiList, List<LuZaoVO> luzaoList) {

		StockPriceVO spvoF = spList.get(spList.size() - 1);
		List<VolumeVO> volumeList = getMAVolumeList(spList);
		List<CheckPointDailySelectionVO> checkPoints = getCheckPoints(spvoF.stockId);
		//
		for (StockPriceVO spvo : spList) {
			ShenXianUIVO sxvo = getShenXianIndVOByDate(spvo.date, sxList);
			MacdVO macdvo = getMacdIndVOByDate(spvo.date, macdList);
			BBIVO bbivo = getBBIIndVOByDate(spvo.date, bbiList);
			LuZaoVO luzaovo = getLuzaoIndVOByDate(spvo.date, luzaoList);
			VolumeVO volumevo = getVolumeVOByDate(spvo.date, volumeList);
			// below can be search from table checkpoint_daily_selection
			CheckPointFlagsVO cpfvo = this.checkPoints(spvo.date, checkPoints);

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
						sxvo.setSellFlagsTitle("空D2");
						sxvo.setSellFlagsText(spvo.close + " " + macdStr + "Macd神仙死叉,清仓2/3");
					} else if (isMacdDead(spvo.date, macdList)) {
						sxvo.setSellFlagsTitle("空D");
						sxvo.setSellFlagsText(spvo.close + " " + macdStr + "Macd死叉,清仓2/3");
					} else if (isShenXianDead(spvo.date, sxList)) {
						sxvo.setSellFlagsTitle("空D");
						sxvo.setSellFlagsText(spvo.close + " 神仙死叉,清仓2/3");
					} else {
						// do not print it since too many occurs
						// sxvo.setSellFlagsTitle("空");
					}
				}

				// 多头
				if (isBBIGordon(spvo.date, bbiList)) {
					if (isMacdGordon(spvo.date, macdList) && isShenXianGordon(spvo.date, sxList)) {
						sxvo.setDuoFlagsTitle("多金2");
						sxvo.setDuoFlagsText(macdStr + " Macd神仙金叉,试仓1/3");
					} else if (isShenXianGordon(spvo.date, sxList)) {
						sxvo.setDuoFlagsTitle("多金");
						sxvo.setDuoFlagsText("神仙金叉,试仓1/3");
					} else if (isMacdGordon(spvo.date, macdList)) {
						sxvo.setDuoFlagsTitle("多金");
						sxvo.setDuoFlagsText(macdStr + " MACD金叉,试仓1/3");
					} else {
						// most of time come here
						// do not print it since too many occurs
						// sxvo.setDuoFlagsTitle("多");
						// sxvo.setDuoFlagsText("");
					}
				}

				// 多头做T
				// if ((sxvo.h1 >= sxvo.h2)) {
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
				// }

				// 86天缩量
				if (volumevo.maVolume < volumevo.hhvVolume / 10) {
					sxvo.setSuoFlagsTitle("缩");
					sxvo.setSuoFlagsText("成交萎缩");
				}

				// append if volume and bottom area or bottom gordon
				if (cpfvo.bottomAreaTitle.toString().trim().length() > 0) {
					sxvo.setSuoFlagsTitle(sxvo.getSuoFlagsTitle().trim() + cpfvo.bottomAreaTitle.toString().trim());
					sxvo.setSuoFlagsText(sxvo.getSuoFlagsText().trim() + cpfvo.bottomAreaText.toString().trim());
				}
				if (cpfvo.bottomGordonTitle.toString().trim().length() > 0) {
					sxvo.setSuoFlagsTitle(sxvo.getSuoFlagsTitle().trim() + cpfvo.bottomGordonTitle.toString().trim());
					sxvo.setSuoFlagsText(sxvo.getSuoFlagsText().trim() + cpfvo.bottomGordonText.toString().trim());
				}

				// append if it has weekGordon
				if (cpfvo.weekGordonTitle.toString().trim().length() > 0) {
					sxvo.setDuoFlagsTitle(cpfvo.weekGordonTitle.toString().trim() + sxvo.getDuoFlagsTitle());
					sxvo.setDuoFlagsText(cpfvo.weekGordonText.toString().trim() + sxvo.getDuoFlagsText());
				}

				// append if it has W Botton and Twice MACD Gordon
				if (cpfvo.wbottomMacdTwiceGordonTitle.toString().trim().length() > 0) {
					sxvo.setDuoFlagsTitle(
							cpfvo.wbottomMacdTwiceGordonTitle.toString().trim() + sxvo.getDuoFlagsTitle());
					sxvo.setDuoFlagsText(cpfvo.wbottomMacdTwiceGordonText.toString().trim() + sxvo.getDuoFlagsText());
				}

				// append if it has zijinliu
				if (cpfvo.ziJinLiuRuText.toString().trim().length() > 0) {
					sxvo.setDuoFlagsTitle(sxvo.getDuoFlagsTitle() + "钱");
					String info = sxvo.getDuoFlagsText().trim().length() > 0 ? sxvo.getDuoFlagsText() + " " : "";
					sxvo.setDuoFlagsText(info + "资金流入");
				}

			}

		}
		return sxList;
	}

	private ShenXianUIVO getShenXianIndVOByDate(String date, List<ShenXianUIVO> indList) {
		for (ShenXianUIVO vo : indList) {
			if (vo.date.equals(date))
				return vo;
		}
		return null;
	}

	private MacdVO getMacdIndVOByDate(String date, List<MacdVO> indList) {
		for (MacdVO vo : indList) {
			if (vo.date.equals(date))
				return vo;
		}
		return null;
	}

	private LuZaoVO getLuzaoIndVOByDate(String date, List<LuZaoVO> indList) {
		for (LuZaoVO vo : indList) {
			if (vo.date.equals(date))
				return vo;
		}
		return null;
	}

	private BBIVO getBBIIndVOByDate(String date, List<BBIVO> indList) {
		for (BBIVO vo : indList) {
			if (vo.date.equals(date))
				return vo;
		}
		return null;
	}

	private boolean isMacdGordon(String date, List<MacdVO> indList) {
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

	private boolean isMacdDead(String date, List<MacdVO> indList) {
		for (int index = 0; index < indList.size(); index++) {
			MacdVO curvo = indList.get(index);
			if (curvo.date.equals(date)) {
				if (index - 1 >= 0) {
					MacdVO prevo = indList.get(index - 1);
					if (curvo.macd <= 0 && prevo.macd > 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isBBIGordon(String date, List<BBIVO> indList) {
		for (int index = 0; index < indList.size(); index++) {
			BBIVO curvo = indList.get(index);
			if (curvo.date.equals(date)) {
				if (index - 1 >= 0) {
					BBIVO prevo = indList.get(index - 1);
					if (curvo.close >= curvo.bbi && prevo.close < prevo.bbi) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isBBIDead(String date, List<BBIVO> indList) {
		for (int index = 0; index < indList.size(); index++) {
			BBIVO curvo = indList.get(index);
			if (curvo.date.equals(date)) {
				if (index - 1 >= 0) {
					BBIVO prevo = indList.get(index - 1);
					if (curvo.close <= curvo.bbi && prevo.close > prevo.bbi) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isShenXianGordon(String date, List<ShenXianUIVO> indList) {
		for (int index = 0; index < indList.size(); index++) {
			ShenXianUIVO curvo = indList.get(index);
			if (curvo.date.equals(date)) {
				if (index - 1 >= 0) {
					ShenXianUIVO prevo = indList.get(index - 1);
					if (curvo.h1 >= curvo.h2 && prevo.h1 < prevo.h2) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isShenXianDead(String date, List<ShenXianUIVO> indList) {
		for (int index = 0; index < indList.size(); index++) {
			ShenXianUIVO curvo = indList.get(index);
			if (curvo.date.equals(date)) {
				if (index - 1 >= 0) {
					ShenXianUIVO prevo = indList.get(index - 1);
					if (curvo.h1 <= curvo.h2 && prevo.h1 > prevo.h2) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private List<VolumeVO> getMAVolumeList(List<StockPriceVO> spList) {
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

	private VolumeVO getVolumeVOByDate(String date, List<VolumeVO> indList) {
		for (VolumeVO vo : indList) {
			if (vo.date.equals(date))
				return vo;
		}
		return null;
	}

	private CheckPointFlagsVO checkPoints(String date, List<CheckPointDailySelectionVO> checkPoints) {
		CheckPointFlagsVO cpfvo = new CheckPointFlagsVO();
		for (CheckPointDailySelectionVO cpvo : checkPoints) {
			if (cpvo.date.equals(date)) {
				// check zijinliu
				for (String cp : zijinliuViewnames) {
					if (cpvo.checkPoint.toUpperCase().equals(cp.toUpperCase())) {
						cpfvo.ziJinLiuRuTitle.append("资");
						cpfvo.ziJinLiuRuText.append("资金流入");
						break;
					}
				}

				// check week Gordon
				if (cpvo.checkPoint.toUpperCase().contains("MACD_WEEK_GORDON_MACD_DAY_DIF_CROSS_0")) {
					cpfvo.weekGordonTitle.append("MD周");
					cpfvo.weekGordonText.append("MACD周线金叉DIF日线金叉");
				}
				if (cpvo.checkPoint.toUpperCase().contains("MACD_WEEK_GORDON_KDJ_WEEK_GORDON")) {
					cpfvo.weekGordonTitle.append("MK周");
					cpfvo.weekGordonText.append("MACD周线金叉日线KDJ金叉");
				}

				// check buttom
				if (cpvo.checkPoint.toUpperCase().equals("WR_Bottom_Area".toUpperCase())) {
					cpfvo.bottomAreaTitle.append("W底");
					cpfvo.bottomAreaText.append("WR底部区间");
				}
				if (cpvo.checkPoint.toUpperCase().equals("QSDD_Bottom_Area".toUpperCase())) {
					cpfvo.bottomAreaTitle.append("Q底");
					cpfvo.bottomAreaText.append("QSDD底部区间");
				}

				// check buttom gordon

				if (cpvo.checkPoint.toUpperCase().equals("WR_Bottom_Gordon".toUpperCase())) {
					cpfvo.bottomGordonTitle.append("W金");
					cpfvo.bottomGordonText.append("WR底部金叉");
				}
				if (cpvo.checkPoint.toUpperCase().equals("QSDD_Bottom_Gordon".toUpperCase())) {
					cpfvo.bottomGordonTitle.append("Q金");
					cpfvo.bottomGordonText.append("QSDD底部金叉");
				}

				// macd二次金叉，W底，MACD背离
				if (cpvo.checkPoint.toUpperCase().contains("MACD_TWICE_GORDON_W_Botton_MACD_DI_BEILI".toUpperCase())) {
					cpfvo.wbottomMacdTwiceGordonTitle.append("W底");
					cpfvo.wbottomMacdTwiceGordonText.append("MACD二次金叉,W底背离");
				}
			}

		}
		return cpfvo;
	}

	private List<CheckPointDailySelectionVO> getCheckPoints(String stockId) {
		return checkPointDailySelectionCache.getCheckPointByStockId(stockId);
	}
}
