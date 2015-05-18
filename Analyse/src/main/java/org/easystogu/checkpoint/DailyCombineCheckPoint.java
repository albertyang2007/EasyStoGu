package org.easystogu.checkpoint;

import org.easystogu.utils.SellPointType;

public enum DailyCombineCheckPoint {
	MACD_Gordon(SellPointType.MACD_Dead, 99400, 10.6 - 10.6), KDJ_Gordon(SellPointType.KDJ_Dead, 210114, 6.0 - 6.0), ShenXian_Gordon(
			SellPointType.ShenXian_Dead, 72577, 12.75 - 12.75), MACD_KDJ_Gordon_3_Days_Red_MA_Ronghe_XiangShang(
			SellPointType.KDJ_Dead, 895, 8.55), MACD_KDJ_Gordon_3_Days_Red_High_MA5_MA10_BOLL(SellPointType.KDJ_Dead,
			43, 8.9), Phase2_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_RSV_KDJ_Gordon(SellPointType.KDJ_Dead, 452,
			8.27), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support(SellPointType.KDJ_Dead, 1058, 8.56), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 61, 9.7), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA20_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 146, 8.8), DuoTou_HuiTiao_Boll_Lower_Support_MA30_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 129, 11.15), DuoTou_HuiTiao_Boll_MB_Support_MA20_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 2561, 9.46), HengPan_3_Weeks_MA5_MA10_MA20_MA30_RongHe_Break_Platform(
			SellPointType.KDJ_Dead, 1829, 8.16), HengPan_2_Weeks_2_Days_Green_RSV_KDJ_Gordon_RongHe_XiangShang_Break_Platform(
			SellPointType.KDJ_Dead, 518, 8.62), ShenXian_Two_Gordons(SellPointType.KDJ_Dead, 25835, 5.6), HengPang_Ready_To_Break_Platform(
			SellPointType.KDJ_Dead, 4252, 9.57), LaoYaZhui_TuPo_MA60_Day_Under_Zero_MACD_Gordon_KDJ_Gordon_Week_KDJ_Gordon(
			SellPointType.KDJ_Dead, 3597, 6.9); 

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
