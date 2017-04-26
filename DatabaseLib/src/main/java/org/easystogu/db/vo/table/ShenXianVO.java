package org.easystogu.db.vo.table;

public class ShenXianVO {
	public String stockId;
	public String name;
	public String date;
	public double h1;
	public double h2;
	public double h3;
	public double hc5;
	public double hc6;

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getH1() {
		return h1;
	}

	public void setH1(double h1) {
		this.h1 = h1;
	}

	public double getH2() {
		return h2;
	}

	public void setH2(double h2) {
		this.h2 = h2;
	}

	public double getH3() {
		return h3;
	}

	public void setH3(double h3) {
		this.h3 = h3;
	}

	public ShenXianVO() {

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("ShenXianVO: {");
		sb.append("stockId:" + stockId);
		sb.append(", name:" + name);
		sb.append(", date:" + date);
		sb.append(", h1:" + h1);
		sb.append(", h2:" + h2);
		sb.append(", h3:" + h3);
		sb.append(", hc5:" + hc5);
		sb.append(", hc6:" + hc6);
		sb.append("}");
		return sb.toString();
	}

	public double getHc5() {
		return hc5;
	}

	public void setHc5(double hc5) {
		this.hc5 = hc5;
	}

	public double getHc6() {
		return hc6;
	}

	public void setHc6(double hc6) {
		this.hc6 = hc6;
	}
}
