package org.easystogu.checkpoint;

public enum DailyCombineCheckPoint {
	MACD_Gordon("Simple MACD Gordon Buy Point", 0, 0), KDJ_Gordon(
			"Simple KDJ Gordon Buy Point", 0, 0), MACD_KDJ_Gordon_3_Days_Red_MA_Ronghe_XiangShang(
			"Macd & KDJ Gordon, 3 days Red, Volume bigger, MA rongHe xiangShang",
			1854, 7.83), MACD_KDJ_Gordon_Pre_3_Days_Green(
			"Macd & KDJ Gordon, Previous 3 days Green, Volume smaller", 218,
			7.00), KDJ_Gordon_3_days_Red(
			"KDJ Gordon, 3 days Red, Volume bigger", 21585, 6.52), KDJ_Gordon_Pre_3_Days_Green(
			"KDJ Gordon and Previous 3 days Green", 3025, 6.12), KDJ_Green_Gordon_Pre_2_Days_Red(
			"KDJ Green Gordon and Previous 2 days Red", 861, 6.76), KDJ_Gordon_High_MA5_MA10_BOLL(
			"KDJ Gordon, Close than MA5 & MA10 and between UP and MB, near MB",
			9575, 7.25), MACD_KDJ_Gordon_High_MA5_MA10_BOLL_MA_RongHe_XiangShang(
			"MACD & KDJ Gordon, Close than MA5 & MA10 and between UP and MB, near MB, MA rongHe XiangShang",
			715, 7.56), KDJ_Near_Gordon_High_MA5_MA10_BOLL(
			"KDJ Near Gordon, Close than MA5 & MA10 and between UP and MB, near MB",
			2091, 7.19), RSV_Gordon_High_MA5_MA10_BOLL(
			"RSV Gordon, Close than MA5 & MA10 and between UP and MB, near MB",
			19635, 7.17), MACD_KDJ_Gordon_3_Days_Red_High_MA5_MA10_BOLL(
			"Macd & KDJ Gordon, 3 days Red, Volume bigger, Close than MA5 & MA10 and between UP and MB, near MB",
			182, 8.25), KDJ_Green_Gordon_Pre_2_Days_Red_High_MA5_MA10_BOLL(
			"KDJ Green Gordon and Previous 2 days Red, , Close than MA5 & MA10 and between UP and MB, near MB",
			14, 13.46), Day_MACD_KDJ_Gordon_Week_KDJ_Gordon("", 3246, 6.11), Day_MACD_KDJ_Gordon_Week_MACD_Gordon(
			"", 4537, 6.56), Phase1_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_KDJ_Zero(
			"", 0, 0), Phase2_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_RSV_KDJ_Gordon(
			"First MACD Gordon is under zero, now MACD is Dead above zero, but RSV/KDJ is Gordon",
			515, 8.30), Phase3_Previous_Under_Zero_MACD_Gordon_Now_MACD_Gordon_Volume_Bigger(
			"Previous MACD Gordon is under 0 and current MACD Gordon is uper 0, Volume is much bigger",
			1615, 5.22), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA20_Support(
			"Duo Tou, MA5<=MA10, MA10>=MA20>=MA30, pre2 pre3 two days green, pre1 and today red, pre1 low < ma20, pre1 close > ma20, pre 1 KDJ J is low 10, now RSV gordon",
			1048, 7.27), DuoTou_Pre_2_Days_Green_Red_MA20_MA30_Support(
			"Duo Tou, MA5>MA10>MA20>MA30, Low <=MA30, Close>=MA20, near MA10",
			415, 5.72), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support(
			"Duo Tou, MA5<=MA10, MA10>=MA20>=MA30, pre2 pre3 two days green, pre1 and today red, pre1 low < ma20, pre1 close > ma20, pre 1 KDJ J is low 10, now RSV gordon",
			1600, 7.76), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support_MA_RongHe_XiangShang(
			"same as above with MA ronghe XiangShang", 154, 8.02), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA20_Support_MA_RongHe_XiangShang(
			"same as above with MA ronghe XiangShang", 283, 8.01), DuoTou_HuiTiao_Boll_Lower_Support_MA30_Support_MA_RongHe_XiangShang(
			"DuoTou huitiao, KDJ J zero, boll lower support, ma30 support, MA5,10,20,30 ronghe, MB support",
			121, 7.85), DuoTou_HuiTiao_Boll_MB_Support_MA20_Support_MA_RongHe_XiangShang(
			"DuoTou huitiao, boll mb support, ma20 support, MA5,10,20 ronghe, MB support",
			761, 6.83), DuoTou_HuiTiao_RSV_Gordon_MA10_Support_MA_RongHe_XiangShang(
			"DuoTou Huitiao, MA10 support, KDJ Dead, KDJ Gordon, MA5,MA10 ronghe xiangShang",
			0, 99), DuoTou_MA5_Wait_MA10_RSV_KDJ_Gordon_Break_Platform(
			"DuoTou, MA5 wait MA10, RSV Gordon, Pre1 is red, pre2 and pre3 is green, MACD is near 0, Close than MA5 & MA10 and between UP and MB, near MB, Why latest analyse only 6.92???",
			102, 6.92), DuoTou_MA5_Wait_MA10_RongHe(
			"MA5>MA10>MA20>MA30, Dif of MA5 and MA10 is little than little til MA10>MA5",
			315, 5.86), Huge_Volume_Increase_3X3_Price_Higher_All_MA120(
			"Huge Volume Increase in days and price higher than MA30", 168,
			7.81), MACD_Gordon_Volume_And_Price_Highest_In_MA90(
			"Volume and Price is the highest in half year(ma120)", 1622, 7.89), KDJ_Gordon_Volume_And_Price_Highest_In_MA90(
			"Volume and Price is the highest in half year(ma120)", 1928, 6.70), Huge_Volume_Increase_Price_Higher_MA120_Previous_Lower_MA120(
			"Volume is biggest in 120 days and price higher than ma120, pre1 day low ma120",
			221, 8.76), Volume_Increase_Higher_MA120(
			"The biggest volume in 120 days", 6859, 4.51), Previous_KDJ_MACD_Gordon_Now_Boll_Gordon_Week_RSV_Gordon_Boll_Gordon(
			"???Two low, do not select it!", 410, 2.9), KongTou_Day_KDJ_Pre3_Days_Dead_Week_KDJ_Dead(
			"Day KDJ Pre 3 days J 0, Week KDJ J 0", 44, 6.37);

	private String condition;
	// history summary that meet the condiction
	private int sampleMeet;
	// �����ʷͳ�Ƴ��������ӯ��ٷֱ�
	private double earnPercent;

	private DailyCombineCheckPoint() {

	}

	private DailyCombineCheckPoint(String condition, int sampleMeet,
			double earnPercent) {
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

	public String toStringWithDetails() {
		return super.toString() + "(" + this.sampleMeet + ", "
				+ this.earnPercent + ")";
	}
}
