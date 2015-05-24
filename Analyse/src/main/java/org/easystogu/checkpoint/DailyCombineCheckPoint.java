package org.easystogu.checkpoint;

import org.easystogu.utils.SellPointType;
import org.easystogu.utils.Strings;

public enum DailyCombineCheckPoint {
	MACD_Gordon(SellPointType.MACD_Dead, 99400, 10.6), KDJ_Gordon(SellPointType.KDJ_Dead, 210114, 6.0), ShenXian_Gordon(
			SellPointType.ShenXian_Dead, 72577, 12.75 - 12.75), ShenXian_Two_Gordons(SellPointType.KDJ_Dead, 25835,
			6.0 - 6.0), BollXueShi2_Dn_Gordon(SellPointType.KDJ_Dead, 6685, 9.67), MACD_KDJ_Gordon_3_Days_Red_MA_Ronghe_XiangShang(
			SellPointType.KDJ_Dead, 895, 8.55), MACD_KDJ_Gordon_3_Days_Red_High_MA5_MA10_BOLL(SellPointType.KDJ_Dead,
			43, 8.9), Phase2_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_RSV_KDJ_Gordon(SellPointType.KDJ_Dead, 452,
			8.27), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support(SellPointType.KDJ_Dead, 1058, 8.56), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 77, 9.13), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA20_Support_MA_RongHe_XiangShang(
			SellPointType.KDJ_Dead, 146, 8.8), DuoTou_HuiTiao_MA30_Support_MA_RongHe_XiangShang(SellPointType.KDJ_Dead,
			142, 11.46), DuoTou_HuiTiao_MA20_Support_MA_RongHe_XiangShang(SellPointType.KDJ_Dead, 2561, 9.46), HengPan_3_Weeks_MA_RongHe_Break_Platform_Orig(
			SellPointType.KDJ_Dead, 509, 10.00), HengPan_3_Weeks_MA_RongHe_Break_Platform(SellPointType.KDJ_Dead, 1625,
			8.75), HengPan_2_Weeks_MA_RongHe_XiangShang_Break_Platform(SellPointType.KDJ_Dead, 665, 8.63), HengPang_Ready_To_Break_Platform(
			SellPointType.KDJ_Dead, 4252, 9.57), Close_Higher_BollUp_BollXueShi2_Dn_Gordon(SellPointType.KDJ_Dead,
			17000, 8.87);

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
}
