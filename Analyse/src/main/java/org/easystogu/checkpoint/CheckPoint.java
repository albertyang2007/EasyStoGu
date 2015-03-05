package org.easystogu.checkpoint;

public enum CheckPoint {
	MACD_Gordon("", 0), KDJ_Gordon("", 0), KDJ_Near_Gordon("", 0), Now_3_Days_Red("", 0), Pre_3_Days_Green("", 0), KDJ_Green_Gordon(
			"", 0), High_MA5_MA10("", 0), Pre_Low_MA5_MA10("", 0), BOLL_Between_MB_UP("", 0), Volume_Big_1("", 0), Volume_Big_2(
			"", 0), Volume_Big_3("", 0), Volume_Big_5("", 0);

	private String condition;
	// �����ʷͳ�Ƴ��������ӯ��ٷֱ�
	private double earnPercent;

	private CheckPoint(String condition, double earnPercent) {
		this.condition = condition;
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

	public String toStringWithEarnPercent() {
		return super.toString() + "(" + this.earnPercent + ")";
	}
}
