package org.easystogu.analyse;

import java.util.List;

import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.db.table.StockSuperVO;
import org.easystogu.utils.CrossType;

public class CombineAnalyseHelper {

	public int[] tempInputArgs = new int[2];// just for temp history analyse

	// overList is order by date, it is daily price and ind
	public boolean isConditionSatisfy(DailyCombineCheckPoint checkPoint, List<StockSuperVO> overDayList,
			List<StockSuperVO> overWeekList) {

		if ((overWeekList == null) || (overWeekList.size() <= 1)) {
			return false;
		}

		StockSuperVO curSuperWeekVO = overWeekList.get(overWeekList.size() - 1);
		StockSuperVO pre1SuperWeekVO = overWeekList.get(overWeekList.size() - 2);

		int dayLength = overDayList.size();
		StockSuperVO curSuperDayVO = overDayList.get(overDayList.size() - 1);
		StockSuperVO pre1SuperDayVO = overDayList.get(overDayList.size() - 2);
		StockSuperVO pre2SuperDayVO = overDayList.get(overDayList.size() - 3);
		StockSuperVO pre3SuperDayVO = overDayList.get(overDayList.size() - 4);
		StockSuperVO pre4SuperDayVO = overDayList.get(overDayList.size() - 5);
		StockSuperVO pre5SuperDayVO = overDayList.get(overDayList.size() - 6);

		switch (checkPoint) {
		case MACD_Gordon:
			if (curSuperDayVO.macdCorssType == CrossType.GORDON) {
				return true;
			}
			break;
		case KDJ_Gordon:
			if (curSuperDayVO.kdjCorssType == CrossType.GORDON) {
				return true;
			}
			break;
		case MACD_KDJ_Gordon_3_Days_Red_MA_Ronghe_XiangShang:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// Macd & KDJ Gordon, 3 days red, volume bigger then bigger, last
			// vol bigger than avg5
			if (overDayList.size() >= 3) {
				if ((curSuperDayVO.kdjCorssType == CrossType.GORDON)
						&& ((curSuperDayVO.macdCorssType == CrossType.GORDON))) {
					if (curSuperDayVO.priceVO.isKLineRed() && (curSuperDayVO.volumeIncreasePercent >= 1.0)) {
						if (pre1SuperDayVO.priceVO.isKLineRed() && (pre1SuperDayVO.volumeIncreasePercent >= 1.0)
								&& pre2SuperDayVO.priceVO.isKLineRed()) {
							return MA5_MA10_MA20_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
						}
					}
				}
			}
			break;
		case MACD_KDJ_Gordon_Pre_3_Days_Green:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			if (overDayList.size() >= 4) {
				if ((curSuperDayVO.kdjCorssType == CrossType.GORDON)
						&& (curSuperDayVO.macdCorssType == CrossType.GORDON)) {
					if (curSuperDayVO.priceVO.isKLineRed() && (curSuperDayVO.volumeIncreasePercent >= 1.0)) {
						if (pre1SuperDayVO.priceVO.isKLineGreen() && pre2SuperDayVO.priceVO.isKLineGreen()
								&& pre3SuperDayVO.priceVO.isKLineGreen()) {
							if ((pre1SuperDayVO.volumeIncreasePercent < 1.0)
									&& (pre2SuperDayVO.volumeIncreasePercent < 1.0)) {
								return true;
							}
						}
					}
				}
			}
			break;
		case KDJ_Gordon_3_days_Red:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// 3 days red, volume bigger then bigger
			if (overDayList.size() >= 3) {
				if ((curSuperDayVO.kdjCorssType == CrossType.GORDON)) {
					if (curSuperDayVO.priceVO.isKLineRed() && (curSuperDayVO.volumeIncreasePercent >= 1.0)) {
						if (pre1SuperDayVO.priceVO.isKLineRed() && (pre1SuperDayVO.volumeIncreasePercent >= 1.0)
								&& pre2SuperDayVO.priceVO.isKLineRed()) {
							return true;
						}
					}
				}
			}
			break;
		case KDJ_Gordon_Pre_3_Days_Green:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// KDJ Red Gordon and Previous 3 days Green, 缂傗晠鍣烘稉瀣┘閻掕泛鎮梜dj缁撅拷
			if (overDayList.size() >= 4) {
				if (curSuperDayVO.kdjCorssType == CrossType.GORDON) {
					if (curSuperDayVO.priceVO.isKLineRed() && (curSuperDayVO.volumeIncreasePercent >= 1.0)) {
						if (pre1SuperDayVO.priceVO.isKLineGreen() && pre2SuperDayVO.priceVO.isKLineGreen()
								&& pre3SuperDayVO.priceVO.isKLineGreen()) {
							if ((pre1SuperDayVO.volumeIncreasePercent < 1.0)
									&& (pre2SuperDayVO.volumeIncreasePercent < 1.0)) {
								return true;
							}
						}
					}
				}
			}
			break;
		case KDJ_Green_Gordon_Pre_2_Days_Red:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// KDJ Gordon but Green, close price higher than previous day,
			// previous 2 days red
			if (overDayList.size() >= 3) {
				if ((curSuperDayVO.kdjCorssType == CrossType.GORDON) && curSuperDayVO.priceVO.isKLineGreen()) {
					if (curSuperDayVO.volumeIncreasePercent >= 1.0) {
						if (curSuperDayVO.priceVO.close > pre1SuperDayVO.priceVO.close) {
							if (pre1SuperDayVO.priceVO.isKLineRed() && pre2SuperDayVO.priceVO.isKLineRed()
									&& (pre1SuperDayVO.volumeIncreasePercent >= 1.0)) {
								return true;
							}
						}
					}
				}
			}
			break;
		case KDJ_Gordon_High_MA5_MA10_BOLL:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// KDJ Red Gordon, close > MA5 & MA10; Previous close < MA5 & MA10;
			// Close between UP and MB
			if (overDayList.size() >= 20) {
				if ((curSuperDayVO.kdjCorssType == CrossType.GORDON) && (curSuperDayVO.volumeIncreasePercent >= 1.0)
						&& curSuperDayVO.priceVO.isKLineRed()) {
					if (pre1SuperDayVO.priceVO.isKLineRed()) {
						if ((curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5)
								&& (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10)
								&& (pre1SuperDayVO.priceVO.close < pre1SuperDayVO.avgMA5)
								&& (pre1SuperDayVO.priceVO.close < pre1SuperDayVO.avgMA10)) {
							if ((curSuperDayVO.bollVO.up > curSuperDayVO.priceVO.close)
									&& (curSuperDayVO.priceVO.close > curSuperDayVO.bollVO.mb)) {
								return true;
							}
						}
					}
				}
			}
			break;
		case MACD_KDJ_Gordon_High_MA5_MA10_BOLL_MA_RongHe_XiangShang:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// MACD & KDJ Red Gordon, close > MA5 & MA10; Previous close < MA5 &
			// MA10; Close between UP and MB
			if (overDayList.size() >= 20) {
				if ((curSuperDayVO.kdjCorssType == CrossType.GORDON)
						&& (curSuperDayVO.macdCorssType == CrossType.GORDON)
						&& (curSuperDayVO.volumeIncreasePercent >= 1.0) && curSuperDayVO.priceVO.isKLineRed()) {
					if (pre1SuperDayVO.priceVO.isKLineRed()) {
						if ((curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5)
								&& (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10)
								&& (pre1SuperDayVO.priceVO.close < pre1SuperDayVO.avgMA5)
								&& (pre1SuperDayVO.priceVO.close < pre1SuperDayVO.avgMA10)) {
							if ((curSuperDayVO.bollVO.up > curSuperDayVO.priceVO.close)
									&& (curSuperDayVO.priceVO.close > curSuperDayVO.bollVO.mb)) {
								return this.MA5_MA10_MA20_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
							}
						}
					}
				}
			}
			break;
		case KDJ_Near_Gordon_High_MA5_MA10_BOLL:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// KDJ Red Near Gordon, close > MA5 & MA10; Previous close < MA5 &
			// MA10; Close between UP and MB
			if (overDayList.size() >= 20) {
				if ((curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON)
						&& (curSuperDayVO.volumeIncreasePercent >= 1.0) && curSuperDayVO.priceVO.isKLineRed()) {
					if (pre1SuperDayVO.priceVO.isKLineRed()) {
						if ((curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5)
								&& (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10)
								&& (pre1SuperDayVO.priceVO.close < pre1SuperDayVO.avgMA5)
								&& (pre1SuperDayVO.priceVO.close < pre1SuperDayVO.avgMA10)) {
							if ((curSuperDayVO.bollVO.up > curSuperDayVO.priceVO.close)
									&& (curSuperDayVO.priceVO.close > curSuperDayVO.bollVO.mb)) {
								return true;
							}
						}
					}
				}
			}
			break;
		case RSV_Gordon_High_MA5_MA10_BOLL:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// RSV Gordon, close > MA5 & MA10; Previous close < MA5 &
			// MA10; Close between UP and MB
			if (overDayList.size() >= 20) {
				if ((curSuperDayVO.rsvCorssType == CrossType.GORDON) && (curSuperDayVO.volumeIncreasePercent >= 1.0)
						&& curSuperDayVO.priceVO.isKLineRed()) {
					if (pre1SuperDayVO.priceVO.isKLineRed()) {
						if ((curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5)
								&& (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10)
								&& (pre1SuperDayVO.priceVO.close < pre1SuperDayVO.avgMA5)
								&& (pre1SuperDayVO.priceVO.close < pre1SuperDayVO.avgMA10)) {
							if ((curSuperDayVO.bollVO.up > curSuperDayVO.priceVO.close)
									&& (curSuperDayVO.priceVO.close > curSuperDayVO.bollVO.mb)) {
								return true;
							}
						}
					}
				}
			}
			break;
		case MACD_KDJ_Gordon_3_Days_Red_High_MA5_MA10_BOLL:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			if (overDayList.size() >= 20) {
				if ((curSuperDayVO.kdjCorssType == CrossType.GORDON)
						&& ((curSuperDayVO.macdCorssType == CrossType.GORDON))) {
					if (curSuperDayVO.priceVO.isKLineRed() && (curSuperDayVO.volumeIncreasePercent >= 1.0)) {
						if (pre1SuperDayVO.priceVO.isKLineRed() && (pre1SuperDayVO.volumeIncreasePercent >= 1.0)
								&& pre2SuperDayVO.priceVO.isKLineRed()) {
							if ((curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5)
									&& (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10)
									&& (pre1SuperDayVO.priceVO.close < pre1SuperDayVO.avgMA5)
									&& (pre1SuperDayVO.priceVO.close < pre1SuperDayVO.avgMA10)) {
								if ((curSuperDayVO.bollVO.up > curSuperDayVO.priceVO.close)
										&& (curSuperDayVO.priceVO.close > curSuperDayVO.bollVO.mb)) {
									return true;
								}
							}
						}
					}
				}
			}
			break;
		case KDJ_Green_Gordon_Pre_2_Days_Red_High_MA5_MA10_BOLL:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			if (overDayList.size() >= 20) {
				if ((curSuperDayVO.kdjCorssType == CrossType.GORDON) && curSuperDayVO.priceVO.isKLineGreen()) {
					if (curSuperDayVO.volumeIncreasePercent >= 1.0) {
						if (curSuperDayVO.priceVO.close > pre1SuperDayVO.priceVO.close) {
							if (pre1SuperDayVO.priceVO.isKLineRed() && pre2SuperDayVO.priceVO.isKLineRed()
									&& (pre1SuperDayVO.volumeIncreasePercent >= 1.0)) {
								if ((curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5)
										&& (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10)
										&& (pre1SuperDayVO.priceVO.close < pre1SuperDayVO.avgMA5)
										&& (pre1SuperDayVO.priceVO.close < pre1SuperDayVO.avgMA10)) {
									if ((curSuperDayVO.bollVO.up > curSuperDayVO.priceVO.close)
											&& (curSuperDayVO.priceVO.close > curSuperDayVO.bollVO.mb)) {
										return true;
									}
								}
							}
						}
					}
				}
			}
			break;
		case Day_MACD_KDJ_Gordon_Week_KDJ_Gordon:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// if today or yesterday is macd gordon
			if ((curSuperDayVO.macdCorssType == CrossType.GORDON) || (pre1SuperDayVO.macdCorssType == CrossType.GORDON)) {
				// if today or yesterday is kdj gordon
				if ((curSuperDayVO.kdjCorssType == CrossType.GORDON)
						|| (pre1SuperDayVO.kdjCorssType == CrossType.GORDON)) {
					// if this week is kdj gordon
					if ((curSuperWeekVO.kdjCorssType == CrossType.GORDON)) {
						return true;
					}
				}
			}

			break;
		case Day_MACD_KDJ_Gordon_Week_MACD_Gordon:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// if today or yesterday is macd gordon
			if ((curSuperDayVO.macdCorssType == CrossType.GORDON) || (pre1SuperDayVO.macdCorssType == CrossType.GORDON)) {
				// if today or yesterday is kdj gordon
				if ((curSuperDayVO.kdjCorssType == CrossType.GORDON)
						|| (pre1SuperDayVO.kdjCorssType == CrossType.GORDON)) {
					// if this week is macd gordon
					if ((curSuperWeekVO.macdCorssType == CrossType.GORDON)) {
						return true;
					}
				}
			}
			break;
		case Phase2_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_RSV_KDJ_Gordon: {

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// first macd gordon is under zero, now macd is dead or near dead,
			// looking for the second above zero macd gordon
			if (overDayList.size() < 40) {
				return false;
			}

			// limit two macd gordon and dead point to about 30 working days
			List<StockSuperVO> overDaySubList = overDayList.subList(overDayList.size() - 30, overDayList.size());
			dayLength = overDaySubList.size();

			boolean findUnderZeroGordon = false;
			boolean findAboveZeroDead = false;
			boolean macdDead = false;
			double minMacd = 100.0;
			double firstDif = 0.0;
			for (int i = 0; i < overDaySubList.size(); i++) {
				StockSuperVO vo = overDaySubList.get(i);
				if ((vo.macdCorssType == CrossType.GORDON) && (vo.macdVO.dif < -0.10)) {
					// 闆朵笅MACD鍙戠敓鍦ㄥ墠鍗婃椂闂�
					if (i <= (dayLength / 1.50)) {
						findUnderZeroGordon = true;
						firstDif = vo.macdVO.dif;
						i += 5;
						continue;
					}
				}

				if (findUnderZeroGordon) {
					// 璁板綍鏈�皯macd鐨勫�锛屽鏋滈噾鍙夋病鏈夊彂鐢燂紝浣嗘槸鏈�皯macd鍊兼帴杩�鐨勮瘽锛屼篃绠楁槸macd閲戝弶
					if (minMacd > vo.macdVO.macd) {
						minMacd = vo.macdVO.macd;
					}

					// 鍒ゆ柇鏄惁姝诲弶鎴栬�灏嗚繎姝诲弶
					if ((vo.macdCorssType == CrossType.DEAD)) {
						macdDead = true;
					}

					// 闆朵笂鍜岄浂涓嬬殑macd閲戝弶锛宒if鍊间笉涓�牱锛屼竴浣庝竴楂�
					// 闆朵笂macd涔熷彲浠ユ槸闆堕檮杩�
					if (macdDead && (firstDif < vo.macdVO.dif) && (vo.macdVO.dif > -0.10)) {
						findAboveZeroDead = true;
					}
				}

				// 褰撴壘鍒伴浂涓媘acd閲戝弶鍜岄浂闄勮繎鐨勬鍙夛紝濡傛灉kdj寰堜綆锛岀瓑寰卥dj閲戝弶鎴栬�rsv閲戝弶
				// 涓�笅绫讳技澶氬ご鍥炶皟锛屽墠涓�ぉ浣庝簬ma5鍜宮a10锛屽綋澶╅珮浜巑a5鍜宮a10
				if (findAboveZeroDead) {
					if ((curSuperDayVO.avgMA5 >= curSuperDayVO.avgMA20)
							&& (curSuperDayVO.avgMA10 >= curSuperDayVO.avgMA20)) {
						if ((curSuperDayVO.kdjVO.j <= 10.0) || (pre1SuperDayVO.kdjVO.j <= 10.0)
								|| (pre2SuperDayVO.kdjVO.j <= 10.0) || (pre3SuperDayVO.kdjVO.j <= 10.0)) {
							if ((curSuperDayVO.rsvCorssType == CrossType.GORDON)
									|| (curSuperDayVO.kdjCorssType == CrossType.GORDON)
									|| (curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON)) {
								if ((curSuperDayVO.priceVO.close > curSuperDayVO.avgMA5)
										&& (curSuperDayVO.priceVO.close > curSuperDayVO.avgMA10)
										&& (pre1SuperDayVO.priceVO.close < curSuperDayVO.avgMA5)
										&& (pre1SuperDayVO.priceVO.close < curSuperDayVO.avgMA10)) {
									if (curSuperDayVO.volumeIncreasePercent >= 1.0) {
										if ((curSuperDayVO.priceVO.isKLineRed() || (curSuperDayVO.priceVO.close > pre1SuperDayVO.priceVO.close))
												&& (pre1SuperDayVO.priceVO.isKLineRed() || (pre1SuperDayVO.priceVO.close > pre2SuperDayVO.priceVO.close))
												&& (pre2SuperDayVO.priceVO.isKLineGreen() || (pre2SuperDayVO.priceVO.close < pre3SuperDayVO.priceVO.close))) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
			break;
		}
		case Phase3_Previous_Under_Zero_MACD_Gordon_Now_MACD_Gordon_Volume_Bigger: {

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			if (overDayList.size() < 40) {
				return false;
			}

			// limit two macd gordon and dead point to about 30 working days
			List<StockSuperVO> overDaySubList = overDayList.subList(overDayList.size() - 30, overDayList.size());
			dayLength = overDaySubList.size();

			boolean findUnderZeroGordon = false;
			boolean macdGordon = false;
			long maxVolume = 0;
			double minMacd = 100.0;
			double firstDif = 0.0;
			for (int i = 0; i < overDaySubList.size(); i++) {
				StockSuperVO vo = overDaySubList.get(i);
				if ((vo.macdCorssType == CrossType.GORDON) && (vo.macdVO.dif < -0.10)) {
					// 闆朵笅MACD鍙戠敓鍦ㄥ墠鍗婃椂闂�
					if (i <= (dayLength / 1.50)) {
						findUnderZeroGordon = true;
						firstDif = vo.macdVO.dif;
						// continue is not enough, it must pass some day
						i += 5;
						continue;
					}
				}

				if (findUnderZeroGordon) {
					// 璁板綍鏈�皯macd鐨勫�锛屽鏋滈噾鍙夋病鏈夊彂鐢燂紝浣嗘槸鏈�皯macd鍊兼帴杩�鐨勮瘽锛屼篃绠楁槸macd閲戝弶
					if (minMacd > vo.macdVO.macd) {
						minMacd = vo.macdVO.macd;
					}

					// 鍒ゆ柇鏄惁閲戝弶鎴栬�绫讳技閲戝弶
					if ((vo.macdCorssType == CrossType.GORDON)) {
						macdGordon = true;
					} else if ((minMacd >= 0.0) && (minMacd <= 0.05)) {
						// macdGordon = true;
						// TBD: 绠楁硶闇�杩涗竴姝ョ‘瀹�
						// 渚嬪瓙锛�02265瑗夸华鑲′唤20150211锛屽苟娌℃湁闆朵笅macd锛屼絾鏄潪甯告帴杩戯紝涔熸槸閲戝弶
					}

					// 闆朵笂鍜岄浂涓嬬殑macd閲戝弶锛宒if鍊间笉涓�牱锛屼竴浣庝竴楂�
					// 闆朵笂macd涔熷彲浠ユ槸闆堕檮杩�
					if (macdGordon && (firstDif < vo.macdVO.dif) && (vo.macdVO.dif > -0.10) && vo.priceVO.isKLineRed()) {
						// 涓婁竴涓氦鏄撴棩鏄浂涓奙ACD:
						// length-2涓婁竴鏃ユ槸macd锛�3灏辨槸鍓嶄袱澶┿�-4灏辨槸鍓�澶╂槸macd
						if (i == (dayLength - 2)) {
							StockSuperVO nextSuperVO = overDaySubList.get(i + 1);
							// 鎴愪氦閲忎袱澶╂毚澧�鏈�繎涓ゅぉ鏄袱涓ぇ闃�
							if ((vo.priceVO.volume > (maxVolume * 0.8))
									&& (nextSuperVO.priceVO.volume > (maxVolume * 0.8))
									&& (vo.volumeIncreasePercent > 1.75) && (nextSuperVO.volumeIncreasePercent > 0.9)) {
								return true;
							}
						}
					}

					// 璁板綍闆朵笅MACD涔嬪悗鏈�ぇ鎴愪氦閲�杩欎竴鍙ヤ竴瀹氭斁鍦ㄨ繖閲岋紝涓嶈兘涓婄Щ
					if (vo.priceVO.volume > maxVolume) {
						maxVolume = vo.priceVO.volume;
					}
				}
			}
			break;
		}
		case DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA20_Support:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// duo tou, ma5 <= ma10, ma10 >= ma20 >= ma30
			// low <= ma20, close >=ma20, KDJ J is zero
			// pre 2 days green, today red (or close higher than pre1)
			// example: 300226 2015-02-26
			// this is not a buy point, waiting next day if RSV/KDJ is gordon,
			// then
			// buy it
			if ((pre1SuperDayVO.avgMA5 <= pre1SuperDayVO.avgMA10) && (pre1SuperDayVO.avgMA10 >= pre1SuperDayVO.avgMA20)
					&& (pre1SuperDayVO.avgMA20 >= pre1SuperDayVO.avgMA30)) {
				if ((pre1SuperDayVO.priceVO.low <= pre1SuperDayVO.avgMA20)
						&& (pre1SuperDayVO.priceVO.close > pre1SuperDayVO.avgMA20)) {
					if (pre2SuperDayVO.priceVO.isKLineGreen() && pre3SuperDayVO.priceVO.isKLineGreen()) {
						if ((pre1SuperDayVO.priceVO.close > pre2SuperDayVO.priceVO.close)
								|| pre1SuperDayVO.priceVO.isKLineRed()) {
							if ((pre1SuperDayVO.kdjVO.j <= 10.0) && (curSuperDayVO.rsvCorssType == CrossType.GORDON)) {
								return true;
							}
						}
					}
				}
			}
			break;
		case DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA20_Support_MA_RongHe_XiangShang:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// duo tou, ma5 <= ma10, ma10 >= ma20 >= ma30
			// low <= ma20, close >=ma20, KDJ J is zero
			// pre 2 days green, today red (or close higher than pre1)
			// example: 300226 2015-02-26
			// this is not a buy point, waiting next day if RSV/KDJ is gordon,
			// then
			// buy it
			if ((pre1SuperDayVO.avgMA5 <= pre1SuperDayVO.avgMA10) && (pre1SuperDayVO.avgMA10 >= pre1SuperDayVO.avgMA20)
					&& (pre1SuperDayVO.avgMA20 >= pre1SuperDayVO.avgMA30)) {
				if ((pre1SuperDayVO.priceVO.low <= pre1SuperDayVO.avgMA20)
						&& (pre1SuperDayVO.priceVO.close > pre1SuperDayVO.avgMA20)) {
					if (pre2SuperDayVO.priceVO.isKLineGreen() && pre3SuperDayVO.priceVO.isKLineGreen()) {
						if ((pre1SuperDayVO.priceVO.close > pre2SuperDayVO.priceVO.close)
								|| pre1SuperDayVO.priceVO.isKLineRed()) {
							if ((pre1SuperDayVO.kdjVO.j <= 10.0) && (curSuperDayVO.rsvCorssType == CrossType.GORDON)) {
								return MA5_MA10_MA20_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
							}
						}
					}
				}
			}
			break;
		case DuoTou_Pre_2_Days_Green_Red_MA20_MA30_Support:
			// Duo Tou, MA5>MA10>MA20>MA30, Low <=MA30, MA10 > Close> MA20
			// Boll: Low <= MB Close > MB
			// example: 002115 20150210
			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			if ((curSuperDayVO.avgMA5 > curSuperDayVO.avgMA10) && (curSuperDayVO.avgMA10 > curSuperDayVO.avgMA20)
					&& (curSuperDayVO.avgMA20 > curSuperDayVO.avgMA30)) {
				if (pre1SuperDayVO.priceVO.isKLineGreen() && pre2SuperDayVO.priceVO.isKLineGreen()) {
					if (curSuperDayVO.priceVO.close > pre1SuperDayVO.priceVO.close) {
						if ((curSuperDayVO.priceVO.low <= curSuperDayVO.avgMA30)
								&& (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA20)
								&& (curSuperDayVO.priceVO.low <= curSuperDayVO.bollVO.mb)
								&& (curSuperDayVO.priceVO.close >= curSuperDayVO.bollVO.mb)) {
							if (curSuperDayVO.kdjVO.kValueBetween(40, 60) && curSuperDayVO.kdjVO.dValueBetween(40, 60)) {
								if ((curSuperDayVO.macdVO.macd >= 0.0) && (curSuperDayVO.macdVO.dif >= 0.0)) {
									return true;
								}
							}
						}
					}
				}
			}
			break;
		case DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// duo tou, ma5 <= ma10, ma10 >= ma20 >= ma30
			// low <= ma30, close >=ma30, KDJ J is zero
			// pre 2 days green, today red (or close higher than pre1)
			// example: 002657 2015-02-26
			// this is not a buy point, waiting next day if RSV/KDJ is gordon,
			// then
			// buy it
			if ((pre1SuperDayVO.avgMA5 <= pre1SuperDayVO.avgMA10) && (pre1SuperDayVO.avgMA10 >= pre1SuperDayVO.avgMA20)
					&& (pre1SuperDayVO.avgMA20 >= pre1SuperDayVO.avgMA30)) {
				if ((pre1SuperDayVO.priceVO.low <= pre1SuperDayVO.avgMA30)
						&& (pre1SuperDayVO.priceVO.close > pre1SuperDayVO.avgMA30)) {
					if (pre2SuperDayVO.priceVO.isKLineGreen() && pre3SuperDayVO.priceVO.isKLineGreen()) {
						if ((pre1SuperDayVO.priceVO.close > pre2SuperDayVO.priceVO.close)
								|| pre1SuperDayVO.priceVO.isKLineRed()) {
							if ((pre1SuperDayVO.kdjVO.j <= 10.0) && (curSuperDayVO.rsvCorssType == CrossType.GORDON)) {
								return true;
							}
						}
					}
				}
			}
			break;

		case DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support_MA_RongHe_XiangShang:
			// same as DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support
			// with MA5_MA10_MA20_MA30_Ronghe_XiangShang
			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// duo tou, ma5 <= ma10, ma10 >= ma20 >= ma30
			// low <= ma30, close >=ma30, KDJ J is zero
			// pre 2 days green, today red (or close higher than pre1)
			// example: 002657 2015-02-26
			// this is not a buy point, waiting next day if RSV/KDJ is gordon,
			// then
			// buy it
			if ((pre1SuperDayVO.avgMA5 <= pre1SuperDayVO.avgMA10) && (pre1SuperDayVO.avgMA10 >= pre1SuperDayVO.avgMA20)
					&& (pre1SuperDayVO.avgMA20 >= pre1SuperDayVO.avgMA30)) {
				if ((pre1SuperDayVO.priceVO.low <= pre1SuperDayVO.avgMA30)
						&& (pre1SuperDayVO.priceVO.close > pre1SuperDayVO.avgMA30)) {
					if (pre2SuperDayVO.priceVO.isKLineGreen() && pre3SuperDayVO.priceVO.isKLineGreen()) {
						if ((pre1SuperDayVO.priceVO.close > pre2SuperDayVO.priceVO.close)
								|| pre1SuperDayVO.priceVO.isKLineRed()) {
							if ((pre1SuperDayVO.kdjVO.j <= 10.0) && (curSuperDayVO.rsvCorssType == CrossType.GORDON)) {
								// check MA rongHe and xiangShang
								return this.MA5_MA10_MA20_MA30_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
							}
						}
					}
				}
			}
			break;
		case DuoTou_HuiTiao_Boll_Lower_Support_MA30_Support_MA_RongHe_XiangShang: {
			// DuoTou huitiao, KDJ J zero, boll lower support, ma30 support,
			// MA5,10,20,30 ronghe, MB support
			// macd<0, dif > 0 ,near gordon, xichou > 4
			// example: 600436 20150310. 300226 20150313

			// limit two macd gordon and dead point to about 30 working days
			List<StockSuperVO> overDaySubList = overDayList.subList(overDayList.size() - 30, overDayList.size());

			boolean findDuoTouHuiTiaoMacdDeadPoint = false;
			int macdDeadPointIndex = 0;
			// first find macd dead point, dif >0
			for (int i = 0; i < overDaySubList.size(); i++) {
				StockSuperVO vo = overDaySubList.get(i);
				if ((vo.macdCorssType == CrossType.DEAD) && (vo.macdVO.dif > 2.0)) {
					macdDeadPointIndex = i;
					if ((i - 1) >= 0) {
						StockSuperVO pre1vo = overDaySubList.get(i - 1);
						if ((pre1vo.avgMA5 >= pre1vo.avgMA10) && (pre1vo.avgMA10 >= pre1vo.avgMA20)
								&& (pre1vo.avgMA20 >= pre1vo.avgMA30)) {
							findDuoTouHuiTiaoMacdDeadPoint = true;
							break;
						}
					}

					if ((i - 2) >= 0) {
						StockSuperVO pre2vo = overDaySubList.get(i - 2);
						if ((pre2vo.avgMA5 >= pre2vo.avgMA10) && (pre2vo.avgMA10 >= pre2vo.avgMA20)
								&& (pre2vo.avgMA20 >= pre2vo.avgMA30)) {
							findDuoTouHuiTiaoMacdDeadPoint = true;
							break;
						}
					}
				}
			}

			if (!findDuoTouHuiTiaoMacdDeadPoint) {
				return false;
			}

			// find MA30 support and BOll lower support
			boolean findMA30Support = false;
			boolean findBollLowerSupport = false;
			for (int i = macdDeadPointIndex; i < overDaySubList.size(); i++) {
				StockSuperVO vo = overDaySubList.get(i);
				if ((vo.priceVO.low <= vo.avgMA30) && (vo.priceVO.close > vo.avgMA30)) {
					findMA30Support = true;
				}

				if ((vo.priceVO.low <= vo.bollVO.dn) && (vo.priceVO.close > vo.bollVO.dn)) {
					findBollLowerSupport = true;
				}
			}

			if (!findMA30Support || !findBollLowerSupport) {
				return false;
			}

			// check close is higher Boll MB and MA5,10,20,30 Ronghe xiangShang
			if ((curSuperDayVO.macdVO.macd <= 0.0) && (curSuperDayVO.macdVO.dif > 0.0)) {
				if ((curSuperDayVO.priceVO.close > curSuperDayVO.bollVO.mb) && curSuperDayVO.priceVO.isKLineRed()) {
					// check MA rongHe and xiangShang
					return this.MA5_MA10_MA20_MA30_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
				}
			}
			break;
		}
		case DuoTou_HuiTiao_Boll_MB_Support_MA20_Support_MA_RongHe_XiangShang: {
			// DuoTou huitiao, boll mb support, ma30 support,
			// MA5,10,20,30 ronghe, MB support
			// macd<0, dif > 0 ,near gordon, xichou > 4
			// example: ??

			// limit two macd gordon and dead point to about 30 working days
			List<StockSuperVO> overDaySubList = overDayList.subList(overDayList.size() - 30, overDayList.size());

			boolean findDuoTouHuiTiaoMacdDeadPoint = false;
			int macdDeadPointIndex = 0;
			// first find macd dead point, dif >0
			for (int i = 0; i < overDaySubList.size(); i++) {
				StockSuperVO vo = overDaySubList.get(i);
				if ((vo.macdCorssType == CrossType.DEAD) && (vo.macdVO.dif > 1.0)) {
					macdDeadPointIndex = i;
					if ((i - 1) >= 0) {
						StockSuperVO pre1vo = overDaySubList.get(i - 1);
						if ((pre1vo.avgMA5 >= pre1vo.avgMA10) && (pre1vo.avgMA10 >= pre1vo.avgMA20)
								&& (pre1vo.avgMA20 >= pre1vo.avgMA30)) {
							findDuoTouHuiTiaoMacdDeadPoint = true;
							break;
						}
					}

					if ((i - 2) >= 0) {
						StockSuperVO pre2vo = overDaySubList.get(i - 2);
						if ((pre2vo.avgMA5 >= pre2vo.avgMA10) && (pre2vo.avgMA10 >= pre2vo.avgMA20)
								&& (pre2vo.avgMA20 >= pre2vo.avgMA30)) {
							findDuoTouHuiTiaoMacdDeadPoint = true;
							break;
						}
					}
				}
			}

			if (!findDuoTouHuiTiaoMacdDeadPoint) {
				return false;
			}

			// find MA20 support and BOll lower support
			boolean findMA20Support = false;
			boolean findBollMBSupport = false;
			for (int i = macdDeadPointIndex; i < overDaySubList.size(); i++) {
				StockSuperVO vo = overDaySubList.get(i);
				if ((vo.priceVO.low <= vo.avgMA20) && (vo.priceVO.close > vo.avgMA20)) {
					findMA20Support = true;
				}

				if ((vo.priceVO.low <= vo.bollVO.mb) && (vo.priceVO.close > vo.bollVO.mb)) {
					findBollMBSupport = true;
				}
			}

			if (!findMA20Support || !findBollMBSupport) {
				return false;
			}

			// check close is higher Boll MB and MA5,10,20,30 Ronghe xiangShang
			if ((curSuperDayVO.macdVO.macd <= 0.0) && (curSuperDayVO.macdVO.dif > 0.0)) {
				if ((curSuperDayVO.priceVO.close > curSuperDayVO.bollVO.mb) && curSuperDayVO.priceVO.isKLineRed()) {
					// check MA rongHe and xiangShang
					return this.MA5_MA10_MA20_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
				}
			}
			break;
		}
		case DuoTou_HuiTiao_RSV_Gordon_MA10_Support_MA_RongHe_XiangShang: {
			// example: 002194 20150311, 600796 20150313
			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}

			if ((curSuperWeekVO.macdVO.macd < 0) || !this.isLatestMACDCrossGordon(overWeekList)) {
				// over all week MACD must after Gordon
				return false;
			}
			// limit point to about 30 working days
			List<StockSuperVO> overDaySubList = overDayList.subList(overDayList.size() - 30, overDayList.size());

			// find KDJ gordon point and one big red k line
			boolean findDuoTouKDJGordonStartPoint = false;
			int kdjGordonPointIndex = 0;
			for (int i = 0; i < overDaySubList.size(); i++) {
				StockSuperVO vo = overDaySubList.get(i);
				if (vo.priceVO.isKLineRed(4.0)) {
					if (((vo.kdjCorssType == CrossType.GORDON) && vo.kdjVO.jValueBetween(20, 80))
							|| (vo.macdCorssType == CrossType.GORDON)) {
						findDuoTouKDJGordonStartPoint = true;
						kdjGordonPointIndex = i;
						break;
					}
				}
			}

			if (!findDuoTouKDJGordonStartPoint) {
				return false;
			}

			boolean findDuoTouHuiTiaoKDJDeadPoint = false;
			int kdjDeadPointIndex = 0;
			// first find kdj dead point, dif >0
			for (int i = kdjGordonPointIndex; i < overDaySubList.size(); i++) {
				StockSuperVO vo = overDaySubList.get(i);
				if ((vo.avgMA5 > vo.avgMA10) && (vo.avgMA10 > vo.avgMA20) && (vo.avgMA20 > vo.avgMA30)) {
					if ((vo.kdjCorssType == CrossType.DEAD) && vo.kdjVO.jValueBetween(60, 90)
							&& (vo.macdVO.macd >= 0.0) && (vo.macdVO.dif >= 0.0)) {
						if ((i - 1) >= 0) {
							StockSuperVO pre1vo = overDaySubList.get(i - 1);
							if ((pre1vo.avgMA5 > pre1vo.avgMA10) && (pre1vo.avgMA10 > pre1vo.avgMA20)
									&& (pre1vo.avgMA20 > pre1vo.avgMA30)) {
								findDuoTouHuiTiaoKDJDeadPoint = true;
								kdjDeadPointIndex = i;
								break;
							}
						}
					}
				}
			}

			if (!findDuoTouHuiTiaoKDJDeadPoint) {
				return false;
			}

			if ((kdjDeadPointIndex - kdjGordonPointIndex) < 7) {
				// duo tou not enough, at least 7 days!
				return false;
			}

			if ((overDaySubList.size() - 1 - kdjDeadPointIndex) < 5) {
				// huitiao not enough, at least 5 days!
				return false;
			}

			// find MA10 support
			boolean findMA10Support = false;
			boolean isMA10AlwaysUp = true;
			for (int i = kdjDeadPointIndex; i < overDaySubList.size(); i++) {
				StockSuperVO vo = overDaySubList.get(i);
				StockSuperVO pre1vo = overDaySubList.get(i - 1);
				if ((vo.priceVO.low < vo.avgMA10) && (vo.priceVO.close > vo.avgMA10)) {
					findMA10Support = true;
				}

				if (vo.avgMA10 < pre1vo.avgMA10) {
					isMA10AlwaysUp = false;
					break;
				}
			}

			if (!findMA10Support || !isMA10AlwaysUp) {
				return false;
			}
			// current is RSV gordon Ronghe xiangShang
			if ((curSuperDayVO.rsvCorssType == CrossType.GORDON)) {
				if (curSuperDayVO.volumeIncreasePercent >= 1.0 && pre1SuperDayVO.volumeIncreasePercent >= 1.0) {
					if ((curSuperDayVO.priceVO.isKLineRed() && curSuperDayVO.priceVO.close > curSuperDayVO.priceVO.lastClose)
							&& pre1SuperDayVO.priceVO.close > pre1SuperDayVO.priceVO.lastClose) {
						return this.MA5_MA10_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
					}
				}
			}

			break;
		}

		case DuoTou_MA5_Wait_MA10_RSV_KDJ_Gordon_Break_Platform:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// is much like XX_Gordon_High_MA5_MA10_BOLL
			// example: 000062 2015-02-27

			// find the first big red K line that index is at the first half
			// days
			boolean hasFlatformStartVO = false;
			int minPlatformLen = 5;
			int maxPlatformLen = 30;
			for (int length = minPlatformLen; length <= maxPlatformLen; length++) {
				if (findPlatformStartVO(overDayList.subList(overDayList.size() - length, overDayList.size()))) {
					hasFlatformStartVO = true;
					break;
				}
			}

			if (!hasFlatformStartVO) {
				return false;
			}

			if ((curSuperDayVO.kdjCorssType == CrossType.GORDON)
					|| (curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON)
					|| (curSuperDayVO.rsvCorssType == CrossType.GORDON)) {
				if ((curSuperDayVO.avgMA5 > curSuperDayVO.avgMA10) && (curSuperDayVO.avgMA10 > curSuperDayVO.avgMA20)
						&& (curSuperDayVO.avgMA20 > curSuperDayVO.avgMA30)) {
					if ((curSuperDayVO.volumeIncreasePercent >= 1.0)) {
						if ((curSuperDayVO.priceVO.close > pre1SuperDayVO.priceVO.close)
								&& (pre1SuperDayVO.priceVO.close > pre2SuperDayVO.priceVO.close)
								&& (pre3SuperDayVO.priceVO.close > pre2SuperDayVO.priceVO.close)) {
							if (pre1SuperDayVO.priceVO.isKLineRed() && pre2SuperDayVO.priceVO.isKLineGreen()
									&& pre3SuperDayVO.priceVO.isKLineGreen()) {
								if ((curSuperDayVO.avgMA5 > curSuperDayVO.avgMA10)
										&& (pre1SuperDayVO.avgMA5 >= pre1SuperDayVO.avgMA10)// ??
										&& (pre2SuperDayVO.avgMA5 >= pre2SuperDayVO.avgMA10)// ??
										&& (pre3SuperDayVO.avgMA5 >= pre3SuperDayVO.avgMA10)) {
									if ((curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5)
											&& (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10)
											&& (pre1SuperDayVO.priceVO.close <= pre1SuperDayVO.avgMA5)
											&& (pre1SuperDayVO.priceVO.close <= pre1SuperDayVO.avgMA10)) {
										if ((curSuperDayVO.bollVO.up > curSuperDayVO.priceVO.close)
												&& (curSuperDayVO.priceVO.close > curSuperDayVO.bollVO.mb)) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
			break;
		case DuoTou_MA5_Wait_MA10_RongHe:
			// example: 002194 2015-03-11
			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}

			double[] dif = new double[6];
			dif[0] = pre5SuperDayVO.avgMA5 - pre5SuperDayVO.avgMA10;
			dif[1] = pre4SuperDayVO.avgMA5 - pre4SuperDayVO.avgMA10;
			dif[2] = pre3SuperDayVO.avgMA5 - pre3SuperDayVO.avgMA10;
			dif[3] = pre2SuperDayVO.avgMA5 - pre2SuperDayVO.avgMA10;
			dif[4] = pre1SuperDayVO.avgMA5 - pre1SuperDayVO.avgMA10;
			dif[5] = curSuperDayVO.avgMA5 - curSuperDayVO.avgMA10;

			if ((dif[0] > dif[1]) && (dif[1] > dif[2]) && (dif[2] > dif[3]) && (dif[3] > dif[4]) && (dif[4] > dif[5])) {
				if (((Math.abs(dif[5]) / curSuperDayVO.avgMA5) * 100) <= 0.1) {
					if ((curSuperDayVO.avgMA5 <= curSuperDayVO.avgMA10)
							&& (curSuperDayVO.avgMA10 > curSuperDayVO.avgMA20)
							&& (curSuperDayVO.avgMA20 > curSuperDayVO.avgMA30)) {
						if ((pre1SuperDayVO.avgMA5 > pre1SuperDayVO.avgMA10)
								&& (pre1SuperDayVO.avgMA10 > pre1SuperDayVO.avgMA20)
								&& (pre1SuperDayVO.avgMA20 > pre1SuperDayVO.avgMA30)) {
							if ((pre2SuperDayVO.avgMA5 > pre2SuperDayVO.avgMA10)
									&& (pre2SuperDayVO.avgMA10 > pre2SuperDayVO.avgMA20)
									&& (pre2SuperDayVO.avgMA20 > pre2SuperDayVO.avgMA30)) {
								if ((pre3SuperDayVO.avgMA5 > pre3SuperDayVO.avgMA10)
										&& (pre3SuperDayVO.avgMA10 > pre3SuperDayVO.avgMA20)
										&& (pre3SuperDayVO.avgMA20 > pre3SuperDayVO.avgMA30)) {
									if ((pre4SuperDayVO.avgMA5 > pre4SuperDayVO.avgMA10)
											&& (pre4SuperDayVO.avgMA10 > pre4SuperDayVO.avgMA20)
											&& (pre4SuperDayVO.avgMA20 > pre4SuperDayVO.avgMA30)) {
										if ((pre5SuperDayVO.avgMA5 > pre5SuperDayVO.avgMA10)
												&& (pre5SuperDayVO.avgMA10 > pre5SuperDayVO.avgMA20)
												&& (pre5SuperDayVO.avgMA20 > pre5SuperDayVO.avgMA30)) {
											if ((pre1SuperDayVO.avgMA10 > pre2SuperDayVO.avgMA10)
													&& (pre2SuperDayVO.avgMA10 > pre3SuperDayVO.avgMA10)
													&& (pre3SuperDayVO.avgMA10 > pre4SuperDayVO.avgMA10)
													&& (pre4SuperDayVO.avgMA10 > pre5SuperDayVO.avgMA10)) {
												if ((curSuperDayVO.priceVO.isKLineRed() && (curSuperDayVO.priceVO.close > pre1SuperDayVO.priceVO.close))
														&& (pre1SuperDayVO.priceVO.isKLineRed() && (pre1SuperDayVO.priceVO.close > pre2SuperDayVO.priceVO.close))) {
													return true;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			break;
		case Huge_Volume_Increase_3X3_Price_Higher_All_MA120:

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// today close higher than ma5,ma10,ma20,ma30,ma60,ma120
			// today volume is 3 times of pre1 day
			// pre1 day volume is 3 times of pre2 day avgVol5
			if ((curSuperDayVO.volumeIncreasePercent >= 3.0) && (pre1SuperDayVO.volumeIncreasePercent >= 3.0)) {
				if ((pre1SuperDayVO.priceVO.volume / pre2SuperDayVO.avgVol5) >= 3.0) {
					if ((curSuperDayVO.priceVO.close > curSuperDayVO.avgMA5)
							&& (curSuperDayVO.priceVO.close > curSuperDayVO.avgMA10)
							&& (curSuperDayVO.priceVO.close > curSuperDayVO.avgMA20)
							&& (curSuperDayVO.priceVO.close > curSuperDayVO.avgMA30)
							&& (curSuperDayVO.priceVO.close > curSuperDayVO.avgMA60)
							&& (curSuperDayVO.priceVO.close > curSuperDayVO.avgMA120)) {
						return true;
					}
				}
			}
			break;
		case MACD_Gordon_Volume_And_Price_Highest_In_MA90: {

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// volume and price is the highest in half year
			if (overDayList.size() < 120) {
				return false;
			}

			if (curSuperDayVO.macdCorssType != CrossType.GORDON) {
				return false;
			}

			// limit two macd gordon and dead point to about 30 working days
			List<StockSuperVO> overDaySubList = overDayList.subList(overDayList.size() - 90, overDayList.size());

			double price = curSuperDayVO.priceVO.close;
			long volume = curSuperDayVO.priceVO.volume;
			for (int index = 0; index < (overDaySubList.size() - 1); index++) {
				StockSuperVO svo = overDaySubList.get(index);
				if (svo.priceVO.close > price) {
					return false;
				}
				if (svo.priceVO.volume > volume) {
					return false;
				}
			}

			if (curSuperDayVO.priceVO.isKLineRed() || (curSuperDayVO.priceVO.close > pre1SuperDayVO.priceVO.close)) {
				return true;
			}
			break;
		}
		case KDJ_Gordon_Volume_And_Price_Highest_In_MA90: {

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// volume and price is the highest in half year
			if (overDayList.size() < 120) {
				return false;
			}

			if (curSuperDayVO.kdjCorssType != CrossType.GORDON) {
				return false;
			}

			// limit two macd gordon and dead point to about 30 working days
			List<StockSuperVO> overDaySubList = overDayList.subList(overDayList.size() - 90, overDayList.size());

			double price = curSuperDayVO.priceVO.close;
			long volume = curSuperDayVO.priceVO.volume;
			for (int index = 0; index < (overDaySubList.size() - 1); index++) {
				StockSuperVO svo = overDaySubList.get(index);
				if (svo.priceVO.close > price) {
					return false;
				}
				if (svo.priceVO.volume > volume) {
					return false;
				}
			}

			if (curSuperDayVO.priceVO.isKLineRed() || (curSuperDayVO.priceVO.close > pre1SuperDayVO.priceVO.close)) {
				return true;
			}
			break;
		}
		case Huge_Volume_Increase_Price_Higher_MA120_Previous_Lower_MA120: {

			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			// volume and price is the highest in half year
			if (overDayList.size() < 120) {
				return false;
			}

			// limit two macd gordon and dead point to about 30 working days
			List<StockSuperVO> overDaySubList = overDayList.subList(overDayList.size() - 90, overDayList.size());

			// the current volume is the biggest one in 90 days
			double price = curSuperDayVO.priceVO.close;
			long volume = curSuperDayVO.priceVO.volume;
			for (int index = 0; index < (overDaySubList.size() - 1); index++) {
				StockSuperVO svo = overDaySubList.get(index);
				if (svo.priceVO.close > price) {
					return false;
				}
				if (svo.priceVO.volume > volume) {
					return false;
				}
			}

			// in two days, close is low then ma120 and higer than ma120
			if ((curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA120)
					&& (pre1SuperDayVO.priceVO.close <= pre1SuperDayVO.avgMA120)) {
				if (curSuperDayVO.priceVO.isKLineRed() || (curSuperDayVO.priceVO.close > pre1SuperDayVO.priceVO.close)) {
					return true;
				}
			}
			break;
		}
		case Volume_Increase_Higher_MA120: {
			// volume and price is the highest in half year
			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			if (overDayList.size() < 120) {
				return false;
			}

			// limit two macd gordon and dead point to about 30 working days
			List<StockSuperVO> overDaySubList = overDayList.subList(overDayList.size() - 120, overDayList.size());

			// the current volume is the biggest one in 90 days
			double price = curSuperDayVO.priceVO.close;
			long volume = curSuperDayVO.priceVO.volume;
			for (int index = 0; index < (overDaySubList.size() - 1); index++) {
				StockSuperVO svo = overDaySubList.get(index);
				if (svo.priceVO.close > price) {
					return false;
				}
				if (svo.priceVO.volume > volume) {
					return false;
				}
			}

			if ((pre1SuperDayVO.priceVO.close / curSuperDayVO.priceVO.close) >= 0.85) {
				return true;
			}
		}
			break;
		case Previous_KDJ_MACD_Gordon_Now_Boll_Gordon_Week_RSV_Gordon_Boll_Gordon:
			// example: 600408 20150227
			// day price (open, low) is around ma5
			if (curSuperWeekVO.rsvCorssType == CrossType.GORDON) {
				if (curSuperWeekVO.kdjVO.kValueBetween(20, 40) && curSuperWeekVO.kdjVO.dValueBetween(20, 40)) {
					if ((curSuperWeekVO.priceVO.open <= curSuperWeekVO.bollVO.mb)
							&& (curSuperWeekVO.priceVO.close >= curSuperWeekVO.bollVO.mb)) {
						if (isLatestMACDCrossGordon(overDayList) && (curSuperDayVO.macdVO.dif <= 0.0)) {
							if (isLatestKDJCrossGordon(overDayList)) {
								if (curSuperDayVO.priceVO.close >= curSuperDayVO.bollVO.mb) {
									return true;
								}
							}
						}
					}
				}
			}
			break;
		case KongTou_Day_KDJ_Pre3_Days_Dead_Week_KDJ_Dead:
			// example: 601318 20150309
			if (curSuperWeekVO.kdjVO.k > curSuperWeekVO.kdjVO.d) {
				// over all week KDJ must after Dead
				return false;
			}

			if ((curSuperWeekVO.kdjVO.j <= 0.0) && (pre1SuperWeekVO.kdjVO.j <= 0.0)) {
				if ((pre1SuperDayVO.kdjVO.j <= 0.0) && (pre2SuperDayVO.kdjVO.j <= 0.0)
						&& (pre3SuperDayVO.kdjVO.j <= 0.0)) {
					if ((curSuperDayVO.macdVO.macd <= 0.0) && (curSuperDayVO.macdVO.dif <= 0.0)
							&& (curSuperDayVO.macdVO.dea <= 0.0)) {
						if (curSuperDayVO.rsvCorssType == CrossType.GORDON) {
							return true;
						}
					}
				}
			}

			break;
		case LaoYaZhui_TuPo_MA60_Day_Under_Zero_MACD_Gordon_KDJ_Gordon_Week_KDJ_Gordon: {
			// week macd < 0 && dif > 0
			if (curSuperWeekVO.macdVO.macd < 0 && curSuperWeekVO.macdVO.dif > 0) {
				// week kdj gordon or rsv gordon
				if (curSuperWeekVO.kdjCorssType == CrossType.GORDON
						|| curSuperWeekVO.kdjCorssType == CrossType.NEAR_GORDON
						|| curSuperWeekVO.rsvCorssType == CrossType.GORDON) {
					// week close price > week ma5, ma10
					if (curSuperWeekVO.priceVO.close >= curSuperWeekVO.avgMA5
							&& curSuperWeekVO.priceVO.close >= curSuperWeekVO.avgMA10) {
						// week is duo tou
						if (curSuperWeekVO.avgMA5 >= curSuperWeekVO.avgMA20
								&& curSuperWeekVO.avgMA10 >= curSuperWeekVO.avgMA20
								&& curSuperWeekVO.avgMA20 >= curSuperWeekVO.avgMA30) {
							// day macd is after gordon and macd > 0
							if (curSuperDayVO.macdVO.macd > 0) {
								// day kdj is after gordon and k > D
								if (curSuperDayVO.kdjVO.k > curSuperDayVO.kdjVO.d) {
									// day ma60 > ma5, ma10, ma20, ma30
									if (curSuperDayVO.avgMA60 >= curSuperDayVO.avgMA5
											&& curSuperDayVO.avgMA60 >= curSuperDayVO.avgMA10
											&& curSuperDayVO.avgMA60 >= curSuperDayVO.avgMA20
											&& curSuperDayVO.avgMA60 >= curSuperDayVO.avgMA30) {
										// TuPo !!! day close price > ma60
										if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA60) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}

			break;
		}
		default:
			return false;
		}
		return false;
	}

	// to check if the list is a platform
	public boolean findPlatformStartVO(List<StockSuperVO> overDayList) {
		StockSuperVO startVO = overDayList.get(0);
		double startPriceIncrease = ((startVO.priceVO.close - startVO.priceVO.lastClose) * 100.0)
				/ startVO.priceVO.lastClose;
		if (startPriceIncrease >= 7.5) {
			double avgClose = 0;
			for (int i = 1; i < overDayList.size(); i++) {
				StockSuperVO vo = overDayList.get(i);
				double priceIncrease = ((vo.priceVO.close - vo.priceVO.lastClose) * 100.0) / vo.priceVO.lastClose;

				// if next day find one priceIncrease is bigger then startVO,
				// then not the platform
				if (priceIncrease > startPriceIncrease) {
					return false;
				}

				// if next day find one high is greater then 10% since platform
				// startVO.hight, then not the platform
				if ((((vo.priceVO.high - startVO.priceVO.high) * 100) / startVO.priceVO.high) >= 10) {
					return false;
				}

				// if next day find one close is less than the platform
				// startVO.open or less then ma20
				if ((vo.priceVO.close < startVO.priceVO.open) || (vo.priceVO.close < vo.avgMA20)) {
					return false;
				}

				avgClose += vo.priceVO.close;
			}

			avgClose = avgClose / (overDayList.size() - 1);
			// next avg close is greater than the middle platform startVO.open +
			// close / 2
			if (avgClose < ((startVO.priceVO.open + startVO.priceVO.close) / 2)) {
				return false;
			}

			// after all condiction is satisfy
			return true;
		}

		return false;
	}

	private boolean isLatestKDJCrossGordon(List<StockSuperVO> overList) {
		for (int i = overList.size() - 1; i >= 0; i--) {
			StockSuperVO svo = overList.get(i);
			if (svo.kdjCorssType == CrossType.GORDON) {
				return true;
			} else if (svo.kdjCorssType == CrossType.DEAD) {
				return false;
			}
		}
		return false;
	}

	private boolean isLatestMACDCrossGordon(List<StockSuperVO> overList) {
		for (int i = overList.size() - 1; i >= 0; i--) {
			StockSuperVO svo = overList.get(i);
			if (svo.macdCorssType == CrossType.GORDON) {
				return true;
			} else if (svo.macdCorssType == CrossType.DEAD) {
				return false;
			}
		}
		return false;
	}

	private boolean MA5_MA10_MA20_MA30_Ronghe_XiangShang(StockSuperVO curSuperDayVO, StockSuperVO pre1SuperDayVO) {
		// rongHe and xiangShang
		double min = this.findMinValue(curSuperDayVO.avgMA5, curSuperDayVO.avgMA10, curSuperDayVO.avgMA20,
				curSuperDayVO.avgMA30);
		double max = this.findMaxValue(curSuperDayVO.avgMA5, curSuperDayVO.avgMA10, curSuperDayVO.avgMA20,
				curSuperDayVO.avgMA30);
		double dif = Math.abs(max - min);
		// MA rongHe
		if (((dif / curSuperDayVO.priceVO.close) * 100) < 5.0) {
			// xiangShang
			if ((curSuperDayVO.avgMA5 >= pre1SuperDayVO.avgMA5) && (curSuperDayVO.avgMA10 >= pre1SuperDayVO.avgMA10)
					&& (curSuperDayVO.avgMA20 >= pre1SuperDayVO.avgMA20)
					&& (curSuperDayVO.avgMA30 >= pre1SuperDayVO.avgMA30)) {
				return true;
			}
		}
		return false;
	}

	private boolean MA5_MA10_MA20_Ronghe_XiangShang(StockSuperVO curSuperDayVO, StockSuperVO pre1SuperDayVO) {
		// rongHe and xiangShang
		double min = this.findMinValue(curSuperDayVO.avgMA5, curSuperDayVO.avgMA10, curSuperDayVO.avgMA20);
		double max = this.findMaxValue(curSuperDayVO.avgMA5, curSuperDayVO.avgMA10, curSuperDayVO.avgMA20);
		double dif = Math.abs(max - min);
		// MA rongHe
		if (((dif / curSuperDayVO.priceVO.close) * 100) < 5.0) {
			// xiangShang
			if ((curSuperDayVO.avgMA5 >= pre1SuperDayVO.avgMA5) && (curSuperDayVO.avgMA10 >= pre1SuperDayVO.avgMA10)
					&& (curSuperDayVO.avgMA20 >= pre1SuperDayVO.avgMA20)) {
				return true;
			}
		}
		return false;
	}

	private boolean MA5_MA10_Ronghe_XiangShang(StockSuperVO curSuperDayVO, StockSuperVO pre1SuperDayVO) {
		// rongHe and xiangShang
		double dif = Math.abs(curSuperDayVO.avgMA5 - curSuperDayVO.avgMA10);
		// MA rongHe
		if (((dif / curSuperDayVO.priceVO.close) * 100) < 2.0) {
			// xiangShang
			if ((curSuperDayVO.avgMA5 >= pre1SuperDayVO.avgMA5) && (curSuperDayVO.avgMA10 >= pre1SuperDayVO.avgMA10)
					&& (curSuperDayVO.avgMA20 >= pre1SuperDayVO.avgMA20)
					&& (curSuperDayVO.avgMA30 >= pre1SuperDayVO.avgMA30)) {
				return true;
			}
		}
		return false;
	}

	private double findMinValue(double v1, double v2, double v3, double v4) {
		double min1 = Math.min(v1, v2);
		double min2 = Math.min(v3, v4);
		return Math.min(min1, min2);
	}

	private double findMaxValue(double v1, double v2, double v3, double v4) {
		double max1 = Math.max(v1, v2);
		double max2 = Math.max(v3, v4);
		return Math.max(max1, max2);
	}

	private double findMinValue(double v1, double v2, double v3) {
		double min1 = Math.min(v1, v2);
		double min2 = Math.min(min1, v3);
		return Math.min(min1, min2);
	}

	private double findMaxValue(double v1, double v2, double v3) {
		double max1 = Math.max(v1, v2);
		double max2 = Math.max(max1, v3);
		return Math.max(max1, max2);
	}
}
