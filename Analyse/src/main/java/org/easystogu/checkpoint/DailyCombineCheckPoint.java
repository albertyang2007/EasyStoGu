package org.easystogu.checkpoint;

import org.easystogu.config.ConfigurationService;
import org.easystogu.config.DBConfigurationService;
import org.easystogu.utils.SellPointType;
import org.easystogu.utils.Strings;

public enum DailyCombineCheckPoint {
    MACD_Gordon(SellPointType.MACD_Dead, 0, 0), MACD_Dead(SellPointType.MACD_Dead, 0, 0), KDJ_Gordon(
            SellPointType.KDJ_Dead, 0, 0), RSV_Gordon(SellPointType.KDJ_Dead, 0, 0), ShenXian_Gordon(
            SellPointType.ShenXian_Dead, 0, 0), ShenXian_Dead(SellPointType.ShenXian_Dead, 0, 0), WEEK_MACD_Gordon(
            SellPointType.MACD_Dead, 0, 0), WEEK_KDJ_Gordon(SellPointType.KDJ_Dead, 0, 0), WEEK_ShenXian_Gordon(
            SellPointType.ShenXian_Dead, 0, 0), Mai1Mai2_1_Gordon(SellPointType.KDJ_Dead, 0, 0), WEEK_Mai1Mai2_1_Gordon(
            SellPointType.KDJ_Dead, 0, 0), Mai1Mai2_2_Gordon(SellPointType.KDJ_Dead, 0, 0), WEEK_Mai1Mai2_2_Gordon(
            SellPointType.KDJ_Dead, 0, 0), YiMengBS_Gordon(SellPointType.KDJ_Dead, 0, 0), WEEK_YiMengBS_Gordon(
            SellPointType.KDJ_Dead, 0, 0), SuoLiang_HuiTiao(SellPointType.KDJ_Dead, 0, 0), PlatForm(
            SellPointType.KDJ_Dead, 0, 0), WEEK_PlatForm(SellPointType.KDJ_Dead, 0, 0), RongHe_XiangShang(
            SellPointType.KDJ_Dead, 0, 0), HengPang_Ready_To_Break_Platform_MA30_Support_MA_RongHe_XiangShang(
            SellPointType.KDJ_Dead, 19, 19.0), HengPang_Ready_To_Break_Platform_MA20_Support_MA_RongHe_XiangShang(
            SellPointType.KDJ_Dead, 447, 11.7), HengPang_Ready_To_Break_Platform_BollUp_BollXueShi2_Dn_Gordon(
            SellPointType.KDJ_Dead, 452, 10.34), DuoTou_HuiTiao_MA30_Support_MA_RongHe_XiangShang(
            SellPointType.KDJ_Dead, 142, 11.46), DuoTou_HuiTiao_MA20_Support_MA_RongHe_XiangShang(
            SellPointType.KDJ_Dead, 2561, 9.46), HengPan_2_Weeks_MA_RongHe_Break_Platform(SellPointType.KDJ_Dead, 665,
            8.63), HengPan_3_Weeks_MA_RongHe_Break_Platform(SellPointType.KDJ_Dead, 1419, 9.22), HengPan_4_Weeks_MA_RongHe_Break_Platform(
            SellPointType.KDJ_Dead, 1419, 9.22), HengPang_Ready_To_Break_Platform_KDJ_Gordon(SellPointType.KDJ_Dead,
            12442, 8.45), HengPang_Ready_To_Break_Platform_MACD_Gordon_Week_KDJ_Gordon(SellPointType.KDJ_Dead, 326,
            10.45), Close_Higher_BollUp_BollXueShi2_Dn_Gordon(SellPointType.KDJ_Dead, 17000, 8.87), ShenXian_Two_Gordons(
            SellPointType.KDJ_Dead, 25835, 6.0 - 6.0), BollXueShi2_Dn_Gordon(SellPointType.KDJ_Dead, 6685, 9.67), MACD_KDJ_Gordon_3_Days_Red_MA_Ronghe_XiangShang(
            SellPointType.KDJ_Dead, 895, 8.55), MACD_KDJ_Gordon_3_Days_Red_High_MA5_MA10_BOLL(SellPointType.KDJ_Dead,
            43, 8.9), Phase2_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_RSV_KDJ_Gordon(SellPointType.KDJ_Dead, 452,
            8.27), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support(SellPointType.KDJ_Dead, 1058, 8.56), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support_MA_RongHe_XiangShang(
            SellPointType.KDJ_Dead, 77, 9.13), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA20_Support_MA_RongHe_XiangShang(
            SellPointType.KDJ_Dead, 146, 8.8), HengPang_7_Days_Ready_To_Break_Platform(SellPointType.KDJ_Dead, 1132,
            8.07), Day_Week_Mai1Mai2_Mai2_Grodon(SellPointType.KDJ_Dead, 15902, 7.24), Day_Week_Mai1Mai2_Mai2_Day_ShenXian_Grodon(
            SellPointType.KDJ_Dead, 719, 11.34), Day_Mai1Mai2_Mai1_ShenXian_Grodon(SellPointType.KDJ_Dead, 7603, 9.11), Day_Mai1Mai2_Mai2_ShenXian_Grodon(
            SellPointType.KDJ_Dead, 19446, 7.7), Day_Week_Mai1Mai2_Mai1_Day_ShenXian_Grodon(SellPointType.KDJ_Dead,
            320, 7.56), DaDie_KDJ_Gordon_Twice_DiWei_Gordon(SellPointType.KDJ_Dead, 352, 6.80), Day_ShenXian_Gordon_ZhuliJinChu_Gordon(
            SellPointType.KDJ_Dead, 11128, 8.21), Day_Mai2_ShenXian_ZhuliJinChu_Gordon_Week_Mai2_Gordon(
            SellPointType.KDJ_Dead, 2086, 9.55), Day_Mai1_ShenXian_ZhuliJinChu_Gordon(SellPointType.KDJ_Dead, 1514,
            8.76), YiYang_Cross_4K_Lines(SellPointType.KDJ_Dead, 10000, 8.50), SuoLiang_HuiTiao_ShenXiao_Gordon(
            SellPointType.KDJ_Dead, 135, 10.77), YiMengBS_KDJ_Gordon(SellPointType.KDJ_Dead, 45142, 8.29), YiMengBS_KDJ_Gordon_SuoLiang_HuiTiao(
            SellPointType.KDJ_Dead, 71, 28.0), Many_ZhangTing_Then_DieTing(SellPointType.KDJ_Dead, 18, 15.09), Continue_ZiJinLiu_DDX_RED_KDJ_Gorden(
            SellPointType.KDJ_Dead, 0, 99), Trend_PhaseI_GuanCha(SellPointType.KDJ_Dead, 0, 99.0), Trend_PhaseII_JianCang(
            SellPointType.KDJ_Dead, 0, 99.0), Trend_PhaseIII_ChiGu(SellPointType.KDJ_Dead, 0, 99.0), Trend_PhaseVI_JianCang(
            SellPointType.KDJ_Dead, 0, 99.0), LuZao_GordonO_MA43_DownCross_MA86(SellPointType.KDJ_Dead, 0, 99.0), LuZao_GordonI_MA19_UpCross_MA43(
            SellPointType.KDJ_Dead, 0, 99.0), LuZao_GordonII_MA19_UpCross_MA86(SellPointType.KDJ_Dead, 0, 99.0), LuZao_DeadI_MA43_UpCross_MA86(
            SellPointType.KDJ_Dead, 0, 99.0), LuZao_DeadII_MA19_DownCross_MA43(SellPointType.KDJ_Dead, 0, 99.0), QSDD_Top_Area(
            SellPointType.KDJ_Dead, 0, 99.0), QSDD_Bottom_Area(SellPointType.KDJ_Dead, 11848, 10.8), QSDD_Bottom_Gordon(
            SellPointType.KDJ_Dead, 5055, 12.0), WR_Bottom_Area(SellPointType.KDJ_Dead, 0, 0), WR_Bottom_Gordon(
            SellPointType.KDJ_Dead, 74303, 9.56),WR_Top_Area(SellPointType.KDJ_Dead, 0, 0),WR_Ready_To_ZhangTing(SellPointType.KDJ_Dead, 0, 0), MACD_DI_BeiLi(
            SellPointType.KDJ_Dead, 0, 99.0),WR_4_Days_SameValue_XianShang(SellPointType.KDJ_Dead,2923,8.35), 
    		LuZao_KDJ_Gordon_TiaoKongGaoKai(SellPointType.KDJ_Dead, 0, 99.0),
    		LuZao_PhaseII_MACD_WEEK_GORDON_MACD_DAY_DIF_CROSS_0(SellPointType.KDJ_Dead, 0, 99.0),
    		LuZao_PhaseIII_MACD_WEEK_GORDON_MACD_DAY_DIF_CROSS_0(SellPointType.KDJ_Dead, 0, 99.0),
    		LuZao_PhaseII_MACD_WEEK_GORDON_KDJ_WEEK_GORDON(SellPointType.KDJ_Dead, 0, 99.0),
    		LuZao_PhaseIII_MACD_WEEK_GORDON_KDJ_WEEK_GORDON(SellPointType.KDJ_Dead, 0, 99.0),
    		MACD_TWICE_GORDON_W_Botton_MACD_DI_BEILI(SellPointType.KDJ_Dead, 0, 99.0), 
    		MACD_TWICE_GORDON_W_Botton_TiaoKong_ZhanShang_Bull(SellPointType.KDJ_Dead, 0, 99.0),
    		High_Price_Digit_In_Order(SellPointType.KDJ_Dead, 0, 99.0),
    		Low_Price_Digit_In_Order(SellPointType.KDJ_Dead, 0, 99.0),
    		KDJ_Over_Buy(SellPointType.KDJ_Dead, 0, 99.0),
    		KDJ_Over_Sell(SellPointType.KDJ_Dead, 0, 99.0);

