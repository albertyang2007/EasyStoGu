package org.easystogu.db.vo.table;

import org.easystogu.utils.Strings;

//主力净流入
public class ZhuLiJingLiuRuVO {

	public int rate;// 当日资金流排名
	public String stockId;
	public String name;
	public String incPer;
	public String date;
	public double majorNetPer;
	public double price;

	public ZhuLiJingLiuRuVO(String id, String date) {
		this.stockId = id;
		this.date = date;
	}

	public ZhuLiJingLiuRuVO(String id) {
		this.stockId = id;
	}

	public ZhuLiJingLiuRuVO() {
	}

	public String toNetInString() {
		return "￥ [" + this.rate + "," + this.incPer + "," + this.majorNetPer + "]";
	}

	public boolean isValidated() {
		if (!Strings.isDateValidate(date))
			return false;
		if (this.majorNetPer == 0)
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getMajorNetPer() {
		return majorNetPer;
	}

	public void setMajorNetPer(double majorNetPer) {
		this.majorNetPer = majorNetPer;
	}

}
