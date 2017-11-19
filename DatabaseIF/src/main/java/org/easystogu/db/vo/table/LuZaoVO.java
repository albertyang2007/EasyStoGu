package org.easystogu.db.vo.table;

public class LuZaoVO  extends IndicatorVO{
	public String stockId;
	public String name;
	public String date;
	public double ma19;
	public double ma43;
	public double ma86;

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

	public double getMa19() {
		return ma19;
	}

	public void setMa19(double ma19) {
		this.ma19 = ma19;
	}

	public double getMa43() {
		return ma43;
	}

	public void setMa43(double ma43) {
		this.ma43 = ma43;
	}

	public double getMa86() {
		return ma86;
	}

	public void setMa86(double ma86) {
		this.ma86 = ma86;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("LuZaoVO: {");
		sb.append("stockId:" + stockId);
		sb.append(", name:" + name);
		sb.append(", date:" + date);
		sb.append(", ma19:" + ma19);
		sb.append(", ma43:" + ma43);
		sb.append(", ma86:" + ma86);
		sb.append("}");
		return sb.toString();
	}
}
