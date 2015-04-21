package org.easystogu.checkpoint;

import org.easystogu.utils.SellPointType;

public enum DailyCombineCheckPoint {
	MACD_Gordon(SellPointType.MACD_Dead, 99400, 10.6 - 10.6), KDJ_Gordon(SellPointType.KDJ_Dead, 210114, 6.0), ShenXian_Gordon(
			SellPointType.ShenXian_Dead, 72577, 12.75), MACD_KDJ_Gordon_3_Days_Red_MA_Ronghe_XiangShang(
			SellPointType.KDJ_Dead, 1064, 7.95), MACD_KDJ_Gordon_Pre_3_Days_Green(SellPointType.KDJ_Dead, 218, 7.00), KDJ_Gordon_3_days_Red(
			SellPointType.KDJ_Dead, 21585, 6.52), KDJ_Gordon_Pre_3_Days_Green(SellPointType.KDJ_Dead, 3025, 6.12), KDJ_Green_Gordon_Pre_2_Days_Red(
			SellPointType.KDJ_Dead, 861, 6.76), KDJ_Gordon_High_MA5_MA10_BOLL(SellPointType.KDJ_Dead, 9575, 7.25), MACD_KDJ_Gordon_High_MA5_MA10_BOLL_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 619, 7.43), KDJ_Near_Gordon_High_MA5_MA10_BOLL(SellPointType.KDJ_Dead, 2091, 7.19), RSV_Gordon_High_MA5_MA10_BOLL(
			SellPointType.KDJ_Dead, 19635, 7.17), MACD_KDJ_Gordon_3_Days_Red_High_MA5_MA10_BOLL(SellPointType.KDJ_Dead,
			182, 8.25), KDJ_Green_Gordon_Pre_2_Days_Red_High_MA5_MA10_BOLL(SellPointType.KDJ_Dead, 14, 13.46), Day_MACD_KDJ_Gordon_Week_KDJ_Gordon(
			SellPointType.KDJ_Dead, 3246, 6.11), Day_MACD_KDJ_Gordon_Week_MACD_Gordon("", 4537, 6.56), Phase1_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_KDJ_Zero(
			SellPointType.KDJ_Dead, 0, 0), Phase2_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_RSV_KDJ_Gordon(
			SellPointType.KDJ_Dead, 515, 8.30), Phase3_Previous_Under_Zero_MACD_Gordon_Now_MACD_Gordon_Volume_Bigger(
			SellPointType.KDJ_Dead, 1615, 5.22), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA20_Support(
			SellPointType.KDJ_Dead, 1048, 7.27), DuoTou_Pre_2_Days_Green_Red_MA20_MA30_Support(SellPointType.KDJ_Dead,
			415, 5.72), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support(SellPointType.KDJ_Dead, 1600, 7.95), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 115, 8.78), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA20_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 212, 8.35), DuoTou_HuiTiao_Boll_Lower_Support_MA30_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 102, 8.97), DuoTou_HuiTiao_Boll_MB_Support_MA20_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 2362, 8.02), DuoTou_HuiTiao_RSV_Gordon_MA10_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 394, 7.02), DuoTou_MA5_Wait_MA10_RSV_KDJ_Gordon_Break_Platform(
			SellPointType.KDJ_Dead, 102, 6.92), DuoTou_MA5_Wait_MA10_RongHe_Break_Platform(SellPointType.KDJ_Dead,
			7094, 6.8), HengPan_2_Weeks_2_Days_Green_RSV_KDJ_Gordon_RongHe_XiangShang_Break_Platform(
			SellPointType.KDJ_Dead, 370, 7.8), DuoTou_MA5_Wait_MA10_RongHe(SellPointType.KDJ_Dead, 315, 5.86), Huge_Volume_Increase_3X3_Price_Higher_All_MA120(
			SellPointType.KDJ_Dead, 168, 7.81), DuoTou_HuiTiao_MA20_Support_KDJ_0_YangBaoYin(SellPointType.KDJ_Dead,
			12, 9.2), MACD_Gordon_Volume_And_Price_Highest_In_MA90(SellPointType.KDJ_Dead, 1622, 8.14), KDJ_Gordon_Volume_And_Price_Highest_In_MA90(
			SellPointType.KDJ_Dead, 1928, 6.70), Huge_Volume_Increase_Price_Higher_MA120_Previous_Lower_MA120(
			SellPointType.KDJ_Dead, 221, 8.86), Volume_Increase_Higher_MA120(SellPointType.KDJ_Dead, 6859, 4.51), Previous_KDJ_MACD_Gordon_Now_Boll_Gordon_Week_RSV_Gordon_Boll_Gordon(
			SellPointType.KDJ_Dead, 410, 2.9), KongTou_Day_KDJ_Pre3_Days_Dead_Week_KDJ_Dead(SellPointType.KDJ_Dead, 44,
			6.37), LaoYaZhui_TuPo_MA60_Day_Under_Zero_MACD_Gordon_KDJ_Gordon_Week_KDJ_Gordon(SellPointType.MACD_Dead,
			1661, 8.59), TiXing_SuoLiang_LaSheng_Close_Price_Higher_Volume_Lower(SellPointType.KDJ_Dead, 225, 7.15), DuoTou_Close_And_MA5_Higher_MA120(
			SellPointType.KDJ_Dead, 1012, 5.26), ShenXian_Two_Gordons(SellPointType.ShenXian_Dead, 23155, 13.35);

	private String condition;
	// history summary that meet the condiction
	private int sampleMeet;
	// �����ʷͳ�Ƴ��������ӯ��ٷֱ�
	private double earnPercent;
	// sell point type
	private SellPointType sellPointType = SellPointType.KDJ_Dead;

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
}
