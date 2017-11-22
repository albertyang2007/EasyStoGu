package org.easystogu.db.vo.table;

public class BBIVO  extends IndicatorVO{
	public String stockId;
	public String date;
	public double close;
	public double bbi;
	
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
	public double getBbi() {
		return bbi;
	}
	public void setBbi(double bbi) {
		this.bbi = bbi;
	}
	public double getClose() {
		return close;
	}
	public void setClose(double close) {
		this.close = close;
	}
}
