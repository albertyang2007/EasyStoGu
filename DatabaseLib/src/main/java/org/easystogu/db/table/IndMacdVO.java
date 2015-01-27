package org.easystogu.db.table;

//table name = "ind_macd"
public class IndMacdVO {
	public String stockId;
	public String name;
	public String date;
	public double dif;
	public double dea;
	public double macd;

	public IndMacdVO() {

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("IndMacdVO: {");
		sb.append("stockId:" + stockId);
		sb.append(", name:" + name);
		sb.append(", date:" + date);
		sb.append(", dif:" + dif);
		sb.append(", dea:" + dea);
		sb.append(", macd:" + macd);
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

	public double getDif() {
		return dif;
	}

	public void setDif(double dif) {
		this.dif = dif;
	}

	public double getDea() {
		return dea;
	}

	public void setDea(double dea) {
		this.dea = dea;
	}

	public double getMacd() {
		return macd;
	}

	public void setMacd(double macd) {
		this.macd = macd;
	}
}
