package org.easystogu.db.vo.table;

import org.easystogu.utils.Strings;

//资金流入
public class ZiJinLiuVO {
	public static final String RealTime = "RealTime";// real time's zijinliu
	public static final String _1Day = "1_day";// current day's zijinliu
	public static final String _3Day = "3_day";// recent 3 day's zijinliu
	public static final String _5Day = "5_day";// recent 5 day's zijinliu
	public String _DayType = _1Day;

	public int rate;// 当日资金流排名
	public String stockId;
	public String name;
	public String curPrice;
	public String incPer;
	public String date;
	public double majorNetIn;
	public double majorNetPer;
	public double biggestNetIn;
	public double biggestNetPer;
	public double bigNetIn;
	public double bigNetPer;
	public double midNetIn;
	public double midNetPer;
	public double smallNetIn;
	public double smallNetPer;

	public ZiJinLiuVO(String id, String date) {
		this.stockId = id;
		this.date = date;
	}

	public ZiJinLiuVO(String id) {
		this.stockId = id;
	}

	public ZiJinLiuVO() {
	}

	public String toNetInString() {
		return "￥ [" + this.rate + "," + this.incPer + "," + this.majorNetIn + "," + this.biggestNetIn + ","
				+ this.bigNetIn + "," + this.midNetIn + "," + this.smallNetIn + "]";
	}

	public String toNetPerString() {
		return "￥ [" + this.rate + "," + this.incPer + "," + this.majorNetPer + "," + this.biggestNetPer + ","
				+ this.bigNetPer + "," + this.midNetPer + "," + this.smallNetPer + "]";
	}

	public boolean isValidated() {
		if (!Strings.isDateValidate(date))
			return false;
		if (this.majorNetIn == 0 && this.biggestNetIn == 0 && this.bigNetIn == 0 && this.midNetIn == 0
				&& this.smallNetIn == 0)
			return false;
		
		return true;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getMajorNetIn() {
		return majorNetIn;
	}

	public void setMajorNetIn(double majorNetIn) {
		this.majorNetIn = majorNetIn;
	}

	public double getMajorNetPer() {
		return majorNetPer;
	}

	public void setMajorNetPer(double majorNetPer) {
		this.majorNetPer = majorNetPer;
	}

	public double getBiggestNetIn() {
		return biggestNetIn;
	}

	public void setBiggestNetIn(double biggestNetIn) {
		this.biggestNetIn = biggestNetIn;
	}

	public double getBiggestNetPer() {
		return biggestNetPer;
	}

	public void setBiggestNetPer(double biggestNetPer) {
		this.biggestNetPer = biggestNetPer;
	}

	public double getBigNetIn() {
		return bigNetIn;
	}

	public void setBigNetIn(double bigNetIn) {
		this.bigNetIn = bigNetIn;
	}

	public double getBigNetPer() {
		return bigNetPer;
	}

	public void setBigNetPer(double bigNetPer) {
		this.bigNetPer = bigNetPer;
	}

	public double getMidNetIn() {
		return midNetIn;
	}

	public void setMidNetIn(double midNetIn) {
		this.midNetIn = midNetIn;
	}

	public double getMidNetPer() {
		return midNetPer;
	}

	public void setMidNetPer(double midNetPer) {
		this.midNetPer = midNetPer;
	}

	public double getSmallNetIn() {
		return smallNetIn;
	}

	public void setSmallNetIn(double smallNetIn) {
		this.smallNetIn = smallNetIn;
	}

	public double getSmallNetPer() {
		return smallNetPer;
	}

	public void setSmallNetPer(double smallNetPer) {
		this.smallNetPer = smallNetPer;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getIncPer() {
		return incPer;
	}

	public void setIncPer(String incPer) {
		this.incPer = incPer;
	}
}
