package org.easystogu.analyse;

import java.util.List;

import org.easystogu.analyse.util.DigitInOrderHelper;
import org.easystogu.analyse.util.StockPriceUtils;
import org.easystogu.checkpoint.DailyCombineCheckPoint;
import org.easystogu.db.vo.table.StockSuperVO;
import org.easystogu.log.LogHelper;
import org.easystogu.utils.CrossType;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class CombineAnalyseHelper {
	private static Logger logger = LogHelper.getLogger(CombineAnalyseHelper.class);
	// overList is order by date, it is daily price and ind
	public boolean isConditionSatisfy(DailyCombineCheckPoint checkPoint, List<StockSuperVO> overDayList,
			List<StockSuperVO> overWeekList) {

		if ((overWeekList == null) || (overWeekList.size() < 1)) {
			// logger.debug("CombineAnalyseHelper overWeekList size is
			// 0");
			return false;
		}
		StockSuperVO curSuperWeekVO = overWeekList.get(overWeekList.size() - 1);

		int dayLength = overDayList.size();
		StockSuperVO curSuperDayVO = overDayList.get(overDayList.size() - 1);
		StockSuperVO pre1SuperDayVO = overDayList.get(overDayList.size() - 2);
		StockSuperVO pre2SuperDayVO = overDayList.get(overDayList.size() - 3);
		StockSuperVO pre3SuperDayVO = overDayList.get(overDayList.size() - 4);
		StockSuperVO pre4SuperDayVO = overDayList.get(overDayList.size() - 5);
		StockSuperVO pre5SuperDayVO = overDayList.get(overDayList.size() - 6);
		StockSuperVO pre6SuperDayVO = overDayList.get(overDayList.size() - 7);
		StockSuperVO pre7SuperDayVO = overDayList.get(overDayList.size() - 8);
		StockSuperVO pre8SuperDayVO = overDayList.get(overDayList.size() - 9);
		StockSuperVO pre9SuperDayVO = overDayList.get(overDayList.size() - 10);
		StockSuperVO pre10SuperDayVO = overDayList.get(overDayList.size() - 11);
		StockSuperVO pre11SuperDayVO = overDayList.get(overDayList.size() - 12);
		StockSuperVO pre12SuperDayVO = overDayList.get(overDayList.size() - 13);
		StockSuperVO pre13SuperDayVO = overDayList.get(overDayList.size() - 14);

		switch (checkPoint) {
		case MACD_Gordon:
			if (curSuperDayVO.macdCorssType == CrossType.GORDON) {
				return true;
			}
			break;
		case MACD_Dead:
			if (curSuperDayVO.macdCorssType == CrossType.DEAD) {
				return true;
			}
			break;
		case RSV_Gordon: {
			if (curSuperDayVO.rsvCorssType == CrossType.GORDON) {
				return true;
			}
			break;
		}
		case KDJ_Gordon:
			if (curSuperDayVO.kdjCorssType == CrossType.GORDON) {
				return true;
			}
			break;

		case ShenXian_Gordon:
			if (curSuperDayVO.shenXianCorssType12 == CrossType.GORDON) {
				return true;
			}
			break;
		case ShenXian_Dead:
			if (curSuperDayVO.shenXianCorssType12 == CrossType.DEAD) {
				return true;
			}
			break;
		case WEEK_MACD_Gordon:
			if (curSuperWeekVO.macdCorssType == CrossType.GORDON) {
				return true;
			}
			break;
		case WEEK_KDJ_Gordon:
			if (curSuperWeekVO.kdjCorssType == CrossType.GORDON) {
				return true;
			}
			break;

		case WEEK_ShenXian_Gordon:
			if (curSuperWeekVO.shenXianCorssType12 == CrossType.GORDON) {
				return true;
			}
			break;
		case Mai1Mai2_1_Gordon:
			if (curSuperDayVO.mai1mai2CrossTypeMai1 == CrossType.GORDON) {
				return true;
			}
			break;
		case WEEK_Mai1Mai2_1_Gordon:
			if (curSuperWeekVO.mai1mai2CrossTypeMai1 == CrossType.GORDON) {
				return true;
			}
			break;
		case Mai1Mai2_2_Gordon:
			if (curSuperDayVO.mai1mai2CrossTypeMai2 == CrossType.GORDON) {
				return true;
			}
			break;
		case WEEK_Mai1Mai2_2_Gordon:
			if (curSuperWeekVO.mai1mai2CrossTypeMai2 == CrossType.GORDON) {
				return true;
			}
			break;
		case YiMengBS_Gordon:
			if (curSuperDayVO.yiMengBSCrossType == CrossType.GORDON) {
				return true;
			}
			break;
		case WEEK_YiMengBS_Gordon:
			if (curSuperWeekVO.yiMengBSCrossType == CrossType.GORDON) {
				return true;
			}
			break;
		case SuoLiang_HuiTiao: {
			// Pre2 Pre1 Cur
			// Green Green Red
			// Vol<Vol5 Vol<Vol5 Not Care
			if (StockPriceUtils.isKLineGreen(pre2SuperDayVO.priceVO)
					&& StockPriceUtils.isKLineGreen(pre1SuperDayVO.priceVO)
					&& StockPriceUtils.isKLineRed(curSuperDayVO.priceVO)
					&& pre2SuperDayVO.priceVO.volume > pre1SuperDayVO.priceVO.volume
					&& pre1SuperDayVO.priceVO.volume <= pre1SuperDayVO.avgVol5) {
				return true;
			}

			// Pre3 Pre2 Pre1 Cur
			// Green Green Red Red
			// Vol<Vol5 Vol<Vol5 Vol Vol
			if (StockPriceUtils.isKLineGreen(pre3SuperDayVO.priceVO)
					&& StockPriceUtils.isKLineGreen(pre2SuperDayVO.priceVO)
					&& StockPriceUtils.isKLineRed(pre1SuperDayVO.priceVO)
					&& StockPriceUtils.isKLineRed(curSuperDayVO.priceVO)
					&& pre3SuperDayVO.priceVO.volume > pre2SuperDayVO.priceVO.volume
					&& pre2SuperDayVO.priceVO.volume <= pre2SuperDayVO.avgVol5
					&& pre1SuperDayVO.priceVO.volume <= curSuperDayVO.priceVO.volume) {
				return true;
			}

			break;
		}
		case PlatForm: {
			return isPlatform(overDayList, overWeekList);
		}
		case WEEK_PlatForm: {
			boolean hasWeekFlatformStartVO = false;
			int minPlatformLen = 3;
			int maxPlatformLen = 10;
			for (int length = minPlatformLen; length <= maxPlatformLen; length++) {
				if (findLongPlatformBasedOnWeekDate(
						overWeekList.subList(overWeekList.size() - length, overWeekList.size()), overDayList)) {
					hasWeekFlatformStartVO = true;
					break;
				}

				if (findLongPlatformBasedOnWeekDateOrig(
						overWeekList.subList(overWeekList.size() - length, overWeekList.size()))) {
					hasWeekFlatformStartVO = true;
					break;
				}
			}

			return hasWeekFlatformStartVO;
		}

		case RongHe_XiangShang: {
			boolean rh1 = MA5_MA10_MA20_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
			boolean rh2 = MA5_MA10_MA20_MA30_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
			return rh1 || rh2;
		}
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
					if (StockPriceUtils.isKLineRed(curSuperDayVO.priceVO)
							&& (curSuperDayVO.volumeIncreasePercent >= 1.0)) {
						if (StockPriceUtils.isKLineRed(pre1SuperDayVO.priceVO)
								&& (pre1SuperDayVO.volumeIncreasePercent >= 1.0)
								&& StockPriceUtils.isKLineRed(pre2SuperDayVO.priceVO)) {
							return MA5_MA10_MA20_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
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
					if (StockPriceUtils.isKLineRed(curSuperDayVO.priceVO)
							&& (curSuperDayVO.volumeIncreasePercent >= 1.0)) {
						if (StockPriceUtils.isKLineRed(pre1SuperDayVO.priceVO)
								&& (pre1SuperDayVO.volumeIncreasePercent >= 1.0)
								&& StockPriceUtils.isKLineRed(pre2SuperDayVO.priceVO)) {
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
										if ((StockPriceUtils.isKLineRed(curSuperDayVO.priceVO)
												|| (curSuperDayVO.priceVO.close > pre1SuperDayVO.priceVO.close))
												&& (StockPriceUtils.isKLineRed(pre1SuperDayVO.priceVO)
														|| (pre1SuperDayVO.priceVO.close > pre2SuperDayVO.priceVO.close))
												&& (StockPriceUtils.isKLineGreen(pre2SuperDayVO.priceVO)
														|| (pre2SuperDayVO.priceVO.close < pre3SuperDayVO.priceVO.close))) {
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
					if (StockPriceUtils.isKLineGreen(pre2SuperDayVO.priceVO)
							&& StockPriceUtils.isKLineGreen(pre3SuperDayVO.priceVO)) {
						if ((pre1SuperDayVO.priceVO.close > pre2SuperDayVO.priceVO.close)
								|| StockPriceUtils.isKLineRed(pre1SuperDayVO.priceVO)) {
							if ((pre1SuperDayVO.kdjVO.j <= 10.0) && (curSuperDayVO.rsvCorssType == CrossType.GORDON)) {
								return MA5_MA10_MA20_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
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
					if (StockPriceUtils.isKLineGreen(pre2SuperDayVO.priceVO)
							&& StockPriceUtils.isKLineGreen(pre3SuperDayVO.priceVO)) {
						if ((pre1SuperDayVO.priceVO.close > pre2SuperDayVO.priceVO.close)
								|| StockPriceUtils.isKLineRed(pre1SuperDayVO.priceVO)) {
							if (pre1SuperDayVO.kdjVO.j <= 10.0) {
								if (curSuperDayVO.rsvCorssType == CrossType.GORDON
										|| curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON
										|| curSuperDayVO.kdjCorssType == CrossType.GORDON) {
									return true;
								}
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
					if (StockPriceUtils.isKLineGreen(pre2SuperDayVO.priceVO)
							&& StockPriceUtils.isKLineGreen(pre3SuperDayVO.priceVO)) {
						if ((pre1SuperDayVO.priceVO.close > pre2SuperDayVO.priceVO.close)
								|| StockPriceUtils.isKLineRed(pre1SuperDayVO.priceVO)) {
							if ((pre1SuperDayVO.kdjVO.j <= 10.0) && (curSuperDayVO.rsvCorssType == CrossType.GORDON)) {
								// check MA rongHe and xiangShang
								return this.MA5_MA10_MA20_MA30_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
							}
						}
					}
				}
			}
			break;
		case DuoTou_HuiTiao_MA30_Support_MA_RongHe_XiangShang: {
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
				if ((curSuperDayVO.priceVO.close > curSuperDayVO.bollVO.mb)
						&& StockPriceUtils.isKLineRed(curSuperDayVO.priceVO)) {
					// check MA rongHe and xiangShang
					return this.MA5_MA10_MA20_MA30_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
				}
			}
			break;
		}

		case HengPang_Ready_To_Break_Platform_MA30_Support_MA_RongHe_XiangShang: {
			// combined with DuoTou_HuiTiao_MA30_Support_MA_RongHe_XiangShang
			// and HengPang_Ready_To_Break_Platform
			boolean isPlatform = this.isPlatform(overDayList, overWeekList);
			if (!isPlatform)
				return false;

			// below is completely copy from
			// DuoTou_HuiTiao_MA30_Support_MA_RongHe_XiangShang
			//
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
				if ((curSuperDayVO.priceVO.close > curSuperDayVO.bollVO.mb)
						&& StockPriceUtils.isKLineRed(curSuperDayVO.priceVO)) {
					// check MA rongHe and xiangShang
					return this.MA5_MA10_MA20_MA30_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
				}
			}

			break;
		}
		case DuoTou_HuiTiao_MA20_Support_MA_RongHe_XiangShang: {
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
				if ((curSuperDayVO.priceVO.close > curSuperDayVO.bollVO.mb)
						&& StockPriceUtils.isKLineRed(curSuperDayVO.priceVO)) {
					// check MA rongHe and xiangShang
					return this.MA5_MA10_MA20_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
				}
			}
			break;
		}

		case HengPang_Ready_To_Break_Platform_MA20_Support_MA_RongHe_XiangShang: {
			// merged by HengPang_Ready_To_Break_Platform and
			// DuoTou_HuiTiao_MA20_Support_MA_RongHe_XiangShang
			boolean isPlatform = this.isPlatform(overDayList, overWeekList);
			if (!isPlatform)
				return false;

			// below is completely copy from
			// DuoTou_HuiTiao_MA20_Support_MA_RongHe_XiangShang
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
				if ((curSuperDayVO.priceVO.close > curSuperDayVO.bollVO.mb)
						&& StockPriceUtils.isKLineRed(curSuperDayVO.priceVO)) {
					// check MA rongHe and xiangShang
					return this.MA5_MA10_MA20_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO);
				}
			}
			break;
		}

		case HengPan_3_Weeks_MA_RongHe_Break_Platform: {
			// example: 300216 @ 20150421; 002040 @ 20150421
			// week platform
			boolean hasWeekFlatformStartVO = false;
			int minPlatformLen = 3;
			int maxPlatformLen = 10;
			for (int length = minPlatformLen; length <= maxPlatformLen; length++) {
				if (findLongPlatformBasedOnWeekDate(
						overWeekList.subList(overWeekList.size() - length, overWeekList.size()), overDayList)) {
					hasWeekFlatformStartVO = true;
					break;
				}

				if (findLongPlatformBasedOnWeekDateOrig(
						overWeekList.subList(overWeekList.size() - length, overWeekList.size()))) {
					hasWeekFlatformStartVO = true;
					break;
				}
			}

			if (!hasWeekFlatformStartVO) {
				return false;
			}

			// original week checking
			// RSV or KDJ gordon
			if (curSuperDayVO.rsvCorssType == CrossType.GORDON || curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON
					|| curSuperDayVO.kdjCorssType == CrossType.GORDON) {
				if (StockPriceUtils.isKLineRed(curSuperDayVO.priceVO)) {
					// check MA5, MA10,MA20,MA30 RongHe
					boolean ma5_10_20_30_rongHe = MA5_MA10_MA20_MA30_Ronghe(pre2SuperDayVO)
							&& MA5_MA10_MA20_MA30_Ronghe(pre1SuperDayVO) && MA5_MA10_MA20_MA30_Ronghe(curSuperDayVO);
					boolean ma5_10_20_rongHe = MA5_MA10_MA20_Ronghe(pre1SuperDayVO)
							&& MA5_MA10_MA20_Ronghe(curSuperDayVO);

					if (ma5_10_20_30_rongHe || ma5_10_20_rongHe) {
						if (MA5_MA10_XiangShang(curSuperDayVO, pre1SuperDayVO)
								|| MA10_MA20_XiangShang(curSuperDayVO, pre1SuperDayVO)) {
							if (close_Higher_MA5_MA10(curSuperDayVO) || close_Higher_MA10_MA20(curSuperDayVO)
									|| close_Higher_MA20_MA30(curSuperDayVO)) {
								if (curSuperDayVO.priceVO.close > curSuperDayVO.avgMA20) {
									return true;
								}
							}
						}
					}
				}
			}

			// oritinal day checking
			// RSV or KDJ gordon
			if (curSuperDayVO.rsvCorssType == CrossType.GORDON || curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON
					|| curSuperDayVO.kdjCorssType == CrossType.GORDON) {
				// pre3 and pre2 green, pre1 and cur red
				// example: 600021 000875 at 2015-04-13, 000062 at 2015-02-27
				if (StockPriceUtils.isKLineRed(curSuperDayVO.priceVO)
						&& StockPriceUtils.isKLineRed(pre1SuperDayVO.priceVO)
						&& StockPriceUtils.isKLineGreen(pre2SuperDayVO.priceVO)
						&& StockPriceUtils.isKLineGreen(pre3SuperDayVO.priceVO)) {
					if (curSuperDayVO.volumeIncreasePercent > 1) {
						if ((pre2SuperDayVO.volumeIncreasePercent < 1)
								|| (pre2SuperDayVO.priceVO.volume < pre2SuperDayVO.avgVol5
										&& pre3SuperDayVO.priceVO.volume < pre3SuperDayVO.avgVol5)) {
							// close higher ma5 ma10
							if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5
									&& curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10) {
								// pre2, pre1, cur rongHe; cur xiangShang
								if (this.MA5_MA10_Ronghe(pre1SuperDayVO) && this.MA5_MA10_Ronghe(pre2SuperDayVO)) {
									if (this.MA5_MA10_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO)) {
										if (curSuperDayVO.priceVO.close >= pre2SuperDayVO.priceVO.close)
											return true;
									}
								}
							}
						}
					}
				}
				// pre3, pre2 and pre1 green, cur red
				// example: 002260 2015-04-17
				if (StockPriceUtils.isKLineRed(curSuperDayVO.priceVO)
						&& StockPriceUtils.isKLineGreen(pre1SuperDayVO.priceVO)
						&& StockPriceUtils.isKLineGreen(pre2SuperDayVO.priceVO)
						&& StockPriceUtils.isKLineGreen(pre3SuperDayVO.priceVO)) {
					if (curSuperDayVO.volumeIncreasePercent > 1) {
						if ((pre1SuperDayVO.volumeIncreasePercent < 1 && pre2SuperDayVO.volumeIncreasePercent < 1)
								|| (pre1SuperDayVO.priceVO.volume < pre1SuperDayVO.avgVol5
										&& pre2SuperDayVO.priceVO.volume < pre2SuperDayVO.avgVol5)) {
							// close higher ma5 ma10
							if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5
									&& curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10) {
								// pre2, pre1, cur rongHe; cur xiangShang
								if (this.MA5_MA10_Ronghe(pre1SuperDayVO) && this.MA5_MA10_Ronghe(pre2SuperDayVO)) {
									if (this.MA5_MA10_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO)) {
										if (curSuperDayVO.priceVO.close >= pre2SuperDayVO.priceVO.close)
											return true;
									}
								}
							}
						}
					}
				}
			}
			break;
		}

		case HengPan_2_Weeks_MA_RongHe_Break_Platform: {
			// example: 600021 000875 at 2015-04-13,
			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}

			// find the first big red K line that index is at the first half
			// days
			boolean hasFlatformStartVO = false;
			int minPlatformLen = 9;
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
			// oritinal day checking
			// RSV or KDJ gordon
			if (curSuperDayVO.rsvCorssType == CrossType.GORDON || curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON
					|| curSuperDayVO.kdjCorssType == CrossType.GORDON) {
				// pre3 and pre2 green, pre1 and cur red
				// example: 600021 000875 at 2015-04-13, 000062 at 2015-02-27
				if (StockPriceUtils.isKLineRed(curSuperDayVO.priceVO)
						&& StockPriceUtils.isKLineRed(pre1SuperDayVO.priceVO)
						&& StockPriceUtils.isKLineGreen(pre2SuperDayVO.priceVO)
						&& StockPriceUtils.isKLineGreen(pre3SuperDayVO.priceVO)) {
					if (curSuperDayVO.volumeIncreasePercent > 1) {
						if ((pre2SuperDayVO.volumeIncreasePercent < 1)
								|| (pre2SuperDayVO.priceVO.volume < pre2SuperDayVO.avgVol5
										&& pre3SuperDayVO.priceVO.volume < pre3SuperDayVO.avgVol5)) {
							// close higher ma5 ma10
							if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5
									&& curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10) {
								// pre2, pre1, cur rongHe; cur xiangShang
								if (this.MA5_MA10_Ronghe(pre1SuperDayVO) && this.MA5_MA10_Ronghe(pre2SuperDayVO)) {
									if (this.MA5_MA10_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO)) {
										if (curSuperDayVO.priceVO.close >= pre2SuperDayVO.priceVO.close)
											return true;
									}
								}
							}
						}
					}
				}
				// pre3, pre2 and pre1 green, cur red
				// example: 002260 2015-04-17
				if (StockPriceUtils.isKLineRed(curSuperDayVO.priceVO)
						&& StockPriceUtils.isKLineGreen(pre1SuperDayVO.priceVO)
						&& StockPriceUtils.isKLineGreen(pre2SuperDayVO.priceVO)
						&& StockPriceUtils.isKLineGreen(pre3SuperDayVO.priceVO)) {
					if (curSuperDayVO.volumeIncreasePercent > 1) {
						if ((pre1SuperDayVO.volumeIncreasePercent < 1 && pre2SuperDayVO.volumeIncreasePercent < 1)
								|| (pre1SuperDayVO.priceVO.volume < pre1SuperDayVO.avgVol5
										&& pre2SuperDayVO.priceVO.volume < pre2SuperDayVO.avgVol5)) {
							// close higher ma5 ma10
							if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5
									&& curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10) {
								// pre2, pre1, cur rongHe; cur xiangShang
								if (this.MA5_MA10_Ronghe(pre1SuperDayVO) && this.MA5_MA10_Ronghe(pre2SuperDayVO)) {
									if (this.MA5_MA10_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO)) {
										if (curSuperDayVO.priceVO.close >= pre2SuperDayVO.priceVO.close)
											return true;
									}
								}
							}
						}
					}
				}
			}
			break;
		}

		case HengPang_Ready_To_Break_Platform_KDJ_Gordon: {
			// week KDJ gordon and day near kdj gordon
			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}
			if (this.isPlatform(overDayList, overWeekList)) {

				// debugInfo(curSuperDayVO, "2016-04-14", "case 2");

				if (curSuperDayVO.rsvCorssType == CrossType.GORDON
						|| curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON
						|| curSuperDayVO.kdjCorssType == CrossType.GORDON) {
					return true;
				}
			}

			// debugInfo(curSuperDayVO, "2016-04-14", "case 3");

			return false;
		}

		case HengPang_Ready_To_Break_Platform_MACD_Gordon_Week_KDJ_Gordon: {
			// example 002673 @ 2015-06-08
			// day macd gordon, week kdj gordon
			if (curSuperDayVO.macdCorssType == CrossType.GORDON && curSuperWeekVO.kdjCorssType == CrossType.GORDON) {
				return this.isPlatform(overDayList, overWeekList);
			}
			break;
		}

		case HengPang_7_Days_Ready_To_Break_Platform: {

			// for safety, macd dif is less than 1.0
			if (curSuperDayVO.macdVO.dif > 1.0) {
				return false;
			}

			// one big red (+7%) and 6 days cuoLiang huiTiao
			// example 001696 @ 2015-06-14 ~ 06-12
			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d)) {
				// over all week KDJ must after Dead
				if (StockPriceUtils.isKLineRed(pre6SuperDayVO.priceVO, 6.0, 9.0)) {
					double high = pre6SuperDayVO.priceVO.high;
					double low = pre6SuperDayVO.priceVO.low;
					if (pre5SuperDayVO.priceVO.high / high <= 1.02 && pre4SuperDayVO.priceVO.high / high <= 1.02
							&& pre3SuperDayVO.priceVO.high / high <= 1.02 && pre2SuperDayVO.priceVO.high / high <= 1.02
							&& pre1SuperDayVO.priceVO.high / high <= 1.02
							&& curSuperDayVO.priceVO.high / high <= 1.02) {
						if (pre5SuperDayVO.priceVO.low >= low && pre4SuperDayVO.priceVO.low >= low
								&& pre3SuperDayVO.priceVO.low >= low && pre2SuperDayVO.priceVO.low >= low
								&& pre1SuperDayVO.priceVO.low >= low && curSuperDayVO.priceVO.low >= low) {
							long totalVol = pre5SuperDayVO.priceVO.volume + pre4SuperDayVO.priceVO.volume
									+ pre3SuperDayVO.priceVO.volume + pre2SuperDayVO.priceVO.volume
									+ pre1SuperDayVO.priceVO.volume + curSuperDayVO.priceVO.volume;
							long avgVol = totalVol / 6;
							if (avgVol < pre6SuperDayVO.priceVO.volume) {
								return true;
							}

						}
					}
				}
				return false;
			}
			break;
		}

		case ShenXian_Two_Gordons: {
			// after H1 corss H2 and then H1 corss H3
			if (curSuperDayVO.shenXianVO.h1 > curSuperDayVO.shenXianVO.h2) {
				if (curSuperDayVO.shenXianCorssType13 == CrossType.GORDON) {
					return true;
				}
			}
			break;
		}

		case Day_Week_Mai1Mai2_Mai2_Grodon: {
			// day and week mai2 gordon
			if (curSuperDayVO.mai1mai2CrossTypeMai2 == CrossType.GORDON
					&& curSuperWeekVO.mai1mai2CrossTypeMai2 == CrossType.GORDON) {
				return true;
			}
			break;
		}

		case Day_Week_Mai1Mai2_Mai2_Day_ShenXian_Grodon: {
			// day mai2 and shenxian gordon, week mai2 gordon
			if (curSuperDayVO.mai1mai2CrossTypeMai2 == CrossType.GORDON
					&& curSuperWeekVO.mai1mai2CrossTypeMai2 == CrossType.GORDON
					&& curSuperDayVO.shenXianCorssType12 == CrossType.GORDON) {
				return true;
			}
			break;
		}

		case Day_Mai1Mai2_Mai1_ShenXian_Grodon: {
			// day mai1 and shenxian gordon
			if (curSuperDayVO.mai1mai2CrossTypeMai1 == CrossType.GORDON
					&& curSuperDayVO.shenXianCorssType12 == CrossType.GORDON) {
				return true;
			}
			break;
		}

		case Day_Mai1Mai2_Mai2_ShenXian_Grodon: {
			if (curSuperDayVO.mai1mai2CrossTypeMai2 == CrossType.GORDON
					&& curSuperDayVO.shenXianCorssType12 == CrossType.GORDON) {
				return true;
			}
			break;
		}

		case Day_Week_Mai1Mai2_Mai1_Day_ShenXian_Grodon: {
			// day mai2 and shenxian gordon, week mai2 gordon
			if (curSuperDayVO.mai1mai2CrossTypeMai1 == CrossType.GORDON
					&& curSuperWeekVO.mai1mai2CrossTypeMai1 == CrossType.GORDON
					&& curSuperDayVO.shenXianCorssType12 == CrossType.GORDON) {
				return true;
			}
			break;
		}

		case BollXueShi2_Dn_Gordon: {
			// when xueShi2 dn cross bull dn
			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}

			if (curSuperDayVO.bullXueShi2DnCrossType == CrossType.GORDON) {
				return true;
			}

			break;
		}

		case Close_Higher_BollUp_BollXueShi2_Dn_Gordon: {
			// close price higher boll upper and
			// boll xueShie2 dn gordon corss
			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}

			// cur close over boll up
			if (curSuperDayVO.priceVO.close >= curSuperDayVO.bollVO.up) {
				// bollXueShi2 gordon or near gordon
				if (curSuperDayVO.bullXueShi2DnCrossType == CrossType.GORDON
						|| curSuperDayVO.bullXueShi2DnCrossType == CrossType.NEAR_GORDON) {
					return true;
				}
			}
			break;
		}

		case HengPang_Ready_To_Break_Platform_BollUp_BollXueShi2_Dn_Gordon: {
			// combined with HengPang_Ready_To_Break_Platform and
			// Close_Higher_BollUp_BollXueShi2_Dn_Gordon
			boolean isPlatform = this.isPlatform(overDayList, overWeekList);
			if (!isPlatform)
				return false;

			// below is completely copy from
			// Close_Higher_BollUp_BollXueShi2_Dn_Gordon
			// close price higher boll upper and
			// boll xueShie2 dn gordon corss
			if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
				// over all week KDJ must after Gordon
				return false;
			}

			// cur close over boll up
			if (curSuperDayVO.priceVO.close >= curSuperDayVO.bollVO.up) {
				// bollXueShi2 gordon or near gordon
				if (curSuperDayVO.bullXueShi2DnCrossType == CrossType.GORDON
						|| curSuperDayVO.bullXueShi2DnCrossType == CrossType.NEAR_GORDON) {
					return true;
				}
			}
			break;
		}

		case DaDie_KDJ_Gordon_Twice_DiWei_Gordon: {
			// da die; more day KDJ zero; KDJ twice gordon, di wei gordon
			// example 002673 and 600750 at 2015-06-30
			if (curSuperDayVO.kdjCorssType == CrossType.GORDON && curSuperDayVO.kdjVO.kValueBetween(15, 30)) {

				boolean pre4DayKDJGordon = (pre4SuperDayVO.kdjCorssType == CrossType.GORDON
						|| pre4SuperDayVO.kdjCorssType == CrossType.NEAR_GORDON
						|| pre4SuperDayVO.rsvCorssType == CrossType.GORDON)
						&& pre4SuperDayVO.kdjVO.kValueBetween(15, 30);

				boolean pre5DayKDJGordon = (pre5SuperDayVO.kdjCorssType == CrossType.GORDON
						|| pre5SuperDayVO.kdjCorssType == CrossType.NEAR_GORDON
						|| pre5SuperDayVO.rsvCorssType == CrossType.GORDON)
						&& pre5SuperDayVO.kdjVO.kValueBetween(15, 30);

				if (pre4DayKDJGordon || pre5DayKDJGordon) {
					if (curSuperDayVO.volumeIncreasePercent >= 1.0) {
						return true;
					}
				}

			}
			break;
		}

		case Day_ShenXian_Gordon_ZhuliJinChu_Gordon: {
			// shenxian gordong and zhuliJinChu gordon
			if (curSuperDayVO.shenXianCorssType12 == CrossType.GORDON
					&& curSuperDayVO.zhuliJinChuCrossType == CrossType.GORDON) {
				return true;
			}
			break;
		}

		case Day_Mai2_ShenXian_ZhuliJinChu_Gordon_Week_Mai2_Gordon: {
			// combine Day_ShenXian_Gordon_ZhuliJinChu_Gordon and
			// Day_Week_Mai1Mai2_Mai2_Grodon
			if (curSuperDayVO.shenXianCorssType12 == CrossType.GORDON
					&& curSuperDayVO.zhuliJinChuCrossType == CrossType.GORDON) {
				if (curSuperDayVO.mai1mai2CrossTypeMai2 == CrossType.GORDON
						&& curSuperWeekVO.mai1mai2CrossTypeMai2 == CrossType.GORDON) {
					return true;
				}
			}
		}

		case Day_Mai1_ShenXian_ZhuliJinChu_Gordon: {
			// combine Day_ShenXian_Gordon_ZhuliJinChu_Gordon and
			// Day_Mai1Mai2_Mai1_ShenXian_Grodon
			if (curSuperDayVO.shenXianCorssType12 == CrossType.GORDON
					&& curSuperDayVO.zhuliJinChuCrossType == CrossType.GORDON) {
				if (curSuperDayVO.mai1mai2CrossTypeMai1 == CrossType.GORDON
						&& curSuperDayVO.shenXianCorssType12 == CrossType.GORDON) {
					return true;
				}
			}
			break;
		}

		case YiYang_Cross_4K_Lines: {
			// big red K line cross MA5,10,20,30
			// example:600522 @2015-07-31
			if (curSuperDayVO.priceVO.low <= curSuperDayVO.avgMA5 && curSuperDayVO.priceVO.low <= curSuperDayVO.avgMA10
					&& curSuperDayVO.priceVO.low <= curSuperDayVO.avgMA20
					&& curSuperDayVO.priceVO.low <= curSuperDayVO.avgMA30) {
				if (curSuperDayVO.priceVO.open <= curSuperDayVO.avgMA5
						&& curSuperDayVO.priceVO.open <= curSuperDayVO.avgMA10
						&& curSuperDayVO.priceVO.open <= curSuperDayVO.avgMA20
						&& curSuperDayVO.priceVO.open <= curSuperDayVO.avgMA30) {
					if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5
							&& curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10
							&& curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA20
							&& curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA30) {
						if (curSuperDayVO.kdjCorssType == CrossType.GORDON
								|| curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON
								|| curSuperDayVO.rsvCorssType == CrossType.GORDON)
							return true;
					}
				}
			}
			break;
		}

		case SuoLiang_HuiTiao_ShenXiao_Gordon: {
			// example: 300039 @2015-08-13
			// week rsv is gordon
			// day shenxian is gordon, before this gordon, suoLiang huiTiao
			if (curSuperDayVO.kdjCorssType == CrossType.GORDON || curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON
					|| curSuperDayVO.rsvCorssType == CrossType.GORDON)
				if (curSuperDayVO.shenXianCorssType12 == CrossType.GORDON) {
					// suoLiang HuiTiao
					if (StockPriceUtils.isKLineGreen(pre2SuperDayVO.priceVO)
							&& StockPriceUtils.isKLineGreen(pre1SuperDayVO.priceVO)
							&& pre2SuperDayVO.priceVO.volume > pre1SuperDayVO.priceVO.volume
							&& pre1SuperDayVO.priceVO.volume > curSuperDayVO.priceVO.volume
							&& pre1SuperDayVO.priceVO.volume <= pre1SuperDayVO.avgVol5
							&& curSuperDayVO.priceVO.volume <= curSuperDayVO.avgVol5) {
						return true;
					}
				}
			break;
		}

		case YiMengBS_KDJ_Gordon: {
			if (curSuperDayVO.kdjCorssType == CrossType.GORDON || curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON
					|| curSuperDayVO.rsvCorssType == CrossType.GORDON) {
				if (curSuperDayVO.yiMengBSCrossType == CrossType.GORDON) {
					return true;
				}
			}
			break;
		}

		case YiMengBS_KDJ_Gordon_SuoLiang_HuiTiao: {
			if (curSuperDayVO.kdjCorssType == CrossType.GORDON || curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON
					|| curSuperDayVO.rsvCorssType == CrossType.GORDON) {
				// logger.debug(" here 2");
				if (curSuperDayVO.yiMengBSCrossType == CrossType.GORDON) {
					// logger.debug(" here 3");
					// suoLiang HuiTiao
					if (StockPriceUtils.isKLineGreen(pre2SuperDayVO.priceVO)
							&& StockPriceUtils.isKLineGreen(pre1SuperDayVO.priceVO)
							&& pre2SuperDayVO.priceVO.volume > pre1SuperDayVO.priceVO.volume
							&& pre1SuperDayVO.priceVO.volume > curSuperDayVO.priceVO.volume
							&& pre1SuperDayVO.priceVO.volume <= pre1SuperDayVO.avgVol5
							&& curSuperDayVO.priceVO.volume <= curSuperDayVO.avgVol5) {
						// logger.debug(" here 4");
						return true;
					}
				}
			}
			break;
		}

		case Many_ZhangTing_Then_DieTing: {
			// example: 002027 @2015-06-16
			if (curSuperDayVO.avgMA5 > curSuperDayVO.avgMA10 && curSuperDayVO.avgMA10 > curSuperDayVO.avgMA20
					&& curSuperDayVO.avgMA20 > curSuperDayVO.avgMA30) {
				if (curSuperDayVO.priceVO.open == curSuperDayVO.priceVO.low) {
					if (StockPriceUtils.isKLineDieTing(pre1SuperDayVO.priceVO)
							&& StockPriceUtils.isKLineZhangTing(pre2SuperDayVO.priceVO)
							&& StockPriceUtils.isKLineZhangTing(pre3SuperDayVO.priceVO)
							&& StockPriceUtils.isKLineZhangTing(pre4SuperDayVO.priceVO)) {
						return true;
					}
				}
			}
			break;
		}

		case Continue_ZiJinLiu_DDX_RED_KDJ_Gorden: {
			// example: 600900,600016 @2015-11-30
			// in 10 days, 7 days DDX red and money in
			if (curSuperDayVO.kdjCorssType == CrossType.GORDON || curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON
					|| curSuperDayVO.rsvCorssType == CrossType.GORDON) {
				if (this.numberOfDDXRedInNDays(overDayList, 10) >= 7) {
					return true;
				}
			}
			break;
		}
		// luzao close>ma19 and ma19<ma43<ma86
		// shenxian close>h1 and h1<h2
		case Trend_PhaseI_GuanCha: {
			if (curSuperDayVO.priceVO.close > curSuperDayVO.avgMA19 && (curSuperDayVO.avgMA19 < curSuperDayVO.avgMA43
					&& curSuperDayVO.avgMA43 < curSuperDayVO.avgMA86)) {
				if (curSuperDayVO.priceVO.close > curSuperDayVO.shenXianVO.h1
						&& curSuperDayVO.shenXianVO.h1 < curSuperDayVO.shenXianVO.h2) {
					return true;
				}
			}
			break;
		}

		// luzao close>ma19 and ma19<ma43<ma86 and ma19 is up
		// shenxian h1>h2
		case Trend_PhaseII_JianCang: {
			if (curSuperDayVO.priceVO.close > curSuperDayVO.avgMA19
					&& (curSuperDayVO.avgMA19 < curSuperDayVO.avgMA43 && curSuperDayVO.avgMA43 < curSuperDayVO.avgMA86)
					&& pre1SuperDayVO.avgMA19 < curSuperDayVO.avgMA19) {
				if (curSuperDayVO.shenXianVO.h1 > curSuperDayVO.shenXianVO.h2) {
					return true;
				}
			}
			break;
		}

		// luzao ma19>ma43 and ma43<ma86
		// shenxian h1>h2
		case Trend_PhaseIII_ChiGu: {
			if (curSuperDayVO.avgMA19 > curSuperDayVO.avgMA43 && curSuperDayVO.avgMA43 < curSuperDayVO.avgMA86) {
				if (curSuperDayVO.shenXianVO.h1 > curSuperDayVO.shenXianVO.h2) {
					return true;
				}
			}
			break;
		}

		// luzao ma19>ma43 and ma43>ma86
		// shenxian h1<h2
		case Trend_PhaseVI_JianCang: {
			if (curSuperDayVO.avgMA19 > curSuperDayVO.avgMA43 && curSuperDayVO.avgMA43 > curSuperDayVO.avgMA86) {
				if (curSuperDayVO.shenXianVO.h1 > curSuperDayVO.shenXianVO.h2) {
					return true;
				}
			}
			break;
		}
		// ma19<ma43, ma19<ma86, ma43 down cross ma86
		case LuZao_GordonO_MA43_DownCross_MA86: {
			if (curSuperDayVO.avgMA19 < curSuperDayVO.avgMA43 && curSuperDayVO.avgMA19 < curSuperDayVO.avgMA86) {
				if (pre1SuperDayVO.avgMA43 > pre1SuperDayVO.avgMA86 && curSuperDayVO.avgMA43 <= curSuperDayVO.avgMA86) {
					return true;
				}
			}
			break;
		}

		// ma19<ma86, ma43<ma86 ma19 up cross ma43
		case LuZao_GordonI_MA19_UpCross_MA43: {
			if (curSuperDayVO.avgMA19 < curSuperDayVO.avgMA86 && curSuperDayVO.avgMA43 < curSuperDayVO.avgMA86) {
				if (pre1SuperDayVO.avgMA19 < pre1SuperDayVO.avgMA43 && curSuperDayVO.avgMA19 >= curSuperDayVO.avgMA43) {
					return true;
				}
			}
			break;
		}

		// ma19>ma43, ma86>ma43, ma19 up cross ma86
		case LuZao_GordonII_MA19_UpCross_MA86: {
			if (curSuperDayVO.avgMA19 > curSuperDayVO.avgMA43 && curSuperDayVO.avgMA86 > curSuperDayVO.avgMA43) {
				if (pre1SuperDayVO.avgMA19 < pre1SuperDayVO.avgMA86 && curSuperDayVO.avgMA19 >= curSuperDayVO.avgMA86) {
					return true;
				}
			}
			break;
		}

		// 三山重叠
		// ma19>ma43, ma19>ma86 ma43 up corss ma86
		case LuZao_DeadI_MA43_UpCross_MA86: {
			if (curSuperDayVO.avgMA19 > curSuperDayVO.avgMA43 && curSuperDayVO.avgMA19 > curSuperDayVO.avgMA86) {
				if (pre1SuperDayVO.avgMA43 < pre1SuperDayVO.avgMA86 && curSuperDayVO.avgMA43 >= curSuperDayVO.avgMA86) {
					return true;
				}
			}
			break;
		}

		// 跌倒山腰
		// ma86<ma19, ma86<ma43, ma19 downcross ma43
		case LuZao_DeadII_MA19_DownCross_MA43: {
			if (curSuperDayVO.avgMA86 < curSuperDayVO.avgMA19 && curSuperDayVO.avgMA86 < curSuperDayVO.avgMA43) {
				if (pre1SuperDayVO.avgMA19 > pre1SuperDayVO.avgMA43 && curSuperDayVO.avgMA19 <= curSuperDayVO.avgMA43) {
					return true;
				}
			}
			break;
		}

		// 跌倒山脚
		// MA43 <= MA86
		case LuZao_DeadIII_MA43_DownCross_MA86: {
			if (curSuperDayVO.avgMA86 < curSuperDayVO.avgMA19 && curSuperDayVO.avgMA86 < curSuperDayVO.avgMA43) {
				if (curSuperDayVO.avgMA43 < curSuperDayVO.avgMA86 && pre1SuperDayVO.avgMA43 >= pre1SuperDayVO.avgMA86) {
					return true;
				}
			}
			break;
		}

		// qsdd top
		case QSDD_Top_Area: {
			if (curSuperDayVO.qsddTopArea)
				return true;
			break;
		}

		// qsdd bottom
		case QSDD_Bottom_Area: {
			if (curSuperDayVO.qsddBottomArea)
				return true;
			break;
		}

		// qsdd bottom
		case QSDD_Bottom_Gordon: {
			if (curSuperDayVO.qsddBottomCrossType == CrossType.GORDON)
				return true;
			break;
		}

		// 最高价是一个特殊数字，比如12.34, 55.55, 67.54 etc
		case High_Price_Digit_In_Order: {
			if (curSuperDayVO.priceVO.close > curSuperDayVO.avgMA19 && curSuperDayVO.avgMA19 > curSuperDayVO.avgMA43
					&& curSuperDayVO.avgMA43 > curSuperDayVO.avgMA86) {
				if (curSuperDayVO.priceVO.high == maxHighPriceInNDays(overDayList, 43)) {
					return DigitInOrderHelper.checkAll(curSuperDayVO.priceVO.high);
				}
			}
			return false;
		}

		case Low_Price_Digit_In_Order: {
			if (curSuperDayVO.priceVO.close < curSuperDayVO.avgMA19 && curSuperDayVO.avgMA19 < curSuperDayVO.avgMA43
					&& curSuperDayVO.avgMA43 < curSuperDayVO.avgMA86) {
				if (curSuperDayVO.priceVO.low == minLowPriceInNDays(overDayList, 43)) {
					return DigitInOrderHelper.checkAll(curSuperDayVO.priceVO.low);
				}
			}
			return false;
		}

		case KDJ_Over_Buy: {
			if (curSuperDayVO.kdjVO.j >= 110.0)
				return true;
			return false;
		}

		case KDJ_Over_Sell: {
			if (curSuperDayVO.kdjVO.j <= -10.0)
				return true;
			return false;
		}

		// wr bottom
		// short, middle and long term is less than 5 and trend is down
		case WR_Bottom_Area: {
			if (curSuperDayVO.wrVO.shoTerm <= 5.0 && curSuperDayVO.wrVO.midTerm <= 5.0
					&& curSuperDayVO.wrVO.lonTerm <= 5.0)
				if (curSuperDayVO.wrVO.shoTerm < pre1SuperDayVO.wrVO.shoTerm
						&& curSuperDayVO.wrVO.midTerm < pre1SuperDayVO.wrVO.midTerm
						&& curSuperDayVO.wrVO.lonTerm < pre1SuperDayVO.wrVO.lonTerm) {
					return true;
				}
			break;
		}

		// wr bottom gordon
		// pre1 avgTerm is the lowest value within 86 days
		// curr avgTerm is bigger then pre1's
		case WR_Bottom_Gordon: {
			if ((curSuperDayVO.avgWR > pre1SuperDayVO.avgWR)
					&& (curSuperDayVO.priceVO.close > pre1SuperDayVO.priceVO.close)) {
				// pre1 avgWR is the LLV of 86 days
				// pre1 shortWR is the LLV of 19, midWR is the LLV of 43
				int period = 86;

				double llvAvgWR = pre1SuperDayVO.avgWR;
				// check shortTerm
				for (int i = overDayList.size() - 1; i >= overDayList.size() - period; i--) {

					if (i < 0 || i > overDayList.size())
						return false;

					StockSuperVO tmpVO = overDayList.get(i);
					if (llvAvgWR > tmpVO.avgWR) {
						return false;
					}
				}
				// all above are satisfy
				return true;
			}

			break;
		}

		// wr bottom
		case WR_Top_Area: {
			if (curSuperDayVO.wrVO.shoTerm >= 95.0 && curSuperDayVO.wrVO.midTerm >= 95.0
					&& curSuperDayVO.wrVO.lonTerm >= 95.0)
				if (curSuperDayVO.wrVO.shoTerm > pre1SuperDayVO.wrVO.shoTerm
						&& curSuperDayVO.wrVO.midTerm > pre1SuperDayVO.wrVO.midTerm
						&& curSuperDayVO.wrVO.lonTerm > pre1SuperDayVO.wrVO.lonTerm) {
					return true;
				}
			break;
		}

		// KDJ gordon & tiao kong gao kai
		case LuZao_KDJ_Gordon_TiaoKongGaoKai: {
			if (curSuperDayVO.avgMA86 >= curSuperDayVO.avgMA43 && curSuperDayVO.avgMA43 >= curSuperDayVO.avgMA19) {
				if (curSuperDayVO.kdjCorssType == CrossType.GORDON || curSuperDayVO.rsvCorssType == CrossType.GORDON
						|| pre1SuperDayVO.kdjCorssType == CrossType.GORDON
						|| pre1SuperDayVO.rsvCorssType == CrossType.GORDON) {
					if (curSuperDayVO.priceVO.low > pre1SuperDayVO.priceVO.high
							&& curSuperDayVO.priceVO.close > curSuperDayVO.priceVO.open) {
						if (curSuperDayVO.kdjVO.k <= 50) {
							return true;
						}
					}
				}
			}
			break;
		}

		case WR_Ready_To_ZhangTing: {
			// two of wr are same and the remain one is uppger near to the both
			// case1: shortTerm and middleTerm are same value, higher than 70,
			// longTerm is bigger than yesterday, near to the shortTerm and
			// middleTerm. Example 600372 @2016-07-01
			// case2: middleTerm and longTerm are same value, higher than 70,
			// shortTerm is bigger than yesterday, near to the middleTerm and
			// longTerm. Example 002211 @2016-07-05

			if (curSuperDayVO.avgMA5 < curSuperDayVO.avgMA10 || curSuperDayVO.avgMA5 < pre1SuperDayVO.avgMA5
					|| curSuperDayVO.priceVO.close < curSuperDayVO.avgMA5) {
				return false;
			}

			// case1:
			if (curSuperDayVO.wrVO.shoTerm == curSuperDayVO.wrVO.midTerm && curSuperDayVO.wrVO.shoTerm >= 70
					&& curSuperDayVO.wrVO.lonTerm > pre1SuperDayVO.wrVO.lonTerm) {
				double diff = 100 * (curSuperDayVO.wrVO.shoTerm - curSuperDayVO.wrVO.lonTerm)
						/ curSuperDayVO.wrVO.shoTerm;
				if (diff > 0 && diff < 5.0) {
					return true;
				}
			}

			// case2:
			if (curSuperDayVO.wrVO.lonTerm == curSuperDayVO.wrVO.midTerm && curSuperDayVO.wrVO.lonTerm >= 70
					&& curSuperDayVO.wrVO.shoTerm > pre1SuperDayVO.wrVO.shoTerm) {
				double diff = 100 * (curSuperDayVO.wrVO.lonTerm - curSuperDayVO.wrVO.shoTerm)
						/ curSuperDayVO.wrVO.lonTerm;
				if (diff > 0 && diff < 5.0) {
					return true;
				}
			}
		}

		// macd di bei li (not correct 该模型还不成熟)
		// 估价比前几日创出新低，但是macd不是最低
		case MACD_DI_BeiLi: {
			// 当日股价创86日新低
			if (isLowestPriceWithinDays(overDayList, 40) && curSuperDayVO.macdVO.macd < 0) {
				if (curSuperDayVO.priceVO.low < pre1SuperDayVO.priceVO.low) {
					if (curSuperDayVO.macdVO.macd > pre1SuperDayVO.macdVO.macd) {
						return true;
					}
				}
			}
			break;
		}

		// wr di bei li
		// 估价比前几日创出新低，但是wr不是最低
		case WR_DI_BeiLi: {
			// 当日股价创86日新低
			if (isLowestPriceWithinDays(overDayList, 40) && curSuperDayVO.wrVO.lonTerm <= 10.0) {
				if (curSuperDayVO.priceVO.low < pre1SuperDayVO.priceVO.low) {
					if (curSuperDayVO.wrVO.lonTerm > pre1SuperDayVO.wrVO.lonTerm) {
						return true;
					}
				}
			}
			break;
		}

		// wr: long and short term ronghe xianshang
		// example: 000673 @2016-05-19
		case WR_4_Days_SameValue_XianShang: {
			// today, two wr are both up corss 20
			if ((curSuperDayVO.wrVO.lonTerm == curSuperDayVO.wrVO.shoTerm)
					&& (pre1SuperDayVO.wrVO.lonTerm == pre1SuperDayVO.wrVO.shoTerm)
					&& (pre2SuperDayVO.wrVO.lonTerm == pre2SuperDayVO.wrVO.shoTerm)
					&& (pre3SuperDayVO.wrVO.lonTerm == pre3SuperDayVO.wrVO.shoTerm)) {
				if (curSuperDayVO.wrVO.lonTerm > 20 && pre1SuperDayVO.wrVO.lonTerm < 20
						&& pre2SuperDayVO.wrVO.lonTerm > 20 && pre3SuperDayVO.wrVO.lonTerm > 20) {
					return true;
				}
			}

			break;
		}

		// example: 002466 @2017-02-21
		case LuZao_PhaseII_MACD_WEEK_GORDON_MACD_DAY_DIF_CROSS_0: {
			if (curSuperWeekVO.macdCorssType == CrossType.GORDON && curSuperDayVO.macdVO.macd > 0
					&& curSuperDayVO.macdVO.dif >= 0 && pre1SuperDayVO.macdVO.dif < 0) {
				if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA19
						&& curSuperDayVO.avgMA19 <= curSuperDayVO.avgMA43
						&& curSuperDayVO.avgMA43 <= curSuperDayVO.avgMA86
						&& pre1SuperDayVO.avgMA19 < curSuperDayVO.avgMA19) {
					return true;
				}
			}
			break;
		}

		// example: 002466 @2017-02-21
		case LuZao_PhaseIII_MACD_WEEK_GORDON_MACD_DAY_DIF_CROSS_0: {
			if (curSuperWeekVO.macdCorssType == CrossType.GORDON && curSuperDayVO.macdVO.macd > 0
					&& curSuperDayVO.macdVO.dif >= 0 && pre1SuperDayVO.macdVO.dif < 0) {
				if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA19
						&& curSuperDayVO.avgMA19 >= curSuperDayVO.avgMA43
						&& curSuperDayVO.avgMA43 <= curSuperDayVO.avgMA86
						&& pre1SuperDayVO.avgMA19 < curSuperDayVO.avgMA19) {
					return true;
				}
			}
			break;
		}

		// example: 600410 @2017-04-12
		case LuZao_PhaseII_MACD_WEEK_GORDON_KDJ_WEEK_GORDON: {
			if (curSuperWeekVO.macdCorssType == CrossType.GORDON && curSuperWeekVO.kdjCorssType == CrossType.GORDON
					&& curSuperDayVO.macdVO.macd > 0) {
				if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA19
						&& curSuperDayVO.avgMA19 <= curSuperDayVO.avgMA43
						&& curSuperDayVO.avgMA43 <= curSuperDayVO.avgMA86
						&& pre1SuperDayVO.avgMA19 < curSuperDayVO.avgMA19) {
					return true;
				}
			}
			break;
		}

		// example: 600410 @2017-04-12
		case LuZao_PhaseIII_MACD_WEEK_GORDON_KDJ_WEEK_GORDON: {
			if (curSuperWeekVO.macdCorssType == CrossType.GORDON && curSuperWeekVO.kdjCorssType == CrossType.GORDON
					&& curSuperDayVO.macdVO.macd > 0) {
				if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA19
						&& curSuperDayVO.avgMA19 >= curSuperDayVO.avgMA43
						&& curSuperDayVO.avgMA43 <= curSuperDayVO.avgMA86
						&& pre1SuperDayVO.avgMA19 < curSuperDayVO.avgMA19) {
					return true;
				}
			}
			break;
		}

		// example: 601336 @2017-04-19 and 000049 @2017-01-16
		// 鲁兆 建仓或者持股，零下MACD二次金叉 W底， MACD底背离
		case MACD_TWICE_GORDON_W_Botton_MACD_DI_BEILI: {

			int curIndex = overDayList.size() - 1;

			if (curSuperDayVO.macdCorssType == CrossType.GORDON && curSuperDayVO.macdVO.dif < 0
					&& curSuperDayVO.avgMA43 <= curSuperDayVO.avgMA86) {
				// find previously macd Gordon within 30 days
				int preMacdGordonIndex = this.findPreviouslyMACDGordonIndex(overDayList, 30);
				if (preMacdGordonIndex < 0) {
					return false;
				}

				// 两个金叉的macd dif值都是出于零轴之下
				StockSuperVO spvo = overDayList.get(preMacdGordonIndex);
				if (curSuperDayVO.macdVO.dif > 0 || spvo.macdVO.dif > 0) {
					return false;
				}

				StockSuperVO pNdaysVO = overDayList.get(preMacdGordonIndex);

				// previously macd dif is less then now dif
				// 两个金叉之间相差12天以上，避免振幅很小的误差
				if (pNdaysVO != null && pNdaysVO.macdVO.dif < curSuperDayVO.macdVO.dif
						&& (curIndex - preMacdGordonIndex) >= 12) {

					// previously lowest price is higher then now lowest price
					StockSuperVO preLowestPriceVO1 = this.findPreviouslyLowestPriceIndex(overDayList,
							preMacdGordonIndex - 10, preMacdGordonIndex);
					StockSuperVO preLowestPriceVO2 = this.findPreviouslyLowestPriceIndex(overDayList, curIndex - 5,
							curIndex);
					// 第一macd金叉的前几日最低价格高于第二金叉前几日的最低价格，构成价格背离
					// 第一macd金叉的前几日最低价格时候macd dif低于第二金叉前几日的最低价格时候macd
					// dif，构成MACD背离
					if (preLowestPriceVO1 != null && preLowestPriceVO2 != null) {
						if (preLowestPriceVO1.priceVO.low > preLowestPriceVO2.priceVO.low
								&& preLowestPriceVO1.macdVO.dif < preLowestPriceVO2.macdVO.dif) {
							// logger.debug(pNdaysVO.macdVO + " " +
							// curSuperDayVO.macdVO);
							return true;
						}
					}
				}
			}
			break;
		}

		// example: 300072@2016-03-14 @2017-10-10 && 000049@2017-01-18
		// 鲁兆建仓阶段，MACD二次金叉，W大底，跳空站稳布林线
		case MACD_TWICE_GORDON_W_Botton_TiaoKong_ZhanShang_Bull: {
			int curIndex = overDayList.size() - 1;

			// 开盘跳空,收盘站上布林线
			if (curSuperDayVO.priceVO.low > pre1SuperDayVO.priceVO.high
					&& curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA20) {

				// luzao phase II or III
				if (curSuperDayVO.avgMA43 <= curSuperDayVO.avgMA86) {

					if ((curSuperDayVO.macdCorssType == CrossType.GORDON
							|| curSuperDayVO.macdCorssType == CrossType.NEAR_GORDON) && curSuperDayVO.macdVO.dif < 0) {

						// find previously macd Gordon within 40 days
						int preMacdGordonIndex = this.findPreviouslyMACDGordonIndex(overDayList, 40);
						if (preMacdGordonIndex < 0) {
							return false;
						}

						// 两个金叉的macd dif值都是出于零轴之下
						StockSuperVO spvo = overDayList.get(preMacdGordonIndex);
						if (curSuperDayVO.macdVO.dif > 0 || spvo.macdVO.dif > 0) {
							return false;
						}
						StockSuperVO pNdaysVO = overDayList.get(preMacdGordonIndex);

						// previously macd dif is less then now dif
						// 两个金叉之间相差12天以上，避免振幅很小的误差
						if (pNdaysVO != null && pNdaysVO.macdVO.dif < curSuperDayVO.macdVO.dif
								&& (curIndex - preMacdGordonIndex) >= 12) {
							return true;
						}
					}
				}
			}
			break;
		}

		// 跳空高开2.5个点以上，当天回补缺口
		case TIAOKONG_GAOKAI_25_POINTS_DAY1_HUIBU: {
			if (curSuperDayVO.priceVO.open - pre1SuperDayVO.priceVO.high > 0) {
				if (curSuperDayVO.priceVO.low <= pre1SuperDayVO.priceVO.high) {
					double tiaoKong = curSuperDayVO.priceVO.open - pre1SuperDayVO.priceVO.high;
					double per = tiaoKong / (pre1SuperDayVO.priceVO.close * 0.1);
					if (per >= 0.25) {
						return true;
					}
				}
			}
			break;
		}

		// 上涨神奇9转
		// continue shang zhang for 9 days
		case MAGIC_NIGHT_DAYS_SHANG_ZHANG: {
			// 9 th day
			if (curSuperDayVO.priceVO.close > pre4SuperDayVO.priceVO.close) {

				// 8 th day
				if (pre1SuperDayVO.priceVO.close > pre5SuperDayVO.priceVO.close) {

					// 7 th day
					if (pre2SuperDayVO.priceVO.close > pre6SuperDayVO.priceVO.close) {

						// 6 th day
						if (pre3SuperDayVO.priceVO.close > pre7SuperDayVO.priceVO.close) {

							// 5 th day
							if (pre4SuperDayVO.priceVO.close > pre8SuperDayVO.priceVO.close) {

								// 4 th day
								if (pre5SuperDayVO.priceVO.close > pre9SuperDayVO.priceVO.close) {

									// 3 th day
									if (pre6SuperDayVO.priceVO.close > pre10SuperDayVO.priceVO.close) {

										// 2 th day
										if (pre7SuperDayVO.priceVO.close > pre11SuperDayVO.priceVO.close) {

											// 1 st day
											if (pre8SuperDayVO.priceVO.close > pre12SuperDayVO.priceVO.close) {
												// 1 st day is up
												if (pre8SuperDayVO.priceVO.close > pre9SuperDayVO.priceVO.close) {
													// logger.debug(pre8SuperDayVO.priceVO.date);
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
		}

		// 下跌神奇9转
		// continue xia die for 9 days
		case MAGIC_NIGHT_DAYS_XIA_DIE: {
			// 9 th day
			if (curSuperDayVO.priceVO.close < pre4SuperDayVO.priceVO.close) {

				// 8 th day
				if (pre1SuperDayVO.priceVO.close < pre5SuperDayVO.priceVO.close) {

					// 7 th day
					if (pre2SuperDayVO.priceVO.close < pre6SuperDayVO.priceVO.close) {

						// 6 th day
						if (pre3SuperDayVO.priceVO.close < pre7SuperDayVO.priceVO.close) {

							// 5 th day
							if (pre4SuperDayVO.priceVO.close < pre8SuperDayVO.priceVO.close) {

								// 4 th day
								if (pre5SuperDayVO.priceVO.close < pre9SuperDayVO.priceVO.close) {

									// 3 th day
									if (pre6SuperDayVO.priceVO.close < pre10SuperDayVO.priceVO.close) {

										// 2 th day
										if (pre7SuperDayVO.priceVO.close < pre11SuperDayVO.priceVO.close) {

											// 1 st day
											if (pre8SuperDayVO.priceVO.close < pre12SuperDayVO.priceVO.close) {
												// 1 st day is down
												if (pre8SuperDayVO.priceVO.close < pre9SuperDayVO.priceVO.close) {
													// logger.debug(pre8SuperDayVO.priceVO.date);
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
		}

		default:
			return false;
		}
		return false;
	}

	private StockSuperVO findPreviouslyLowestPriceIndex(List<StockSuperVO> overDayList, int startIndex, int endIndex) {
		if (startIndex < 0)
			return null;

		StockSuperVO lowestPriceVO = overDayList.get(endIndex);

		for (int index = startIndex; index < endIndex; index++) {
			StockSuperVO spVO = overDayList.get(index);
			if (lowestPriceVO.priceVO.low > spVO.priceVO.low) {
				lowestPriceVO = spVO;
			}
		}

		// find a lowest price vo which is 10 days before the current day.
		if (!lowestPriceVO.priceVO.date.equals(overDayList.get(endIndex).priceVO.date)) {
			return lowestPriceVO;
		}

		return null;
	}

	private int findPreviouslyMACDGordonIndex(List<StockSuperVO> overDayList, int daysBefore) {
		int curIndex = overDayList.size() - 1;
		int startIndex = curIndex - daysBefore;
		if (startIndex < 0)
			return -1;

		for (int index = startIndex; index < curIndex; index++) {
			StockSuperVO spvo = overDayList.get(index);
			if (spvo.macdCorssType == CrossType.GORDON) {
				return index;
			}
		}
		return -1;
	}

	public boolean isPlatform(List<StockSuperVO> overDayList, List<StockSuperVO> overWeekList) {
		StockSuperVO curSuperDayVO = overDayList.get(overDayList.size() - 1);

		// debugInfo(curSuperDayVO, "2016-04-14", "isPlatform 1");

		// merge with findPlatformStartVO and
		// findLongPlatformBasedOnWeekDateOrig
		// return true if is a hengPan platform
		// day platform
		int minPlatformLen = 9;
		int maxPlatformLen = 30;
		boolean findPlatform = false;
		for (int length = minPlatformLen; length <= maxPlatformLen; length++) {
			if (findPlatformStartVO(overDayList.subList(overDayList.size() - length, overDayList.size()))) {
				findPlatform = true;
				curSuperDayVO.hengPanWeekLen = length / 5;
				break;
			}
		}

		// debugInfo(curSuperDayVO, "2016-04-14", "isPlatform 2");

		// week platform
		minPlatformLen = 3;
		maxPlatformLen = 10;
		for (int length = minPlatformLen; length <= maxPlatformLen; length++) {
			if (findLongPlatformBasedOnWeekDate(overWeekList.subList(overWeekList.size() - length, overWeekList.size()),
					overDayList)) {
				findPlatform = true;
				curSuperDayVO.hengPanWeekLen = length;
				break;
			}

			if (findLongPlatformBasedOnWeekDateOrig(
					overWeekList.subList(overWeekList.size() - length, overWeekList.size()))) {
				findPlatform = true;
				curSuperDayVO.hengPanWeekLen = length;
				break;
			}
		}

		// debugInfo(curSuperDayVO, "2016-04-14", "isPlatform 3 findPlatform=" +
		// findPlatform);

		return findPlatform;
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
				if ((((vo.priceVO.high - startVO.priceVO.high) * 100) / startVO.priceVO.high) >= 15) {
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

	// original checker, only use week data
	// to check if the list is a platform
	public boolean findLongPlatformBasedOnWeekDateOrig(List<StockSuperVO> overWeekList) {
		// example: 300216 @ 20150421; 002040 @ 20150421
		// pls also consider: 000901, 600818, 300177 ,000768
		// at least 5 weeks data
		// the first week is a big red K line,
		// J is much higher (>80), MACD bigger 0;
		// then ~5 week hengPan; KDJ dead find;
		// the continue high and low is between the first K line
		StockSuperVO startVO = overWeekList.get(0);
		StockSuperVO endVO = overWeekList.get(overWeekList.size() - 1);

		String Sdate = startVO.priceVO.date;
		String Edate = endVO.priceVO.date;
		// logger.debug("debug 1 " + Sdate + " ~ " + Edate + " " +
		// startVO.kdjVO);

		if (startVO.kdjVO.j < 73)
			return false;

		// logger.debug("debug 2 " + Sdate + " ~ " + Edate);

		if (startVO.macdVO.macd < 0)
			return false;

		// logger.debug("debug 3 " + Sdate + " ~ " + Edate);

		double startPriceIncrease = ((startVO.priceVO.close - startVO.priceVO.lastClose) * 100.0)
				/ startVO.priceVO.lastClose;

		double avgClose = 0;
		boolean findKDJDead = false;
		double maxKDJ_K = 0;
		double minKDJ_K = 100;

		if (startPriceIncrease < 12) {
			// logger.debug("debug 4 " + Sdate + " ~ " + Edate);
			return false;
		}

		for (int i = 1; i < overWeekList.size(); i++) {
			StockSuperVO vo = overWeekList.get(i);
			double priceIncrease = ((vo.priceVO.close - vo.priceVO.lastClose) * 100.0) / vo.priceVO.lastClose;

			// if next week find one priceIncrease is bigger then startVO,
			// then not the platform
			if (priceIncrease > startPriceIncrease) {
				// logger.debug("debug 4 " + Sdate + " ~ " + Edate);
				return false;
			}

			// if next week find one high is greater since platform
			// startVO.hight, then not the platform
			if (vo.priceVO.high > startVO.priceVO.high * 1.05) {
				// logger.debug("debug 5 " + Sdate + " ~ " + Edate);
				return false;
			}

			// if next week find one low is less than the platform
			// startVO.open or less then ma20
			if (vo.priceVO.low < startVO.priceVO.low * 0.975) {
				// logger.debug("debug 6 " + Sdate + " ~ " + Edate);
				return false;
			}

			if (vo.kdjCorssType == CrossType.DEAD) {
				// logger.debug("debug 7 " + Sdate + " ~ " + Edate);
				findKDJDead = true;
			}

			avgClose += vo.priceVO.close;

			if (maxKDJ_K < vo.kdjVO.k)
				maxKDJ_K = vo.kdjVO.k;
			if (minKDJ_K > vo.kdjVO.k)
				minKDJ_K = vo.kdjVO.k;
		}

		// if no found KDJ dead, not the long platform
		if (!findKDJDead) {
			// logger.debug("debug 8 " + Sdate + " ~ " + Edate);
			return false;
		}

		// max KDJ_K and min KDJ_K must between 15%
		if ((maxKDJ_K - minKDJ_K) / minKDJ_K * 100 >= 25) {
			// logger.debug("debug 9 " + Sdate + " ~ " + Edate);
			return false;
		}

		avgClose = avgClose / (overWeekList.size() - 1);
		// next avg close is greater than the middle platform startVO.open +
		// close / 2
		if (avgClose < ((startVO.priceVO.open + startVO.priceVO.close) / 2.05)) {
			// logger.debug("debug 10 " + Sdate + " ~ " + Edate);
			return false;
		}
		// logger.debug("debug 11 " + Sdate + " ~ " + Edate);
		// logger.debug("findLongPlatformBasedOnWeekDateOrig from " +
		// startVO.priceVO.date + " to "
		// + endVO.priceVO.date);
		return true;
	}

	// new checker, use both week and day data
	// to check if the list is a platform
	public boolean findLongPlatformBasedOnWeekDate(List<StockSuperVO> overWeekList, List<StockSuperVO> overDayList) {
		// example: 300216 @ 20150421; 002040 @ 20150421
		// pls also consider: 000901, 600818, 300177 ,000768
		// at least 5 weeks data
		// the first week is a big red K line,
		// J is much higher (>80), MACD bigger 0;
		// then ~5 week hengPan; KDJ dead find;
		// the continue high and low is between the first K line
		StockSuperVO startVO = overWeekList.get(0);
		StockSuperVO endVO = overWeekList.get(overWeekList.size() - 1);

		String Sdate = startVO.priceVO.date;
		String Edate = endVO.priceVO.date;
		int redKLineCount = 0;

		// logger.debug("debug 1 " + Sdate + " ~ " + Edate + " " +
		// startVO.kdjVO);

		if (startVO.kdjVO.j < 73)
			return false;

		// logger.debug("debug 2 " + Sdate + " ~ " + Edate);

		if (startVO.macdVO.macd < 0)
			return false;

		// logger.debug("debug 3 " + Sdate + " ~ " + Edate);

		double startPriceIncrease = ((startVO.priceVO.close - startVO.priceVO.lastClose) * 100.0)
				/ startVO.priceVO.lastClose;

		double avgClose = 0;
		boolean findKDJDead = false;
		double maxKDJ_K = 0;
		double minKDJ_K = 100;

		if (startPriceIncrease < 12) {
			// logger.debug("debug 41 " + Sdate + " ~ " + Edate);
			return false;
		}

		int startIndex = this.getDayIndex(overDayList, Sdate);
		int endIndex = this.getDayIndex(overDayList, Edate);
		for (int i = startIndex; i <= endIndex; i++) {
			StockSuperVO vo = overDayList.get(i);
			double priceIncrease = ((vo.priceVO.close - vo.priceVO.lastClose) * 100.0) / vo.priceVO.lastClose;

			// if next week find one priceIncrease is bigger then startVO,
			// then not the platform
			if (priceIncrease > startPriceIncrease) {
				// logger.debug("debug 42 " + Sdate + " ~ " + Edate);
				return false;
			}

			// if next week find one high is greater since platform
			// startVO.hight, then not the platform
			if (vo.priceVO.high > startVO.priceVO.high * 1.05) {
				// logger.debug("debug 5 " + Sdate + " ~ " + Edate +
				// " High Price=" + vo.priceVO.high
				// + ", start High Price=" + startVO.priceVO.high);
				return false;// reasonable???????
			}

			// if next week find one low is less than the platform
			// startVO.open or less then ma20
			if (vo.priceVO.low < startVO.priceVO.low * 0.975) {
				// logger.debug("debug 6 " + Sdate + " ~ " + Edate);
				return false;
			}

			if (vo.kdjCorssType == CrossType.DEAD) {
				// logger.debug("debug 7 " + Sdate + " ~ " + Edate);
				findKDJDead = true;
			}

			avgClose += vo.priceVO.close;

			if (StockPriceUtils.isKLineRed(vo.priceVO)) {
				redKLineCount++;
			}

			if (maxKDJ_K < vo.kdjVO.k)
				maxKDJ_K = vo.kdjVO.k;
			if (minKDJ_K > vo.kdjVO.k)
				minKDJ_K = vo.kdjVO.k;
		}

		// if no found KDJ dead, not the long platform
		if (!findKDJDead) {
			// logger.debug("debug 8 " + Sdate + " ~ " + Edate);
			return false;
		}

		// max KDJ_K and min KDJ_K must between 15%
		if ((maxKDJ_K - minKDJ_K) / minKDJ_K * 100 >= 25) {
			// logger.debug("debug 9 " + Sdate + " ~ " + Edate);
			// return false;
		}

		avgClose = avgClose / (endIndex - startIndex + 1);
		// next avg close is greater than the middle platform startVO.open +
		// close / 2.05
		if (avgClose < ((startVO.priceVO.open + startVO.priceVO.close) / 2.05)) {
			// logger.debug("debug 10 " + avgClose + " " + Sdate + " ~ " +
			// Edate);
			return false;
		}

		if (redKLineCount < (endIndex - startIndex) / 2) {
			// logger.debug("debug 11 " + Sdate + " ~ " + Edate);
			// return false;
		}
		// logger.debug("debug OK " + Sdate + " ~ " + Edate);
		// logger.debug("findLongPlatformBasedOnWeekDate from " +
		// overDayList.get(startIndex).priceVO.date + " to "
		// + overDayList.get(endIndex).priceVO.date);
		return true;
	}

	private int getDayIndex(List<StockSuperVO> overDayList, String date) {
		for (int index = 0; index < overDayList.size(); index++) {
			StockSuperVO vo = overDayList.get(index);
			if (vo.priceVO.date.equals(date))
				return index;
		}
		return 0;
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

	private boolean MA5_MA10_Ronghe(StockSuperVO curSuperDayVO) {
		// rongHe and xiangShang
		double dif = Math.abs(curSuperDayVO.avgMA5 - curSuperDayVO.avgMA10);
		double min = Math.min(curSuperDayVO.avgMA5, curSuperDayVO.avgMA10);
		// MA rongHe
		if (((dif / min) * 100) < 2.0) {
			return true;
		}
		return false;
	}

	private boolean MA10_MA20_Ronghe(StockSuperVO curSuperDayVO) {
		// rongHe and xiangShang
		double dif = Math.abs(curSuperDayVO.avgMA10 - curSuperDayVO.avgMA20);
		double min = Math.min(curSuperDayVO.avgMA10, curSuperDayVO.avgMA20);
		// MA rongHe
		if (((dif / min) * 100) < 2.0) {
			return true;
		}
		return false;
	}

	private boolean MA5_MA10_MA20_Ronghe(StockSuperVO curSuperDayVO) {
		// rongHe and xiangShang
		double min = this.findMinValue(curSuperDayVO.avgMA5, curSuperDayVO.avgMA10, curSuperDayVO.avgMA20);
		double max = this.findMaxValue(curSuperDayVO.avgMA5, curSuperDayVO.avgMA10, curSuperDayVO.avgMA20);
		double dif = Math.abs(max - min);
		// MA rongHe
		if (((dif / min) * 100) < 3.0) {
			return true;
		}
		return false;
	}

	private boolean MA5_MA10_MA20_MA30_Ronghe(StockSuperVO curSuperDayVO) {
		// rongHe and xiangShang
		double min = this.findMinValue(curSuperDayVO.avgMA5, curSuperDayVO.avgMA10, curSuperDayVO.avgMA20,
				curSuperDayVO.avgMA30);
		double max = this.findMaxValue(curSuperDayVO.avgMA5, curSuperDayVO.avgMA10, curSuperDayVO.avgMA20,
				curSuperDayVO.avgMA30);
		double dif = Math.abs(max - min);
		// MA rongHe
		if (((dif / min) * 100) < 4.5) {
			return true;
		}
		return false;
	}

	private boolean MA5_MA10_Ronghe_XiangShang(StockSuperVO curSuperDayVO, StockSuperVO pre1SuperDayVO) {
		return MA5_MA10_Ronghe(curSuperDayVO) && MA5_MA10_XiangShang(curSuperDayVO, pre1SuperDayVO);
	}

	private boolean MA10_MA20_Ronghe_XiangShang(StockSuperVO curSuperDayVO, StockSuperVO pre1SuperDayVO) {
		return MA10_MA20_Ronghe(curSuperDayVO) && MA10_MA20_XiangShang(curSuperDayVO, pre1SuperDayVO);
	}

	private boolean MA5_MA10_MA20_Ronghe_XiangShang(StockSuperVO curSuperDayVO, StockSuperVO pre1SuperDayVO) {
		// rongHe
		if (!MA5_MA10_MA20_Ronghe(curSuperDayVO))
			return false;

		// xiangShang
		if ((curSuperDayVO.avgMA5 >= pre1SuperDayVO.avgMA5) && (curSuperDayVO.avgMA10 >= pre1SuperDayVO.avgMA10)
				&& (curSuperDayVO.avgMA20 >= pre1SuperDayVO.avgMA20)) {
			return true;
		}

		return false;
	}

	private boolean MA5_MA10_MA20_MA30_Ronghe_XiangShang(StockSuperVO curSuperDayVO, StockSuperVO pre1SuperDayVO) {

		// rongHe
		if (!MA5_MA10_MA20_MA30_Ronghe(curSuperDayVO))
			return false;

		// xiangShang
		if ((curSuperDayVO.avgMA5 >= pre1SuperDayVO.avgMA5) && (curSuperDayVO.avgMA10 >= pre1SuperDayVO.avgMA10)
				&& (curSuperDayVO.avgMA20 >= pre1SuperDayVO.avgMA20)
				&& (curSuperDayVO.avgMA30 >= pre1SuperDayVO.avgMA30)) {
			return true;
		}

		return false;
	}

	private boolean MA5_MA10_XiangShang(StockSuperVO curSuperDayVO, StockSuperVO pre1SuperDayVO) {
		// xiangShang
		if ((curSuperDayVO.avgMA5 >= pre1SuperDayVO.avgMA5) && (curSuperDayVO.avgMA10 >= pre1SuperDayVO.avgMA10)) {
			return true;
		}
		return false;
	}

	private boolean MA10_MA20_XiangShang(StockSuperVO curSuperDayVO, StockSuperVO pre1SuperDayVO) {
		// xiangShang
		if ((curSuperDayVO.avgMA10 >= pre1SuperDayVO.avgMA10) && (curSuperDayVO.avgMA20 >= pre1SuperDayVO.avgMA20)) {
			return true;
		}
		return false;
	}

	private boolean close_Higher_MA5_MA10(StockSuperVO curSuperDayVO) {

		// close higher than ma5 and ma10
		if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5
				&& curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10) {
			return true;
		}

		return false;
	}

	private boolean close_Higher_MA10_MA20(StockSuperVO curSuperDayVO) {

		// close higher than ma5 and ma10
		if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10
				&& curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA20) {
			return true;
		}

		return false;
	}

	private boolean close_Higher_MA20_MA30(StockSuperVO curSuperDayVO) {

		// close higher than ma5 and ma10
		if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA20
				&& curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA30) {
			return true;
		}

		return false;
	}

	private boolean close_Higher_N_Percent_Than_LastClose(StockSuperVO curSuperDayVO, double increasePercent) {

		// close higher N% than lastClose
		if ((curSuperDayVO.priceVO.close - curSuperDayVO.priceVO.lastClose) * 100
				/ curSuperDayVO.priceVO.lastClose >= increasePercent) {
			return true;
		}

		return false;
	}

	private boolean close_Lower_N_Percent_Than_LastClose(StockSuperVO curSuperDayVO, double increasePercent) {

		// close higher N% than lastClose
		if ((curSuperDayVO.priceVO.close - curSuperDayVO.priceVO.lastClose) * 100
				/ curSuperDayVO.priceVO.lastClose < increasePercent) {
			return true;
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

	private int numberOfDDXRedInNDays(List<StockSuperVO> overDayList, int NDays) {
		int number = 0;
		for (int index = overDayList.size() - 1; index >= (overDayList.size() - NDays); index--) {
			StockSuperVO spvo = overDayList.get(index);
			if (spvo.ddxVO != null && spvo.ddxVO.ddx > 0.0) {
				number++;
			}
		}
		return number;
	}

	private double maxHighPriceInNDays(List<StockSuperVO> overDayList, int NDays) {
		double high = overDayList.get(overDayList.size() - 1).priceVO.high;
		for (int index = overDayList.size() - 1; index >= (overDayList.size() - NDays); index--) {
			StockSuperVO spvo = overDayList.get(index);
			if (high < spvo.priceVO.high) {
				high = spvo.priceVO.high;
			}
		}
		return high;
	}

	private double minLowPriceInNDays(List<StockSuperVO> overDayList, int NDays) {
		double low = overDayList.get(overDayList.size() - 1).priceVO.low;
		for (int index = overDayList.size() - 1; index >= (overDayList.size() - NDays); index--) {
			StockSuperVO spvo = overDayList.get(index);
			if (low > spvo.priceVO.low) {
				low = spvo.priceVO.low;
			}
		}
		return low;
	}

	// 判断当日价格是否86天之内最低
	private boolean isLowestPriceWithinDays(List<StockSuperVO> overDayList, int dayRange) {
		StockSuperVO curSuperDayVO = overDayList.get(overDayList.size() - 1);
		double curLow = curSuperDayVO.priceVO.low;
		for (int i = overDayList.size() - dayRange; i < overDayList.size() - 1; i++) {
			if (curLow > overDayList.get(i).priceVO.low) {
				return false;
			}
		}
		return true;
	}
}
