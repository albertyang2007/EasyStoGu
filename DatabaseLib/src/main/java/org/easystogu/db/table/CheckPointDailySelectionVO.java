package org.easystogu.db.table;

public class CheckPointDailySelectionVO {
	public String date;
	public String checkPoint;
	public String stockIdList;// stockIds split by ;

	public String toString() {
		return "Date=" + this.date + ", CheckPoint=" + this.checkPoint + ", stockIds=" + this.stockIdList;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCheckPoint() {
		return checkPoint;
	}

	public void setCheckPoint(String checkPoint) {
		this.checkPoint = checkPoint;
	}

	public String getStockIdList() {
		return stockIdList;
	}

	public void setStockIdList(String stockIdList) {
		this.stockIdList = stockIdList;
	}
}
