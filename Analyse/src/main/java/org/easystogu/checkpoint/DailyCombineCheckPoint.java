package org.easystogu.checkpoint;

import org.easystogu.config.FileConfigurationService;
import org.easystogu.utils.SellPointType;
import org.easystogu.utils.Strings;

public enum DailyCombineCheckPoint {
    HengPang_Ready_To_Break_Platform_MA30_Support_MA_RongHe_XiangShang(SellPointType.KDJ_Dead, 19, 19.0), HengPang_Ready_To_Break_Platform_MA20_Support_MA_RongHe_XiangShang(
            SellPointType.KDJ_Dead, 447, 11.7), HengPang_Ready_To_Break_Platform_BollUp_BollXueShi2_Dn_Gordon(
            SellPointType.KDJ_Dead, 452, 10.34), DuoTou_HuiTiao_MA30_Support_MA_RongHe_XiangShang(
            SellPointType.KDJ_Dead, 142, 11.46), DuoTou_HuiTiao_MA20_Support_MA_RongHe_XiangShang(
            SellPointType.KDJ_Dead, 2561, 9.46), HengPan_3_Weeks_MA_RongHe_Break_Platform_Orig(SellPointType.KDJ_Dead,
            521, 10.91), HengPan_3_Weeks_MA_RongHe_Break_Platform(SellPointType.KDJ_Dead, 1419, 9.22), HengPan_2_Weeks_MA_RongHe_XiangShang_Break_Platform(
            SellPointType.KDJ_Dead, 665, 8.63), HengPang_Ready_To_Break_Platform_KDJ_Gordon(SellPointType.KDJ_Dead,
            12442, 8.45), HengPang_Ready_To_Break_Platform_MACD_Gordon_Week_KDJ_Gordon(SellPointType.KDJ_Dead, 326,
            10.45), Close_Higher_BollUp_BollXueShi2_Dn_Gordon(SellPointType.KDJ_Dead, 17000, 8.87), MACD_Gordon(
            SellPointType.MACD_Dead, 99400, 10.6), KDJ_Gordon(SellPointType.KDJ_Dead, 210114, 6.0), ShenXian_Gordon(
            SellPointType.ShenXian_Dead, 72577, 12.75 - 12.75), ShenXian_Two_Gordons(SellPointType.KDJ_Dead, 25835,
            6.0 - 6.0), BollXueShi2_Dn_Gordon(SellPointType.KDJ_Dead, 6685, 9.67), MACD_KDJ_Gordon_3_Days_Red_MA_Ronghe_XiangShang(
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
            SellPointType.KDJ_Dead, 99, 7.96), YiMengBS_Gordon(SellPointType.KDJ_Dead, 125505, 7.66), YiMengBS_KDJ_Gordon(
            SellPointType.KDJ_Dead, 45142, 8.29);

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
        return super.toString() + "(" + this.sellPointType + ", " + this.sampleMeet + ", " + this.earnPercent + ")";
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

    public static void main(String[] args) {
        FileConfigurationService config = FileConfigurationService.getInstance();
        double minEarnPercent = config.getDouble("minEarnPercent_Select_CheckPoint");
        for (DailyCombineCheckPoint checkPoint : DailyCombineCheckPoint.values()) {
            if (checkPoint.getSampleMeet() < 10000 && checkPoint.getEarnPercent() >= minEarnPercent) {
                System.out.println(checkPoint);
            }
        }
    }
}
