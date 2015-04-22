package org.easystogu.checkpoint;

import org.easystogu.utils.SellPointType;

public enum DailyCombineCheckPoint {
	MACD_Gordon(SellPointType.MACD_Dead, 99400, 10.6 - 10.6), KDJ_Gordon(SellPointType.KDJ_Dead, 210114, 6.0 - 6.0), ShenXian_Gordon(
			SellPointType.ShenXian_Dead, 72577, 12.75 - 12.75), MACD_KDJ_Gordon_3_Days_Red_MA_Ronghe_XiangShang(
			SellPointType.KDJ_Dead, 955, 8.06), MACD_KDJ_Gordon_High_MA5_MA10_BOLL_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 375, 7.30), MACD_KDJ_Gordon_3_Days_Red_High_MA5_MA10_BOLL(SellPointType.KDJ_Dead,
			84, 10.7), Phase2_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_RSV_KDJ_Gordon(SellPointType.KDJ_Dead, 352,
			8.41), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support(SellPointType.KDJ_Dead, 1506, 8.0), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 64, 9.6), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA20_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 128, 8.69), DuoTou_HuiTiao_Boll_Lower_Support_MA30_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 105, 9.03), DuoTou_HuiTiao_Boll_MB_Support_MA20_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 2431, 8.28), HengPan_2_Weeks_2_Days_Green_RSV_KDJ_Gordon_RongHe_XiangShang_Break_Platform(
			SellPointType.KDJ_Dead, 500, 7.82), Huge_Volume_Increase_3X3_Price_Higher_All_MA120(SellPointType.KDJ_Dead,
			143, 8.0), MACD_Gordon_Volume_And_Price_Highest_In_MA90(SellPointType.KDJ_Dead, 1587, 8.58), Huge_Volume_Increase_Price_Higher_MA120_Previous_Lower_MA120(
			SellPointType.KDJ_Dead, 217, 8.48), LaoYaZhui_TuPo_MA60_Day_Under_Zero_MACD_Gordon_KDJ_Gordon_Week_KDJ_Gordon(
			SellPointType.KDJ_Dead, 364, 5.85), ShenXian_Two_Gordons(SellPointType.KDJ_Dead, 25835, 5.6);

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
