package org.easystogu.db.vo.table;

public class DDXVO {
	public String stockId;
	public String date;
	public double ddx;
	public double ddy;
	public double ddz;

	public DDXVO() {

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("ddx: {");
		sb.append("stockId:" + stockId);
		sb.append(", date:" + date);
		sb.append(", ddx:" + ddx);
		sb.append(", ddy:" + ddy);
		sb.append(", ddz:" + ddz);
		sb.append("}");
		return sb.toString();
	}

	public String toDDXString() {
		return String.format("ddx [%.2f]", ddx);
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

	public double getDdx() {
		return ddx;
	}

	public void setDdx(double ddx) {
		this.ddx = ddx;
	}

	public double getDdy() {
		return ddy;
	}

	public void setDdy(double ddy) {
		this.ddy = ddy;
	}

	public double getDdz() {
		return ddz;
	}

	public void setDdz(double ddz) {
		this.ddz = ddz;
	}
}
