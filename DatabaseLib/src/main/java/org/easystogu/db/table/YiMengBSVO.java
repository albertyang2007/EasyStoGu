package org.easystogu.db.table;

public class YiMengBSVO {

	public String stockId;
	public String name;
	public String date;
	public double x2;
	public double x3;

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

	 
	public YiMengBSVO() {

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("YiMengBSVO: {");
		sb.append("stockId:" + stockId);
		sb.append(", name:" + name);
		sb.append(", date:" + date);
		sb.append(", x2:" + x2);
		sb.append(", x3:" + x3);
		sb.append("}");
		return sb.toString();
	}

	public double getX2() {
		return x2;
	}

	public void setX2(double x2) {
		this.x2 = x2;
	}

	public double getX3() {
		return x3;
	}

	public void setX3(double x3) {
		this.x3 = x3;
	}

}
