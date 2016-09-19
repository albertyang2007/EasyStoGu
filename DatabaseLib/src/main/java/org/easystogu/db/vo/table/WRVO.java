package org.easystogu.db.vo.table;

public class WRVO {

	public String stockId;
	public String name;
	public String date;
	public double lonTerm;
	public double midTerm;
	public double shoTerm;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("WR: {");
		sb.append("stockId:" + stockId);
		sb.append(", date:" + date);
		sb.append(", lonTerm:" + lonTerm);
		sb.append(", midTerm:" + midTerm);
		sb.append(", shoTerm:" + shoTerm);
		sb.append("}");
		return sb.toString();
	}

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

	public double getLonTerm() {
		return lonTerm;
	}

	public void setLonTerm(double lonTerm) {
		this.lonTerm = lonTerm;
	}

	public double getMidTerm() {
		return midTerm;
	}

	public void setMidTerm(double midTerm) {
		this.midTerm = midTerm;
	}

	public double getShoTerm() {
		return shoTerm;
	}

	public void setShoTerm(double shoTerm) {
		this.shoTerm = shoTerm;
	}
}
