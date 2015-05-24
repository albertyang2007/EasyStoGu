package org.easystogu.db.table;

public class CheckPointDailySelectionVO {
	public String stockId;
	public String date;
	public String checkPoint;

	public String toString() {
		return stockId + " Date=" + this.date + ", CheckPoint=" + this.checkPoint;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getCheckPoint() {
		return checkPoint;
	}

	public void setCheckPoint(String checkPoint) {
		this.checkPoint = checkPoint;
	}
}
