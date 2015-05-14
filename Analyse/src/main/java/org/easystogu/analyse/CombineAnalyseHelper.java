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
        StockSuperVO pre2SuperWeekVO = overWeekList.get(overWeekList.size() - 3);
        StockSuperVO pre3SuperWeekVO = overWeekList.get(overWeekList.size() - 4);
        StockSuperVO pre4SuperWeekVO = overWeekList.get(overWeekList.size() - 5);
        StockSuperVO pre5SuperWeekVO = overWeekList.get(overWeekList.size() - 6);

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

        case ShenXian_Gordon:
            if (curSuperDayVO.shenXianCorssType12 == CrossType.GORDON) {
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

        case HengPan_3_Weeks_MA5_MA10_MA20_MA30_RongHe_Break_Platform: {
            // example: 300216 @ 20150421; 002040 @ 20150421
            // week platform
            boolean hasWeekFlatformStartVO = false;
            int minPlatformLen = 3;
            int maxPlatformLen = 10;
            for (int length = minPlatformLen; length <= maxPlatformLen; length++) {
                if (findLongPlatformBasedOnWeekDate(overWeekList.subList(overWeekList.size() - length,
                        overWeekList.size()))) {
                    hasWeekFlatformStartVO = true;
                    break;
                }
            }

            if (!hasWeekFlatformStartVO) {
                return false;
            }

            // RSV or KDJ gordon
            if (curSuperDayVO.rsvCorssType == CrossType.GORDON || curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON
                    || curSuperDayVO.kdjCorssType == CrossType.GORDON) {
                if (curSuperDayVO.priceVO.isKLineRed()) {
                    // check MA5, MA10,MA20,MA30 RongHe
                    if (MA5_MA10_MA20_MA30_Ronghe(pre1SuperDayVO)) {
                        if (MA5_MA10_MA20_MA30_Ronghe(pre2SuperDayVO)) {
                            if (MA5_MA10_MA20_MA30_Ronghe(curSuperDayVO)) {
                                return true;
                            }
                        }
                    }
                }
            }
            break;
        }

        case HengPan_2_Weeks_2_Days_Green_RSV_KDJ_Gordon_RongHe_XiangShang_Break_Platform: {
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

            // RSV or KDJ gordon
            if (curSuperDayVO.rsvCorssType == CrossType.GORDON || curSuperDayVO.kdjCorssType == CrossType.NEAR_GORDON
                    || curSuperDayVO.kdjCorssType == CrossType.GORDON) {
                // pre3 and pre2 green, pre1 and cur red
                // example: 600021 000875 at 2015-04-13, 000062 at 2015-02-27
                if (curSuperDayVO.priceVO.isKLineRed() && pre1SuperDayVO.priceVO.isKLineRed()
                        && pre2SuperDayVO.priceVO.isKLineGreen() && pre3SuperDayVO.priceVO.isKLineGreen()) {
                    if (curSuperDayVO.volumeIncreasePercent > 1 && pre2SuperDayVO.volumeIncreasePercent < 1) {
                        // close higher ma5 ma10
                        if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5
                                && curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10) {
                            // pre2, pre1, cur rongHe; cur xiangShang
                            if (this.MA5_MA10_Ronghe(pre1SuperDayVO) && this.MA5_MA10_Ronghe(pre2SuperDayVO)) {
                                if (this.MA5_MA10_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                // pre3, pre2 and pre1 green, cur red
                // example: 002260 2015-04-17
                if (curSuperDayVO.priceVO.isKLineRed() && pre1SuperDayVO.priceVO.isKLineGreen()
                        && pre2SuperDayVO.priceVO.isKLineGreen() && pre3SuperDayVO.priceVO.isKLineGreen()) {
                    if (curSuperDayVO.volumeIncreasePercent > 1 && pre1SuperDayVO.volumeIncreasePercent < 1
                            && pre2SuperDayVO.volumeIncreasePercent < 1) {
                        // close higher ma5 ma10
                        if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5
                                && curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10) {
                            // pre2, pre1, cur rongHe; cur xiangShang
                            if (this.MA5_MA10_Ronghe(pre1SuperDayVO) && this.MA5_MA10_Ronghe(pre2SuperDayVO)) {
                                if (this.MA5_MA10_Ronghe_XiangShang(curSuperDayVO, pre1SuperDayVO)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            break;
        }

        case HengPan_1_Weeks_4_Days_JiBu_Break_Platform: {
            // example 600175 @ 2015-04-21
            if ((curSuperWeekVO.kdjVO.k < curSuperWeekVO.kdjVO.d) || !this.isLatestKDJCrossGordon(overWeekList)) {
                // over all week KDJ must after Gordon
                return false;
            }

            String date = pre3SuperDayVO.priceVO.date;

            // startVO price increase at least 3%
            if (pre3SuperDayVO.priceVO.isKLineRed() && pre3SuperDayVO.priceVO.isKLineRed(3.0, 10.5)) {
                // System.out.println("debug 1 " + date);
                // pre2,pre1,cur is red cross
                if (pre2SuperDayVO.priceVO.isKLineRedCross() && pre1SuperDayVO.priceVO.isKLineRedCross()
                        && curSuperDayVO.priceVO.isKLineRedCross()) {
                    // System.out.println("debug 2 " + date);
                    // close is higher than higher
                    if (pre2SuperDayVO.priceVO.close <= pre1SuperDayVO.priceVO.close
                            && pre1SuperDayVO.priceVO.close <= curSuperDayVO.priceVO.close) {
                        // System.out.println("debug 4 " + date);
                        // open is higher than higher
                        if (pre2SuperDayVO.priceVO.open < pre1SuperDayVO.priceVO.open
                                && pre1SuperDayVO.priceVO.open < curSuperDayVO.priceVO.open) {
                            // pre2, pre1 low is higher ma5, cur low is
                            // lower ma5
                            if (pre2SuperDayVO.priceVO.low > pre2SuperDayVO.avgMA5
                                    && pre1SuperDayVO.priceVO.low > pre1SuperDayVO.avgMA5
                                    && curSuperDayVO.priceVO.low <= curSuperDayVO.avgMA5) {
                                // System.out.println("debug 6 " +
                                // date);
                                return true;
                            }
                        }
                    }
                }
            }

            break;
        }

        case HengPang_Ready_To_Break_Platform: {
            // return true if is a hengPan platform
            // day platform
            int minPlatformLen = 9;
            int maxPlatformLen = 30;
            boolean findPlatform = false;
            for (int length = minPlatformLen; length <= maxPlatformLen; length++) {
                if (findPlatformStartVO(overDayList.subList(overDayList.size() - length, overDayList.size()))) {
                    findPlatform = true;
                    break;
                }
            }

            // week platform
            minPlatformLen = 3;
            maxPlatformLen = 10;
            for (int length = minPlatformLen; length <= maxPlatformLen; length++) {
                if (findLongPlatformBasedOnWeekDate(overWeekList.subList(overWeekList.size() - length,
                        overWeekList.size()))) {
                    findPlatform = true;
                    break;
                }
            }

            if (!findPlatform)
                return false;

            // pre2, pre1 green, cur red
            if (curSuperDayVO.priceVO.isKLineRed() && pre1SuperDayVO.priceVO.isKLineGreen()
                    && pre2SuperDayVO.priceVO.isKLineGreen()) {
                if (pre1SuperDayVO.volumeIncreasePercent < 1) {
                    //if boll is ready to entry qiangQu
                    return true;
                }
            }
            return false;
        }
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

        case LaoYaZhui_TuPo_MA60_Day_Under_Zero_MACD_Gordon_KDJ_Gordon_Week_KDJ_Gordon: {
            // example: 601088 20150318, 601318 20150313
            // week macd < 0 && dif > 0
            if ((curSuperWeekVO.macdVO.macd < 0) && (curSuperWeekVO.macdVO.dif > 0)) {
                // week kdj gordon or rsv gordon
                if ((curSuperWeekVO.kdjCorssType == CrossType.GORDON)
                        || (curSuperWeekVO.kdjCorssType == CrossType.NEAR_GORDON)
                        || (curSuperWeekVO.rsvCorssType == CrossType.GORDON)) {
                    // week close price > week ma5, ma10
                    if ((curSuperWeekVO.priceVO.close >= curSuperWeekVO.avgMA5)
                            && (curSuperWeekVO.priceVO.close >= curSuperWeekVO.avgMA10)) {
                        // week is duo tou
                        if ((curSuperWeekVO.avgMA5 >= curSuperWeekVO.avgMA20)
                                && (curSuperWeekVO.avgMA10 >= curSuperWeekVO.avgMA20)
                                && (curSuperWeekVO.avgMA20 >= curSuperWeekVO.avgMA30)) {
                            // day macd is after gordon and macd > 0
                            if (curSuperDayVO.macdVO.macd > 0) {
                                // day kdj is after gordon and k > D
                                if (curSuperDayVO.kdjVO.k > curSuperDayVO.kdjVO.d) {
                                    // day ma60 > ma5 ma10, ma20, ma30
                                    if ((curSuperDayVO.avgMA60 >= curSuperDayVO.avgMA5)
                                            && (curSuperDayVO.avgMA60 >= curSuperDayVO.avgMA10)
                                            && (curSuperDayVO.avgMA60 >= curSuperDayVO.avgMA20)
                                            && (curSuperDayVO.avgMA60 >= curSuperDayVO.avgMA30)) {
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
        case ShenXian_Two_Gordons:
            // after H1 corss H2 and then H1 corss H3
            if (curSuperDayVO.shenXianVO.h1 > curSuperDayVO.shenXianVO.h2) {
                if (curSuperDayVO.shenXianCorssType13 == CrossType.GORDON) {
                    return true;
                }
            }
            break;

        case GaoSongZhuan_TianQuan:
            // 高送转，填权
            if (curSuperDayVO.priceVO.lastClose != 0 && curSuperDayVO.priceVO.lastClose != pre1SuperDayVO.priceVO.close) {
                return true;
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

    // to check if the list is a platform
    public boolean findLongPlatformBasedOnWeekDate(List<StockSuperVO> overWeekList) {
        // example: 300216 @ 20150421; 002040 @ 20150421
        // at least 5 weeks data
        // the first week is a big red K line,
        // J is much higher (>80), MACD bigger 0;
        // then ~5 week hengPan; KDJ dead find;
        // the continue high and low is between the first K line
        StockSuperVO startVO = overWeekList.get(0);
        StockSuperVO endVO = overWeekList.get(overWeekList.size() - 1);

        String Sdate = startVO.priceVO.date;
        String Edate = endVO.priceVO.date;
        // System.out.println("debug 1 " + Sdate + " ~ " + Edate + " " +
        // startVO.kdjVO);

        if (startVO.kdjVO.j < 80)
            return false;

        // System.out.println("debug 2 " + Sdate + " ~ " + Edate);

        if (startVO.macdVO.macd < 0)
            return false;

        // System.out.println("debug 3 " + Sdate + " ~ " + Edate);

        double startPriceIncrease = ((startVO.priceVO.close - startVO.priceVO.lastClose) * 100.0)
                / startVO.priceVO.lastClose;

        double avgClose = 0;
        boolean findKDJDead = false;
        double maxKDJ_K = 0;
        double minKDJ_K = 100;

        if (startPriceIncrease < 12) {
            // System.out.println("debug 4 " + Sdate + " ~ " + Edate);
            return false;
        }

        for (int i = 1; i < overWeekList.size(); i++) {
            StockSuperVO vo = overWeekList.get(i);
            double priceIncrease = ((vo.priceVO.close - vo.priceVO.lastClose) * 100.0) / vo.priceVO.lastClose;

            // if next week find one priceIncrease is bigger then startVO,
            // then not the platform
            if (priceIncrease > startPriceIncrease) {
                // System.out.println("debug 4 " + Sdate + " ~ " + Edate);
                return false;
            }

            // if next week find one high is greater since platform
            // startVO.hight, then not the platform
            if (vo.priceVO.high > startVO.priceVO.high) {
                // System.out.println("debug 5 " + Sdate + " ~ " + Edate);
                return false;
            }

            // if next week find one low is less than the platform
            // startVO.open or less then ma20
            if (vo.priceVO.low < startVO.priceVO.low) {
                // System.out.println("debug 6 " + Sdate + " ~ " + Edate);
                return false;
            }

            if (vo.kdjCorssType == CrossType.DEAD) {
                // System.out.println("debug 7 " + Sdate + " ~ " + Edate);
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
            // System.out.println("debug 8 " + Sdate + " ~ " + Edate);
            return false;
        }

        // max KDJ_K and min KDJ_K must between 15%
        if ((maxKDJ_K - minKDJ_K) / minKDJ_K * 100 >= 15) {
            // System.out.println("debug 9 " + Sdate + " ~ " + Edate);
            return false;
        }

        avgClose = avgClose / (overWeekList.size() - 1);
        // next avg close is greater than the middle platform startVO.open +
        // close / 2
        if (avgClose < ((startVO.priceVO.open + startVO.priceVO.close) / 2)) {
            // System.out.println("debug 10 " + Sdate + " ~ " + Edate);
            return false;
        }
        // System.out.println("debug 11 " + Sdate + " ~ " + Edate);
        return true;
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
        if (((dif / min) * 100) < 4.0) {
            return true;
        }
        return false;
    }

    private boolean MA5_MA10_Ronghe_XiangShang(StockSuperVO curSuperDayVO, StockSuperVO pre1SuperDayVO) {
        // rongHe
        if (!MA5_MA10_Ronghe(curSuperDayVO))
            return false;
        // xiangShang
        if ((curSuperDayVO.avgMA5 >= pre1SuperDayVO.avgMA5) && (curSuperDayVO.avgMA10 >= pre1SuperDayVO.avgMA10)
                && (curSuperDayVO.avgMA20 >= pre1SuperDayVO.avgMA20)
                && (curSuperDayVO.avgMA30 >= pre1SuperDayVO.avgMA30)) {
            return true;
        }

        return false;
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

    private boolean close_Higher_MA5_MA10(StockSuperVO curSuperDayVO, StockSuperVO pre1SuperDayVO) {

        // close higher than ma5 and ma10
        if (curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA5 && curSuperDayVO.priceVO.close >= curSuperDayVO.avgMA10) {
            return true;
        }

        return false;
    }

    private boolean close_Higher_N_Percent_Than_LastClose(StockSuperVO curSuperDayVO, double increasePercent) {

        // close higher N% than lastClose
        if ((curSuperDayVO.priceVO.close - curSuperDayVO.priceVO.lastClose) * 100 / curSuperDayVO.priceVO.lastClose >= increasePercent) {
            return true;
        }

        return false;
    }

    private boolean close_Lower_N_Percent_Than_LastClose(StockSuperVO curSuperDayVO, double increasePercent) {

        // close higher N% than lastClose
        if ((curSuperDayVO.priceVO.close - curSuperDayVO.priceVO.lastClose) * 100 / curSuperDayVO.priceVO.lastClose < increasePercent) {
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
}
