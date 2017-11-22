package org.easystogu.db.vo.table;

//table name = "ind_macd"
public class MacdVO extends IndicatorVO{
	public String stockId;
	public String date;
	public double dif;
	public double dea;
	public double macd;

	public MacdVO() {

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("MacdVO: {");
		sb.append("stockId:" + stockId);
		sb.append(", date:" + date);
		sb.append(", DIF:" + dif);
		sb.append(", DEA:" + dea);
		sb.append(", MACD:" + macd);
		sb.append("}");
		return sb.toString();
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