    private ConfigurationService config = DBConfigurationService.getInstance();
    private double minEarnPercent = config.getDouble("minEarnPercent_Select_CheckPoint");

    private String condition;
    // history summary that meet the condiction
    private int sampleMeet;
    // �����ʷͳ�Ƴ��������ӯ��ٷֱ�
    private double earnPercent;
    // sell point type
    private SellPointType sellPointType = SellPointType.KDJ_Dead;
    // for merge
    private String mergeName = "";

    private DailyCombineCheckPoint() {
    }

    private DailyCombineCheckPoint(SellPointType sellPointType, int sampleMeet, double earnPercent) {
        this.sellPointType = sellPointType;
        this.condition = "N/A";
        this.sampleMeet = sampleMeet;
        this.earnPercent = earnPercent;
    }

    private DailyCombineCheckPoint(String condition, int sampleMeet, double earnPercent) {
        this.condition = condition;
        this.sampleMeet = sampleMeet;
        this.earnPercent = earnPercent;
    }

    private DailyCombineCheckPoint(int sampleMeet, double earnPercent) {
        this.condition = "N/A";
        this.sampleMeet = sampleMeet;
        this.earnPercent = earnPercent;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getEarnPercent() {
        return this.earnPercent;
    }

    public int getSampleMeet() {
        return sampleMeet;
    }

    public void setSampleMeet(int sampleMeet) {
        this.sampleMeet = sampleMeet;
    }

    public SellPointType getSellPointType() {
        return sellPointType;
    }

    public String toStringWithDetails() {
        return super.toString() + "(" + this.sampleMeet + ", " + this.earnPercent + ")";
    }

    @Override
    public String toString() {
        if (Strings.isNotEmpty(mergeName))
            return mergeName;
        return super.toString();
    }

    public static DailyCombineCheckPoint getCheckPointByName(String cpName) {
        for (DailyCombineCheckPoint checkPoint : DailyCombineCheckPoint.values()) {
            if (checkPoint.toString().equals(cpName)) {
                return checkPoint;
            }
        }
        return null;
    }

    public boolean isSatisfyMinEarnPercent() {
        return this.getEarnPercent() >= minEarnPercent ? true : false;
    }

    public static void main(String[] args) {
    	ConfigurationService config = DBConfigurationService.getInstance();
        double minEarnPercent = config.getDouble("minEarnPercent_Select_CheckPoint");
        for (DailyCombineCheckPoint checkPoint : DailyCombineCheckPoint.values()) {
            if (checkPoint.getSampleMeet() < 10000 && checkPoint.getEarnPercent() >= 10) {
                System.out.println(checkPoint.toStringWithDetails());
            }
        }
    }
}
