package org.easystogu.checkpoint;

import org.easystogu.utils.SellPointType;

public enum DailyCombineCheckPoint {
	MACD_Gordon(SellPointType.MACD_Dead, 99400, 10.6 - 10.6), KDJ_Gordon(SellPointType.KDJ_Dead, 210114, 6.0 - 6.0), ShenXian_Gordon(
			SellPointType.ShenXian_Dead, 72577, 12.75 - 12.75), MACD_KDJ_Gordon_3_Days_Red_MA_Ronghe_XiangShang(
			SellPointType.KDJ_Dead, 1009, 8.36), MACD_KDJ_Gordon_3_Days_Red_High_MA5_MA10_BOLL(SellPointType.KDJ_Dead,
			47, 9.0), Phase2_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_RSV_KDJ_Gordon(SellPointType.KDJ_Dead, 515,
			8.35), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support(SellPointType.KDJ_Dead, 1166, 8.3), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 75, 9.2), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA20_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 171, 8.7), DuoTou_HuiTiao_Boll_Lower_Support_MA30_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 113, 8.9), DuoTou_HuiTiao_Boll_MB_Support_MA20_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 2562, 8.14), HengPan_3_Weeks_MA5_MA10_MA20_MA30_RongHe_Break_Platform(
			SellPointType.KDJ_Dead, 121, 9.33), HengPan_2_Weeks_2_Days_Green_RSV_KDJ_Gordon_RongHe_XiangShang_Break_Platform(
			SellPointType.KDJ_Dead, 559, 8.07), Huge_Volume_Increase_3X3_Price_Higher_All_MA120(SellPointType.KDJ_Dead,
			146, 7.7), MACD_Gordon_Volume_And_Price_Highest_In_MA90(SellPointType.KDJ_Dead, 1617, 8.6), Huge_Volume_Increase_Price_Higher_MA120_Previous_Lower_MA120(
			SellPointType.KDJ_Dead, 211, 8.31), LaoYaZhui_TuPo_MA60_Day_Under_Zero_MACD_Gordon_KDJ_Gordon_Week_KDJ_Gordon(
			SellPointType.KDJ_Dead, 5408, 6.48), ShenXian_Two_Gordons(SellPointType.KDJ_Dead, 25835, 5.6);

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
