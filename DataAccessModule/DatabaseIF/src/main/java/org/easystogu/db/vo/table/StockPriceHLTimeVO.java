package org.easystogu.db.vo.table;

public class StockPriceHLTimeVO {
	public String stockId;
	public String date;
	public String hightTime;
	public String lowTime;

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

	public String getHightTime() {
		return hightTime;
	}

	public void setHightTime(String hightTime) {
		this.hightTime = hightTime;
	}

	public String getLowTime() {
		return lowTime;
	}

	public void setLowTime(String lowTime) {
		this.lowTime = lowTime;
	}
}
