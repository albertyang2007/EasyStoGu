package org.easystogu.checkpoint;

public enum DailyCombineCheckPoint {
    MACD_KDJ_Gordon_3_Days_Red("Macd & KDJ Gordon, 3 days Red, Volume bigger", 2184, 7.73), MACD_KDJ_Gordon_Pre_3_Days_Green(
            "Macd & KDJ Gordon, Previous 3 days Green, Volume smaller", 142, 8.50), KDJ_Gordon_3_days_Red(
            "KDJ Gordon, 3 days Red, Volume bigger", 15187, 6.81), KDJ_Gordon_Pre_3_Days_Green(
            "KDJ Gordon and Previous 3 days Green", 2048, 6.60), KDJ_Green_Gordon_Pre_2_Days_Red(
            "KDJ Green Gordon and Previous 2 days Red", 624, 8.01), KDJ_Gordon_High_MA5_MA10_BOLL(
            "KDJ Gordon, Close than MA5 & MA10 and between UP and MB, near MB", 6743, 7.68), MACD_KDJ_Gordon_High_MA5_MA10_BOLL(
            "MACD & KDJ Gordon, Close than MA5 & MA10 and between UP and MB, near MB", 1042, 7.73), KDJ_Near_Gordon_High_MA5_MA10_BOLL(
            "KDJ Near Gordon, Close than MA5 & MA10 and between UP and MB, near MB", 1446, 7.61), RSV_Gordon_High_MA5_MA10_BOLL(
            "RSV Gordon, Close than MA5 & MA10 and between UP and MB, near MB", 13955, 7.67), MACD_KDJ_Gordon_3_Days_Red_High_MA5_MA10_BOLL(
            "Macd & KDJ Gordon, 3 days Red, Volume bigger, Close than MA5 & MA10 and between UP and MB, near MB", 133,
            8.48), KDJ_Green_Gordon_Pre_2_Days_Red_High_MA5_MA10_BOLL(
            "KDJ Green Gordon and Previous 2 days Red, , Close than MA5 & MA10 and between UP and MB, near MB", 6, 8.8), Day_MACD_KDJ_Gordon_Week_KDJ_Gordon(
            "", 5259, 6.91), Day_MACD_KDJ_Gordon_Week_MACD_Gordon("", 458, 6.90), Phase1_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_KDJ_Zero(
            "", 0, 0), Phase2_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_RSV_Gordon(
            "First MACD Gordon is under zero, now MACD is Dead above zero, but RSV is Gordon", 10870, 7.5), Phase3_Previous_Under_Zero_MACD_Gordon_Now_MACD_Dead_KDJ_Gordon(
            "First MACD Gordon is under zero, now MACD is Dead above zero, but KDJ is Gordon", 8631, 6.4), Phase4_Previous_Under_Zero_MACD_Gordon_Now_MACD_Gordon_Volume_Bigger(
            "Previous MACD Gordon is under 0 and current MACD Gordon is uper 0, Volume is much bigger", 3505, 5.97), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA20_Support(
            "Duo Tou, MA5<=MA10, MA10>=MA20>=MA30, pre2 pre3 two days green, pre1 and today red, pre1 low < ma20, pre1 close > ma20, pre 1 KDJ J is low 10, now RSV gordon",
            675, 7.35), DuoTou_Pre_2_Days_Green_Red_KDJ_Zero_MA30_Support(
            "Duo Tou, MA5<=MA10, MA10>=MA20>=MA30, pre2 pre3 two days green, pre1 and today red, pre1 low < ma20, pre1 close > ma20, pre 1 KDJ J is low 10, now RSV gordon",
            1105, 8.14), DuoTou_MA5_Wait_MA10_RSV_Gordon_Break_Platform(
            "DuoTou, MA5 wait MA10, RSV Gordon, Pre1 is red, pre2 and pre3 is green, MACD is near 0, Close than MA5 & MA10 and between UP and MB, near MB",
            441, 6.76), Huge_Volume_Increase_3X3_Price_Higher_All_MA120(
            "Huge Volume Increase in days and price higher than MA30", 98, 9.22), MACD_Gordon_Volume_And_Price_Highest_In_MA120(
            "Volume and Price is the highest in half year(ma120)", 912, 8.01), Huge_Volume_Increase_Price_Higher_MA120_Previous_Lower_MA120(
            "Volume is biggest in 120 days and price higher than ma120, pre1 day low ma120", 114, 9.29), Week_Volume_Increase_Price_4_Plus(
            "Week Volume increase 4 plus", 0, 0.0);

    private String condition;
    // history summary that meet the condiction
    private int sampleMeet;
    // �����ʷͳ�Ƴ��������ӯ��ٷֱ�
    private double earnPercent;

    private DailyCombineCheckPoint() {

    }

    private DailyCombineCheckPoint(String condition, int sampleMeet, double earnPercent) {
        this.condition = condition;
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
        return super.toString() + "(" + this.sampleMeet + ", " + this.earnPercent + ")";
    }
}
